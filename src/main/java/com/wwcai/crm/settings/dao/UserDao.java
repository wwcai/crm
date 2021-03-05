package com.wwcai.crm.settings.dao;

import com.wwcai.crm.settings.domain.User;

import java.util.Map;

public interface UserDao {


    User login(Map<String, String> map);
}
