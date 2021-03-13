<%@page import="java.sql.DriverManager"%>
<%@page import="javax.sql.DataSource"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="javax.naming.Context"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!-- jstl을 사용하기 위해 taglib 지시자 사용 -->

<%
	request.setCharacterEncoding("UTF-8");
	Connection conn = null;
	PreparedStatement pstmt = null;
	String sql = "";
	int updateCount = 0;

	String id = request.getParameter("id");
	String pwd = request.getParameter("pw");
	String name = request.getParameter("name");
	String addr = request.getParameter("addr");
	String tel = request.getParameter("tel");

	sql = "INSERT INTO user_info (user_id, user_pw, user_name, user_addr, user_tel)";
	System.out.println(sql);

	try {
		String connUrl = "jdbc:mysql://127.0.0.1:3306/ssafydb?erverTimezone=UTC&useUniCode=yes&characterEncoding=UTF-8";
		Class.forName("com.mysql.cj.jdbc.Driver");
		conn = DriverManager.getConnection(connUrl, "ssafy", "ssafy");
		pstmt = conn.prepareStatement(sql);

		pstmt.setString(1, id);
		pstmt.setString(2, pwd);
		pstmt.setString(3, name);
		pstmt.setString(4, addr);
		pstmt.setString(5, tel);

		updateCount = pstmt.executeUpdate(); // 변경된 행의 개수

		if (updateCount != 0) {
			System.out.println("회원가입 성공");
		} else {
			System.out.println("회원가입 실패");
		}
	} catch (Exception e) {
		System.out.println("연결에 실패하였습니다.(member_add)");
		out.println("<h3>연결에 실패하였습니다.(member_add)</h3>");
	} finally {
		try {

			pstmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	response.sendRedirect("login.jsp"); // 회원 추가 후 login.jsp 로
%>




<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>