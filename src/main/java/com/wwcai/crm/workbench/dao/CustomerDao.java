package com.wwcai.crm.workbench.dao;

import com.wwcai.crm.workbench.domain.Customer;

public interface CustomerDao {

    Customer getCustomerByName(String company);

    int save(Customer cus);
}
