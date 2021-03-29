package com.wwcai.crm.workbench.service.impl;

import com.wwcai.crm.utils.SqlSessionUtil;
import com.wwcai.crm.workbench.dao.ContactsDao;
import com.wwcai.crm.workbench.dao.ContactsRemarkDao;
import com.wwcai.crm.workbench.domain.Contacts;
import com.wwcai.crm.workbench.service.ContactsService;

import java.util.List;

public class ContactsServiceImpl implements ContactsService {

    private ContactsDao contactsDao  =
            SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);

    private ContactsRemarkDao contactsRemarkDao =
            SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);

    @Override
    public List<Contacts> getContactsByName(String cname) {

        List<Contacts> clist = contactsDao.getContactsByName(cname);

        return clist;
    }
}
