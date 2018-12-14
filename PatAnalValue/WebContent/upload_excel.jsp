<%@page import="helper.UploadHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.oreilly.servlet.MultipartRequest,
				com.oreilly.servlet.multipart.DefaultFileRenamePolicy,
				java.io.*,
				java.util.*,
				javax.servlet.RequestDispatcher,
				javax.servlet.ServletException,
				javax.servlet.http.HttpServlet,
				javax.servlet.http.HttpServletRequest,
				javax.servlet.http.HttpServletResponse" %>
<!-- TODO : loading bar... -->
<%
	String savePath = application.getRealPath(UploadHelper.upDir);


	request.setCharacterEncoding("UTF-8");
	
	boolean isSizeOver = false;
	int fileSize = 10 * 1024 * 1024;
	System.out.println(UploadHelper.upDir);
	//<todo : com.orielly.servlet 의 MultipartRequest 를 이용하여 구현할 것>
	System.out.println(savePath);
	System.out.println(".............1123");
	MultipartRequest multi = new MultipartRequest(request, savePath, fileSize, "UTF-8", new DefaultFileRenamePolicy());
	
	//10MB 설정
	
	
     if(multi == null) {
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write("<script>alert('업로드실패');location.href='/';</script>");
		return;
	}
	if(isSizeOver) {
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write("<script>alert('10MB를 초과하였습니다!');location.href='/';</script>");
		return;
	}
	
	File f = null;
	String arg = "/ProcessExcel";
	
	if((f=multi.getFile("upExcelFile")) != null) {
		String filePath = multi.getFile("upExcelFile").getPath();
		String ext = filePath.substring(filePath.lastIndexOf('.')+1, filePath.length());
		if(ext.equals("xls") || ext.equals("xlsx") || ext.equals("XLSX") || ext.equals("XLS")) {
			 
				request.setAttribute("filePath", filePath);
				request.setAttribute("ext", ext);
				request.setAttribute("cmd", "parsingExcel");
				
				RequestDispatcher rd = request.getRequestDispatcher(arg);
				rd.forward(request, response);
			
		}
		else {
			response.setContentType("text/html;charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write("<script>alert('엑셀파일이 아닙니다. 확장자 : "+ext+"');location.href='./upload.jsp';</script>");
		}
		if(f.exists()) f.delete();
	}
%>