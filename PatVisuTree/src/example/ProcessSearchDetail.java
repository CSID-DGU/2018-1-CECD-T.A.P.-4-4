package example;

import java.io.IOException;
import java.sql.Connection;
import java.sql.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class ProcessSearchDetail
 */
@WebServlet("/ProcessSearchDetail")
public class ProcessSearchDetail extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProcessSearchDetail() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/x-json; charset=utf-8");

		String cmd = request.getParameter("cmd");

		System.out.println(cmd);
		if (cmd == null || cmd.equals("viewSearchDetail")) {
			try {
				viewSearchDetail(request, response);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
	}
	
	public void viewSearchDetail(HttpServletRequest request, HttpServletResponse response) throws SQLException, ClassNotFoundException, ServletException, IOException {
		HttpSession session = request.getSession();
		
		DetailDataSet tmpDetail = new DetailDataSet();
		
		String tle =null;
		
		
		String searchOption = request.getParameter("searchOption");
		String keyWord = request.getParameter("keyword");
		String date = request.getParameter("date");
		String before = request.getParameter("before");
		String after = request.getParameter("after");
		
		System.out.println(keyWord+"vvvzxcx");
		
		String idx = request.getParameter("idx");
		String cls = request.getParameter("cls");
		String uid = (String) session.getAttribute("uid");
		String pa = request.getParameter("page");
		String pg = request.getParameter("pg");
		
			String pdfNum = request.getParameter("pdfNum");
		String patNum = request.getParameter("patNum");
		
		String table = "visualization";
		String table2 = "tree";
		int total = 0;
		
		String sqlURL = "jdbc:mysql://"+DbLoginData.url+"?serverTimezone=UTC";
		Connection conn = null;
		String id = DbLoginData.id;
		String pw = DbLoginData.pwd;
		String query = "show databases;";
		Class.forName("com.mysql.cj.jdbc.Driver");
		conn = DriverManager.getConnection(sqlURL, id, pw);
		Statement stmt = conn.createStatement();
		ResultSet rs =null;
		
		stmt.executeQuery("use "+DbLoginData.dbName+";");

		String sql = null;
		
		sql = "select fName from fileName where serial = '"+uid+"'";
		rs = stmt.executeQuery(sql);
		if(rs.next()){
			tle =  rs.getString("fName");
		}
		
		sql = "select distinct * from " + table +" as S, " + table2 + " as B where S.patNum = '"+patNum+"' and S.patNum = B.patNum";
		rs = stmt.executeQuery(sql);
		System.out.println(sql);
		 java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
		 Timestamp tmp = null;
		 
		if(rs.next()){
			/* int num = rs.getInt(1); */
			String invName = rs.getString("invName");
			String natCode = rs.getString("natCode");
			String regNum = rs.getString("regNum");
			if(regNum.equals("")){
				regNum="-";
			}
			String openNum = rs.getString("openNum");
			if(openNum.equals("")){
				openNum="-";
			}
			String appName = rs.getString("appName");
			
			tmp = rs.getTimestamp("appDate");
			String appDate = null;
			if(tmp == null){
				appDate = "-";
			} else{
				appDate = formatter.format(tmp);
			}
			
			tmp = rs.getTimestamp("regDay");
			String regDay = null;
			if(tmp == null){
				regDay = "-";
			} else{
				regDay = formatter.format(tmp);
			}
			
			 
			String preferDate = rs.getString("preferDate");
			if(preferDate.equals("")){
				preferDate="-";
			}
			
			tmp = rs.getTimestamp("openDate");
			String openDate = null;
			if(tmp == null){
				openDate = "-";
			} else{
				openDate = formatter.format(tmp);
			}
			
			String summary = rs.getString("summary");
			String repClaim = rs.getString("repClaim");
			String detail = rs.getString("detail");
			
			String inventor = rs.getString("inventor");
			if(inventor.equals("")){
				inventor="-";
			}
			
			String preferNatCode = rs.getString("preferNat");
			if(preferNatCode.equals("")){
				preferNatCode="-";
			}
			
			String pdfN = rs.getString("pdfNum");
			
			tmpDetail.setInvName(invName);
			tmpDetail.setAppDate(appDate);
			tmpDetail.setAppName(appName);
			tmpDetail.setDetail(detail);
			tmpDetail.setNatCode(natCode);
			tmpDetail.setOpenDate(openDate);;
			tmpDetail.setOpenNum(openNum);
			tmpDetail.setPdfNum(pdfN);
			tmpDetail.setRegDay(regDay);
			tmpDetail.setRegNum(regNum);
			tmpDetail.setInventor(inventor);
			tmpDetail.setSummary(summary);
			tmpDetail.setRepClaim(repClaim);
			tmpDetail.setPreferNat(preferNatCode);
			tmpDetail.setPreferDate(preferDate);
			tmpDetail.setPatNum(patNum);
		}
		
		request.setAttribute("pa", "ProcessSearch");
		request.setAttribute("total", total);
		request.setAttribute("pg", pg);
		request.setAttribute("tmpDetail", tmpDetail);
		request.setAttribute("idx", idx);
		request.setAttribute("cls", cls);
		request.setAttribute("title", tle);
		
		request.setAttribute("searchOption", searchOption);
		request.setAttribute("keyword", keyWord);
		request.setAttribute("date", date);
		request.setAttribute("before", before);
		request.setAttribute("after", after);
	
		RequestDispatcher dispatcher = request.getRequestDispatcher("detail2.jsp");

		dispatcher.forward(request, response);
	}

}
