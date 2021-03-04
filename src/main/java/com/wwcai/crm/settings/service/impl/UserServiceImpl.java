package com.wwcai.crm.settings.service.impl;

import com.wwcai.crm.settings.dao.UserDao;
import com.wwcai.crm.settings.service.UserService;
import com.wwcai.crm.utils.SqlSessionUtil;

public class UserServiceImpl implements UserService {

    private UserDao userDao =
            SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
}
