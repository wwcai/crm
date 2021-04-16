package com.wwcai.crm.workbench.service;

import com.wwcai.crm.workbench.domain.Tran;
import com.wwcai.crm.workbench.domain.TranHistory;

import java.util.List;
import java.util.Map;

public interface TranService {
    boolean save(Tran t, String customerName);

    Tran detail(String id);

    List<TranHistory> getHistoryByTranId(String tranId);

    boolean changeStage(Tran t);

    Map<String, Object> getChrats();
}
