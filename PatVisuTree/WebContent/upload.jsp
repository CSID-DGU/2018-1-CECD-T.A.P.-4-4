<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>


<style>
	.container {
	  width: 100%;
	  height: 100%;
	  margin: 330px auto;
	 
	}
	
		.outer {
	  display: table;
	  width: 100%;
	  height: 100%;
	}
	.inner {
	  display: table-cell;
	  vertical-align: middle;
	  text-align: center;
	}
	
		.centered {
	  position: relative;
	  display: inline-block;
	 
	  width: 50%;
	  padding: 1em;
	 
	}
</style>

<style type="text/css">
 a:link { color: blue; text-decoration: none;}
 a:visited { color: blue; text-decoration: none;}
 a:hover { color: violet; text-decoration: underline;}
</style>


<% int k = 3; %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="refresh" content="1800; URL=javascript:window.location.reload(true)">
<title>DooHo Patent Map Viewer</title>
</head>
<body>

	<div class="container">
	  <div class="outer">
	    <div class="inner">
	      <div class="centered">
	        <a href="index.jsp"><img src = "jpg/Dooho_Logo_2016_1.jpg" style="max-width: 60%; height: auto;margin-bottom: 50px;"/></a>
	
			<form action = "/upload_excel.jsp" method = "post" enctype = "multipart/form-data">
				파일을 선택하세요(*.xls, *.xlsx)
				<!-- //엑셀 파일만 입력 -->
				<br><br>
				<input type="hidden" name="cmd" value="parsingExcel" />
				<input type="file" name="upExcelFile" id="upExcelFile" accept="application/vnd.ms-excel, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet">
				<input type="submit" value = "확인">
				<br><br>
				<a href = adminpage.jsp>serial 관리 페이지</a>
			</form>
	      </div>
	    </div>
	  </div>
	</div>
	
	
</body>
</html>