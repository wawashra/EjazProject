package com.ejazbzu.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.ejazbzu.domain.enumeration.Gender;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link com.ejazbzu.domain.Student} entity. This class is used
 * in {@link com.ejazbzu.web.rest.StudentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /students?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class StudentCriteria implements Serializable, Criteria {
    /**
     * Class for filtering Gender
     */
    public static class GenderFilter extends Filter<Gender> {

        public GenderFilter() {
        }

        public GenderFilter(GenderFilter filter) {
            super(filter);
        }

        @Override
        public GenderFilter copy() {
            return new GenderFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LocalDateFilter birthday;

    private StringFilter phoneNumber;

    private GenderFilter gender;

    private StringFilter profileImgUrl;

    private StringFilter coverImgUrl;

    private BooleanFilter star;

    private LongFilter userId;

    private LongFilter universityId;

    private LongFilter departmentId;

    private LongFilter collegeId;

    private LongFilter documentsId;

    private LongFilter reportId;

    private LongFilter coursesId;

    public StudentCriteria() {
    }

    public StudentCriteria(StudentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.birthday = other.birthday == null ? null : other.birthday.copy();
        this.phoneNumber = other.phoneNumber == null ? null : other.phoneNumber.copy();
        this.gender = other.gender == null ? null : other.gender.copy();
        this.profileImgUrl = other.profileImgUrl == null ? null : other.profileImgUrl.copy();
        this.coverImgUrl = other.coverImgUrl == null ? null : other.coverImgUrl.copy();
        this.star = other.star == null ? null : other.star.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.universityId = other.universityId == null ? null : other.universityId.copy();
        this.departmentId = other.departmentId == null ? null : other.departmentId.copy();
        this.collegeId = other.collegeId == null ? null : other.collegeId.copy();
        this.documentsId = other.documentsId == null ? null : other.documentsId.copy();
        this.reportId = other.reportId == null ? null : other.reportId.copy();
        this.coursesId = other.coursesId == null ? null : other.coursesId.copy();
    }

    @Override
    public StudentCriteria copy() {
        return new StudentCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public LocalDateFilter getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDateFilter birthday) {
        this.birthday = birthday;
    }

    public StringFilter getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(StringFilter phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public GenderFilter getGender() {
        return gender;
    }

    public void setGender(GenderFilter gender) {
        this.gender = gender;
    }

    public StringFilter getProfileImgUrl() {
        return profileImgUrl;
    }

    public void setProfileImgUrl(StringFilter profileImgUrl) {
        this.profileImgUrl = profileImgUrl;
    }

    public StringFilter getCoverImgUrl() {
        return coverImgUrl;
    }

    public void setCoverImgUrl(StringFilter coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }

    public BooleanFilter getStar() {
        return star;
    }

    public void setStar(BooleanFilter star) {
        this.star = star;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getUniversityId() {
        return universityId;
    }

    public void setUniversityId(LongFilter universityId) {
        this.universityId = universityId;
    }

    public LongFilter getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(LongFilter departmentId) {
        this.departmentId = departmentId;
    }

    public LongFilter getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(LongFilter collegeId) {
        this.collegeId = collegeId;
    }

    public LongFilter getDocumentsId() {
        return documentsId;
    }

    public void setDocumentsId(LongFilter documentsId) {
        this.documentsId = documentsId;
    }

    public LongFilter getReportId() {
        return reportId;
    }

    public void setReportId(LongFilter reportId) {
        this.reportId = reportId;
    }

    public LongFilter getCoursesId() {
        return coursesId;
    }

    public void setCoursesId(LongFilter coursesId) {
        this.coursesId = coursesId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final StudentCriteria that = (StudentCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(birthday, that.birthday) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(gender, that.gender) &&
            Objects.equals(profileImgUrl, that.profileImgUrl) &&
            Objects.equals(coverImgUrl, that.coverImgUrl) &&
            Objects.equals(star, that.star) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(universityId, that.universityId) &&
            Objects.equals(departmentId, that.departmentId) &&
            Objects.equals(collegeId, that.collegeId) &&
            Objects.equals(documentsId, that.documentsId) &&
            Objects.equals(reportId, that.reportId) &&
            Objects.equals(coursesId, that.coursesId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        birthday,
        phoneNumber,
        gender,
        profileImgUrl,
        coverImgUrl,
        star,
        userId,
        universityId,
        departmentId,
        collegeId,
        documentsId,
        reportId,
        coursesId
        );
    }

    @Override
    public String toString() {
        return "StudentCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (birthday != null ? "birthday=" + birthday + ", " : "") +
                (phoneNumber != null ? "phoneNumber=" + phoneNumber + ", " : "") +
                (gender != null ? "gender=" + gender + ", " : "") +
                (profileImgUrl != null ? "profileImgUrl=" + profileImgUrl + ", " : "") +
                (coverImgUrl != null ? "coverImgUrl=" + coverImgUrl + ", " : "") +
                (star != null ? "star=" + star + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (universityId != null ? "universityId=" + universityId + ", " : "") +
                (departmentId != null ? "departmentId=" + departmentId + ", " : "") +
                (collegeId != null ? "collegeId=" + collegeId + ", " : "") +
                (documentsId != null ? "documentsId=" + documentsId + ", " : "") +
                (reportId != null ? "reportId=" + reportId + ", " : "") +
                (coursesId != null ? "coursesId=" + coursesId + ", " : "") +
            "}";
    }

}
