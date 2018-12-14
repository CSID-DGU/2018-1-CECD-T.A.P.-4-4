
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ page import="example.DbLoginData"%>
<%@ page import="example.ListDataSet"%>
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



<c:set var="setup" value="${requestScope.setup}" />
<c:set var="cls" value="${requestScope.cls}" />
<c:set var="idx" value="${requestScope.idx}" />
<c:set var="dataList" value="${requestScope.dataList}" />
<c:set var="arrow" value="${requestScope.arrow}" />
<c:set var="title" value="${requestScope.title}" />

<%
	String pSorted_index = (String) request.getAttribute("pSorted_index");
	String pADS = (String) request.getAttribute("pADS");

	String pdfAdd = "http://sd.wips.co.kr/wipslink/api/dwn_pdf_dsdirect.wips?skey=";

	final int ROWSIZE = 20;
	final int BLOCK = 5;

	int total = (int) request.getAttribute("total");
	int pg = 1;

	if (request.getParameter("pg") != null) {
		pg = Integer.parseInt(request.getParameter("pg"));
	}

	int start = (pg * ROWSIZE) - (ROWSIZE - 1);
	int end = (pg * ROWSIZE);

	int allPage = 0;

	int startPage = ((pg - 1) / BLOCK * BLOCK) + 1;
	int endPage = ((pg - 1) / BLOCK * BLOCK) + BLOCK;

	allPage = (int) Math.ceil(total / (double) ROWSIZE);

	if (endPage > allPage) {
		endPage = allPage;
	}

	pageContext.setAttribute("pSorted_index", pSorted_index);
	pageContext.setAttribute("pADS", pADS);
	pageContext.setAttribute("pdfAdd", pdfAdd);
	pageContext.setAttribute("total", total);
	pageContext.setAttribute("pg", pg);
%>




<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>DooHo Patent Map Viewer</title>
<link rel="stylesheet" href="css/table.css?ver=1">
<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css">
<link
	href="//netdna.bootstrapcdn.com/twitter-bootstrap/2.3.2/css/bootstrap-combined.no-icons.min.css"
	rel="stylesheet">
<script
	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
<script src="http://code.jquery.com/jquery-1.10.2.js"></script>


<script type="text/javascript">
	$(document).ready(function() {
		var arrow = '<c:out value = "${arrow}"/>';
		var pADS = '<c:out value="${pADS}"/>';
		if ($(arrow).length > 0) {
			$(arrow).each(function() {
				var a = $(this), inverse = false;
				if (pADS == 'desc') {
					
					inverse = false;
				} else {
					
					inverse = true;
				}

				setIcon(this, inverse);

			});

		}

	});
	function setIcon(element, inverse) {
		var iconSpan = $(element).find('span');
		if (inverse) {
			
			$(iconSpan).removeClass();
			$(iconSpan).addClass("glyphicon glyphicon-arrow-up");
		} else {
			$(iconSpan).removeClass();
			$(iconSpan).addClass("glyphicon glyphicon-arrow-down");
		}
		$(element).siblings().find('span').removeClass();
	}
</script>
</head>
<body>



	<div class="container"
		style="margin-left: auto; margin-right: auto; margin-top: 50px;">


		<img src="jpg/Dooho_Logo_2016_1.jpg"
			style="max-width: 20%; float: left; height: auto; clear: both; align: top; margin-bottom: 20px;" />
		<span
			style="height: auto; vertical-align: middle; margin-left: 130px;">
			<font size=5> ${title}</font>
		</span>

		<div style="clear: both"></div>

		<table class="type11" style="margin-left: auto; margin-right: auto;">
			<caption>
				<h5 style="float: left;">${setup}</h5>
				<a href="ProcessView"><h5 style="float: right;">전체 트리 보기</h5></a>
			</caption>

			<thead>
				<tr>
					<th scope="cols" width="5%">NO.</th>
					<th scope="cols" width="5%"><a id="natCode"
						href="ProcessList?pg=1&idx=${idx}&cls=${cls}&wSorted_index=natCode&pSorted_index=${pSorted_index}&pADS=${pADS}"><font
							color='white'>국가코드</font><span style="color: white;"
							aria-hidden="true"></a></a></th>
					<th scope="cols" width="10%"><a id= "patNum"
						href="ProcessList?pg=1&idx=${idx}&cls=${cls}&wSorted_index=patNum&pSorted_index=${pSorted_index}&pADS=${pADS}"><font
							color='white'>출원번호</font><span style="color: white;"
							aria-hidden="true"></a></a></th>
					<th scope="cols" width="7%"><a id = "state"
						href="ProcessList?pg=1&idx=${idx}&cls=${cls}&wSorted_index=state&pSorted_index=${pSorted_index}&pADS=${pADS}"><font
							color='white'>상태</font><span style="color: white;"
							aria-hidden="true"></a></a></th>
					<th scope="cols" width="30%"><a id="invName"
						href="ProcessList?pg=1&idx=${idx}&cls=${cls}&wSorted_index=invName&pSorted_index=${pSorted_index}&pADS=${pADS}"><font
							color='white'>발명의 명칭</font><span style="color: white;"
							aria-hidden="true"></a></span></th>
					<th scope="cols" width="10%"><a id="appName"
						href="ProcessList?pg=1&idx=${idx}&cls=${cls}&wSorted_index=appName&pSorted_index=${pSorted_index}&pADS=${pADS}"><font
							color='white'>출원인</font><span style="color: white;"
							aria-hidden="true"></span></a></th>
					<th scope="cols" width="10%"><a id = "appDate"
						href="ProcessList?pg=1&idx=${idx}&cls=${cls}&wSorted_index=appDate&pSorted_index=${pSorted_index}&pADS=${pADS}"><font
							color='white'>출원일</font><span style="color: white;"
							aria-hidden="true"></a></a></th>
					<th scope="cols" width="10%"><a id = "openNum"
						href="ProcessList?pg=1&idx=${idx}&cls=${cls}&wSorted_index=openNum&pSorted_index=${pSorted_index}&pADS=${pADS}"><font
							color='white'>공개번호 </font><span style="color: white;"
							aria-hidden="true"></a></a><br>(공개일)</th>
					<th scope="cols" width="10%"><a id="regNum"
						href="ProcessList?pg=1&idx=${idx}&cls=${cls}&wSorted_index=regNum&pSorted_index=${pSorted_index}&pADS=${pADS}"><font
							color='white'>등록번호</font><span style="color: white;"
							aria-hidden="true"></a> </a><br>(등록일)</th>
					<th scope="cols" width="5%">pdf</th>
				</tr>
			</thead>

			<tbody>
				<c:if test="${total eq 0 }">
					<tr align="center" bgcolor="#FFFFFF" height="30">
						<td colspan="10">등록된 글이 없습니다.</td>
					</tr>
				</c:if>

				<c:if test="${total ne 0 }">
					<c:forEach var="v" begin="0" end="${fn:length(dataList)-1}"
						step="1">
						<tr>
							<td>${dataList[v].num}</td>
							<td>${dataList[v].natCode}</td>
							<td>${dataList[v].patNum}</td>
							<td>${dataList[v].state}</td>
							<td><a
								href="ProcessDetail?patNum=${dataList[v].patNum}&idx=${idx}&cls=${cls}&page=list&pg=${pg}&pSorted_index=${pSorted_index}&pADS=${pADS}">${dataList[v].invName}</a></td>
							<td>${dataList[v].appName}</td>
							<td>${dataList[v].appDate}</td>
							<td>${dataList[v].openNum}<br>(${dataList[v].openDay})
							</td>
							<td>${dataList[v].regNum}<br>(${dataList[v].regDay})
							</td>
							<c:set var="tempName" value="${pdfAdd}${dataList[v].pdfNum}" />
							<td>
								<button type="button" class="btn btn-default" aria-label="pdf"
									onclick="window.open('${tempName}') ">
									<span class="glyphicon glyphicon-file" aria-hidden="true"></span>
								</button>
							</td>
						</tr>
					</c:forEach>
				</c:if>

			</tbody>
		</table>


		<div class="text-center">

			<form name="form1" method="get" action="ProcessSearch">
				<select name="searchOption">
					<option value="key">키워드</option>
					<option value="app">출원인</option>
				</select> <input name="keyword" type="text"> &nbsp;&nbsp;&nbsp;&nbsp;
				<select name="date">
					<option value="not">---</option>
					<option value="reg">등록일</option>
					<option value="app">출원일</option>
					<option value="writer">공개일</option>
				</select> <input type="month" name="before"> ~ <input type="month"
					name="after"> <input type="submit" value="조회"> <input
					type="hidden" name="cls" value=${cls } /> <input type="hidden"
					name="idx" value=${idx } />
			</form>
			<ul class="pagination">
				<%
					if (pg > BLOCK) {
				%>
				<li><a
					href="ProcessList?pg=1&idx=${idx}&cls=${cls}&wSorted_index=${pSorted_index}&wADS=${pADS}&pSorted_index=${pSorted_index}&pADS=${pADS}">◀◀</a></li>
				<li><a
					href="ProcessList?pg=<%=startPage - 1%>&idx=${idx}&cls=${cls}&wSorted_index=${pSorted_index}&wADS=${pADS}&pSorted_index=${pSorted_index}&pADS=${pADS}">◀</a></li>
				<%
					}
				%>

				<%
					for (int i = startPage; i <= endPage; i++) {
						if (i == pg) {
				%>

				<li><a href="#"><%=i%></a></li>
				<%
					} else {
				%>
				<li><a
					href="ProcessList?pg=<%=i%>&idx=${idx}&cls=${cls}&wSorted_index=${pSorted_index}&wADS=${pADS}&pSorted_index=${pSorted_index}&pADS=${pADS}"><%=i%></a></li>

				<%
					}
					}
				%>

				<%
					if (endPage < allPage) {
				%>
				<li><a
					href="ProcessList?pg=<%=endPage + 1%>&idx=${idx}&cls=${cls}&wSorted_index=${pSorted_index}&wADS=${pADS}&pSorted_index=${pSorted_index}&pADS=${pADS}">▶</a></li>
				<li><a
					href="ProcessList?pg=<%=allPage%>&idx=${idx}&cls=${cls}&wSorted_index=${pSorted_index}&wADS=${pADS}&pSorted_index=${pSorted_index}&pADS=${pADS}">▶▶</a></li>
				<%
					}
				%>
			</ul>
		</div>


	</div>


</body>
</html>