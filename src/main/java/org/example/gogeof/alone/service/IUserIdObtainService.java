package org.example.gogeof.alone.service;

import java.util.List;

public interface IUserIdObtainService {
    /**
     * 通过登录方式查询其他的id
     *
     * @param clientName 登录方式
     * @param id         用户id
     * @return 所有用户id
     */
    List<String> obtain(String clientName, String id);
}
