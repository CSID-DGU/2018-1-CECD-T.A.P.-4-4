package processer;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import controller.AnalyzeController;
import controller.FileController;
import controller.JasonMakeController;
import data.AnalyzeData;

/**
 * Servlet implementation class ProcessAnal
 */
@WebServlet("/ProcessAnal")
public class ProcessAnal extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProcessAnal() {
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
		AnalyzeData tmp = null;
		String averValue = null;
		String investAttention = null;
		String investValue = null;
		String skillNum = URLDecoder.decode(request.getParameter("skillNum"), "UTF-8");
		tmp = analyzeData(request, response, skillNum);
		
		averValue = selectColor(tmp.getAverValue());
		investAttention = selectColor(tmp.getInvestAttention());
		investValue = selectColor(tmp.getInvestValue());
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("anal.jsp");
		request.setAttribute("name", tmp.getSkillName());
		request.setAttribute("aver", tmp.getAver());
		request.setAttribute("flowV", tmp.getFlowV());
		request.setAttribute("averValue", tmp.getAverValue());
		request.setAttribute("investAttention", tmp.getInvestAttention());
		request.setAttribute("investValue", tmp.getInvestValue());
		
		request.setAttribute("averValueColor", averValue);
		request.setAttribute("investAttentionColor", investAttention);
		request.setAttribute("investValueColor", investValue);
		
		System.out.println("averValue"+averValue);
		
		
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

	public AnalyzeData analyzeData(HttpServletRequest request, HttpServletResponse response, String skillNum) {
		AnalyzeData tmp = null;
		AnalyzeController ins = AnalyzeController.getInstance();
		tmp = ins.makeAnalyzeData(skillNum);
			 
		return tmp;
		
	}
	
	public String selectColor(String val) {
		String res = null;
		if((val.equals("높음"))||(val.equals("상당히 높음"))||(val.equals("매우 높음"))){
			res = "red";
		} else {
			res = "blue";
		}
		
		return res;
	}
}
