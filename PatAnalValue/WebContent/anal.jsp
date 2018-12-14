<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


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


<c:set var="investValue" value="${requestScope.investValue}" />
<c:set var="investAttention" value="${requestScope.investAttention}" />
<c:set var="averValue" value="${requestScope.averValue}" />
<c:set var="flowV" value="${requestScope.flowV}" />
<c:set var="aver" value="${requestScope.aver}" />
<c:set var="name" value="${requestScope.name}" />


<%
	String averValueColor = (String) request.getAttribute("averValueColor");
	String investAttentionColor = (String) request.getAttribute("investAttentionColor");
	String investValueColor = (String) request.getAttribute("investValueColor");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>DooHo Patent Map Viewer</title>
<link rel="stylesheet" href="css/table.css?ver=1">
<link rel="stylesheet" href="css/bootstrap.css">
</head>

<body>

	<div class="container"
		style='margin-left: auto; margin-right: auto; margin-top: 50px;'>
		<img src='jpg/Dooho_Logo_2016_1.jpg'
			style='max-width: 20%; float: left; height: auto; clear: both; align: top; margin-bottom: 20px;' />

		<div style='clear: both'></div>

		<table class="type05" style="margin-left: auto; margin-right: 250px;">

			<caption>
				<a href='index.jsp'><h5 style='float: right;'>뒤로 가기</h5></a>
			</caption>

			<tbody>

				<tr>
					<th width='20%'>산업 이름</th>
					<td width='95%' colspan=2>${name}</td>
				</tr>
				<tr>
					<th width='20%'>가중 평균</th>
					<td width='95%' colspan=2>${aver}</td>
				</tr>

				<tr>
					<th width='20%'>흐름값</th>
					<td width='95%' colspan=2>${flowV}</td>
				</tr>


				<tr>
					<th width='20%' rowspan=4>가중 평균 기준 분석</th>
				<tr>
					<th width='30%'>개별특허 평균가치</th>
					<%
						if (averValueColor.equals("red")) {
					%>
					<td><span style="color: red;">${averValue}</span></td>
					<%
						} else {
					%>
					<td><span style="color: blue;">${averValue}</span></td>
					<%
						}
					%>
				</tr>
				<tr>
					<th width='30%'>산업 주목도</th>
					<%
						if (averValueColor.equals("red")) {
					%>
					<td><span style="color: red;">${averValue}</span></td>
					<%
						} else {
					%>
					<td><span style="color: blue;">${averValue}</span></td>
					<%
						}
					%>
				</tr>
				<tr>
					<th width='30%'>산업 발달도</th>
					<%
						if (averValueColor.equals("red")) {
					%>
					<td><span style="color: red;">${averValue}</span></td>
					<%
						} else {
					%>
					<td><span style="color: blue;">${averValue}</span></td>
					<%
						}
					%>
				</tr>

				</tr>

				<tr>
					<th width='20%' rowspan=3>흐름값 기준 분석</th>
				<tr>
					<th width='30%'>투자 집중도</th>
					<%
						if (investAttentionColor.equals("red")) {
					%>
					<td><span style="color: red;">${investAttention}</span></td>
					<%
						} else {
					%>
					<td><span style="color: blue;">${investAttention}</span></td>
					<%
						}
					%>
				</tr>
				<tr>
					<th width='30%'>산업 관심 향상도</th>
					<%
						if (investAttentionColor.equals("red")) {
					%>
					<td><span style="color: red;">${investAttention}</span></td>
					<%
						} else {
					%>
					<td><span style="color: blue;">${investAttention}</span></td>
					<%
						}
					%>
				</tr>
				</tr>

				<tr>
					<th width='20%' rowspan=3>최종 분석</th>
				<tr>
					<th width='30%'>투자 가치</th>
					<%
						if (investValueColor.equals("red")) {
					%>
					<td><span style="color: red;">${investValue}</span></td>
					<%
						} else {
					%>
					<td><span style="color: blue;">${investValue}</span></td>
					<%
						}
					%>
				</tr>
				<tr>
					<th width='30%'>발전 가능성</th>
					<%
						if (investValueColor.equals("red")) {
					%>
					<td><span style="color: red;">${investValue}</span></td>
					<%
						} else {
					%>
					<td><span style="color: blue;">${investValue}</span></td>
					<%
						}
					%>
				</tr>

				</tr>

				<!-- <tr>
			<th width='20%'> 분석 코멘트 </th>
			<td>"+"-"+"</td>
			</tr> -->


			</tbody>

		</table>

	</div>







</body>
</html>