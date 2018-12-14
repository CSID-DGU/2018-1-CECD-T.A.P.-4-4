package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import data.DbLoginData;
import data.GScore;

public class GScoreController {
	private  static GScoreController instance;
	private GScore data;
	private List<GScore> list;
	
	private String sqlURL = "jdbc:mysql://" + DbLoginData.url + "?serverTimezone=UTC";
	private String id = DbLoginData.id;
	private String pw = DbLoginData.pwd;
	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	private String sql = null;
	
	public GScoreController() {
		super();
		// TODO Auto-generated constructor stub
		this.data = new GScore();
		this.list = new ArrayList<GScore>();
		
		try {
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


	static public GScoreController getInstance() {
		if(instance == null) {
			instance = new GScoreController();
		}
		return instance;
	}
	
	public void setScore(String patNum, String grade, double score) {
		try {
			stmt.executeQuery("use " + DbLoginData.dbName + ";");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sql = "select * from gscore where patNum = '"+patNum+"'";
		
		try {
			rs = stmt.executeQuery(sql);
			if(!(rs.next())) {
				sql = "insert into gscore (patNum, grade, score) Value('"+patNum+"','"+grade+"',"+score+")";
				
				try {
					stmt.execute(sql);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		
		
	}
	
	public void setGrade(int total) throws SQLException {
		ResultSet res = null;
		String patnum = null;
		List<String> list2 = new ArrayList<String>();
		char grade = 'A'-1;
		int [] percent;
		int c = 0;
		percent = calPercent(total);
		for(int i = 0; i < 6; i++) {
			grade += 1;
			c = (percent[i+1])-(percent[i]);
			
			sql = "select patnum from gscore order by score desc, patnum desc"
					+ " limit "+percent[i]+", "+ c ;
			
			if(i==5) {
				sql = "select patnum from gscore order by score desc,patnum desc limit "+percent[i]+", "+ (c) ;
			}
			
			System.out.println(sql);
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				patnum = rs.getString("patnum");
				list2.add(patnum);
			}
			
			for(int w = 0; w < list2.size();w++) {
				sql = "update gscore set grade='"+grade+"' where patnum = '"+list2.get(w)+"'";
				System.out.println(sql);
				stmt.executeUpdate(sql);
			}
			
			
				list2.clear();
			
			
		}
		
		
	}
	
	public int[] calPercent(int total) {
		System.out.println("total"+total);
		double [] tmp = new double[7];
		int [] k = new int[7];
		k[0] = 0;
		
		tmp[0] = 0;
		tmp[1] = ((double)total * 0.1);
		tmp[2] = ((double)total * 0.3);
		tmp[3] = ((double)total * 0.5);
		tmp[4] = ((double)total * 0.7);
		tmp[5] = ((double)total * 0.9);
		tmp[6] = total;
		for(int i= 1; i < 7; i++) {
			if((tmp[i] - (int)tmp[i])>=0.5) {
				k[i] = ((int)tmp[i])+1;
			}else {
				k[i] = (int)tmp[i];
			}
		}
		
		return k;
	}
	
}
