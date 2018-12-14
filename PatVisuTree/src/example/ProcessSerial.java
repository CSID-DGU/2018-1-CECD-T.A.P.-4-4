package example;

import java.io.IOException;
import java.sql.Connection;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class ProcessSerial
 */
@WebServlet("/ProcessSerial")
public class ProcessSerial extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String adminId = "doohopatmap";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProcessSerial() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=euc-kr");

		String cmd = request.getParameter("cmd");

		System.out.println(cmd);
		if (cmd == null || cmd.equals("regSerial")) {
			try {
				regSerial(request, response);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void regSerial(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ClassNotFoundException, ServletException, IOException {
		HttpSession session = request.getSession();

		String password = request.getParameter("pw");
		String serial = request.getParameter("serial");

		String sqlURL = "jdbc:mysql://" + DbLoginData.url + "?serverTimezone=UTC";
		Connection conn = null;
		String id = DbLoginData.id;
		String pw = DbLoginData.pwd;
		String query = "show databases;";
		Class.forName("com.mysql.cj.jdbc.Driver");
		conn = DriverManager.getConnection(sqlURL, id, pw);
		Statement stmt = conn.createStatement();
		ResultSet rs = null;

		stmt.executeQuery("use " + DbLoginData.dbName + ";");

		System.out.println(password + " " + serial);
		
		if(password.equals(adminId)) {
			response.getWriter()
			.write("<script>alert('비밀 번호가 이미 등록 되어 있습니다.');location.href='./adminpage.jsp';</script>");
} else {
		}
		query = "select passWords from serialNum" + " where passWords = '" + password + "';";
		rs = stmt.executeQuery(query);
		if (rs.next()) {
			response.getWriter()
					.write("<script>alert('비밀 번호가 이미 등록 되어 있습니다.');location.href='./adminpage.jsp';</script>");
		} else {
			query = "insert into serialNum(passWords,serial)" + " values('" + password + "','" + serial + "' );";

			System.out.println(query);
			stmt.executeUpdate(query);

			response.getWriter().write("<script>alert('시리얼 넘버 성공적으로 입력');location.href='./adminpage.jsp';</script>");
		}

	}

}
