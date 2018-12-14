package processer;

import java.io.IOException;
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
 * Servlet implementation class ProcessFAver
 */
@WebServlet("/ProcessFAver")
public class ProcessFAver extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProcessFAver() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/x-json; charset=utf-8");

	
		String cmd=(String) request.getServletContext().getAttribute("cmd");
		
		System.out.println(cmd);
		
		if (cmd == null || cmd.equals("calFAver")) {
			calFAver(request, response);
			/*calFAverS(request, response);*/
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
		
	}
	
	public void calFAver(HttpServletRequest request, HttpServletResponse response) {
		SkillDataController ins = SkillDataController.getInstance();
		PriDataController insP = PriDataController.getInstance();
		
		List<String> skillNum = ins.getAllSkillNum();
		
		/*for(int i = 0; i < ipcpc.size(); i++){
			insP.getFAValue(ipcpc.get(i));
			ins.setFlowV(ipcpc.get(i), insP.getFlowV());
			ins.setAver(ipcpc.get(i), insP.getAver());
			
			insP.setFlowV(0);
			insP.setAver(0);
		}*/
		
		for(int i = 0; i < skillNum.size(); i++){
			insP.getFAValue(skillNum.get(i));
			ins.setFlowV(skillNum.get(i), insP.getFlowV());
			ins.setAver(skillNum.get(i), insP.getAver());
			
			insP.setFlowV(0);
			insP.setAver(0);
		}
		
	
	}
	
	/*public void calFAverS(HttpServletRequest request, HttpServletResponse response) {
		CIDataController ins = CIDataController.getInstance();
		PriDataController insP = PriDataController.getInstance();

		List<String> ipcpc = ins.getAllIpcpcNum();

		for (int i = 0; i < ipcpc.size(); i++) {
			insP.getFAValueS(ipcpc.get(i));
			ins.setFlowV(ipcpc.get(i), insP.getFlowV());
			ins.setAver(ipcpc.get(i), insP.getAver());
			
			insP.setFlowV(0);
			insP.setAver(0);

		}
	}
*/
}
