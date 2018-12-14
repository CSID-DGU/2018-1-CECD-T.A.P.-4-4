package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import data.CIData;
import data.ColorData;
import data.DbLoginData;
import data.GScore;
import data.PriData;
import data.SkillData;

public class JasonMakeController {

	private static JasonMakeController instance;

	private String sqlURL = "jdbc:mysql://" + DbLoginData.url + "?serverTimezone=UTC";
	private String id = DbLoginData.id;
	private String pw = DbLoginData.pwd;
/*	private Connection conn = null;
	private Statement stmt = null;*/
	private ResultSet rs = null;
	private String sql = null;

	public JasonMakeController() {
		super();
		// TODO Auto-generated constructor stub

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static public JasonMakeController getInstance() {
		if (instance == null) {
			instance = new JasonMakeController();
		}
		return instance;
	}

	public JSONObject totalJasonObjectS(String str) {
		System.out.println("!!!@#$" + str);
		JSONObject fin = new JSONObject();
		JSONArray res = new JSONArray();
		
		List<String> tmpListS = new ArrayList<String>();
		List<SkillData> tmpList = new ArrayList<SkillData>();
		
		Statement stmt = null;
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(sqlURL, id, pw);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		int i = 0;
		try {
			stmt.executeQuery("use " + DbLoginData.dbName + ";");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sql = "SELECT DISTINCT skillNum FROM claimDigest where claim like '%" + str + "%' or digest like '%" + str
				+ "%'";

		try {
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				tmpListS.add(rs.getString("skillNum"));

			}

			for (int j = 0; j < tmpListS.size(); j++) {
				sql = "select * from skillData as s,skillCode as c where s.ipcpc = c.skillNum and s.ipcpc = '"
						+ tmpListS.get(j) + "'";
				rs = stmt.executeQuery(sql);

				if (rs.next()) {
					System.out.println(i + "iii");

					SkillData tmpS = new SkillData();
					tmpS.setIpcpc(rs.getString("ipcpc"));
					tmpS.setAver(rs.getDouble("aver"));
					tmpS.setFlowV(rs.getDouble("flowV"));
					tmpS.setName(rs.getString("skillName"));

					tmpList.add(tmpS);
				}

			}

			for (int j = 0; j < tmpList.size(); j++) {
				JSONObject t = new JSONObject();
				t.put("name", tmpList.get(j).getName());
				t.put("skillNum", tmpList.get(j).getIpcpc());
				t.put("aver", tmpList.get(j).getAver());
				t.put("flowV", tmpList.get(j).getFlowV());
				t.put("cls", "bbb");
				/*
				 * t.put("size", (int)(4000+(tmpList.get(j).getAver()*10))); t.put("Color",
				 * "#A48DA5");
				 */

				t.put("children", ciJasonObjectS(tmpList.get(j).getIpcpc(), i, str));
				i++;
				if (i == 5) {
					i = 0;
				}
				res.add(t);
			}

			fin.put("name", "산업 평가 분류");
			fin.put("children", res);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return fin;
	}

	public JSONArray ciJasonObjectS(String ipcpc, int val, String str) {
		String tmp = null;
		JSONArray res = new JSONArray();
		int seek = 0;
		
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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<String> seekList = new ArrayList<String>();
		List<CIData> tmpList = new ArrayList<CIData>();
		List<String> tmpListS = new ArrayList<String>();

		sql = "SELECT DISTINCT ipcpc FROM claimDigest where skillNum = '" + ipcpc + "' and (claim like '%" + str
				+ "%' or digest like '%" + str + "%')";

		try {
			rs = stmt2.executeQuery(sql);
			while (rs.next()) {
				tmp = rs.getString("ipcpc");
				String[] txtArr = tmp.split(" ");
				for (int i = 0; i < seekList.size(); i++) {
					if (seekList.get(i).equals(txtArr[0])) {
						seek = 1;
					}
				}
				if (seek == 0) {
					seekList.add(txtArr[0]);
					tmpListS.add(txtArr[0]);
					System.out.println("TXTARR" + txtArr[0]);
				}

				seek = 0;

			}

			for (int j = 0; j < tmpListS.size(); j++) {
				sql = "select * from ciData as c, ipcCode as i where c.ipcpc = i.ipcpc and skillNum = '" + ipcpc
						+ "' and c.ipcpc = '" + tmpListS.get(j) + "'";
				System.out.println("SQQL" + sql);
				rs = stmt2.executeQuery(sql);

				if (rs.next()) {

					CIData tmpS = new CIData();
					tmpS.setSkillNum(rs.getString("skillNum"));
					tmpS.setIpcpc(rs.getString("ipcpc"));
					tmpS.setAver(rs.getDouble("aver"));
					tmpS.setFlowV(rs.getDouble("flowV"));
					tmpS.setCIName(rs.getString("ipcName"));

					tmpList.add(tmpS);
				}

			}

			for (int j = 0; j < tmpList.size(); j++) {
				JSONObject t = new JSONObject();
				t.put("name", tmpList.get(j).getCIName());
				t.put("ipcpc", tmpList.get(j).getIpcpc());
				t.put("aver", tmpList.get(j).getAver());
				t.put("flowV", tmpList.get(j).getFlowV());
				t.put("cls", "bb");
				/*
				 * t.put("size", (int)(2000+(tmpList.get(j).getAver()*10))); t.put("Color",
				 * "#CBD38C");
				 */
				t.put("children", priJasonObjectS(tmpList.get(j).getIpcpc(), tmpList.get(j).getSkillNum(), val, str));
				res.add(t);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return res;

	}

	public JSONArray priJasonObjectS(String ipcpc, String skillNum, int val, String str) {

		JSONArray arr = new JSONArray();

		List<String> tmpListS = new ArrayList<String>();
		
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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		sql = "SELECT  distinct c.patNum FROM claimDigest as c, priData as p where c.patNum = p.patNum and p.skillNum = '"
				+ skillNum + "' and p.ipcpc like '" + ipcpc + "%' and (claim like '%" + str + "%' or digest like '%"
				+ str + "%')";
		System.out.println("SSSQL" + sql);
		try {
			rs = stmt2.executeQuery(sql);
			while (rs.next()) {
				tmpListS.add(rs.getString("patNum"));
				System.out.println("PPAtnum" + rs.getString("patNum"));
			}

			for (int j = 0; j < tmpListS.size(); j++) {
				sql = "select * from priData as p, gscore as g where g.patNum = '" + tmpListS.get(j)
						+ "' and p.patnum = g.patnum";
				rs = stmt2.executeQuery(sql);

				if (rs.next()) {

					System.out.println("kk");
					JSONObject res = new JSONObject();
					res.put("cls", "b");
					res.put("name", rs.getString("name"));
					res.put("patNum", rs.getString("patNum"));
					res.put("grade", rs.getString("grade"));
					res.put("score", rs.getDouble("score"));
					res.put("size", (int) (3000 + (rs.getDouble("score") * 20)));
					res.put("Color", getColor(rs.getString("grade"), val));
					arr.add(res);
				}

			}

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return arr;
	}

	public JSONObject skillJasonObject() {

		Random random = new Random();
		int val = random.nextInt(4);

		JSONObject fin = new JSONObject();
		JSONArray res = new JSONArray();

		List<SkillData> tmpList = new ArrayList<SkillData>();
		int i = 0;
		
		Statement stmt = null;
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(sqlURL, id, pw);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			stmt.executeQuery("use " + DbLoginData.dbName + ";");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		sql = "select * from skillData as s, skillCode as c where s.ipcpc = c.skillNum";
		/* where patnum is not null */

		try {

			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			while (rs.next()) {
				System.out.println(i + "iii");

				SkillData tmpS = new SkillData();
				tmpS.setIpcpc(rs.getString("ipcpc"));
				tmpS.setAver(rs.getDouble("aver"));
				tmpS.setFlowV(rs.getDouble("flowV"));
				tmpS.setName(rs.getString("skillName"));

				tmpList.add(tmpS);
			}

			for (int j = 0; j < tmpList.size(); j++) {
				JSONObject t = new JSONObject();
				t.put("name", tmpList.get(j).getName());
				t.put("cls", "aaa");
				t.put("skillNum", tmpList.get(j).getIpcpc());
				t.put("aver", tmpList.get(j).getAver());
				t.put("flowV", tmpList.get(j).getFlowV());

				t.put("Color", getColor(tmpList.get(j).getAver(), val));
				t.put("size", (int) (3000 + (tmpList.get(j).getFlowV() * 100)));

				res.add(t);
			}

			fin.put("name", "산업 평가 분류");
			fin.put("children", res);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return fin;

	}

	public JSONObject totalJasonObject(String skillNum) {

		JSONObject fin = new JSONObject();
		JSONArray res = new JSONArray();
		Statement stmt2 = null;

		
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(sqlURL, id, pw);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		List<SkillData> tmpList = new ArrayList<SkillData>();
		int i = 0;

		try {
			stmt2 = conn.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			stmt2.executeQuery("use " + DbLoginData.dbName + ";");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		sql = "select * from skillData as s, skillCode as c where s.ipcpc = c.skillNum and s.ipcpc='" + skillNum + "'";
		/* where patnum is not null */

		try {

			rs = stmt2.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			if (rs.next()) {
				System.out.println(i + "iii");

				SkillData tmpS = new SkillData();
				tmpS.setIpcpc(rs.getString("ipcpc"));
				tmpS.setAver(rs.getDouble("aver"));
				tmpS.setFlowV(rs.getDouble("flowV"));
				tmpS.setName(rs.getString("skillName"));

				tmpList.add(tmpS);
			}

			JSONObject t = new JSONObject();
			t.put("name", tmpList.get(0).getName());
			t.put("cls", "aaa");
			t.put("skillNum", tmpList.get(0).getIpcpc());
			t.put("aver", tmpList.get(0).getAver());
			t.put("flowV", tmpList.get(0).getFlowV());

			t.put("children", ciJasonObject(tmpList.get(0).getIpcpc(), 0));

			res.add(t);

			fin.put("name", "산업 평가 분류");
			fin.put("children", res);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return fin;

	}

	public JSONArray ciJasonObject(String ipcpc, int vall) {

		Random random = new Random();
		int val = random.nextInt(4);

		JSONArray res = new JSONArray();

		List<CIData> tmpList = new ArrayList<CIData>();
		
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

		CIData tmp = new CIData();
		int i = 0;
		sql = "select * from ciData as c, ipcCode as i where c.ipcpc = i.ipcpc and skillNum ='" + ipcpc + "'";
		try {
			stmt2.execute(sql);
			rs = stmt2.getResultSet();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(sql);
		try {
			while (rs.next()) {
				i++;
				CIData tmpS = new CIData();
				tmpS.setCIName(rs.getString("ipcName"));
				tmpS.setSkillNum(rs.getString("skillNum"));
				tmpS.setIpcpc(rs.getString("ipcpc"));
				tmpS.setAver(rs.getDouble("aver"));
				tmpS.setFlowV(rs.getDouble("flowV"));

				tmpList.add(tmpS);
			}

			for (int j = 0; j < tmpList.size(); j++) {
				JSONObject t = new JSONObject();
				t.put("name", tmpList.get(j).getCIName());
				t.put("cls", "aa");
				t.put("ipcpc", tmpList.get(j).getIpcpc());
				t.put("aver", tmpList.get(j).getAver());
				t.put("flowV", tmpList.get(j).getFlowV());
				t.put("skillNum", tmpList.get(j).getSkillNum());

				t.put("Color", getColor(tmpList.get(j).getAver(), val));
				t.put("size", (int) (3000 + (tmpList.get(j).getFlowV() * 100)));

				/*
				 * t.put("children",
				 * priJasonObject(tmpList.get(j).getIpcpc(),tmpList.get(j).getSkillNum(),val));
				 */
				res.add(t);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return res;

	}

	public JSONObject ciJasonObjectW(String skillNum, String ipcpc) {

		JSONObject fin = new JSONObject();
		JSONArray res = new JSONArray();

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

		List<CIData> tmpList = new ArrayList<CIData>();

		CIData tmp = new CIData();
		int i = 0;
		sql = "select * from ciData as c, ipcCode as i where c.ipcpc = i.ipcpc and c.ipcpc = '" + ipcpc
				+ "' and c.skillNum ='" + skillNum + "'";
		try {
			stmt2.execute(sql);
			rs = stmt2.getResultSet();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(sql);
		try {
			if (rs.next()) {

				CIData tmpS = new CIData();
				tmpS.setCIName(rs.getString("ipcName"));
				tmpS.setSkillNum(rs.getString("skillNum"));
				tmpS.setIpcpc(rs.getString("ipcpc"));
				tmpS.setAver(rs.getDouble("aver"));
				tmpS.setFlowV(rs.getDouble("flowV"));

				tmpList.add(tmpS);
			}

			JSONObject t = new JSONObject();
			t.put("name", tmpList.get(0).getCIName());
			t.put("cls", "aa");
			t.put("ipcpc", tmpList.get(0).getIpcpc());
			t.put("aver", tmpList.get(0).getAver());
			t.put("flowV", tmpList.get(0).getFlowV());

			t.put("children", priJasonObjectW(tmpList.get(0).getIpcpc(), tmpList.get(0).getSkillNum()));

			res.add(t);

			fin.put("name", "산업 평가 분류");
			fin.put("children", res);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return fin;

	}

	public JSONArray priJasonObjectW(String ipcpc, String skillNum) {
		Random random = new Random();
		int val = random.nextInt(4);
		JSONArray arr = new JSONArray();
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

		sql = "select * from priData as p, gscore as g where ipcpc like'" + ipcpc + "%' and skillNum = '" + skillNum
				+ "' and p.patnum = g.patnum";
		System.out.println("SQL" + sql);
		try {

			rs = stmt2.executeQuery(sql);

			while (rs.next()) {
				System.out.println("kk");
				JSONObject res = new JSONObject();
				res.put("name", rs.getString("name"));
				res.put("cls", "a");
				res.put("patNum", rs.getString("patNum"));
				res.put("grade", rs.getString("grade"));
				res.put("score", rs.getString("score"));

				res.put("size", (int) (3000 + (rs.getDouble("score") * 100)));
				res.put("Color", getColor(rs.getString("grade"), val));
				arr.add(res);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return arr;
	}

	public String getColor(String grade, int val) {

		String res = null;

		System.out.println("VVAL" + val);
		if (grade.equals("A")) {
			res = (String) ColorData.getInstance().getColorValue().get(val).get(0);
		} else if (grade.equals("B")) {
			res = (String) ColorData.getInstance().getColorValue().get(val).get(1);
		} else if (grade.equals("C")) {
			res = (String) ColorData.getInstance().getColorValue().get(val).get(2);
		} else if (grade.equals("D")) {
			res = (String) ColorData.getInstance().getColorValue().get(val).get(3);
		} else {
			res = (String) ColorData.getInstance().getColorValue().get(val).get(4);
		}

		return res;

	}

	public String getColor(double aver, int val) {

		String res = null;

		System.out.println("VVAL" + val);
		if (aver >= 9) {
			res = (String) ColorData.getInstance().getColorValue().get(val).get(0);
		} else if ((aver >= 7) && (aver < 9)) {
			res = (String) ColorData.getInstance().getColorValue().get(val).get(1);
		} else if ((aver >= 5) && (aver < 7)) {
			res = (String) ColorData.getInstance().getColorValue().get(val).get(2);
		} else if ((aver >= 3) && (aver < 5)) {
			res = (String) ColorData.getInstance().getColorValue().get(val).get(3);
		} else {
			res = (String) ColorData.getInstance().getColorValue().get(val).get(4);
		}

		return res;

	}

}
