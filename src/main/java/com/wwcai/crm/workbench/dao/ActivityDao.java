package com.wwcai.crm.workbench.dao;

import com.wwcai.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityDao {
    int save(Activity a);

    List<Activity> getActivityListByCondition(Map<String, Object> map);

    Integer getTotalByCondition(Map<String, Object> map);

    int delete(String[] ids);
}
