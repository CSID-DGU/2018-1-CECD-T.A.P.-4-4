package processer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.CIDataController;
import controller.PriDataController;
import controller.SkillDataController;

/**
 * Servlet implementation class ProcessScoring
 */
@WebServlet("/ProcessScoring")
public class ProcessScoring extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProcessScoring() {
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

	
		String cmd=(String) request.getServletContext().getAttribute("cmd");
		
		System.out.println(cmd);
		
		if (cmd == null || cmd.equals("calGScore")) {
			calGScore(request, response);
			
			/*calGScore(request, response);*/

		} 

	}

	public void calGScore(HttpServletRequest request, HttpServletResponse response) {
		PriDataController ins = PriDataController.getInstance();
		
		ins.getGScore();
		
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

	/*public void calFAverS(HttpServletRequest request, HttpServletResponse response) {
		SkillDataController ins = SkillDataController.getInstance();
		PriDataController insP = PriDataController.getInstance();
		
		List<String> skillNum = ins.getAllSkillNum();
		
		for(int i = 0; i < skillNum.size(); i++){
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
	}
*/
	/*public void calFAver(HttpServletRequest request, HttpServletResponse response) {
		CIDataController ins = CIDataController.getInstance();
		PriDataController insP = PriDataController.getInstance();
		
		List<String> ipcpc = ins.getAllIpcpcNum();
		
		for(int i = 0; i < ipcpc.size(); i++){
			insP.getFAValue(ipcpc.get(i));
			ins.setFlowV(ipcpc.get(i), insP.getFlowV());
			ins.setAver(ipcpc.get(i), insP.getAver());
			
			insP.setFlowV(0);
			insP.setAver(0);
		}
		
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
	}*/

	
}
