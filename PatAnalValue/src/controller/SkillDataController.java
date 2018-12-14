package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import data.SkillData;
import data.DbLoginData;

public class SkillDataController {
	
	private  static SkillDataController instance;
	private SkillData data;
	private List<SkillData> list;
	private String sqlURL = "jdbc:mysql://" + DbLoginData.url + "?serverTimezone=UTC";
	private String id = DbLoginData.id;
	private String pw = DbLoginData.pwd;
	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	private String sql = null;
	
	public SkillDataController() throws ClassNotFoundException {
		super();
		// TODO Auto-generated constructor stub
		this.data = new SkillData();
		this.list = new ArrayList<SkillData>();
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			this.conn = DriverManager.getConnection(sqlURL, id, pw);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	static public SkillDataController getInstance() {
		if(instance == null) {
			try {
				instance = new SkillDataController();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return instance;
	}
	
	public List<String> getAllSkillNum() {
		
		List<String> tmpSkillNum = new ArrayList<String>();
		try {
			stmt.executeQuery("use "+DbLoginData.dbName+";");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sql = "select * from skillData";
		try {
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			while(rs.next()) {
				tmpSkillNum.add(rs.getString("ipcpc"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return tmpSkillNum;
	}
	
	public void setFlowV(String skillNum, double flowV) {
		
		try {
			stmt.executeQuery("use " + DbLoginData.dbName + ";");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		sql = "update skillData set flowV = "+flowV+" where ipcpc ='" + skillNum + "'";
		
		try {
			stmt.execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setAver(String skillNum, double aver) {

		try {
			stmt.executeQuery("use " + DbLoginData.dbName + ";");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		sql = "update skillData set aver = "+aver+" where ipcpc ='" + skillNum +"'";
		
		try {
			stmt.execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
