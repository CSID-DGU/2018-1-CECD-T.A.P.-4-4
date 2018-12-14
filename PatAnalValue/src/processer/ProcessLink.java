package processer;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import controller.FileController;
import controller.JasonMakeController;

/**
 * Servlet implementation class ProcessLink
 */
@WebServlet("/ProcessLink")
public class ProcessLink extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProcessLink() {
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
		doPost(request, response);
		System.out.println("checkckckck");

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("checkckckck");
		String jsonName = null;
		String val = URLDecoder.decode(request.getParameter("val"), "UTF-8");
		String ipcpc = URLDecoder.decode(request.getParameter("ipcpc"), "UTF-8");
		String skillNum = URLDecoder.decode(request.getParameter("skillNum"), "UTF-8");

		if  (val.equals("listing")) {

			searchDataW(request, response,ipcpc,  skillNum);

			jsonName = ipcpc+skillNum+".json";
		} else if (val.equals("list")) {
			searchData(request, response, skillNum);
			jsonName = skillNum+".json";
		}

		RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
		request.setAttribute("jsonName", jsonName);
		try {
			dispatcher.forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void searchData(HttpServletRequest request, HttpServletResponse response, String skillNum) {

		JasonMakeController ins = JasonMakeController.getInstance();
		FileController fa = new FileController();
		JSONObject jsonObject = new JSONObject();

		jsonObject = ins.totalJasonObject(skillNum);

		String decoded = getServletContext().getRealPath("/");

		if (decoded.startsWith("/")) {
			// path "C:/Program Files/Tomcat 6.0/webapps/myapp/WEB-INF/classes/"
			decoded = decoded.replaceFirst("/", "");
		}

		fa.saveFileS(decoded, jsonObject, skillNum + ".json");

	}

	public void searchDataW(HttpServletRequest request, HttpServletResponse response, String ipcpc, String skillNum) {

		JasonMakeController ins = JasonMakeController.getInstance();
		FileController fa = new FileController();
		JSONObject jsonObject = new JSONObject();

		jsonObject = ins.ciJasonObjectW(skillNum, ipcpc);

		String decoded = getServletContext().getRealPath("/");

		if (decoded.startsWith("/")) {
			// path "C:/Program Files/Tomcat 6.0/webapps/myapp/WEB-INF/classes/"
			decoded = decoded.replaceFirst("/", "");
		}

		fa.saveFileS(decoded, jsonObject, ipcpc + skillNum + ".json");

	}

}
