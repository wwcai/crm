package com.wwcai.crm.workbench.service.impl;

import com.wwcai.crm.utils.DateTimeUtil;
import com.wwcai.crm.utils.SqlSessionUtil;
import com.wwcai.crm.utils.UUIDUtil;
import com.wwcai.crm.workbench.dao.CustomerDao;
import com.wwcai.crm.workbench.dao.TranDao;
import com.wwcai.crm.workbench.dao.TranHistoryDao;
import com.wwcai.crm.workbench.domain.Customer;
import com.wwcai.crm.workbench.domain.Tran;
import com.wwcai.crm.workbench.domain.TranHistory;
import com.wwcai.crm.workbench.service.TranService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranServiceImpl implements TranService {

    private TranDao tranDao =
            SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao =
            SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);

    private CustomerDao customerDao =
            SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    @Override
    public boolean save(Tran t, String customerName) {

        /*
            交易添加业务
                在添加之前，t里面少了customerId
                处理客户相关需求
                    有 则取Id
                    没有则新建 再取Id

                添加交易成功后，创建一条交易历史


         */

        boolean flag = true;

        Customer cus = customerDao.getCustomerByName(customerName);

        if(cus == null) {

            cus = new Customer();
            cus.setId(UUIDUtil.getUUID());
            cus.setName(customerName);
            cus.setCreateTime(t.getCreateTime());
            cus.setCreateBy(t.getCreateBy());
            cus.setContactSummary(t.getContactSummary());
            cus.setOwner(t.getOwner());
            cus.setNextContactTime(t.getNextContactTime());

            int count1 = customerDao.save(cus);
            if(count1 != 1)
                flag = false;
        }

        // 已得到客户Id customerId
        t.setCustomerId(cus.getId());

        int count2 = tranDao.save(t);
        if(count2 != 1)
            flag = false;

        // 添加交易历史
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setTranId(t.getId());
        th.setStage(t.getStage());
        th.setMoney(t.getMoney());
        th.setExpectedDate(t.getExpectedDate());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setCreateBy(t.getCreateBy());

        int count3 = tranHistoryDao.save(th);
        if(count3 != 1)
            flag = false;

        return flag;
    }

    @Override
    public Tran detail(String id) {

        Tran t = tranDao.detail(id);

        return t;
    }

    @Override
    public List<TranHistory> getHistoryByTranId(String tranId) {

        List<TranHistory> thlist = tranHistoryDao.getHistoryByTranId(tranId);

        return thlist;
    }

    @Override
    public boolean changeStage(Tran t) {

        boolean flag = true;

        // 改变交易阶段
        int count1 = tranDao.changeStage(t);
        if(count1 != 1)
            flag = false;

        // 交易阶段改变后生成交易历史
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setCreateBy(t.getEditBy());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setExpectedDate(t.getExpectedDate());
        th.setMoney(t.getMoney());
        th.setStage(t.getStage());
        th.setTranId(t.getId());
        // 添加交易历史
        int count2 = tranHistoryDao.save(th);
        if(count2 != 1)
            flag = false;

        return flag;
    }

    @Override
    public Map<String, Object> getChrats() {

        // 取得total
        int total = tranDao.getTotal();

        // 取得dataList
        List<Map<String, Object>> dataList = tranDao.getCharts();

        // 将total和dataList保存到map中
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("dataList", dataList);



        return map;
    }
}
