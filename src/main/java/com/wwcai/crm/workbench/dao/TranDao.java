package com.wwcai.crm.workbench.dao;

import com.wwcai.crm.workbench.domain.Tran;

public interface TranDao {

    int save(Tran t);

    Tran detail(String id);

    int changeStage(Tran t);
}
