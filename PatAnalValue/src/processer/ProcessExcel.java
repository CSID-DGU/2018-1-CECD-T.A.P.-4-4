package processer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import controller.CIDataController;
import controller.FileController;
import controller.JasonMakeController;
import controller.PriDataController;
import controller.SkillDataController;
import data.CIData;
import data.DbLoginData;
import helper.UploadHelper;

/**
 * Servlet implementation class ProcessExcel
 */
@WebServlet("/ProcessExcel")
public class ProcessExcel extends HttpServlet {
	private static final long serialVersionUID = 1L;
	double familyAdd = 0;
	double regAdd = 0;
	double influAdd = 0;
	double endDateAdd = 0;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProcessExcel() {
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
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/x-json; charset=utf-8");

		String cmd = request.getParameter("cmd");

		System.out.println(cmd);
		if (cmd == null || cmd.equals("parsingExcel")) {
			parsingExcel(request, response);
			calGScore(request, response);
			calFAver(request, response);
			calFAverS(request, response);
			makeJson(request, response);
		}

		response.getWriter().println("<script>alert('엑셀 파일 성공적으로 업로드');location.href='./upload.jsp';</script>");

	}

	public void makeJson(HttpServletRequest request, HttpServletResponse response) {
		FileController fa = new FileController();
		JSONObject jsonObject = new JSONObject();
		JasonMakeController jmc = new JasonMakeController();

		// path decoded "/C:/Program Files/Tomcat 6.0/webapps/myapp/WEB-INF/classes/"
		String decoded = getServletContext().getRealPath("/");

		if (decoded.startsWith("/")) {
			// path "C:/Program Files/Tomcat 6.0/webapps/myapp/WEB-INF/classes/"
			decoded = decoded.replaceFirst("/", "");
		}

		jsonObject = jmc.skillJasonObject();

		fa.saveFile(decoded, jsonObject);

	}

	public void calFAver(HttpServletRequest request, HttpServletResponse response) {
		SkillDataController ins = SkillDataController.getInstance();
		PriDataController insP = PriDataController.getInstance();

		List<String> skillNum = ins.getAllSkillNum();

		/*
		 * for(int i = 0; i < ipcpc.size(); i++){ insP.getFAValue(ipcpc.get(i));
		 * ins.setFlowV(ipcpc.get(i), insP.getFlowV()); ins.setAver(ipcpc.get(i),
		 * insP.getAver());
		 * 
		 * insP.setFlowV(0); insP.setAver(0); }
		 */

		for (int i = 0; i < skillNum.size(); i++) {
			insP.getFAValue(skillNum.get(i));
			ins.setFlowV(skillNum.get(i), insP.getFlowV());
			ins.setAver(skillNum.get(i), insP.getAver());

			insP.setFlowV(0);
			insP.setAver(0);
		}

	}

	public void calFAverS(HttpServletRequest request, HttpServletResponse response) {
		CIDataController ins = CIDataController.getInstance();
		PriDataController insP = PriDataController.getInstance();

		List<CIData> ipcpc = ins.getAllIpcpcNum();

		for (int i = 0; i < ipcpc.size(); i++) {
			insP.getFAValueS(ipcpc.get(i).getIpcpc(),ipcpc.get(i).getSkillNum());
			ins.setFlowV(ipcpc.get(i).getIpcpc(),ipcpc.get(i).getSkillNum(), insP.getFlowV());
			ins.setAver(ipcpc.get(i).getIpcpc(),ipcpc.get(i).getSkillNum(), insP.getAver());

			insP.setFlowV(0);
			insP.setAver(0);

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

	public void parsingExcel(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String savePath = request.getSession().getServletContext().getRealPath(UploadHelper.upDir);
		PriDataController con = PriDataController.getInstance();
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

						case 0: // 발명의 명칭 : string(mysql 입력)
							boolean isAlreadyStored = false;
							if (i == 0) {
								title[2] = cell.getStringCellValue();
								break;
							}

							con.getInsert().setName(cell.getStringCellValue().trim());
							break;
						case 1: // 요약
							if (i > 0) {
								String temp = cell.getStringCellValue().trim();
								String res = temp.replaceAll("\'", "");
								con.getInsert().setSum(res);
							} else
								title[7] = cell.getStringCellValue();
							break;
						case 2: // 청구항
							if (i > 0) {
								String temp = cell.getStringCellValue().trim();
								String res = temp.replaceAll("\'", "");
								con.getInsert().setClaim(res);
							} else
								title[8] = cell.getStringCellValue();
							break;
						case 3: // 출원 번호
							if (i > 0) {
								String temp = cell.getStringCellValue().trim();
								String res = temp.replaceAll("\'", "");
								con.getInsert().setPatNum(res);
							} else
								title[9] = cell.getStringCellValue();
							break;
						case 5: // 출원연도
							if (i > 0)
								con.getInsert().setYear((int) cell.getNumericCellValue());
							else
								title[10] = cell.getStringCellValue();
							break;
						case 4: // 출원일 (4) : double -> 나중에 Date로 바꿀 것

							if (i > 0) {

								con.getInsert().setAppDate(cell.getStringCellValue().trim());

							} else
								title[3] = cell.getStringCellValue();
							break;
						case 7: // 등록 날짜
							if (i > 0) {
								con.getInsert().setRegisterDate(Double.toString(cell.getNumericCellValue()));

							} else
								title[11] = cell.getStringCellValue();
							break;
						case 8: // 출원인
							if (i > 0)
								con.getInsert().setAppName(cell.getStringCellValue().trim());
							else
								title[12] = cell.getStringCellValue();
							break;

						case 9: // CPC
							if (i > 0) {
								/* con.getInsert().setAppName(cell.getStringCellValue().trim()); */
							} else {
								title[12] = cell.getStringCellValue();
							}
							break;

						case 10: // IPC
							if (i > 0)
								con.getInsert().setIpcpc(cell.getStringCellValue().trim());
							else
								title[12] = cell.getStringCellValue();
							break;
						case 11: // family
							if (i > 0) {

								con.getInsert().setFamily(cell.getStringCellValue().trim());

							} else
								title[6] = cell.getStringCellValue();
							break;
						case 12: // 상태정보
							if (i > 0) {
								con.getInsert().setRegisterCheck(cell.getStringCellValue().trim());

							} else
								title[4] = cell.getStringCellValue();
							break;
						case 13: // 인용회수
							if (i > 0) {
								con.getInsert().setCited((int) cell.getNumericCellValue());

							} else
								title[4] = cell.getStringCellValue();
							break;
						case 14: // 인용 특허
							if (i > 0) {
								con.getInsert().setCitedPat(cell.getStringCellValue().trim());

							} else
								title[4] = cell.getStringCellValue();
							break;
						case 15: // 인용 특허
							if (i > 0) {
								con.getInsert().setSkillNum(cell.getStringCellValue().trim());

							} else
								title[4] = cell.getStringCellValue();
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

							con.getInsert().setName(cell.getStringCellValue().trim());
							break;
						case 1: // 요약
							if (i > 0) {
								String temp = cell.getStringCellValue().trim();
								String res = temp.replaceAll("\'", "");
								con.getInsert().setSum(res);
							} else
								title[7] = cell.getStringCellValue();
							break;
						case 2: // 청구항
							if (i > 0) {
								String temp = cell.getStringCellValue().trim();
								String res = temp.replaceAll("\'", "");
								con.getInsert().setClaim(res);
							} else
								title[8] = cell.getStringCellValue();
							break;
						case 3: // 출원 번호
							if (i > 0) {
								String temp = cell.getStringCellValue().trim();
								String res = temp.replaceAll("\'", "");
								con.getInsert().setPatNum(res);
							} else
								title[9] = cell.getStringCellValue();
							break;
						case 5: // 출원연도
							if (i > 0)
								con.getInsert().setYear((int) cell.getNumericCellValue());
							else
								title[10] = cell.getStringCellValue();
							break;
						case 4: // 출원일 (4) : double -> 나중에 Date로 바꿀 것

							if (i > 0) {

								con.getInsert().setAppDate(cell.getStringCellValue().trim());

							} else
								title[3] = cell.getStringCellValue();
							break;
						case 7: // 등록 날짜
							if (i > 0) {
								System.out.println(cell.getStringCellValue().trim());
								con.getInsert().setRegisterDate(cell.getStringCellValue().trim());

							} else
								title[11] = cell.getStringCellValue();
							break;
						case 8: // 출원인
							if (i > 0)
								con.getInsert().setAppName(cell.getStringCellValue().trim());
							else
								title[12] = cell.getStringCellValue();
							break;

						case 9: // CPC
							if (i > 0) {
								/* con.getInsert().setAppName(cell.getStringCellValue().trim()); */
							} else {
								title[12] = cell.getStringCellValue();
							}
							break;

						case 10: // IPC
							if (i > 0)
								con.getInsert().setIpcpc(cell.getStringCellValue().trim());
							else
								title[12] = cell.getStringCellValue();
							break;
						case 11: // family
							if (i > 0) {

								con.getInsert().setFamily(cell.getStringCellValue().trim());
								System.out.println(con.getInsert().getFamily());
							} else
								title[6] = cell.getStringCellValue();
							break;
						case 12: // 상태정보
							if (i > 0) {
								con.getInsert().setRegisterCheck(cell.getStringCellValue().trim());

							} else
								title[4] = cell.getStringCellValue();
							break;
						case 13: // 인용회수
							if (i > 0) {
								con.getInsert().setCited((int) cell.getNumericCellValue());

							} else
								title[4] = cell.getStringCellValue();
							break;
						case 14: // 인용 특허
							if (i > 0) {
								con.getInsert().setCitedPat(cell.getStringCellValue().trim());

							} else
								title[4] = cell.getStringCellValue();
							break;
						case 15: // 인용 특허
							if (i > 0) {
								con.getInsert().setSkillNum(cell.getStringCellValue().trim());

							} else
								title[4] = cell.getStringCellValue();
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

					con.setList();

					con.newInsert();
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
			query = "CREATE TABLE  IF NOT EXISTS priData(" + "name VARCHAR(5000) character set utf8, "
					+ " ipcpc VARCHAR(50), " + " skillNum VARCHAR(50), " + " registerDate VARCHAR(50), "
					+ " appDate VARCHAR(50), " + " citedPat VARCHAR(500) character set utf8, "
					+ " family VARCHAR(200) character set utf8, " + " patNum VARCHAR(20) ," + " year int,"
					+ " cited int," + " leftyear int,"
					/* + " sum VARCHAR(10000) character set utf8," */
					/* + " claim VARCHAR(4000) character set utf8," */ + " appName VARCHAR(50) character set utf8,"
					+ " registerCheck VARCHAR(50) character set utf8" + ");";
			stmt.executeUpdate(query);

			query = "CREATE TABLE  IF NOT EXISTS gscore(patNum VARCHAR(20) , "
					+ " grade VARCHAR(5) character set utf8, " + " score double" + ");";
			stmt.executeUpdate(query);

			query = "CREATE TABLE  IF NOT EXISTS skillData(" + " ipcpc VARCHAR(20) character set utf8," + " num int,"
					+ "aver double," + "flowV double" + ");";
			stmt.executeUpdate(query);

			query = "CREATE TABLE  IF NOT EXISTS ciData(ipcpc VARCHAR(20) , skillNum  VARCHAR(50),"
					+ " CIName VARCHAR(1000) character set utf8," + "aver double," + "flowV double" + ");";

			stmt.executeUpdate(query);

			query = "CREATE TABLE  IF NOT EXISTS claimDigest(patNum VARCHAR(20) ,ipcpc VARCHAR(20) , skillNum  VARCHAR(50), "
					+ " claim VARCHAR(10000) character set utf8," + "digest VARCHAR(10000) character set utf8" + ");";

			System.out.println("QQQQ" + query);
			stmt.executeUpdate(query);

			java.util.Date registerData = null;
			java.util.Date appData = null;

			java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
			String strRegi = null;
			String strApp = null;

			for (i = 1; i < con.getListLength(); i++) {

				/*
				 * appDay = new
				 * java.text.SimpleDateFormat("yyyy-MM-dd").parse(ds[i].getDateAppDate());
				 */

				if (con.getList(i).getRegisterDate() == null) {
					strRegi = "";
				} else if (!con.getList(i).getRegisterDate().equals("")) {
					registerData = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(con.getList(i).getRegisterDate());

					strRegi = dateFormat.format(registerData);
				} else {
					strRegi = "";
				}

				/*
				 * if(con.getList(i).getAppDate()==null) { strRegi = "2000-00-00"; } else
				 */
				if (con.getList(i).getAppDate() == null) {
					strApp = "";
				} else if (!con.getList(i).getAppDate().equals("")) {
					appData = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(con.getList(i).getAppDate());
					strApp = dateFormat.format(appData);
					/* con.getList(i).setYear(appData.getYear()); */
				} else {
					strApp = "";
				}

				/*
				 * registerData = new
				 * java.text.SimpleDateFormat("yyyy-MM-dd").parse(con.getList(i).getRegisterDate
				 * ()); appData = new
				 * java.text.SimpleDateFormat("yyyy-MM-dd").parse(con.getList(i).getAppDate());
				 * 
				 * strRegi = dateFormat.format(registerData); strApp =
				 * dateFormat.format(appData);
				 */
				query = "select * from priData where patnum = '" + con.getList(i).getPatNum() + "'";

				rs = stmt.executeQuery(query);

				if (!(rs.next())) {

					query = "insert into priData(name,ipcpc, skillNum, registerDate, appDate, citedPat, family, patNum, year, cited, leftyear, appName,registerCheck)"
							+ " values('" + con.getList(i).getName() + "','" + con.getList(i).getIpcpc() + "','"
							+ con.getList(i).getSkillNum() + "','" + strRegi + "','" + strApp + "','"
							+ con.getList(i).getCitedPat() + "','" + con.getList(i).getFamily() + "','"
							+ con.getList(i).getPatNum() + "'," + con.getList(i).getYear() + ","
							+ con.getList(i).getCited() + "," + con.getList(i).getLeftyear() + ",'"
							/* + con.getList(i).getSum() + "','" */
							/* + con.getList(i).getClaim() + "','" */
							+ con.getList(i).getAppName() + "','" + con.getList(i).getRegisterCheck() + "');";

					stmt.executeUpdate(query);

					query = "insert into claimDigest(patNum,ipcpc, skillNum,claim,digest)" + " values('"
							+ con.getList(i).getPatNum() + "','" + con.getList(i).getIpcpc() + "','"
							+ con.getList(i).getSkillNum() + "','" + con.getList(i).getClaim() + "','"
							+ con.getList(i).getSum() + "');";

					stmt.executeUpdate(query);
				}
				
				//skillNum 저장 부분
				query = "select ipcpc from skillData where ipcpc = '" + con.getList(i).getSkillNum() + "'";
				rs = stmt.executeQuery(query);
				System.out.println("qq" + query);

				if (rs.next()) {
					String tmp = rs.getString("ipcpc");
					query = "update skillData set num=num+1 where ipcpc = '" + con.getList(i).getSkillNum() + "'";
					stmt.executeUpdate(query);
					/*
					 * if (tmp == null) { query = "insert into skillData(ipcpc, num, aver,flowV)" +
					 * " values('" + con.getList(i).getSkillNum() + "'," + "1," + "0,0" + ");";
					 * stmt.executeUpdate(query); }
					 */
				} else if (!rs.next()) {
					query = "insert into skillData(ipcpc, num, aver,flowV)" + " values('" + con.getList(i).getSkillNum()
							+ "'," + "1," + "0,0" + ");";
					stmt.executeUpdate(query);

					/*
					 * query =
					 * "update skillData set num=num+1 where ipcpc = '"+con.getList(i).getIpcpc()+
					 * "'"; stmt.executeUpdate(query);
					 */

				}
				
				String tmp = con.getList(i).getIpcpc();
				
				String[] txtArr = tmp.split(" ");
				//ipcpc 저장 부분
				query = "select ipcpc from ciData where ipcpc = '" + txtArr[0] + "' and skillNum = '"+con.getList(i).getSkillNum()+"'";

				rs = stmt.executeQuery(query);
				System.out.println("qq2" + query);

				if (rs.next()) {
					
					System.out.println(tmp);
					System.out.println("추가 될 이유가 없슴");

					/*
					 * if (tmp == null) { query =
					 * "insert into ciData(ipcpc,skillNum ,CIName, aver,flowV)" + " values('" +
					 * con.getList(i).getIpcpc() + "','" + con.getList(i).getSkillNum() + "','" +
					 * " '," + "0,0" + ");"; stmt.executeUpdate(query); }
					 */
				} else if (!rs.next()) {
					
					
					query = "insert into ciData(ipcpc,skillNum, CIName, aver,flowV)" + " values('"
							+ txtArr[0] + "','" + con.getList(i).getSkillNum() + "','" + " '," + "0,0"
							+ ");";
					System.out.println("QQuery" + query);
					stmt.executeUpdate(query);
				}

			}

			con.getList().clear();

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
