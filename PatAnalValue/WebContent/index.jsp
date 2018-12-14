<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page
	import="com.oreilly.servlet.MultipartRequest,
				com.oreilly.servlet.multipart.DefaultFileRenamePolicy,
				javax.json.*,
				javax.servlet.*,
				java.io.*,
				java.util.*,
				java.text.*,
				org.apache.poi.ss.usermodel.*,
				org.apache.poi.hssf.usermodel.*,
				org.apache.poi.openxml4j.opc.OPCPackage,
				org.apache.poi.xssf.usermodel.*,
				java.sql.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Upload Parsing</title>
<link rel="stylesheet" type="text/css" href="./style/style.css">
<link rel="stylesheet" type="text/css" href="./style/table.css">
<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="./css/bootstrap.css?ver=1">
<script src="./js/implements/createDIVDynamically.js"></script>
<script src="./js/implements/createSVGDynamically.js"></script>
<!-- 데이터 visualization 할 수 있는 graph를 그려주는 d3.js 의 축약형 라이브러리 -->
<script src="./js/plugins/D3jsVersion4/d3.v4.min.js" charset="utf-8"></script>
<script src="./js/plugins/jQuery3.1.0/jquery.min.js" charset="utf-8"></script>
<!-- 도넛을 3d화 시켜서 보여주기 위한 간단한 plugin -->
<script src="./js/plugins/Donut3D/Donut3D.js"></script>
<!-- 5번쨰 시트의 표를 이미지로 바꿔주는 html2canvas 라이브러리(추가 예정) -->
<script src="./js/plugins/html2canvas/html2canvas.js"></script>
</head>
<body>
	<%
		int line_num = 0, i = 0;
		int pivotCount = 5;
		int tabCount = 11;
		
		
		
		
		
		
		
		%>
	<div class="container"
		style="margin-left: auto; margin-right: auto; margin-top: 50px;">

		<a href="index.jsp"><img src="jpg/Dooho_Logo_2016_1.jpg"
			style="max-width: 20%; float: lef
			t; height: auto; clear: both; align: top; margin-bottom: 20px;" />
		</a>
		<div id="Chart11" class="general"></div>



		<script src="./js/implements/CircleGrouping.js" charset="utf-8"></script>
		<%
			int numberOfDiv = 3;
		String check = (String)request.getAttribute("jsonName");
		String jsonName = null;
		
		
		if(check != null){
			jsonName =  "./"+check;
		} else {
			jsonName = "./tmp.json";
		} 
		System.out.println(jsonName+"JJJ");
		pageContext.setAttribute("jsonName", jsonName);
			String svg1, json1, svg2, json2;
			%>

		<script type="text/javascript">
					var jsonName ="${jsonName}";
						divCreation('Chart11', 'sheet11Chart'+'<%=numberOfDiv%>');
						svgCreation('sheet11Chart'+'<%=numberOfDiv%>', '특허 사용 별 분류', 960, 960, 'svgExample');
						<%numberOfDiv+=1;%>
						divCreation('Chart11', 'sheet11Chart'+'<%=numberOfDiv%>');
						CircleGrouping_sheet11('svgExample', 'sheet11Chart'+'<%=numberOfDiv%>',jsonName);
					</script>
		<script src="./js/implements/CircleGrouping.js" charset="utf-8"></script>

		<script src="./js/implements/TabCtrl.js" charset="utf-8"></script>
		<br>
		<br>
		<div style="flat: right;">
			<form name="form1" method="get" action="ProcessSearch">
				<input name="keyword" type="text"> <input type="submit"
					value="조회">
			</form>
		</div>
</body>
</html>