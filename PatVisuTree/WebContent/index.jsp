<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
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
<%
	session.invalidate();
%>

<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>DooHo Patent Map Viewer</title>
</head>

<body>

	<div class="container">
	  <div class="outer">
	    <div class="inner">
	      <div class="centered">
	      <img src = "jpg/Dooho_Logo_2016_1.jpg" style="max-width: 60%; height: auto; margin-bottom: 50px;"/>
	        <form  action="ProcessLogin" method="post">
			            <label>PW: </label>
			            <input name="cmd" type="hidden" value="processLogin">
			            <input name="pw" type="password">&nbsp;
			            <input type="submit" value="로그인">
			      </form>
	      </div>
	    </div>
	  </div>
	</div>
	

</body>
</html>