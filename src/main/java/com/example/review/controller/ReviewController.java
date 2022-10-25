package com.example.review.controller;

import com.example.domain.PageRequest;
import com.example.domain.PageResponse;
import com.example.domain.ReviewVO;
import com.example.review.service.ReviewService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import static com.example.common.SingletonProvideUtil.SINGLETON_UTIL;

@WebServlet(name="reviewController",value="/review/*")
@Slf4j
public class ReviewController extends HttpServlet {

    private final ReviewService reviewService = SINGLETON_UTIL.reviewService();
    private final Gson gson = SINGLETON_UTIL.gson();

    //자바객체를 json 객체로 응답하는 메서드
    private void sendAsJson(HttpServletResponse response,
                            Object obj) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        String json = gson.toJson(obj);

        PrintWriter out = response.getWriter();

        out.print(json);
        out.flush();
    }

    @Override //get // /review?cno=1&page=1&size=12
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("ReviewController.doGet");
        try {
            Long cno = Long.valueOf(req.getParameter("cno"));
            String paramPage = req.getParameter("page");
            System.out.println("paramPage = " + paramPage);
            String paramSize = req.getParameter("size");
            System.out.println("paramSize = " + paramSize);
            PageRequest pageRequest;
            //쿼리스트링으로 받아온 page size를 검증후 그에 맞는 PageRequest생성
            if(paramPage==null&&paramSize==null){
                pageRequest=PageRequest.builder().build();
            }
            else if (paramPage==null||"".equals(paramPage)){
                pageRequest = PageRequest.builder()
                        .size(Integer.parseInt(paramSize))
                        .build();
            }
            else if (paramSize==null|| "".equals(paramSize)) {
                pageRequest = PageRequest.builder()
                        .page(Integer.parseInt(paramPage))
                        .build();
            }
            else{ //paramPage!=null && paramSize!=null
                pageRequest = PageRequest.builder()
                        .page(Integer.parseInt(paramPage))
                        .size(Integer.parseInt(paramSize))
                        .build();
            }
            System.out.println("pageRequest!!!!!!!!!!!!!!!!!!! = " + pageRequest);
            PageResponse<ReviewVO> pageResponse = reviewService.getReviews(cno,pageRequest);
            //고의 예외발생 테스트
//            throw new Exception("고의 발생 예외");
            sendAsJson(resp,pageResponse);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("리뷰작성 실패");
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST); //400에러
//            resp.setStatus(404);
//            sendAsJson(resp,"WRITE_ERR");
//            throw new RuntimeException("잘못된 요청이 들어왔습니다");
        }
    }//review get

    @Override // /review
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("ReviewController.doPost");
        //req 로부터 io스트림을 가져온다
        BufferedReader reader = req.getReader();
        //io로부터 읽어올 변수
        String line;
        //읽어온 변수를 저장할 변수 (json이라 문자열)
        StringBuilder stringBuilder = new StringBuilder();
        //더이상 읽어올 문자가 없을때까지 읽어온다
        while((line=reader.readLine())!=null){
            stringBuilder.append(line);
        }
        //읽어온 데이터를 playload에 저장
        String payload = stringBuilder.toString();
        //읽어온 문자열(json)을 자바 객체로 파싱
        ReviewVO reviewVO = gson.fromJson(payload, ReviewVO.class);
        //읽어오지 못했다면 400에러 리턴
        if(reviewVO==null){
            log.error("reviewVO 를 가져오지 못함");
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST); //400에러
            return;
        }
        //같은 cno에 리뷰를 이미 썼으면 400에러 리턴
        if(reviewService.getReview(reviewVO.getId(),reviewVO.getCno())!=null) {
            log.error("중복 cno,id 리뷰 등록");
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST); //400에러
            return;
        }
        //reviewVO!=null && 중복id x
        reviewService.writeReview(reviewVO);
        resp.setStatus(200); //200 OK
    }//post

    @Override // /review/{re_no}  -> /{re_no} -> /27 == pathInfo
    //doput 리팩토링 해야함
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("ReviewController.doPut");
        String pathInfo = req.getPathInfo();

        if(pathInfo == null || pathInfo.equals("/")){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String[] splits = pathInfo.split("/");

        if(splits.length!=2){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Long re_no = null;
        try {
            re_no = Long.valueOf(splits[1]);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        ReviewVO reviewVO = reviewService.getReview(re_no);

        if(reviewVO==null){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while((line = reader.readLine())!=null) {
            stringBuilder.append(line);
        }

        String payload = stringBuilder.toString();

        ReviewVO reviewVO1 = gson.fromJson(payload, ReviewVO.class);

        //업데이트할 칼럼만 가져와서 저장후 업데이트
        String content = reviewVO1.getContent();
        Integer grade = reviewVO1.getGrade();

        reviewVO.setContent(content);
        reviewVO.setGrade(grade);

        reviewService.updateReview(reviewVO);
        resp.setStatus(200);
    }

    @Override // /review/{re_no}  -> /{re_no} -> /27 == pathInfo
    //dodelete 리팩토링 해야함
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("ReviewController.doDelete");
        HttpSession session = req.getSession();
        String pathInfo = req.getPathInfo();

        if(pathInfo == null || pathInfo.equals("/")){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String[] splits = pathInfo.split("/");

        if(splits.length!=2){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Long re_no = null;
        try {
            re_no = Long.valueOf(splits[1]);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        ReviewVO reviewVO = reviewService.getReview(re_no);

        if(reviewVO==null||reviewVO.getId()!=session.getAttribute("user")){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        reviewService.removeReview(re_no);
        resp.setStatus(200);
    }
}
