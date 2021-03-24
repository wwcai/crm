package com.wwcai.crm.workbench.service;

import com.wwcai.crm.vo.PaginationVo;
import com.wwcai.crm.workbench.domain.Clue;
import com.wwcai.crm.workbench.domain.Tran;

import java.util.Map;

public interface ClueService {
    boolean save(Clue clue);

    PaginationVo<Clue> pageList(Map<String, Object> map);

    Clue detail(String id);

    boolean unbund(String id);

    Map<String, Object> getUserListAndClue(String id);

    boolean update(Clue clue);

    boolean delete(String[] ids);

    boolean bund(String cid, String[] aids);

    boolean convert(String clueId, Tran t, String createBy);
}
