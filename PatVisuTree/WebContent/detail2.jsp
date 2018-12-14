<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
	
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
	
<%@ page import="example.DbLoginData"%>
<%@page import="java.net.URLEncoder"%>

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

<c:set var="cls" value="${requestScope.cls}" />
<c:set var="idx" value="${requestScope.idx}" />
<c:set var="tmpDetail" value="${requestScope.tmpDetail}" />
<c:set var="pa" value="${requestScope.pa}" />
<c:set var="pg" value="${requestScope.pg}" />
<c:set var="keyword" value="${requestScope.keyword}" />
<c:set var="searchOption" value="${requestScope.searchOption}" />
<c:set var="date" value="${requestScope.date}" />
<c:set var="before" value="${requestScope.before}" />
<c:set var="after" value="${requestScope.after}" />
<c:set var="title" value="${requestScope.title}" />


<%
	String pdfAdd = " http://sd.wips.co.kr/wipslink/api/dwn_pdf_dsdirect.wips?skey=";
	String tmpKey = (String)request.getAttribute("keyword");
	
	String keyword = URLEncoder.encode(tmpKey, "UTF-8");
	pageContext.setAttribute("keyword", keyword);
	pageContext.setAttribute("pdfAdd", pdfAdd);
	
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
	<div class="container">
	

		
	<c:set var="tempName" value="${pdfAdd}${tmpDetail.pdfNum}" />		
		
	<div class = 'container' style='margin-left: auto; margin-right: auto;margin-top:50px;'>
		<img src='jpg/Dooho_Logo_2016_1.jpg'style='max-width: 20%;float:left; height: auto;clear:both; align:top; margin-bottom: 20px;' />
		 <span style='height:auto; vertical-align:middle;margin-left:130px;'><font size=5>${title}</font></span><div style='clear:both'></div>
		
		<table class = "type05" style="margin-left: auto; margin-right: auto;">
			

			<caption><a href ='ProcessSearch?pg=${pg}&cls=${cls}&idx=${idx}&searchOption=${searchOption}&date=${date}&keyword=${keyword}&before=${before}&after=${after}'><h5 style='float: right;'>뒤로 가기</h5></a></caption> 
			
			<tbody>
			<tr>
			<th colspan = 2>${tmpDetail.invName}</th>
			</tr>
		
			<tr>
			<th width='20%'> 국가 </th>
			<td width='95%'>${tmpDetail.natCode}</td>
			</tr>
			
			<tr>
			<th width='20%'> 출원번호(출원일) </th>
			<td width='95%'>${tmpDetail.patNum} (${tmpDetail.appDate})</td>
			</tr>
			
			<tr>
			<th width='20%'> 공개번호(공개일) </th>
			<td>${tmpDetail.openNum} (${tmpDetail.openDate})</td>
			</tr>
			
			<tr>
			<th width='20%'> 등록번호(등록일) </th>
			<td>${tmpDetail.regNum} (${tmpDetail.regDay})</td>
			</tr>
			
			<tr>
			<th width='20%'> 우선권 국가 </th>
			<td>${tmpDetail.preferNat}</td>
			</tr>
			
			<tr>
			<th width='20%'> 우선권 주장일 </th>
			<td>${tmpDetail.preferDate}</td>
			</tr>
			
			<tr>
			<th width='20%'> 출원인 </th>
			<td>${tmpDetail.appName}</td>
			</tr>
			
			<tr>
			<th width='20%'> 발명자 </th>
			<td>${tmpDetail.inventor}</td>
			</tr>
			
			<tr>
			<th width='20%'> 요약 </th>
			<td>${tmpDetail.summary}</td>
			</tr>
			
			<tr>
			<th width='20%'> 대표청구항 </th>
			<td>${tmpDetail.repClaim}</td>
			</tr>
			
			<!-- <tr>
			<th width='20%'> 분석 코멘트 </th>
			<td>"+"-"+"</td>
			</tr> -->
			
			<tr>
			<td colspan = 2 style=\"text-align:center\">
			<a href = '${tmpDetail.detail}' target='_blank'>WIPS 상세페이지 바로가기</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href = '${tempName}' target='_blank'>PDF 다운로드</a>
			</td>
			</tbody>
		
			</table>
			
			</div>
			
		}

  

	</div>

</body>
</html>