import { Component, OnInit, RendererFactory2, Renderer2 } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Router, ActivatedRouteSnapshot, NavigationEnd, NavigationError } from '@angular/router';
import { TranslateService, LangChangeEvent } from '@ngx-translate/core';
import { AccountService } from 'app/core/auth/account.service';
import { FindLanguageFromKeyPipe } from 'app/shared/language/find-language-from-key.pipe';
import { ICourse } from 'app/shared/model/course.model';
import { Account } from 'app/core/user/account.model';
import { CourseService } from 'app/entities/course/course.service';
import { UserService } from 'app/core/user/user.service';
import { IUser } from 'app/core/user/user.model';
import { LayoutService } from '../layout.service';

@Component({
  selector: 'jhi-main',
  templateUrl: './main.component.html',
  styleUrls: ['main.scss']
})
export class MainComponent implements OnInit {
  private renderer: Renderer2;
  sideBarOpen = true;
  isExpanded = false;
  account: Account | null = null;
  user: IUser | null = null;
  courses: ICourse[] | null = null;
  constructor(
    protected courseService: CourseService,
    private accountService: AccountService,
    private userService: UserService,
    private titleService: Title,
    private router: Router,
    private findLanguageFromKeyPipe: FindLanguageFromKeyPipe,
    private translateService: TranslateService,
    rootRenderer: RendererFactory2,
    private layoutService: LayoutService
  ) {
    this.renderer = rootRenderer.createRenderer(document.querySelector('html'), null);
  }

  ngOnInit(): void {
    this.layoutService.isExpanded.subscribe(boolValue => {
      this.sideBarOpen = boolValue;
    });
    this.accountService.getAuthenticationState().subscribe(account => {
      this.account = account;
      if (account) {
        localStorage.setItem('login', account.login);
        this.userService.find(account.login).subscribe(res => {
          if (res) {
            this.user = res;

            localStorage.setItem('user', res.id);
            const param = {
              'studentsId.equals': res.id
            };

            this.courseService.query(param).subscribe(resp => {
              this.courses = resp.body;
            });
          }
        });
      }
    });

    this.accountService.identity().subscribe();

    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.updateTitle();
      }
      if (event instanceof NavigationError && event.error.status === 404) {
        this.router.navigate(['/404']);
      }
    });

    this.translateService.onLangChange.subscribe((langChangeEvent: LangChangeEvent) => {
      this.updateTitle();

      this.renderer.setAttribute(document.querySelector('html'), 'lang', langChangeEvent.lang);

      this.updatePageDirection();
    });
  }
  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }
  private getPageTitle(routeSnapshot: ActivatedRouteSnapshot): string {
    let title: string = routeSnapshot.data && routeSnapshot.data['pageTitle'] ? routeSnapshot.data['pageTitle'] : '';
    if (routeSnapshot.firstChild) {
      title = this.getPageTitle(routeSnapshot.firstChild) || title;
    }
    return title;
  }

  onClick(): void {
    this.isExpanded = !this.isExpanded;
  }

  private updateTitle(): void {
    let pageTitle = this.getPageTitle(this.router.routerState.snapshot.root);
    if (!pageTitle) {
      pageTitle = 'global.title';
    }
    this.translateService.get(pageTitle).subscribe(title => this.titleService.setTitle(title));
  }

  private updatePageDirection(): void {
    this.renderer.setAttribute(
      document.querySelector('html'),
      'dir',
      this.findLanguageFromKeyPipe.isRTL(this.translateService.currentLang) ? 'rtl' : 'ltr'
    );
  }
}
