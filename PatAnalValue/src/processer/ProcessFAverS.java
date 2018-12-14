package processer;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.PriDataController;
import controller.SkillDataController;

/**
 * Servlet implementation class ProcessFAverS
 */
@WebServlet("/ProcessFAverS")
public class ProcessFAverS extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProcessFAverS() {
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
		String cmd = (String) request.getServletContext().getAttribute("cmd");

		System.out.println(cmd);

		if (cmd == null || cmd.equals("calFAverS")) {
			/*calFAverS(request, response);*/
		}
	}

	/*public void calFAverS(HttpServletRequest request, HttpServletResponse response) {
		SkillDataController ins = SkillDataController.getInstance();
		PriDataController insP = PriDataController.getInstance();

		List<String> skillNum = ins.getAllSkillNum();

		for (int i = 0; i < skillNum.size(); i++) {
			insP.getFAValueS(skillNum.get(i));
			ins.setFlowV(skillNum.get(i), insP.getFlowV());
			ins.setAver(skillNum.get(i), insP.getAver());

			RequestDispatcher dispatcher = request.getRequestDispatcher("upload.jsp");

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
	}*/

}
