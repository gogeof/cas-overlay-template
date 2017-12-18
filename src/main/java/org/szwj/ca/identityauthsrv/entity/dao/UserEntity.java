package org.szwj.ca.identityauthsrv.entity.dao;

import java.sql.Timestamp;

/**
 * 用户实体类
 */
public class UserEntity {

    // 数据表主键
    private String ID;

    // 工号
    private String EmployeeNum;

    // 姓名
    private String Name;

    // 部门
    private String Department;

    // 岗位类别
    private String JobPosts;

    // 证件类型
    private String IdentityType;

    // 证件号码
    private String IdentityNumber;

    // 资格
    private String Qualification;

    // 执业证编号
    private String License;

    // 联系方式
    private String Phone;

    // 电子邮箱
    private String Email;

    // 创建时间
    private Timestamp CreatedTime;

    // 最后修改时间
    private Timestamp LastTime;

    // 是否被锁定
    private Integer LockFlag;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getEmployeeNum() {
        return EmployeeNum;
    }

    public void setEmployeeNum(String employeeNum) {
        EmployeeNum = employeeNum;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public String getJobPosts() {
        return JobPosts;
    }

    public void setJobPosts(String jobPosts) {
        JobPosts = jobPosts;
    }

    public String getIdentityType() {
        return IdentityType;
    }

    public void setIdentityType(String identityType) {
        IdentityType = identityType;
    }

    public String getIdentityNumber() {
        return IdentityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        IdentityNumber = identityNumber;
    }

    public String getQualification() {
        return Qualification;
    }

    public void setQualification(String qualification) {
        Qualification = qualification;
    }

    public String getLicense() {
        return License;
    }

    public void setLicense(String license) {
        License = license;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public Timestamp getCreatedTime() {
        return CreatedTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        CreatedTime = createdTime;
    }

    public Timestamp getLastTime() {
        return LastTime;
    }

    public void setLastTime(Timestamp lastTime) {
        LastTime = lastTime;
    }

    public Integer getLockFlag() {
        return LockFlag;
    }

    public void setLockFlag(Integer lockFlag) {
        LockFlag = lockFlag;
    }
}
