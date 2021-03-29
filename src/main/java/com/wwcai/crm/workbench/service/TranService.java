package com.wwcai.crm.workbench.service;

import com.wwcai.crm.workbench.domain.Tran;
import com.wwcai.crm.workbench.domain.TranHistory;

import java.util.List;

public interface TranService {
    boolean save(Tran t, String customerName);

    Tran detail(String id);

    List<TranHistory> getHistoryByTranId(String tranId);

    boolean changeStage(Tran t);
}
