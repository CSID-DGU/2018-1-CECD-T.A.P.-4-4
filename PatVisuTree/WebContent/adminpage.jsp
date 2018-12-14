<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<%@ page import="example.DbLoginData"%>
<%@ page
	import="com.oreilly.servlet.MultipartRequest,
				com.oreilly.servlet.multipart.DefaultFileRenamePolicy,
				javax.json.*,
				javax.servlet.*,
				java.io.*,
				java.util.*,
				org.apache.poi.ss.usermodel.*,
				org.apache.poi.hssf.usermodel.*,
				org.apache.poi.openxml4j.opc.OPCPackage,
				org.apache.poi.xssf.usermodel.*,
				java.sql.*"%>
<%@ page import="java.sql.*,java.text.SimpleDateFormat,java.util.Date"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>DooHo Patent Map Viewer</title>
<link rel="stylesheet" href="css/table.css?ver=1">
<link rel="stylesheet" href="css/align.css?ver=1">
<link rel="stylesheet" href="css/bootstrap.css">

<%
	String sqlURL = "jdbc:mysql://"+DbLoginData.url+"?serverTimezone=UTC";
	Connection conn = null;
	String id = DbLoginData.id;
	String pwod = DbLoginData.pwd;
	String query = "show databases;";
	Class.forName("com.mysql.cj.jdbc.Driver");
	conn = DriverManager.getConnection(sqlURL, id, pwod);
	Statement stmt = conn.createStatement();
	ResultSet rs = null;
	
	String fName = null;
	String serial = null;
	stmt.executeQuery("use "+DbLoginData.dbName+";");
	
	query = "select * from fileName";
	
	rs = stmt.executeQuery(query);
	
	while (rs.next()) {
		fName = rs.getString(1);
		serial = rs.getString(2);
		
	}
%>

<div class="container" style="margin-left: auto; margin-right: auto;">


	<img src="jpg/Dooho_Logo_2016_1.jpg"
		style="max-width: 20%; height: auto; margin-top: 30px;" />

	<table class="type11" style="margin-left: auto; margin-right: auto;">
		<caption style="text-align: center;">
			<h3>관리자 페이지</h3>
		</caption>
		
		<caption>
			<a href='upload.jsp'><h5 style="float:left;">업로드 페이지</h5></a>
			<div style="float:right;">
				<form action="ProcessSerial" method="post">
				<label>PW: </label> <input name="pw" type="password">&nbsp;&nbsp;&nbsp;
				<label>serialNum 입력: </label> <input name="serial" type="password">&nbsp;
				<input type="submit" value="입력">
				</form>
				</div>
		</caption>

		<thead>
			<tr>
				<th scope="cols" width="30">제목</th>
				<th scope="cols" width="70%">serial코드</th>

			</tr>
		</thead>

		<tbody>
			<%-- <%
					if (total == 0) {
				%>
				<tr align="center" bgcolor="#FFFFFF" height="30">
					<td colspan="10">등록된 글이 없습니다.</td>
				</tr>

				<%
					} else { --%>

			<%query = "select * from fileName";
	
							rs = stmt.executeQuery(query);
							
							while (rs.next()) {
								fName = rs.getString(1);
								serial = rs.getString(2);
								
							 %>
			<!-- %> -->

			<tr>
				<td><%=fName%></td>
				<td><%=serial%></td>

			</tr>
			<%
					
					}
				%>

		</tbody>
	</table>
</head>
<body>

</body>
</html>