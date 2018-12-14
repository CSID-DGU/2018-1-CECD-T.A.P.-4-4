package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import data.AnalyzeData;
import data.CIData;
import data.DbLoginData;
import data.SkillData;

public class AnalyzeController {

	private static AnalyzeController instance;

	private String sqlURL = "jdbc:mysql://" + DbLoginData.url + "?serverTimezone=UTC";
	private String id = DbLoginData.id;
	private String pw = DbLoginData.pwd;

	/*
	 * private Statement stmt = null; private ResultSet rs = null;
	 */
	private ResultSet rs = null;
	private String sql = null;

	public AnalyzeController() {
		super();
		// TODO Auto-generated constructor stub

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * try { stmt = conn.createStatement(); } catch (SQLException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */
	}

	static public AnalyzeController getInstance() {
		if (instance == null) {
			instance = new AnalyzeController();
		}
		return instance;
	}

	public AnalyzeData makeAnalyzeData(String skillNum) {
		AnalyzeData tmp = new AnalyzeData();

		Statement stmt2 = null;

		Connection conn = null;

		try {
			conn = DriverManager.getConnection(sqlURL, id, pw);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			stmt2 = conn.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		sql = "select * from skillData as s, skillCode as c where s.ipcpc = c.skillNum and s.ipcpc='" + skillNum + "'";
		try {

			rs = stmt2.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			if (rs.next()) {

				SkillData tmpS = new SkillData();
				tmpS.setIpcpc(rs.getString("ipcpc"));
				tmpS.setAver(rs.getDouble("aver"));
				tmpS.setFlowV(rs.getDouble("flowV"));
				tmpS.setName(rs.getString("skillName"));

				analAll(tmp, tmpS);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return tmp;
	}

	public void analAll(AnalyzeData tmp, SkillData skill) {
		double max = 0;
		double min = 0;
		Statement stmt2 = null;

		Connection conn = null;

		try {
			conn = DriverManager.getConnection(sqlURL, id, pw);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			stmt2 = conn.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		tmp.setSkillName(skill.getName());
		tmp.setAver(skill.getAver());
		tmp.setFlowV(skill.getFlowV());
		try {
			stmt2 = conn.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (skill.getAver() > 5) {
			sql = "select MAX(aver),MIN(aver) from skillData where (aver > 5)";
		} else {
			sql = "select MAX(aver),MIN(aver) from skillData where (aver <= 5)";
		}

		try {

			rs = stmt2.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			if (rs.next()) {

				max = rs.getDouble(1);
				min = rs.getDouble(2);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setAverAnal(max, min, skill.getAver(), tmp);

		if (skill.getFlowV() > 0) {
			sql = "select MAX(flowV),MIN(flowV) from skillData where (flowV > 0)";
		} else {
			sql = "select MAX(flowV),MIN(flowV) from skillData where (flowV < 0)";
		}

		try {

			rs = stmt2.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			if (rs.next()) {

				max = rs.getDouble(1);
				min = rs.getDouble(2);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setFlowVAnal(max, min, skill.getFlowV(), tmp);

		setFinalAnal(skill.getAver(), skill.getFlowV(), tmp);

	}

	public void setFinalAnal(double aver, double flowV, AnalyzeData tmp) {

		int first = 0;
		int second = 0;
		int third = 0;

		if ((aver > 5) && (flowV > 0)) {
			if (tmp.getAverValue().equals("�ſ� ����")) {
				first = 3;
			} else if (tmp.getAverValue().equals("����� ����")) {
				first = 2;
			} else if (tmp.getAverValue().equals("����")) {
				first = 1;
			}

			if (tmp.getInvestAttention().equals("�ſ� ����")) {
				second = 3;
			} else if (tmp.getInvestAttention().equals("����� ����")) {
				second = 2;
			} else if (tmp.getInvestAttention().equals("����")) {
				second = 1;
			}
			third = first + second;

			System.out.println(first + second + "3." + third + tmp.getAverValue() + tmp.getInvestAttention());
			switch (third) {
			case 6:
				tmp.setInvestValue("�ſ� ����");
				tmp.setDevelPossibility("�ſ� ����");
				break;
			case 5:
				tmp.setInvestValue("����� ����");
				tmp.setDevelPossibility("����� ����");
				break;
			case 4:
				tmp.setInvestValue("����� ����");
				tmp.setDevelPossibility("����� ����");
				break;
			case 3:
				tmp.setInvestValue("����");
				tmp.setDevelPossibility("����");
				break;
			case 2:
				tmp.setInvestValue("����");
				tmp.setDevelPossibility("����");
				break;

			}

			System.out.println("++" + tmp.getInvestValue() + tmp.getDevelPossibility());

		} else if ((aver > 5) && (flowV < 0)) {
			if (tmp.getAverValue().equals("�ſ� ����")) {
				first = 3;
			} else if (tmp.getAverValue().equals("����� ����")) {
				first = 2;
			} else if (tmp.getAverValue().equals("����")) {
				first = 1;
			}

			if (tmp.getInvestAttention().equals("����")) {
				second = 3;
			} else if (tmp.getInvestAttention().equals("����� ����")) {
				second = 2;
			} else if (tmp.getInvestAttention().equals("�ſ� ����")) {
				second = 1;
			}
			third = first + second;
			System.out.println(first + second + "3." + third + tmp.getAverValue() + tmp.getInvestAttention());
			switch (third) {
			case 6:
				tmp.setInvestValue("����");
				tmp.setDevelPossibility("����");
				break;
			case 5:
				tmp.setInvestValue("����� ����");
				tmp.setDevelPossibility("����� ����");
				break;
			case 4:
				tmp.setInvestValue("����� ����");
				tmp.setDevelPossibility("����� ����");
				break;
			case 3:
				tmp.setInvestValue("�ſ� ����");
				tmp.setDevelPossibility("�ſ� ����");
				break;
			case 2:
				tmp.setInvestValue("�ſ� ����");
				tmp.setDevelPossibility("�ſ� ����");
				break;

			}
			System.out.println("++" + tmp.getInvestValue() + tmp.getDevelPossibility());

		} else if ((aver <= 5) && (flowV > 0)) {
			if (tmp.getAverValue().equals("����")) {
				first = 3;
			} else if (tmp.getAverValue().equals("����� ����")) {
				first = 2;
			} else if (tmp.getAverValue().equals("�ſ� ����")) {
				first = 1;
			}

			if (tmp.getInvestAttention().equals("�ſ� ����")) {
				second = 3;
			} else if (tmp.getInvestAttention().equals("����� ����")) {
				second = 2;
			} else if (tmp.getInvestAttention().equals("����")) {
				second = 1;
			}
			third = first + second;

			switch (third) {
			case 6:
				tmp.setInvestValue("�ſ� ����");
				tmp.setDevelPossibility("�ſ� ����");
				break;
			case 5:
				tmp.setInvestValue("����� ����");
				tmp.setDevelPossibility("����� ����");
				break;
			case 4:
				tmp.setInvestValue("����� ����");
				tmp.setDevelPossibility("����� ����");
				break;
			case 3:
				tmp.setInvestValue("����");
				tmp.setDevelPossibility("����");
				break;
			case 2:
				tmp.setInvestValue("����");
				tmp.setDevelPossibility("����");
				break;
			}

		} else if ((aver <= 5) && (flowV < 0)) {
			if (tmp.getAverValue().equals("����")) {
				first = 3;
			} else if (tmp.getAverValue().equals("����� ����")) {
				first = 2;
			} else if (tmp.getAverValue().equals("�ſ� ����")) {
				first = 1;
			}

			if (tmp.getInvestAttention().equals("����")) {
				second = 3;
			} else if (tmp.getInvestAttention().equals("����� ����")) {
				second = 2;
			} else if (tmp.getInvestAttention().equals("�ſ� ����")) {
				second = 1;
			}
			third = first + second;

			switch (third) {
			case 6:
				tmp.setInvestValue("����");
				tmp.setDevelPossibility("����");
				break;
			case 5:
				tmp.setInvestValue("����� ����");
				tmp.setDevelPossibility("����� ����");
				break;
			case 4:
				tmp.setInvestValue("����� ����");
				tmp.setDevelPossibility("����� ����");
				break;
			case 3:
				tmp.setInvestValue("�ſ� ����");
				tmp.setDevelPossibility("�ſ� ����");
				break;
			case 2:
				tmp.setInvestValue("�ſ� ����");
				tmp.setDevelPossibility("�ſ� ����");
				break;

			}

		}
	}

	public void setAverAnal(double max, double min, double aver, AnalyzeData tmp) {
		System.out.println("Amax=" + max + "Amin=" + min);
		if (aver > 5) {
			if ((aver >= max - 0.5)) {
				tmp.setAverValue("�ſ� ����");
				tmp.setIndustryAttention("�ſ� ����");
				tmp.setDevel("�ſ� ����");
			} else if (aver <= 6) {
				tmp.setAverValue("����");
				tmp.setIndustryAttention("����");
				tmp.setDevel("����");
			} else if ((aver > 6) && (aver < max - 0.5)) {
				tmp.setAverValue("����� ����");
				tmp.setIndustryAttention("����� ����");
				tmp.setDevel("����� ����");
			}
		} else {
			if ((aver >= max - 0.5)) {
				tmp.setAverValue("����");
				tmp.setIndustryAttention("����");
				tmp.setDevel("����");
			} else if (aver <= 1) {
				tmp.setAverValue("�ſ� ����");
				tmp.setIndustryAttention("�ſ� ����");
				tmp.setDevel("�ſ� ����");
			} else if ((aver > 1) && (aver < max - 0.5)) {
				tmp.setAverValue("����� ����");
				tmp.setIndustryAttention("����� ����");
				tmp.setDevel("����� ����");
			}
		}

	}

	public void setFlowVAnal(double max, double min, double flowV, AnalyzeData tmp) {
		System.out.println("max=" + max + "min=" + min);
		if (flowV > 0) {
			if ((flowV >= max - 10)) {
				tmp.setInvestAttention("�ſ� ����");
				tmp.setAttention("�ſ� ����");

			} else if (flowV <= min + 10) {
				tmp.setInvestAttention("����");
				tmp.setAttention("����");

			} else if ((flowV > min + 10) && (flowV < max - 10)) {
				tmp.setInvestAttention("����� ����");
				tmp.setAttention("����� ����");

			}
		} else {
			if ((flowV <= max + 10)) {
				tmp.setInvestAttention("�ſ� ����");
				tmp.setAttention("�ſ� ����");

			} else if (flowV >= min - 10) {
				tmp.setInvestAttention("����");
				tmp.setAttention("����");

			} else if ((flowV < min - 10) && (flowV > max + 10)) {
				tmp.setInvestAttention("����� ����");
				tmp.setAttention("����� ����");

			}
		}

	}

}
