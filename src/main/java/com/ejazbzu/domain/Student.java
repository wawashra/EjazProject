package com.ejazbzu.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.ejazbzu.domain.enumeration.Gender;

/**
 * A Student.
 */
@Entity
@Table(name = "student")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Student extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "birthday", nullable = false)
    private LocalDate birthday;

    @NotNull
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Column(name = "profile_img_url")
    private String profileImgUrl;

    @Column(name = "cover_img_url")
    private String coverImgUrl;

    @Column(name = "star")
    private Boolean star;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToOne
    @JoinColumn(unique = true)
    private University university;

    @OneToOne
    @JoinColumn(unique = true)
    private Department department;

    @OneToOne
    @JoinColumn(unique = true)
    private College college;

    @OneToMany(mappedBy = "student")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Document> documents = new HashSet<>();

    @OneToMany(mappedBy = "student")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Report> reports = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "student_courses",
               joinColumns = @JoinColumn(name = "student_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "courses_id", referencedColumnName = "id"))
    private Set<Course> courses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Student name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public Student birthday(LocalDate birthday) {
        this.birthday = birthday;
        return this;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Student phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Gender getGender() {
        return gender;
    }

    public Student gender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getProfileImgUrl() {
        return profileImgUrl;
    }

    public Student profileImgUrl(String profileImgUrl) {
        this.profileImgUrl = profileImgUrl;
        return this;
    }

    public void setProfileImgUrl(String profileImgUrl) {
        this.profileImgUrl = profileImgUrl;
    }

    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public Student coverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
        return this;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }

    public Boolean isStar() {
        return star;
    }

    public Student star(Boolean star) {
        this.star = star;
        return this;
    }

    public void setStar(Boolean star) {
        this.star = star;
    }

    public User getUser() {
        return user;
    }

    public Student user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public University getUniversity() {
        return university;
    }

    public Student university(University university) {
        this.university = university;
        return this;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    public Department getDepartment() {
        return department;
    }

    public Student department(Department department) {
        this.department = department;
        return this;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public College getCollege() {
        return college;
    }

    public Student college(College college) {
        this.college = college;
        return this;
    }

    public void setCollege(College college) {
        this.college = college;
    }

    public Set<Document> getDocuments() {
        return documents;
    }

    public Student documents(Set<Document> documents) {
        this.documents = documents;
        return this;
    }

    public Student addDocuments(Document document) {
        this.documents.add(document);
        document.setStudent(this);
        return this;
    }

    public Student removeDocuments(Document document) {
        this.documents.remove(document);
        document.setStudent(null);
        return this;
    }

    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
    }

    public Set<Report> getReports() {
        return reports;
    }

    public Student reports(Set<Report> reports) {
        this.reports = reports;
        return this;
    }

    public Student addReport(Report report) {
        this.reports.add(report);
        report.setStudent(this);
        return this;
    }

    public Student removeReport(Report report) {
        this.reports.remove(report);
        report.setStudent(null);
        return this;
    }

    public void setReports(Set<Report> reports) {
        this.reports = reports;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public Student courses(Set<Course> courses) {
        this.courses = courses;
        return this;
    }

    public Student addCourses(Course course) {
        this.courses.add(course);
        course.getStudents().add(this);
        return this;
    }

    public Student removeCourses(Course course) {
        this.courses.remove(course);
        course.getStudents().remove(this);
        return this;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Student)) {
            return false;
        }
        return id != null && id.equals(((Student) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Student{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", birthday='" + getBirthday() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", gender='" + getGender() + "'" +
            ", profileImgUrl='" + getProfileImgUrl() + "'" +
            ", coverImgUrl='" + getCoverImgUrl() + "'" +
            ", star='" + isStar() + "'" +
            "}";
    }
}
