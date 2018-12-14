package example;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.*;
import java.sql.SQLException;
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
 * Servlet implementation class ProcessSearch
 */
@WebServlet("/ProcessSearch")
public class ProcessSearch extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProcessSearch() {
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
		// TODO Auto-generated method stub
		
		
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=euc-kr");

		String cmd = request.getParameter("cmd");

		System.out.println(cmd);
		if (cmd == null || cmd.equals("viewResult")) {
			try {
				viewResult(request, response);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
	}
	
	public void viewResult(HttpServletRequest request, HttpServletResponse response) throws SQLException, ClassNotFoundException, ServletException, IOException {
		HttpSession session = request.getSession();
		
		final int ROWSIZE = 20;
		final int BLOCK = 5;
		
		int pg = 1;

		if (request.getParameter("pg") != null) {
			pg = Integer.parseInt(request.getParameter("pg"));
		}

		int start = (pg * ROWSIZE) - (ROWSIZE - 1);
		int end = (pg * ROWSIZE);

		int allPage = 0;

		int startPage = ((pg - 1) / BLOCK * BLOCK) + 1;
		int endPage = ((pg - 1) / BLOCK * BLOCK) + BLOCK;
		
		List<ListDataSet> dataList = new ArrayList<ListDataSet>();
		ListDataSet tmpData = null;
		
		String tle =null;
		
		String uid = (String) session.getAttribute("uid");

		String searchOption = request.getParameter("searchOption");
		String keyWord = URLDecoder.decode(request.getParameter("keyword") , "UTF-8") ;


		String date = request.getParameter("date");
		String cls = request.getParameter("cls");
		String idx = request.getParameter("idx");
		
		String before = request.getParameter("before");
		String after = request.getParameter("after");
		String day = "-01";
		
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
		ResultSet rs2 = null;
		
		stmt.executeQuery("use "+DbLoginData.dbName+";");
		
		String sqlCount = null;
		String sqlt = " ";
		String sqlc = " ";
		
		if(!date.equals("not")) {
			if(wrongDate(before,after,response)) {
				return;
			}
		}
		
		if(date.equals("reg")){
			sqlt = " and B.regDay >= '" + before+day + "' and "+ "B.regDay <= '" + after+day +"'"; 
		} else if(date.equals("app")){
			sqlt = " and S.appDate >= '" + before+day + "' and "+ "S.appDate <= '" + after+day +"'"; 
		} else if(date.equals("writer")){
			sqlt = " and B.openDate >= '" + before +day+ "' and "+ "B.openDate <= '" + after+day +"'";
		}
		
		if (cls.equals("m")) {
			sqlc = " and mClass = '" + idx + "'";
		} else if (cls.equals("b")) {
			sqlc = " and bClass = '" + idx + "'";
		} else {
			sqlc = " and sClass = '" + idx + "'";
		}
		
		if(searchOption.equals("key")){
			
			sqlCount = "SELECT  COUNT(*) FROM "+table +" as S,"+ table2 +" as B where S.patNum = B.patNum and (B.invName like '%" + keyWord +"%' or B.summary like '%" + keyWord + "%' or B.repClaim like '%" + keyWord +"%')" + sqlt + sqlc +" group by sClass,B.patNum";
			
			/* else{
				if(date.equals("app")){
					sqlCount = "SELECT  distinct COUNT(*) FROM "+table +" as S,"+ table2 +" as T where S.patNum = T.patNum and S.appDate like '%"+  (T.invName like '%" + keyWord +"%' or T.summary like '%" + keyWord + "%' or T.repClaim like '%" + keyWord +"%')";
				}else if(date.equals("reg")){
					
				}else if (date.equals("writer")){
					
				}
			} */
		} else{
			
			sqlCount = "SELECT  COUNT(*) FROM "+table +" as S,"+ table2 +" as B where S.patNum = B.patNum and S.appName like '%" + keyWord +"%'" + sqlt + sqlc+" group by sClass,B.patNum";
		}
		
		System.out.println(sqlCount);
		
		rs = stmt.executeQuery(sqlCount);
		
		if(rs.next()){
			rs.last();
			
			total = rs.getRow();
			System.out.println("TTTT"+total);
		}
		
		allPage = (int)Math.ceil(total/(double)ROWSIZE);

		if(endPage > allPage) {
			endPage = allPage;
		}
		
		String sql = null;
		
		sql = "select fName from fileName where serial = '"+uid+"'";
		rs = stmt.executeQuery(sql);
		if(rs.next()){
			tle =  rs.getString("fName");
		}
		
		String root = "TREE";
		String cfa = null;
		String line = "-";
		StringBuilder setup = new StringBuilder();
		int j = 0;
		List<String> slist = new ArrayList<String>();

		for (int i = 1; i < idx.length() + 1; i++) {
			if (i == idx.length()) {
				slist.add(idx);
			} else {
				slist.add(i - 1, idx.substring(j, i));

			}

			System.out.println(slist.get(i - 1));
		}
		/* setup.append("<a href =\"tree.jsp?uid=" + uid + "\">" + root + "</a>");
		setup.append(line); */
		for (int i = 0; i < slist.size(); i++) {

			if ((slist.get(i).length() == 1) && (i == 0)) {
				cls = "b";
				sql = "select distinct exp from BClsExp where serial ='" + uid + "' and "+"bCls = '" + slist.get(i)+"' order by "
						+ "bCls asc";
				rs2 = stmt.executeQuery(sql);
				if(rs2.next()){
					cfa = rs2.getString("exp");
				}
				setup.append(cfa + "(<a href ='ProcessList?pg=1&idx=" + slist.get(i) + "&cls=" + cls
						+ "'>" + slist.get(i) + "</a>)");
			} else if ((slist.get(i).length() == 2) && (i == 1)) {
				cls = "m";
				sql = "select distinct exp from MClsExp where serial ='" + uid + "' and "+"mCls = '" + slist.get(i)+"' order by "
						+ "mCls asc";
				rs2 = stmt.executeQuery(sql);
				if(rs2.next()){
					cfa = rs2.getString("exp");
				}
				setup.append(cfa + "(<a href ='ProcessList?pg=1&idx=" + slist.get(i) + "&cls=" + cls 
						+ "'>" + slist.get(i) + "</a>)");
			}

			if ((i == 2) && (slist.size() == 3)) {
				System.out.println("여기는 오냐");
				cls = "s";
				sql = "select distinct exp from techExp where serial ='" + uid + "' and "+"sCls = '" + slist.get(i)+"' order by "
						+ "sCls asc";
				rs2 = stmt.executeQuery(sql);
				if(rs2.next()){
					cfa = rs2.getString("exp");
				}
				
				setup.append(cfa + "(<a href ='ProcessList?pg=1&idx=" + slist.get(i) + "&cls=" + cls 
						+ "'>" + slist.get(i) + "</a>)");
				break;
			} else if ((i == 3) && (slist.size() == 4)) {
				cls = "s";
				sql = "select distinct exp from techExp where serial ='" + uid + "' and "+"sCls = '" + slist.get(i)+"' order by "
						+ "sCls asc";
				rs2 = stmt.executeQuery(sql);
				if(rs2.next()){
					cfa = rs2.getString("exp");
				}
				
				setup.append(cfa + "(<a href ='ProcessSearch?pg=1&idx=" + slist.get(i) + "&cls=" + cls + "&uid=" + uid
						+ "'>" + slist.get(i) + "</a>)");
				break;
			}

			if (i != slist.size() - 1) {
				setup.append(line);
			}

		}
		
		
		if(searchOption.equals("key")){
			if(keyWord.equals("")){
				sql = "SELECT A.num, A.natCode,A.appName, A.appDate, A.invName, A.regDay , A.patNum, A.state, A.openDate,A.detail,A.regNum,A.openNum,A.pdfNum from ("
						+ "SELECT @rownum:=@rownum+1 as num, W.natCode,W.appName, W.appDate, W.invName, W.regDay , W.patNum, W.state, W.openDate,W.detail,W.regNum,W.openNum,W.pdfNum from ("
						+ "SELECT natCode,appName, appDate , invName, regDay, S.patNum, state, openDate,detail,regNum,openNum,pdfNum from visualization as S , tree as B where  B.serial = '" + uid + "'"+sqlc
						+ " and S.patNum = B.patNum "+sqlt+" group by sClass,B.patNum) W, (select @rownum:=0) tmp) A  Where "
						+ " num >= " + start + " and num <= " + end + " order by num asc";
			
			} else{
				sql = "SELECT A.num, A.natCode,A.appName, A.appDate, A.invName, A.regDay , A.patNum, A.state, A.openDate,A.detail,A.regNum,A.openNum,A.pdfNum from ("
						+ "SELECT @rownum:=@rownum+1 as num, W.natCode,W.appName, W.appDate, W.invName, W.regDay , W.patNum, W.state, W.openDate,W.detail,W.regNum,W.openNum,W.pdfNum from ("
						+ "SELECT natCode,appName, appDate , invName, regDay, S.patNum, state, openDate,detail,regNum,openNum,pdfNum from visualization as S , tree as B where B.serial = '" + uid + "'"+sqlc
						+ " and (B.invName like '%" + keyWord +"%' or B.summary like '%" + keyWord + "%' or B.repClaim like '%" + keyWord +"%') and S.patNum = B.patNum"+sqlt+" group by sClass,B.patNum) W, (select @rownum:=0) tmp) A  Where "
						+ " num >= " + start + " and num <= " + end + " order by num asc";
				
			}
			
			
		} else if (searchOption.equals("app")){
			if(keyWord.equals("")){
				sql = "SELECT A.num, A.natCode,A.appName, A.appDate, A.invName, A.regDay , A.patNum, A.state, A.openDate,A.detail,A.regNum,A.openNum,A.pdfNum from ("
						+ "SELECT natCode,appName, appDate , invName, regDay, S.patNum, state, openDate,detail,regNum,openNum,pdfNum from visualization as S , tree as B where  B.serial = '" + uid + "'"+sqlc
						+ " and S.patNum = B.patNum "+sqlt+" group by sClass,B.patNum) W, (select @rownum:=0) tmp) A  Where "
						+ " num >= " + start + " and num <= " + end + " order by num asc";
				
			} else{
				sql = "SELECT A.num, A.natCode,A.appName, A.appDate, A.invName, A.regDay , A.patNum, A.state, A.openDate,A.detail,A.regNum,A.openNum,A.pdfNum from ("
						+ "SELECT @rownum:=@rownum+1 as num, W.natCode,W.appName, W.appDate, W.invName, W.regDay , W.patNum, W.state, W.openDate,W.detail,W.regNum,W.openNum,W.pdfNum from ("
						+ "SELECT natCode,appName, appDate , invName, regDay, S.patNum, state, openDate,detail,regNum,openNum,pdfNum from visualization as S , tree as B where B.serial = '" + uid + "'"+sqlc
						+ " and appName like '%" + keyWord + "%' and S.patNum = B.patNum"+sqlt+" group by sClass,B.patNum) W, (select @rownum:=0) tmp) A  Where "
						+ " num >= " + start + " and num <= " + end + " order by num asc";
			
			}
			
			/* sql = "SELECT  A.num, A.natCode,A.appName, A.appDate, A.invName, A.regDay, A.patNum from ("+
					"SELECT @rownum:=@rownum+1 as num, natCode,appName, appDate, invName, regDay, S.patNum from visualization as S , tree as B ,(select @rownum:=0) tmp  where appName like '%" + keyWord + "%' and S.patNum = B.patNum  ) A where " +
					  " num >= "+start + " and num <= "+ end +" order by num asc"; */
		} 
		
		
	
		rs = stmt.executeQuery(sql);
		
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
		 Timestamp tmp = null;
		 int i = 1;
		while(rs.next()) {
			tmpData = new ListDataSet();
			
			int num = rs.getInt(1);
			String natCode = rs.getString(2);
			/* String regNum = rs.getString(3); */
			String appName = rs.getString(3);
			
			tmp = rs.getTimestamp(4);
			String appDate = null;
			if(tmp == null){
				appDate = "-";
			} else{
				appDate = formatter.format(tmp);
			}
			
			String invName = rs.getString(5);
			
			tmp = rs.getTimestamp(6);
			String regDay = null;
			if(tmp == null){
				regDay = "-";
			} else{
				regDay = formatter.format(tmp);
			}
			
			String patNum = rs.getString(7);
			String state = rs.getString(8);
			tmp = rs.getTimestamp(9);
			String openDay = null;
			if(tmp == null){
				openDay = "-";
			} else{
				openDay = formatter.format(tmp);
			}
			String detail = rs.getString(10);
			String regNum = rs.getString(11);
			if(regNum.equals("")){
				regNum = "-";
			}
			String openNum = rs. getString(12);
			if(openNum.equals("")){
				openNum = "-";
			}
			String pdfNum= rs.getString("pdfNum");
			
			tmpData.setInvName(invName);
			tmpData.setAppDate(appDate);
			tmpData.setAppName(appName);
			tmpData.setDetail(detail);
			tmpData.setNatCode(natCode);
			tmpData.setNum(num);
			tmpData.setOpenDay(openDay);
			tmpData.setOpenNum(openNum);
			tmpData.setPatNum(patNum);
			tmpData.setPdfNum(pdfNum);
			tmpData.setRegDay(regDay);
			tmpData.setRegNum(regNum);
			tmpData.setState(state);
			
			dataList.add(tmpData);
		}
		
		request.setAttribute("setup", setup); //객체를 request객체에 담음 (data가 문자열이 아니어도 가능)
		request.setAttribute("total", total);
		request.setAttribute("pg", pg);
		request.setAttribute("dataList", dataList);
		request.setAttribute("idx", idx);
		request.setAttribute("cls", cls);
		request.setAttribute("searchOption", searchOption);
		request.setAttribute("keyword", keyWord);
		request.setAttribute("date", date);
		request.setAttribute("before", before);
		request.setAttribute("after", after);
		request.setAttribute("title", tle);
		
	
		RequestDispatcher dispatcher = request.getRequestDispatcher("list2.jsp");

		dispatcher.forward(request, response);
	}
	
	public boolean wrongDate(String before, String after, HttpServletResponse response) throws IOException {
		if((before.equals(""))||(after.equals(""))) {
			response.getWriter().println("<script> alert(\"기간 검색 시 양쪽 다 입력\"); history.go(-1);</script>");
			return true;
		}
		return false;
	}

}
