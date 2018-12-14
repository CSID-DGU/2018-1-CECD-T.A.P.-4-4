package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.poi.util.SystemOutLogger;

import com.sun.javafx.collections.MappingChange.Map;

import data.DbLoginData;
import data.PriData;

public class PriDataController {
	private static PriDataController instance;
	private PriData data;
	private List<PriData> list;

	private PriData insert;

	private String sqlURL = "jdbc:mysql://" + DbLoginData.url + "?serverTimezone=UTC";
	private String id = DbLoginData.id;
	private String pw = DbLoginData.pwd;
	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	private String sql = null;

	private double aver = 0;
	private double flowV = 0;

	public PriDataController() {
		super();
		// TODO Auto-generated constructor stub
		this.data = new PriData();
		this.list = new ArrayList<PriData>();

		this.insert = new PriData();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			this.conn = DriverManager.getConnection(sqlURL, id, pw);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
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

	static public PriDataController getInstance() {
		if (instance == null) {
			instance = new PriDataController();
		}
		return instance;
	}

	public PriData getList(int i) {
		return list.get(i);
	}

	public List<PriData> getList() {
		return list;
	}

	public int getListLength() {

		return list.size();
	}

	public void setList() {
		list.add(insert);
	}

	public void newInsert() {
		insert = new PriData();
	}

	/**
	 * @return the insert
	 */
	public PriData getInsert() {
		return insert;
	}

	/**
	 * @param insert
	 *            the insert to set
	 */
	public void setInsert(PriData insert) {
		this.insert = insert;
	}

	/**
	 * @return the aver
	 */
	public double getAver() {
		return aver;
	}

	/**
	 * @param aver
	 *            the aver to set
	 */
	public void setAver(double aver) {
		this.aver = aver;
	}

	/**
	 * @return the flowV
	 */
	public double getFlowV() {
		return flowV;
	}

	/**
	 * @param flowV
	 *            the flowV to set
	 */
	public void setFlowV(double flowV) {
		this.flowV = flowV;
	}

	public void getGScore() {

		GScoreController tmpCon = new GScoreController().getInstance();

		double score = 0;
		int total = 0;
		try {
			stmt.executeQuery("use " + DbLoginData.dbName + ";");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		sql = "select * from priData";
		/* where patnum is not null */

		try {
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			while (rs.next()) {
				data.setPatNum(rs.getString("patNum"));
				data.setYear(rs.getInt("year"));
				data.setCited(rs.getInt("cited"));
				data.setRegisterCheck(rs.getString("registerCheck"));
				data.setFamily(rs.getString("family"));

				score = calScore(data);

				tmpCon.setScore(data.getPatNum(), "-", score);
			}

			sql = "select count(*) from priData";
			rs = stmt.executeQuery(sql);

			

			if (rs.next()) {
				total = rs.getInt(1);
			}

			System.out.println(total);
			tmpCon.setGrade(total);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public double calScore(PriData tmp) {
		java.util.Calendar cal = java.util.Calendar.getInstance();

		String tmpF = tmp.getFamily();

		int family = 0;

		int tmpC = tmp.getCited();

		int year = cal.get(cal.YEAR);

		int regiCheck = 0;

		double maintainYear = (double) (20 - (year - tmp.getYear()));

		if (maintainYear < 0) {
			maintainYear = 0;
		}

		double res = 0;
		if (tmp.getRegisterCheck().equals("등록")) {
			regiCheck = 10;
		}

		family = calFamily(tmpF);
		
		
		res = (double) regiCheck + (double) family + (maintainYear*0.5) + tmpC;
		
		return res;

	}

	public int calFamily(String tmp) {
		int res = 0;

		String str = tmp.replaceAll("\\p{Z}", "");
		String[] txtArr = str.split(",");

		String[] txtArrT = null;

		for (int i = 0; i < txtArr.length; i++) {

			txtArrT = txtArr[i].split("-");

			for (int j = 0; j < txtArrT.length; j++) {
				txtArrT[j] = txtArrT[j].replaceAll("\\p{Z}", "");
			}

			if ((txtArrT[0].equals("한국")) && (txtArrT[1].equals("1"))) {
				res += 4;
			} else if ((txtArrT[0].equals("미국")) && (txtArrT[1].equals("1"))) {
				res += 10;
			} else if ((txtArrT[0].equals("일본")) && (txtArrT[1].equals("1"))) {
				res += 4;
			} else if ((txtArrT[0].equals("중국")) && (txtArrT[1].equals("1"))) {
				res += 4;
			} else if ((txtArrT[0].equals("EP")) && (txtArrT[1].equals("1"))) {
				res += 4;
			} else if ((txtArrT[0].equals("PCT")) && (txtArrT[1].equals("1"))) {
				res += 4;
			} else if ((txtArrT[0].equals("기타")) && (txtArrT[1].equals("1"))) {
				res += 1;
			}
		}

		return res;
	}

	public void getFAValue(String skillNum) {//skill 관련

		int centerYear = 2009;
		int year = 0;
		int count = 0;
		int totalCount = 0;
	
		int total = 0;
	
		
		List<Double> listD = new ArrayList<Double>();
		String patNum;
		String grade;

		double gradeNum;
		double totalGradeNum = 0;
		double aver = 0;
		double totalAver = 0;
		double averGradeNum= 0;

		double averSepar = 0;
		ResultSet rs2 = null;
		Statement stmt2 = null;

		String sql2 = null;

		try {
			stmt2 = conn.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		;

		try {
			stmt2.executeQuery("use " + DbLoginData.dbName + ";");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		sql2 = "select count(*) from priData as p, gscore as g where skillNum ='" + skillNum
				+ "' and p.patnum = g.patnum order by year";
		/*and patnum is not null */
		try {
			rs2 = stmt2.executeQuery(sql2);
			if (rs2.next()) {
				total = rs2.getInt(1);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		/*
		 * sql =
		 * "select p.year,p.patNum,g.grade from priData as p, GScore as g where ipcpc ='"
		 * + ipcpc + "' and p.patnum = g.patnum  order by year";
		 */

	

		try {

			for (int i = 0; i < 10; i++) {
				sql2 = "select p.year,p.patNum,g.grade from priData as p, gscore as g where skillNum ='" + skillNum
						+ "' and year = " + (centerYear + i) + " and p.patnum = g.patnum";
				rs2 = stmt2.executeQuery(sql2);
			
				while (rs2.next()) {

					year = rs2.getInt("year");
					patNum = rs2.getString("patNum");
					grade = rs2.getString("grade");

					/*
					 * if((tmpYear != year) && (tmpYear != 0 ) && (count > 1)) {
					 * 
					 * listI.add(tmpYear); listD.add(averSepar);
					 * 
					 * aver += (averSepar); count = 0; totalGradeNum = 0; averSepar = 0;
					 * System.out.println("aver!="+aver);
					 * 
					 * } else if((tmpYear != year) && (tmpYear != 0 ) && (count == 1)) {
					 * 
					 * listI.add(tmpYear); listD.add(averSepar);
					 * 
					 * aver += (averSepar); count = 0; totalGradeNum = 0; averSepar = 0;
					 * System.out.println("aver!!="+aver);
					 * 
					 * }
					 */

					/*
					 * if(tmpYear==0) { aver += (averSepar); }
					 */
				

					gradeNum = calGrade(patNum, grade);
					System.out.println("gradeNum = " + gradeNum);
					totalGradeNum += gradeNum;
					count++;
					totalCount++;

					/*if (total == totalCount) {
						
						 * if(count == 1) { aver += (averSepar); listI.add(tmpYear);
						 * listD.add(averSepar); } else {
						 
						aver += (averSepar);
						listI.add(tmpYear);
						listD.add(averSepar);
					}

					tmpYear = year;*/
				}
				
				averGradeNum = totalGradeNum / (double)count;
				averSepar = totalGradeNum;
				listD.add(averSepar);
				aver += averSepar;
				totalGradeNum =0;
				count = 0;
				
			}
			System.out.println("aver!!"+aver);
			System.out.println("totalCount"+totalCount);
			
			totalAver = (aver / totalCount);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.aver = totalAver;
		this.flowV = calFlow(listD);

		System.out.println("aver=" + this.aver);
		System.out.println("flowV=" + this.flowV);

	}

	public void getFAValueS(String ipcpc, String skillNum) {//ci 관련

		int centerYear = 2009;
		int year = 0;
		int count = 0;
		int totalCount = 0;
	
		int total = 0;
	
		
		List<Double> listD = new ArrayList<Double>();
		String patNum;
		String grade;

		double gradeNum;
		double totalGradeNum = 0;
		double aver = 0;
		double totalAver = 0;
		double averGradeNum= 0;

		double averSepar = 0;
		ResultSet rs2 = null;
		Statement stmt2 = null;

		String sql2 = null;

		try {
			stmt2 = conn.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		;

		try {
			stmt2.executeQuery("use " + DbLoginData.dbName + ";");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		sql2 = "select count(*) from priData as p, gscore as g where ipcpc like '" + ipcpc
				+ "%'and skillNum='"+skillNum+"' and p.patnum = g.patnum order by year";
		
		System.out.println("SQL@@"+sql2);
		/*and patnum is not null */
		try {
			rs2 = stmt2.executeQuery(sql2);
			if (rs2.next()) {
				total = rs2.getInt(1);
				System.out.println("TOTAL"+total);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		/*
		 * sql =
		 * "select p.year,p.patNum,g.grade from priData as p, GScore as g where ipcpc ='"
		 * + ipcpc + "' and p.patnum = g.patnum  order by year";
		 */


		try {

			for (int i = 0; i < 10; i++) {
				sql2 = "select p.year,p.patNum,g.grade from priData as p, gscore as g where ipcpc like '" + ipcpc
						+ "%'and skillNum='"+skillNum+"' and year = " + (centerYear + i) + " and p.patnum = g.patnum";
				System.out.println("SQL@@"+sql2);
				rs2 = stmt2.executeQuery(sql2);
				
				while (rs2.next()) {

					year = rs2.getInt("year");
					patNum = rs2.getString("patNum");
					grade = rs2.getString("grade");

					/*
					 * if((tmpYear != year) && (tmpYear != 0 ) && (count > 1)) {
					 * 
					 * listI.add(tmpYear); listD.add(averSepar);
					 * 
					 * aver += (averSepar); count = 0; totalGradeNum = 0; averSepar = 0;
					 * System.out.println("aver!="+aver);
					 * 
					 * } else if((tmpYear != year) && (tmpYear != 0 ) && (count == 1)) {
					 * 
					 * listI.add(tmpYear); listD.add(averSepar);
					 * 
					 * aver += (averSepar); count = 0; totalGradeNum = 0; averSepar = 0;
					 * System.out.println("aver!!="+aver);
					 * 
					 * }
					 */

					/*
					 * if(tmpYear==0) { aver += (averSepar); }
					 */
				

					gradeNum = calGrade(patNum, grade);
					System.out.println("gradeNum = " + gradeNum);
					totalGradeNum += gradeNum;
					count++;
					totalCount++;

					/*if (total == totalCount) {
						
						 * if(count == 1) { aver += (averSepar); listI.add(tmpYear);
						 * listD.add(averSepar); } else {
						 
						aver += (averSepar);
						listI.add(tmpYear);
						listD.add(averSepar);
					}

					tmpYear = year;*/
				}
				
				averGradeNum = totalGradeNum / (double)count;
				averSepar = totalGradeNum;
				listD.add(averSepar);
				aver += averSepar;
				totalGradeNum =0;
				count = 0;
				
			}
			System.out.println("aver!!"+aver);
			System.out.println("totalCount"+totalCount);
			
			totalAver = (aver / totalCount);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.aver = totalAver;
		this.flowV = calFlow(listD);

		System.out.println("aver=" + this.aver);
		System.out.println("flowV=" + this.flowV);
	}

	public double calFlow(List<Double> listD) {

		double flow = 0;
		for (int i = 1; i < 10; i++) {
			
			System.out.println("listD[i]"+listD.get(i));
			flow += ((listD.get(i)- listD.get(i-1))/2);
		}

		return flow;
	}
	

	public double calGrade(String patNum, String grade) {

		double total = 0;

		if (grade.equals("A")) {
			total = 10;
		} else if (grade.equals("B")) {
			total = 8;
		} else if (grade.equals("C")) {
			total = 6;
		} else if (grade.equals("D")) {
			total = 4;
		} else if (grade.equals("E")) {
			total = 2;
		} else if (grade.equals("F")) {
			total = 0;
		}

		try {
			stmt.executeQuery("use " + DbLoginData.dbName + ";");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		sql = "select citedPat from priData where patNum = '" + patNum + "'";
		

		try {
			rs = stmt.executeQuery(sql);

			if (rs.next()) {
				System.out.println("citedPat"+rs.getString("citedPat"));
				if(rs.getString("citedPat")!=null) {
					total += calPlus(rs.getString("citedPat"));
				}
				
			}
			
			System.out.println("total"+total);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return total;
	}

	public double calPlus(String tmp) {
		double res = 0;
		String grade = null;
		
		String str = tmp.replaceAll("\\p{Z}", "");
		String[] txtArr = str.split(",");
		
		System.out.println("기본값"+tmp);
	
		for (int i = 0; i < txtArr.length; i++) {
			
			System.out.println("txtArr"+txtArr[i]);
			txtArr[i] = txtArr[i].replaceAll("\\p{Z}", "");

			try {
				stmt.executeQuery("use " + DbLoginData.dbName + ";");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			sql = "select grade from gscore where patNum = '" + txtArr[i] + "'";
			System.out.println("sql!!"+sql);
			try {
				rs = stmt.executeQuery(sql);

				if (rs.next()) {
					grade = rs.getString("grade");
					System.out.println("grade!!"+grade);

				}

				if (grade == null) {
					continue;
				} else if (grade.equals("A")) {
					res += 1;
				} else if (grade.equals("B")) {
					res += 0.8;
				} else if (grade.equals("C")) {
					res += 0.6;
				} else if (grade.equals("D")) {
					res += 0.4;
				} else if (grade.equals("E")) {
					res += 0.2;
				} else if (grade.equals("F")) {
					res += 0;
				}

			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return res;
	}
}
