package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import data.CIData;
import data.DbLoginData;

public class CIDataController {
	private static CIDataController instance;
	private CIData data;
	private List<CIData> list;

	private String sqlURL = "jdbc:mysql://" + DbLoginData.url + "?serverTimezone=UTC";
	private String id = DbLoginData.id;
	private String pw = DbLoginData.pwd;
	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	private String sql = null;

	public CIDataController() {
		super();
		// TODO Auto-generated constructor stub
		this.data = new CIData();
		this.list = new ArrayList<CIData>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			this.conn = DriverManager.getConnection(sqlURL, id, pw);
		} catch (SQLException | ClassNotFoundException e) {
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

	static public CIDataController getInstance() {
		if (instance == null) {
			instance = new CIDataController();
		}
		return instance;
	}
	
	

	public List<CIData> getAllIpcpcNum() {

		List<CIData> tmpIpcpc = new ArrayList<CIData>();
		CIData tmp = null;
		try {
			stmt.executeQuery("use " + DbLoginData.dbName + ";");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sql = "select ipcpc,skillNum from ciData";
		try {
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			while (rs.next()) {
				 tmp = new CIData();
				tmp.setIpcpc(rs.getString("ipcpc"));
				tmp.setSkillNum(rs.getString("skillNum"));
				tmpIpcpc.add(tmp);
				System.out.println("ipcpc="+rs.getString("ipcpc"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return tmpIpcpc;
	}

	public void setFlowV(String ipcpc,String skillNum, double flowV) {
		try {
			stmt.executeQuery("use " + DbLoginData.dbName + ";");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		sql = "update ciData set flowV = "+flowV+" where ipcpc ='" + ipcpc + "' and skillNum = '"+skillNum+"'";
		
		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void setAver(String ipcpc,String skillNum, double aver) {
		try {
			stmt.executeQuery("use " + DbLoginData.dbName + ";");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		sql = "update ciData set aver = "+aver+" where ipcpc ='" + ipcpc + "' and skillNum = '"+skillNum+"'";
		
		try {
			System.out.println(aver+"cxvjoij");
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
