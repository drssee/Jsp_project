<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="ko">

<head>
    <meta charset="UTF-8">
    <title>마이페이지</title>
    <link rel="stylesheet" href="<c:url value="/resources/css/sub.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/css/myPage.css"/>">
    <script src="<c:url value="/resources/javascript/jquery-3.6.0.min.js"/>"></script>
</head>
    <script>
        $(document).ready(function (){
            //common.jsp 에서 실행될땐 바디태그 아래부분이라 로그인시 바로 적용인 안되서 옮김
            let user = '<c:out value="${sessionScope.get('user')}"/>';
            console.log("logined_cookie: : " + getCookie("logined_cookie").valueOf());
            console.log("user: " + user);
            if(user === ""){
                //alert('로그아웃 상태');
                $(".u_out").css("display","none");
                $(".u_admin").css("display","none");
                $(".u_on").css("display","inline-block");
            } else{
                if(user.valueOf()==='admin'){
                    $(".u_admin").css("display","inline-block");
                    $(".u_out").css("display","none");
                    $(".u_on").css("display","none");
                }
                else{
                    //alert('로그인 상태');
                    $(".u_out").css("display","inline-block");
                    $(".u_admin").css("display","none");
                    $(".u_on").css("display","none");
                }
            }
        })
    </script>
<body>
    <jsp:include page="/WEB-INF/view/common/header.jsp" flush="true" />
    <main id="mypage">
        <div class="sub_tit_line">
            <ul>
                <li class="sub_tit_home"><a href="#">H</a></li>
                <li><a href="#">마이페이지</a></li>
                <li><a href="#"></a></li>
            </ul>
        </div>
        <!--     상세페이지 영역       -->
        <div class="container_wrap">
            <h2 class="mypage_tit">마이페이지</h2>
            <section class="mypage_list">
                <ul>
                    <li class="on profile"><img src="<c:url value="/resources/images/user_default.png"/>" alt="" />
                        <h4>김진경</h4>
                    </li>
                    <li><a href="#">회원 정보 관리</a></li>
                    <li><a href="#">예약 관리</a></li>
                    <li><a href="#">1:1 문의 내역</a></li>
                </ul>
            </section>
            <section class="mypage_con">
               <!--    회원 정보 관리    -->
                <div class="mypage_user_info">
                    <h3>회원 정보 관리</h3>
                    <table>
<%--                        <tr>--%>
<%--                            <td>이름</td>--%>
<%--                            <td><input type="text" value="${}" readonly></td>--%>
<%--                        </tr>--%>
<%--                        <tr>--%>
<%--                            <td>전화번호</td>--%>
<%--                            <td><input type="text" value="${}" readonly></td>--%>
<%--                        </tr>--%>
<%--                        <tr>--%>
<%--                            <td>이메일</td>--%>
<%--                            <td><input type="text" value="${}" readonly></td>--%>
<%--                        </tr>--%>
<%--                        <tr>--%>
<%--                            <td>주소</td>--%>
<%--                            <td><input type="text" value="${}" readonly></td>--%>
<%--                        </tr>--%>
<%--                    </table>--%>
<%--                </div>                --%>
<%--                <!--    예약 관리    -->--%>
<%--                <div class="mypage_reserve_info">--%>
<%--                    <h3>예약 내역</h3>--%>
<%--                    <form action="">--%>
<%--                        <table>--%>
<%--                            <tr>--%>
<%--                                <td>제목</td>--%>
<%--                                <td><input type="text" value="${}" readonly></td>--%>
<%--                            </tr>--%>
<%--                            <tr>--%>
<%--                                <td>장소</td>--%>
<%--                                <td><input type="text" value="${}" readonly></td>--%>
<%--                            </tr>--%>
<%--                            <tr>--%>
<%--                                <td>예약 날짜</td>--%>
<%--                                <td><input type="text" value="${}" readonly></td>--%>
<%--                            </tr>--%>
<%--                            <tr>--%>
<%--                                <td>예약 인원</td>--%>
<%--                                <td><input type="text" value="${}" readonly></td>--%>
<%--                            </tr>--%>
<%--                            <tr>--%>
<%--                                <td>문의전화</td>--%>
<%--                                <td><input type="text" value="${}" readonly></td>--%>
<%--                            </tr>--%>
                        </table>
                        <p class="btn_cancel">
                            <button type="submit">예약 취소</button>
                        </p>
                    </form>
                </div>
            </section>

        </div>
    </main>

</body>

</html>
