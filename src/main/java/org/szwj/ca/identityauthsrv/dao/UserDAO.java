package org.szwj.ca.identityauthsrv.dao;

import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.szwj.ca.identityauthsrv.entity.dao.UserEntity;

import java.util.List;

@Repository
public interface UserDAO {

    // 查询所有用户
    @Select("SELECT * FROM T_USER")
    @ResultType(UserEntity.class)
    List<UserEntity> QueryAllUser();

    // 根据用户ID查询用户工号
    @Select("SELECT EmployeeNum FROM T_USER WHERE ID = #{ID}")
    @ResultType(String.class)
    String QueryEmployeeNumByID(String ID);
}
