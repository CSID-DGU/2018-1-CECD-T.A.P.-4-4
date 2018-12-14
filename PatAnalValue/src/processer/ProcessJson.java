package processer;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import controller.FileController;
import controller.JasonMakeController;

/**
 * Servlet implementation class ProcessJson
 */
@WebServlet("/ProcessJson")
public class ProcessJson extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProcessJson() {
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
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		FileController fa = new FileController();
		JSONObject jsonObject = new JSONObject();
		JasonMakeController jmc = new JasonMakeController();

		// path decoded "/C:/Program Files/Tomcat 6.0/webapps/myapp/WEB-INF/classes/"
		String decoded = getServletContext().getRealPath("/");

		if (decoded.startsWith("/")) {
			// path "C:/Program Files/Tomcat 6.0/webapps/myapp/WEB-INF/classes/"
			decoded = decoded.replaceFirst("/", "");
		}

		jsonObject = jmc.skillJasonObject();

		fa.saveFile(decoded, jsonObject);

		RequestDispatcher dispatcher = request.getRequestDispatcher("upload.jsp");
		request.setAttribute("jsonName","tmp.json");
		
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
	
	/*public void makeJson(HttpServletRequest request, HttpServletResponse response) {
		FileController fa = new FileController();
		JSONObject jsonObject = new JSONObject();
		JasonMakeController jmc = new JasonMakeController();

		// path decoded "/C:/Program Files/Tomcat 6.0/webapps/myapp/WEB-INF/classes/"
		String decoded = getServletContext().getRealPath("/");

		if (decoded.startsWith("/")) {
			// path "C:/Program Files/Tomcat 6.0/webapps/myapp/WEB-INF/classes/"
			decoded = decoded.replaceFirst("/", "");
		}

		jsonObject = jmc.skillJasonObject();

		fa.saveFile(decoded, jsonObject);

		RequestDispatcher dispatcher = request.getRequestDispatcher("upload.jsp");
		request.setAttribute("jsonName","tmp.json");
		
		try {
			dispatcher.forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/

}
