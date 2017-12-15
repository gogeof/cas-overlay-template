package org.szwj.ca.identityauthsrv.service.intfc;

import org.szwj.ca.identityauthsrv.entity.dao.UserEntity;
import java.util.List;

/**
 * UserService 接口类
 */
public interface UserService {

    /**
     * 查询所有用户
     *
     * @return 用户列表
     */
    List<UserEntity> QueryAllUser();

    /**
     * 根据用户ID查询用户工号
     *
     * @param ID 用户ID
     */
    String QueryEmployeeNumByID(String ID);
}
