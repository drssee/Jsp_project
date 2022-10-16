package com.example.controller;



import com.example.service.SingletonService;
import com.example.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserController extends HttpServlet {
    //**session id 목록**
    // user - 로그인 성공저장 (userid,adminid)

    //**cookie id 목록**
    //key(remember_id) 아이디기억
    //remember_login 로그인유지

    SingletonService instance = SingletonService.getInstance();
    UserService userService = instance.userService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

}
