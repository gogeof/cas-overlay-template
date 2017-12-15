package org.example.gogeof.alone.service;

import java.util.ArrayList;
import java.util.List;

public class UserIdObtainServiceImpl implements IUserIdObtainService {
    public UserIdObtainServiceImpl() {

    }

    @Override
    public List<String> obtain(String clientName, String id) {
        // TODO. 根据我们的需要，我决定所有用户都只有一个唯一的用户名或者ID，不管将来使用那种用户登录，可以增加用户认证方式的表示，但是用户ID都只能为一个

        List<String> ids = new ArrayList<>();
        ids.add(id);
        return ids;
    }
}
