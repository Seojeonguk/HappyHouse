<%@page import="java.sql.Statement"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
		request.setCharacterEncoding("UTF-8");
	
	String id = request.getParameter("id");
	String pw = request.getParameter("pw");
	String name = request.getParameter("name");
	String addr = request.getParameter("addr");
	String tel = request.getParameter("tel");
	Class.forName("com.mysql.cj.jdbc.Driver");
	
	try
	{
	 String connUrl = "jdbc:mysql://127.0.0.1:3306/haapyhouse?erverTimezone=UTC&useUniCode=yes&characterEncoding=UTF-8";
	 String user = "ssafy";
	 String pwd = "ssafy";
	 String sql="insert into user_info (user_id,user_pw, user_name, user_addr, user_tel )values('" + id + "','" + pw + "','" + name +"','" + addr + "','" + tel +"')";


	Connection conn = DriverManager.getConnection(connUrl, user, pwd);
	Statement st=conn.createStatement();

	st.executeUpdate(sql);
	out.println("<script>");
	out.println("location.href='loginForm.jsp'");
	out.println("</script>");

	}
	catch(Exception e)
	{
	System.out.print(e);
	e.printStackTrace();
	}


	%>
</body>
</html>