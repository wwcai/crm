package com.wwcai.crm.workbench.service.impl;

import com.wwcai.crm.utils.SqlSessionUtil;
import com.wwcai.crm.workbench.dao.CustomerDao;
import com.wwcai.crm.workbench.service.CustomerService;

import java.util.List;

public class CustomerServiceImpl  implements CustomerService {

    private CustomerDao customerDao =
            SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    @Override
    public List<String> getCustomerName(String name) {

        List<String> slist = customerDao.getCustomerName(name);

        return slist;
    }
}
