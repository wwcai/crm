package com.wwcai.crm.workbench.service.impl;

import com.wwcai.crm.utils.SqlSessionUtil;
import com.wwcai.crm.vo.PaginationVo;
import com.wwcai.crm.workbench.dao.ActivityDao;
import com.wwcai.crm.workbench.dao.ActivityRemarkDao;
import com.wwcai.crm.workbench.domain.Activity;
import com.wwcai.crm.workbench.service.ActivityService;

import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {

    private ActivityDao activityDao =
            SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao =
            SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);

    @Override
    public boolean save(Activity a) {

        boolean falg = true;

        int count = activityDao.save(a);
        if(count != 1) {
            falg = false;
        }
        return falg;
    }

    @Override
    public PaginationVo<Activity> pageList(Map<String, Object> map) {

        // 取得total
        int total = activityDao.getTotalByCondition(map);

        // 取得datalist
        List<Activity> datalist = activityDao.getActivityListByCondition(map);

        // 将total和datalsit封装到vo中
        PaginationVo<Activity> vo = new PaginationVo<>();
        vo.setTotal(total);
        vo.setDatalist(datalist);

        // 返回vo
        return vo;
    }

    @Override
    public boolean delete(String[] ids) {

        boolean falg = true;
        // 查询出需要删除的备注的数量
        int count1 = activityRemarkDao.getCountByAids(ids);
        // 删除备注，返回受到影响的条数（实际删除的数量）
        int count2 = activityRemarkDao.deleteByIds(ids);

        if(count1 != count2) {
            falg = false;
        }

        // 删除市场活动
        int count3 = activityDao.delete(ids);
        if(count3 != ids.length) {
            falg = false;
        }

        return falg;
    }
}
