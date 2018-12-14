package processer;

import java.util.List;
import java.io.BufferedWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import controller.PriDataController;
import data.Code;
import data.DbLoginData;
import helper.UploadHelper;

/**
 * Servlet implementation class ProcessCodeExcel
 */
@WebServlet("/ProcessCodeExcel")
public class ProcessCodeExcel extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProcessCodeExcel() {
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
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/x-json; charset=utf-8");

		parsingExcel(request, response);

	}

	public void parsingExcel(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String savePath = request.getSession().getServletContext().getRealPath(UploadHelper.upDir);
		List<Code> list = new ArrayList<Code>();
		Code tmp = null;

		int line_num = 0;
		int i = 0, nCnt = 0, nADScnt = 0, nNADScnt = 0, w = 0, cB = 0, cM = 0;
		String[] title = new String[50];

		/*
		 * int[] cntClassYearArr = new int[5]; // 연도별 classification(소분류) 의 크기 String
		 * imgSrc = "/example/img/dooho_main_logo.jpg";
		 */
		String indexLink = "./index.jsp";
		String filePath = (String) request.getAttribute("filePath");
		String ext = (String) request.getAttribute("ext");
		String fileName = filePath.substring(filePath.lastIndexOf('/') + 1, filePath.lastIndexOf('.'));
		/* String savePath = UploadHelper.fileDir; */
		String sqlURL = "jdbc:mysql://" + DbLoginData.url + "?serverTimezone=UTC";
		BufferedWriter fw = null;
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		/*
		 * response.getWriter().write("<a href=\"" + indexLink + "\">");
		 * response.getWriter().write("<img src=\"" + imgSrc + "\">");
		 * response.getWriter().write("</a><br/><br/>");
		 */

		String uid = UUID.randomUUID().toString();

		Connection conn = null;
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs;

		/*
		 * try{ File f = new File("E:/"); if(!f.exists())f.mkdirs(); } catch(Exception
		 * e){ response.getWriter().write(e.toString()); }
		 */

		if (ext.equals("xls") || ext.equals("XLS")) {

			try {
				OPCPackage opcPackage = OPCPackage.open(filePath);
				XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);
				opcPackage.close();

				XSSFSheet sheet = workbook.getSheetAt(0);

				Iterator<Row> rowIterator = sheet.iterator();
				/* fw = new BufferedWriter(new FileWriter(savePath + "outputTest2.txt")); */
				while (rowIterator.hasNext()) {

					Row row = rowIterator.next();
					Iterator<Cell> cellIterator = row.cellIterator();
					while (cellIterator.hasNext()) {
						Cell cell = cellIterator.next();
						int k = cell.getColumnIndex();

						switch (k) {

						case 0: // ㄱㅣ술군명
							boolean isAlreadyStored = false;
							if (i == 0) {
								title[2] = cell.getStringCellValue();
								break;
							}

							String t = cell.getStringCellValue().trim();
							String re = t.replaceAll("\'", "");
							tmp.setSkillName(t);

							break;
						case 1: // 코드값
							if (i > 0) {
								String temp = cell.getStringCellValue().trim();
								String res = temp.replaceAll("\'", "");
								tmp.setSkillCode(res);
							} else
								title[7] = cell.getStringCellValue();
							break;
						case 2: // ipc명
							if (i > 0) {
								String temp = cell.getStringCellValue().trim();
								String res = temp.replaceAll("\'", "");
								tmp.setIpcName(res);
							} else
								title[8] = cell.getStringCellValue();
							break;
						case 3: // 코드값
							if (i > 0) {
								String temp = cell.getStringCellValue().trim();
								String res = temp.replaceAll("\'", "");
								tmp.setIpcCode(res);
							} else
								title[9] = cell.getStringCellValue();
							break;

						default:
							break;
						}
					}
					/*
					 * if(i == 0) { for(int k=0;k<7;k++) { System.out.println(title[k]);
					 * 
					 * fw.write(title[k]); fw.write("\t\t"); } fw.newLine(); fw.flush(); } else {
					 * example.DataSet.PrintData(ds[i], i, fw); }
					 */
					i++;
				}
			} catch (Exception e) {
				response.getWriter().write("<script>alert('문제가 발생하였습니다.');</script>");
				e.printStackTrace();
			} finally {
				/* fw.close(); */
			}
			line_num = i;
		} else if (ext.equals("xlsx") || ext.equals("XLSX")) {

			try {
				OPCPackage opcPackage = OPCPackage.open(filePath);
				XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);
				opcPackage.close();

				XSSFSheet sheet = workbook.getSheetAt(0);

				Iterator<Row> rowIterator = sheet.iterator();
				/* fw = new BufferedWriter(new FileWriter(savePath + "outputTest2.txt")); */
				while (rowIterator.hasNext()) {
					tmp = new Code();

					Row row = rowIterator.next();
					Iterator<Cell> cellIterator = row.cellIterator();
					while (cellIterator.hasNext()) {
						Cell cell = cellIterator.next();
						int k = cell.getColumnIndex();

						switch (k) {

						case 0: // 발명의 명칭 : string(mysql 입력)
							boolean isAlreadyStored = false;
							if (i == 0) {
								title[2] = cell.getStringCellValue();
								break;
							}

							String t = cell.getStringCellValue().trim();
							String re = t.replaceAll("\'", "");
							tmp.setSkillName(t);
							break;
						case 1: // 코드값
							if (i > 0) {
								String temp = cell.getStringCellValue().trim();
								String res = temp.replaceAll("\'", "");
								tmp.setSkillCode(res);
							} else
								title[7] = cell.getStringCellValue();
							break;
						case 2: // ipc명
							if (i > 0) {
								String temp = cell.getStringCellValue().trim();
								String res = temp.replaceAll("\'", "");
								tmp.setIpcName(res);
							} else
								title[8] = cell.getStringCellValue();
							break;
						case 3: // 코드값
							if (i > 0) {
								String temp = cell.getStringCellValue().trim();
								String res = temp.replaceAll("\'", "");
								tmp.setIpcCode(res);
							} else
								title[9] = cell.getStringCellValue();
							break;

						default:
							break;
						}

					}
					/*
					 * if(i == 0) { for(int k=0;k<7;k++) { System.out.println(title[k]);
					 * 
					 * fw.write(title[k]); fw.write("\t\t"); } fw.newLine(); fw.flush(); } else {
					 * example.DataSet.PrintData(ds[i], i, fw); }
					 */

					list.add(tmp);

					i++;
				}
			} catch (Exception e) {
				response.getWriter().write("<script>alert('문제가 발생하였습니다.');</script>");
				e.printStackTrace();
			} finally {
				/* fw.close(); */
			}
			line_num = i;
		}

		try {

			String id = DbLoginData.id;
			String pw = DbLoginData.pwd;

			String query = "show databases;";
			boolean isCreate = false;
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(sqlURL, id, pw);

			// Check DB create or not
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			if (stmt.execute(query)) {
				rs = stmt.getResultSet();
			}
			while (rs.next()) {
				if (rs.getString("Database").equals(DbLoginData.dbName)) {
					isCreate = true;
					break;
				}
			}

			/*
			 * if (!isCreate) { query = "Create database " + DbLoginData.dbName;
			 * stmt.executeUpdate(query); }
			 */
			stmt.executeQuery("use " + DbLoginData.dbName + ";");

			/* "name VARCHAR(13000) character set utf8, " */

			query = "CREATE TABLE  IF NOT EXISTS skillCode(skillName VARCHAR(1000) character set utf8, skillNum  VARCHAR(50)"
					+ ");";

			stmt.executeUpdate(query);

			query = "CREATE TABLE  IF NOT EXISTS ipcCode(ipcName VARCHAR(1000) character set utf8, ipcpc  VARCHAR(50)"
					+ ");";

			System.out.println("QQQQ" + query);
			stmt.executeUpdate(query);

			for (i = 1; i < list.size(); i++) {

				/*
				 * appDay = new
				 * java.text.SimpleDateFormat("yyyy-MM-dd").parse(ds[i].getDateAppDate());
				 */

				/*
				 * registerData = new
				 * java.text.SimpleDateFormat("yyyy-MM-dd").parse(con.getList(i).getRegisterDate
				 * ()); appData = new
				 * java.text.SimpleDateFormat("yyyy-MM-dd").parse(con.getList(i).getAppDate());
				 * 
				 * strRegi = dateFormat.format(registerData); strApp =
				 * dateFormat.format(appData);
				 */

				// skill코드 저장 부분
				if (list.get(i).getSkillName() != null) {
					query = "select skillNum from skillCode where skillNum = '" + list.get(i).getSkillCode() + "'";
					rs = stmt.executeQuery(query);
					System.out.println("qq" + query);

					if (rs.next()) {
						/*
						 * if (tmp == null) { query = "insert into skillData(ipcpc, num, aver,flowV)" +
						 * " values('" + con.getList(i).getSkillNum() + "'," + "1," + "0,0" + ");";
						 * stmt.executeUpdate(query); }
						 */
					} else if (!rs.next()) {
						query = "insert into skillCode(skillName,skillNum)" + " values('" + list.get(i).getSkillName()
								+ "','" + list.get(i).getSkillCode() + "');";
						stmt.executeUpdate(query);

						/*
						 * query =
						 * "update skillData set num=num+1 where ipcpc = '"+con.getList(i).getIpcpc()+
						 * "'"; stmt.executeUpdate(query);
						 */

					}
				}

				
				// ipcpc 저장 부분
				query = "select ipcpc from ipcCode where ipcpc = '" + list.get(i).getIpcCode() + "'";
				rs = stmt.executeQuery(query);
				System.out.println("qq" + query);

				if (rs.next()) {
					/*
					 * if (tmp == null) { query = "insert into skillData(ipcpc, num, aver,flowV)" +
					 * " values('" + con.getList(i).getSkillNum() + "'," + "1," + "0,0" + ");";
					 * stmt.executeUpdate(query); }
					 */
				} else if (!rs.next()) {
					query = "insert into ipcCode(ipcName,ipcpc)" + " values('" + list.get(i).getIpcName()
							+ "','" + list.get(i).getIpcCode() + "');";
					stmt.executeUpdate(query);

					/*
					 * query =
					 * "update skillData set num=num+1 where ipcpc = '"+con.getList(i).getIpcpc()+
					 * "'"; stmt.executeUpdate(query);
					 */

				}
			}

			
		} catch (Exception e) {
			response.getWriter().write("<script>alert('문제가 발생하였습니다.');location.href='./upload.jsp';</script>");
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
