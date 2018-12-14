package example;

import java.io.IOException;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class ProcessLogin
 */
@WebServlet("/ProcessLogin")
public class ProcessLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private String adminId = "doohopatmap";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProcessLogin() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doPost(request, response);

		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=euc-kr");

		String cmd = request.getParameter("cmd");

		System.out.println(cmd);
		if (cmd == null || cmd.equals("processLogin")) {
			try {
				processLogin(request, response);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
	}
	
	public void processLogin(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, ClassNotFoundException, SQLException {
		HttpSession session = request.getSession();

		boolean loginCount = false ;
	    
	    String pw = request.getParameter("pw");
		String serial = null;

		String sqlURL = "jdbc:mysql://"+DbLoginData.url+"?serverTimezone=UTC";
		Connection conn = null;
		String id = DbLoginData.id;
		String pwod = DbLoginData.pwd;
		String query = "show databases;";
		Class.forName("com.mysql.cj.jdbc.Driver");
		conn = DriverManager.getConnection(sqlURL, id, pwod);
		Statement stmt = conn.createStatement();
		ResultSet rs = null;
		
		if(pw.equals("doohopatmap")) {
			response.sendRedirect("upload.jsp");
		}
		
		stmt.executeQuery("use "+DbLoginData.dbName+";");
		
		query = "select serial from serialNum where passWords = '"+ pw +"'" ;
		System.out.println(query);					
		rs = stmt.executeQuery(query);
		
		if (rs.next()) {
			serial = rs.getString(1);
			
		}
		
	    // 2: 사용자 인증
	    String redirectUrl = null; // 인증 실패시 재요청 될 url 
	    
	    if(serial != null ){
	    	session.setAttribute("signedUser", pw); // 인증되었음 세션에 남김
	    	session.setAttribute("uid", serial);
	       	redirectUrl = "ProcessView"; 
	       	request.setAttribute("cmd", "viewTree");
	        loginCount = true;
	        response.sendRedirect(redirectUrl);
	    }
	  	 
	      
	   
	    
	    if(loginCount == false){ 
	    	response.getWriter().println("<script> alert(\"로그인 실패\"); history.go(-1);</script>");
	    	/*response.getWriter().println("<script>alert('시리얼 넘버 성공적으로 입력');location.href='./upload.jsp';</script>");*/
	    }
	}

}
