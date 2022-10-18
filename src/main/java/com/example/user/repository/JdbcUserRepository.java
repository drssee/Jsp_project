package com.example.user.repository;



import com.example.domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;

import static com.example.util.ConnectionUtil.*;

public class JdbcUserRepository implements UserRepository {

    @Override
    public void insert(User user) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            String sql = "insert into user_basic "
                    + "(id,pwd,name)"
                    + " values(?,?,?) ";
            conn = CONN_UTIL.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getId());
            pstmt.setString(2, user.getPwd());
            pstmt.setString(3, user.getName());
            pstmt.executeUpdate();

            sql = "insert into user_info "
                    + "(id,age,gender)"
                    + " values(?,?,?) ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getId());
            pstmt.setInt(2, user.getAge());
            pstmt.setInt(3, user.getGender());
            pstmt.executeUpdate();

            sql = "insert into user_res "
                    + "(id,email,phone)"
                    + " values(?,?,?) ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getId());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPhone());
            pstmt.executeUpdate();

            conn.commit();
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
                System.out.println("user insert를 롤백합니다");
                throw new RuntimeException(ex);
            }
            e.printStackTrace();
            System.out.println("회원가입에 실패했습니다");
            throw new RuntimeException(e);
        } finally {
            CONN_UTIL.close(pstmt,conn);
        }
    }

    @Override
    public Optional<User> getById(String id) {
        String sql = "select basic.id,basic.pwd,basic.name,basic.regDate," +
                "res.email,res.phone,info.age,info.gender from " +
                "(user_basic as basic inner join user_info as info " +
                "on basic.id=info.id " +
                "inner join user_res as res " +
                "on info.id=res.id)" +
                "where basic.id = ? " +
                "order by basic.id";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;
        try {
            conn = CONN_UTIL.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            rs.next();
            user = User.builder()
                    .id(rs.getString("id"))
                    .pwd(rs.getString("pwd"))
                    .name(rs.getString("name"))
                    .regDate(new Date(rs.getDate("regDate").getTime()))
                    .email(rs.getString("email"))
                    .phone(rs.getString("phone"))
                    .age(rs.getInt("age"))
                    .gender(rs.getInt("gender"))
                    .build();
            return Optional.ofNullable(user);
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
            System.out.println("회원정보 조회에 실패 했습니다");
            throw new RuntimeException(e);
        } finally {
            CONN_UTIL.close(rs,pstmt,conn);
        }
    }
}