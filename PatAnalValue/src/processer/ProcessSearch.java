package processer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import controller.FileController;
import controller.JasonMakeController;
import controller.PriDataController;
import controller.SearchController;
import controller.SkillDataController;

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
		
		System.out.println("checkckckck");
		
		String keyWord = URLDecoder.decode(request.getParameter("keyword"), "UTF-8");
		searchData(request, response,keyWord);
		System.out.println("keyWord"+keyWord);
		RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
		request.setAttribute("jsonName",keyWord+".json");
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

	public void searchData(HttpServletRequest request, HttpServletResponse response,String keyWord) {

		JasonMakeController ins = JasonMakeController.getInstance();
		FileController fa = new FileController();
		JSONObject jsonObject = new JSONObject();
		 

		jsonObject = ins.totalJasonObjectS(keyWord);

		String decoded = getServletContext().getRealPath("/");

		if (decoded.startsWith("/")) {
			// path "C:/Program Files/Tomcat 6.0/webapps/myapp/WEB-INF/classes/"
			decoded = decoded.replaceFirst("/", "");
		}

		fa.saveFileS(decoded, jsonObject, keyWord+".json");

	}

}
