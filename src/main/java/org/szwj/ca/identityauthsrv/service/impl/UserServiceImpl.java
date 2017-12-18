package org.szwj.ca.identityauthsrv.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.szwj.ca.identityauthsrv.dao.UserDAO;
import org.szwj.ca.identityauthsrv.entity.dao.UserEntity;
import org.szwj.ca.identityauthsrv.service.intfc.UserService;

import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

    // 依赖注入
    @Autowired
    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public List<UserEntity> QueryAllUser() {
        return userDAO.QueryAllUser();
    }

    @Override
    public String QueryEmployeeNumByID(String ID) {
        return userDAO.QueryEmployeeNumByID(ID);
    }
}
