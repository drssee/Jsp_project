package com.example.controller;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "loginController", value = "/login")
public class LoginController extends UserController {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("login get ...");
        HttpSession session = req.getSession();
        Cookie[] cookies = req.getCookies();
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("logined_cookie")){
                session.removeAttribute("logined");
                System.out.println("로그인 기억중");
                req.getRequestDispatcher("/").forward(req,resp);
            }
        }
        req.getRequestDispatcher("/login.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("login post ...");

        HttpSession session = req.getSession();

        //id pwd chk(1=아이디기억 , 2=로그인유지)
        String id = req.getParameter("id");
        String pwd = req.getParameter("pwd");
        //로그인체크
        try {
            if (userService.login(id, pwd)) {
                System.out.println("로그인 성공");

                //관리자모드
                if ("admin".equals(id)) {
                    session.setAttribute("user", "admin");
                }
                //유저모드
                else {
                    session.setAttribute("user", id);
                }
                //로그인 성공시 홈으로 리다이렉트
                resp.sendRedirect("/");
            }//if
            //로그인 실패시 login ... get
            else {
                new Exception("로그인 실패");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("로그인 실패");
            session.setAttribute("msg", "아이디와 비밀번호를 확인해주세요");
            resp.sendRedirect("/login");
        }
    }
}
