package com.softtech.webapp.user.service;

import com.softtech.webapp.general.service.BaseEntityService;
import com.softtech.webapp.user.dao.UserDao;
import com.softtech.webapp.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserEntityService extends BaseEntityService<User, UserDao> {
    public UserEntityService(UserDao dao) {
        super(dao, User.class);
    }

    public User findByUsername(String username) {
        return getDao().findByUsername(username);
    }

    public User findByUsernameUpper(String username) {
        return getDao().findByUsernameUpper(username);
    }
}
