import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {
  logoFooter = '../../../content/images/ejaz-logo.png';

  constructor() {}

  ngOnInit(): void {}
}
