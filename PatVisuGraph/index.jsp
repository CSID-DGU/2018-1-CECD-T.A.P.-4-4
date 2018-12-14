<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style>
	div
	{
		left-margin : 15%;
		top-margin : 15%;
	}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="refresh" content="1800; URL=javascript:window.location.reload(true)">
<title>Excel Upload Parsing</title>
</head>
<body>
	<a href="javascript:window.location.reload(true);"><img src = "./img/dooho_main_logo.jpg"/></a>
	<!--<img src = "./img/dooho_main_left.jpg" />-->
	<!--<img src = "./img/dooho_main_right.jpg" />-->
	<div>
		<br>
		<form action = "upload_excel.jsp" method = "post" enctype = "multipart/form-data">
			파일을 선택하세요(*.xls, *.xlsx)
			<!-- //엑셀 파일만 입력 -->
			<input type="file" name="upExcelFile" id="upExcelFile" accept="application/vnd.ms-excel, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet">
			<input type="submit" value = "확인">
		</form>
	</div>
</body>
</html>