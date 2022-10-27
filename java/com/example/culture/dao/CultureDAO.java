package com.example.culture.dao;

import com.example.common.vo.PageRequestVO;
import com.example.common.vo.PageResponseVO;
import com.example.culture.vo.CultureVO;

public interface CultureDAO<T> {
    void insert(T t);
    PageResponseVO<CultureVO> selectAll(PageRequestVO pageRequestVO);
    CultureVO selectOne(Long cno);
    int selectCount();
    void deleteAll();
}