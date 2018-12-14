package example;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class ProcessView
 */
@WebServlet("/ProcessView")
public class ProcessView extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProcessView() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/x-json; charset=utf-8");

		String cmd = (String) request.getAttribute("cmd");

		System.out.println(cmd);
		if (cmd == null || cmd.equals("viewTree")) {
			try {
				viewTree(request, response);
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void viewTree(HttpServletRequest request, HttpServletResponse response) throws SQLException, ClassNotFoundException, ServletException, IOException {
		HttpSession session = request.getSession();
		
		int size = 1;
		String tle =null;
		String se = session.getAttribute("signedUser").toString();
		if(se.equals("ak11")){
			tle = "삼성SDS 홈IOT 플랫폼 도어락 특허 MAP";
		}else{
			tle = "삼성SDS 홈IOT 인터페이스 구축 특허 MAP";
		}
		
		String uid = (String) session.getAttribute("uid");
		System.out.println(uid);
		String sqlURL = "jdbc:mysql://"+DbLoginData.url+"?serverTimezone=UTC";
		Connection conn = null;
		String id = DbLoginData.id;
		String pw = DbLoginData.pwd;
		Class.forName("com.mysql.cj.jdbc.Driver");
		conn = DriverManager.getConnection(sqlURL, id, pw);
		Statement stmt = conn.createStatement();
		ResultSet rs = null;
		String next = "list.jsp";
		String title = null;

		String what = null;
		String table = null;
		String table2 = null;
		String exp = null;
		StringBuilder sb = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		StringBuilder sb3 = new StringBuilder();
		String sql = null;
		
		List<String> tmp = new ArrayList<String>();
		List<String> tmpExp = new ArrayList<String>();
		
		List<String> tmp2 = new ArrayList<String>();
		List<String> tmpExp2 = new ArrayList<String>();

		List<String> top = new ArrayList<String>();
		List<String> big = new ArrayList<String>();
		List<String> middle = new ArrayList<String>();
		List<String> small = new ArrayList<String>();
		List<String> expList = new ArrayList<String>();
		List<String> bExpList = new ArrayList<String>();
		List<String> mExpList = new ArrayList<String>();
		List<Integer> sizeList = new ArrayList<Integer>();
		List<List<String>> mPrintExpList = new ArrayList<List<String>>();
		List<List<String>> printMiddle = new ArrayList<List<String>>();
		List<List<String>> printExpList = new ArrayList<List<String>>();
		List<List<String>> printSmall = new ArrayList<List<String>>();

		stmt.executeQuery("use "+ DbLoginData.dbName+";");
		
		sql = "select fName from fileName where serial = '"+uid+"'";
		rs = stmt.executeQuery(sql);
		if(rs.next()){
			title =  rs.getString("fName");
		}
		

		table = "techExp";
		table2 = "BClsExp";

		what = "bCls";
		sql = sb3.append("select distinct " + what + " from " + table + " where serial ='" + uid + "' order by "
				+ what + " asc").toString();
		rs = stmt.executeQuery(sql);
		while (rs.next()) {
			if( (rs.getString(what).equals("null"))||(rs.getString(what).isEmpty())){
				continue;
			}
			big.add(rs.getString(what));
			System.out.println("VVVVV"+rs.getString(what));
		}
		
		for(int cc = 0; cc < big.size(); cc++){
			sb3.setLength(0);
			sql = sb3.append("select distinct " + what + ",exp from " + table2 + " where serial ='" + uid + "' and "+"bCls = '" + big.get(cc)+"' order by "
					+ what + " asc").toString();
			rs = stmt.executeQuery(sql);
			System.out.println("VVVV"+sql);

			while (rs.next()) {
				if( (rs.getString(what).equals("null"))||(rs.getString(what).isEmpty())){
					continue;
				}
				System.out.println("WWWWWW"+rs.getString("exp"));
				bExpList.add(rs.getString("exp"));
				
			}		
		}
		
		
		table2 = "MClsExp";
		what = "mCls";
		sql = sb.append("select distinct " + what + " from " + table + " where serial ='" + uid + "' order by "
				+ what + " asc").toString();
		rs = stmt.executeQuery(sql);
		while (rs.next()) {
			if( (rs.getString(what).equals("null"))||(rs.getString(what).isEmpty())){
				continue;
			}
			middle.add(rs.getString(what));
		}
		
		for(int cc = 0; cc < middle.size(); cc++){
			sb.setLength(0);
			sql = sb.append("select distinct " + what + ",exp from " + table2 + " where serial ='" + uid + "' and "+"mCls = '" + middle.get(cc)+"' order by "
					+ what + " asc").toString();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				if( (rs.getString("exp").equals("null"))||(rs.getString("exp").isEmpty())){
					continue;
				}
				mExpList.add(rs.getString("exp"));
			}		
		}

		what = "sCls";
		
		sql = sb2.append("select distinct " + what + " from " + table + " where serial ='" + uid + "' order by "
				+ what + " asc").toString();
		System.out.println(sql);
		rs = stmt.executeQuery(sql);
		while (rs.next()) {
			
			if( (rs.getString(what).equals("null"))||(rs.getString(what).isEmpty())){
				continue;
			}
			small.add(rs.getString(what));
			
		}
		
		for(int i = 0; i < small.size();i++){
			sql = "select exp from techExp where sCls = '"+small.get(i)+"' and serial = '"+uid+"'";
			rs = stmt.executeQuery(sql);
			System.out.println("ccccc"+small.get(i));
			while(rs.next()){
				System.out.println(rs.getString("exp"));
				expList.add(rs.getString("exp"));
			}
		}
		
		System.out.println("uuuuucvcvcv111");
		for (int j = 0; j < big.size(); j++) {
			
			
			
			for (int t = 0; t < small.size(); t++) {
				
				
				if (small.get(t).charAt(0)==big.get(j).charAt(0)) {
					++size;
				}
			}
			
			
			
			
			for(int w = 0; w < middle.size(); w++){
				
				if (middle.get(w).substring(0, 1).equals(big.get(j))) {
					tmp.add(middle.get(w));
					tmpExp.add(mExpList.get(w));
				}
				
			}
			
			printMiddle.add(tmp);
			mPrintExpList.add(tmpExp);
			
		
			
			for (int i = 0; i < tmp.size(); i++) {
				
				for (int k = 0; k < small.size(); k++) {
					
					
					if (small.get(k).substring(0, 2).equals(tmp.get(i))) {
						tmp2.add(small.get(k));
						tmpExp2.add(expList.get(k));
					}

				}
				
				printSmall.add(tmp2);
				printExpList.add(tmpExp2);
				

				tmp2 = new ArrayList<String>(); 
				tmpExp2 = new ArrayList<String>();
			}
				tmp = new ArrayList<String>(); 
				tmpExp = new ArrayList<String>();

		}
		System.out.println("uuuuucvcvcv");
		
		request.setAttribute("big", big); //객체를 request객체에 담음 (data가 문자열이 아니어도 가능)
		request.setAttribute("middle", middle);
		request.setAttribute("small", small);
		request.setAttribute("bExpList", bExpList);
		request.setAttribute("printMiddle", printMiddle);
		request.setAttribute("mPrintExpList", mPrintExpList);
		request.setAttribute("printSmall", printSmall);
		request.setAttribute("printExpList", printExpList);
		request.setAttribute("size", size);
		request.setAttribute("title", title);
	
		RequestDispatcher dispatcher = request.getRequestDispatcher("tree.jsp");

		dispatcher.forward(request, response);
	
	}

}
