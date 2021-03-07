package com.wwcai.crm.workbench.dao;

public interface ActivityRemarkDao {
    int getCountByAids(String[] ids);

    int deleteByIds(String[] ids);
}
