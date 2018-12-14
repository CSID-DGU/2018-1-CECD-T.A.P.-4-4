<%@page import="example.UploadHelper"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ page import="example.DataSet"%>
<%@ page import="example.NationDataSet"%>
<%@ page import="example.DataSetYear"%>
<%@ page import="example.DbLoginData" %>

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

<c:set var="big" value="${requestScope.big}"/>
<c:set var="middle" value="${requestScope.middle}"/>
<c:set var="bExpList" value="${requestScope.bExpList}"/>
<c:set var="sizeList" value="${requestScope.sizeList}"/>
<c:set var="title" value="${requestScope.title}"/>
<%-- <c:set var="printMiddle" value="${requestScope.printMiddle}"/>
<c:set var="mPrintExpList" value="${requestScope.mPrintExpList}"/> --%>


<%
	
	int k = 0;
	int size = 0;
	int printSmallSize = 0;
	int printMiddleSize = 0;
	
	List<List<String>> mPrintExpList = (List<List<String>>)request.getAttribute("mPrintExpList");
	List<List<String>> printMiddle = (List<List<String>>)request.getAttribute("printMiddle");
	List<List<String>> printSmall = (List<List<String>>)request.getAttribute("printSmall");
	List<List<String>> printExpList = (List<List<String>>)request.getAttribute("printExpList");
	
	size = (int)request.getAttribute("size");
	String uid = (String)session.getAttribute("uid");
	pageContext.setAttribute("uid", uid);
	

	pageContext.setAttribute("printMiddle", printMiddle.get(k));
    pageContext.setAttribute("mPrintExpList", mPrintExpList.get(k));
    pageContext.setAttribute("printSmall", printSmall.get(k));
    pageContext.setAttribute("printExpList", printExpList.get(k));
	
    printSmallSize = printSmall.get(k).size();
    printMiddleSize = printMiddle.get(k).size();
 
	
%>
<c:set var="uid" value="${pageScope.uid}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<meta charset="utf-8">
<head>

<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>DooHo Patent Map Viewer</title>
<link rel="stylesheet" href="css/table.css?ver=1">
<link rel="stylesheet" href="css/align.css?ver=1">
<link rel="stylesheet" href="css/bootstrap.css">


</head>
<body>
	
		
		<div class="container" style="margin-left: auto; margin-right: auto;margin-top:50px; ">

		
		<img src = "jpg/Dooho_Logo_2016_1.jpg" style=" display:inline-block;max-width: 20%;float:left;vertical-align:middle; height: auto; margin-bottom:20px"/>
		
		 <span style='height:auto; vertical-align:middle;margin-left:130px;'><font size=5>${title}</font></span><div style='clear:both'></div>
		

		<table class = "type11" style="margin-left: 0; margin-right: auto;">
		<caption style="text-align: center;">
		<h3>기술트리</h3>
		</caption>
		<thead>
		<tr>
		<th width="150px"> 대분류 </th>
		<th width="150px"> 중분류 </th>
		<th width="250px"> 소분류 </th>
		<th width="500px"> 기술설명</th>
		</tr>
		</thead>

		<tbody>
		
		<c:forEach var="x" begin="0" end="${fn:length(big)-1}" step="1">
			
			<tr>
			<td rowspan = <%=size%> >
				<a href= "ProcessList?idx=${big[x]}&cls=b">${bExpList[x]}</a><br>(${big[x]})
			</td>
		
			

			<c:forEach var="y" begin="0" end="<%=printMiddleSize-1 %>" step="1">
			
				
				<tr>
				<td rowspan =<%=printSmallSize %>>
					<a href="ProcessList?idx=${middle[y]}&cls=m">${mPrintExpList[y]}</a><br>(${printMiddle[y]})
				</td>
				<c:if test = "${fn:length(printSmall) ne 0}" >
					<td>
					<a href="ProcessList?idx=${printSmall[0]}&cls=s">
							${printExpList[0]}</a>
							<br>(${printSmall[0]})
					</td>
					<td>
							${printExpList[0]}
					</td>
					</tr>
					
					<c:forEach var="v" begin = "1" end="${fn:length(printSmall)-1}" step="1">
						<td>
						<a href="ProcessList?idx=${printSmall[v]}&cls=s">
								${printExpList[v]}</a>
								<br>(${printSmall[v]})
						</td>
						<td>
								${printExpList[v]}
						</td>
						</tr>
					</c:forEach>
				
				</c:if>
				
				<c:if test =" ${fn:length(printSmall) eq 0 }">
					<td>  </td>
					<td>  </td>
					</tr>
				</c:if>
				
		
				
				<c:if test = "${y ne  fn:length(printMiddle)-1 }">
					<%	
						k++;
					    pageContext.setAttribute("printSmall", printSmall.get(k));
					    pageContext.setAttribute("printExpList", printExpList.get(k));
						
					    printSmallSize = printSmall.get(k).size();
						
					%>
				</c:if>
			 
				</c:forEach>
				
				<!-- 두줄인경우 여기에 코드 삽입 -->	
			</c:forEach>
				
			
		
		
	
		
 <!-- 
		for (int j = 0; j < big.size(); j++) {
			
			
			
			for (int i = 0; i < printMiddle.size(); i++) {
				
				<tr>
				<td rowspan =" + printSmall.size() + " >" + "<a href='" + next + "?idx=" + middle.get(i)
						+ "&cls=m&uid="+uid+"'>" +mPrintExpList.get(i)+"</a><br>("+printMiddle.get(i)+")" + "</td>
				if (printSmall.size() != 0) {
					<td>" + "<a href='" + next + "?idx=" + printSmall.get(0) + "&cls=s&uid="+uid+"'>"
							+ printExpList.get(0)+"</a><br>("+ printSmall.get(0) + ")"+ "</td>
					<td>" + printExpList.get(0) + "</td>
					</tr>
					for (int v = 1; v < printSmall.size(); v++) {
						<tr>
						<td>" + "<a href='" + next + "?idx=" + printSmall.get(v) + "&cls=s&uid="+uid+"'>"
								+ printExpList.get(v) +"</a><br>("+ printSmall.get(v) + ")"+ "</td>
						<td>" + printExpList.get(v) + "</td>
						</tr>

					}
				} else {
					<td>  </td>
					<td>  </td>
					</tr>

				}
				

				while (printSmall.size() != 0) {

					printSmall.remove(0);
				}
				
				while (printExpList.size() != 0) {

					printExpList.remove(0);
				}
				
				

			}
			while (printMiddle.size() != 0) {

				printMiddle.remove(0);
			}

		} -->
	
		</tbody>
		</table>
		</div>

		
	
	
	<form name="bF" action="/list.jsp" method="POST">
		<input type="hidden" name="cls" value="b" />
		<input type="hidden" name="idx" value="''" />
		<input type="hidden" name="idx2" value="'null'" />
	</form>
	
	<form name="mF" action="/list.jsp" method="POST">
		<input type="hidden" name="cls" value="m" />
		<input type="hidden" name="idx" value="''" />
		<input type="hidden" name="idx2" value="'null'" />
	</form>
	
	<form name="sF" action="/list.jsp" method="POST">
		<input type="hidden" name="cls" value="s" />
		<input type="hidden" name="idx" value="''" />
		<input type="hidden" name="idx2" value="'null'" />
	</form>

</body>
</html>