package com.ejazbzu.service.dto;

public class DepartmentFilterDTO {

    private Long collageId;
    private String departmentName;

    public Long getCollageId() {
        return collageId;
    }

    public void setCollageId(Long collageId) {
        this.collageId = collageId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}
