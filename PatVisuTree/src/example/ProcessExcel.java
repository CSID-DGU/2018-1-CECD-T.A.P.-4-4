package example;

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

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

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

		String cmd = request.getParameter("cmd");

		System.out.println(cmd);
		if (cmd == null || cmd.equals("parsingExcel")) {
			parsingExcel(request, response);
		} 
	}
	

	public void parsingExcel(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String savePath = request.getSession().getServletContext().getRealPath(UploadHelper.upDir);

		int line_num = 0;
		int i = 0, nCnt = 0, nADScnt = 0, nNADScnt = 0, w = 0, cB = 0, cM = 0;
		String[] title = new String[50];
		DataSet[] ds = new DataSet[100010];
		TechExp[] tw = new TechExp[500];
		BigClsExp[] bce = new BigClsExp[20];
		MidClsExp[] mce = new MidClsExp[30];
		NationDataSet[] nds = new NationDataSet[200]; // 일반적인 도넛형
		DataSetYear[][] dsy = new DataSetYear[5][20200]; // 연도별로 classification 한 도넛형
		int[] cntClassYearArr = new int[5]; // 연도별 classification(소분류) 의 크기
		String imgSrc = "/example/img/dooho_main_logo.jpg";
		String indexLink = "./index.jsp";
		String filePath = (String) request.getAttribute("filePath");
		String ext = (String) request.getAttribute("ext");
		String fileName = filePath.substring(filePath.lastIndexOf('/') + 1, filePath.lastIndexOf('.'));
		/* String savePath = UploadHelper.fileDir; */
		String sqlURL = "jdbc:mysql://" + DbLoginData.url + "?serverTimezone=UTC";
		BufferedWriter fw = null;
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write("<a href=\"" + indexLink + "\">");
		response.getWriter().write("<img src=\"" + imgSrc + "\">");
		response.getWriter().write("</a><br/><br/>");

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
					ds[i] = new DataSet();
					tw[w] = new TechExp();
					bce[cB] = new BigClsExp();
					mce[cM] = new MidClsExp();
					while (cellIterator.hasNext()) {
						Cell cell = cellIterator.next();
						int k = cell.getColumnIndex();

						switch (k) {

						case 0: // 국가코드(3) : string
							boolean isAlreadyStored = false;
							if (i == 0) {
								title[2] = cell.getStringCellValue();
								break;
							}

							ds[i].setStrNationCode(cell.getStringCellValue().trim());

							for (int j = 0; j < nCnt; j++) {
								if (ds[i].getStrNationCode().equals(nds[j].getNation())) {
									nds[j].setNCount((nds[j].getNCount() + 1));
									isAlreadyStored = true;
									break;
								}
							}
							if (!isAlreadyStored) {
								nds[nCnt] = new NationDataSet();
								nds[nCnt].setNation(ds[i].getStrNationCode());
								nds[nCnt].setNCount(1);
								nCnt++;
							}

							break;
						case 4: // 발명의 명칭 : string(mysql 입력)
							if (i > 0) {
								String temp = cell.getStringCellValue().trim();
								String res = temp.replaceAll("\'", "");
								ds[i].setInvname(res);
							} else
								title[7] = cell.getStringCellValue();
							break;
						case 6: // 요약 : string(mysql 입력)
							if (i > 0) {
								String temp = cell.getStringCellValue().trim();
								String res = temp.replaceAll("\'", "");
								ds[i].setSummary(res);
							} else
								title[8] = cell.getStringCellValue();
							break;
						case 8: // 대표청구항 : string(mysql 입력)
							if (i > 0) {
								String temp = cell.getStringCellValue().trim();
								String res = temp.replaceAll("\'", "");
								ds[i].setRepclaim(res);
							} else
								title[9] = cell.getStringCellValue();
							break;
						case 15: // 출원번호 : string(mysql 입력)
							if (i > 0)
								ds[i].setPatnumber(cell.getStringCellValue().trim());
							else
								title[10] = cell.getStringCellValue();
							break;
						case 16: // 출원일 (4) : double -> 나중에 Date로 바꿀 것

							if (i > 0) {

								ds[i].setDateAppDate(cell.getStringCellValue());

							} else
								title[3] = cell.getStringCellValue();
							break;
						case 23: // 등록번호 : string(mysql 입력)
							if (i > 0) {
								if (cell.getCellTypeEnum() == CellType.NUMERIC) {
									ds[i].setRegnum(String.valueOf(cell.getNumericCellValue()));
								} else {
									ds[i].setRegnum(cell.getStringCellValue().trim());
								}

							} else
								title[11] = cell.getStringCellValue();
							break;
						case 24: // 등록일 : string or date(mysql 입력)
							if (i > 0)
								ds[i].setRegday(cell.getStringCellValue().trim());
							else
								title[12] = cell.getStringCellValue();
							break;
						case 27: // 출원인대표명화(7) : string
							if (i > 0) {

								ds[i].setStrAppName(cell.getStringCellValue()); // 오류 1

							} else
								title[6] = cell.getStringCellValue();
							break;
						case 28: // 출원인대표국적(5) : string
							if (i > 0) {
								ds[i].setStrAppNation(cell.getStringCellValue().trim());

							} else
								title[4] = cell.getStringCellValue();
							break;

						case 12: // 중분류(1) : string
							if (i > 0) {
								ds[i].setStrMidClassification(cell.getStringCellValue());

							} else
								title[0] = cell.getStringCellValue();
							break;
						case 13:
							if (i > 0) {
								String tmp = cell.getStringCellValue();
								ds[i].setStrSmallClassifiation(tmp);
								ds[i].setStrBigClassfication(tmp.substring(0, 1));

							} else
								title[0] = cell.getStringCellValue();
							break;

						case 37: // 발명자
							if (i > 0) {
								ds[i].setInventor(cell.getStringCellValue());

							} else
								title[14] = cell.getStringCellValue();
							break;
						case 43: // 우선권국가
							if (i > 0) {
								ds[i].setPreferNat(cell.getStringCellValue());

							} else
								title[15] = cell.getStringCellValue();
							break;
						case 44: // 우선권 주장일
							if (i > 0) {
								ds[i].setPreferDate(cell.getStringCellValue());

							} else
								title[16] = cell.getStringCellValue();
							break;
						case 19: // 공개 번호
							if (i > 0) {

								if (cell.getCellTypeEnum() == CellType.NUMERIC) {
									ds[i].setOpenNum(String.valueOf(cell.getNumericCellValue()));
								} else {
									ds[i].setOpenNum(cell.getStringCellValue());
								}

							} else
								title[17] = cell.getStringCellValue();
							break;
						case 20: // 공개일
							if (i > 0) {
								ds[i].setOpenDate(cell.getStringCellValue());

							} else
								title[18] = cell.getStringCellValue();
							break;
						case 85: // 상태
							if (i > 0) {
								ds[i].setState(cell.getStringCellValue());

							} else
								title[19] = cell.getStringCellValue();
							break;
						case 79: // 패밀리 개별국 문헌 수(6) : double -> 나중에 int로 바꿀 것
							if (i > 0) {
								ds[i].setNumFamilyLiterature(cell.getStringCellValue()); // 오류 2

							} else
								title[5] = cell.getStringCellValue();
							break;
						case 97: // 상세보기 링크 : string(mysql 입력)
							if (i > 0)
								ds[i].setDetail(cell.getStringCellValue().trim());
							else
								title[13] = cell.getStringCellValue();
							break;
						case 104: // 대분류 분류값 : string
							if (i > 0) {
								String tmp = cell.getStringCellValue();
								System.out.println(tmp);

								if ((tmp != null) && (!(tmp.equals("")))) {

									bce[cB].setbCls(cell.getStringCellValue());

								}

							} else
								title[1] = cell.getStringCellValue();

							break;
						case 105: // 대분류 설명값
							if (i > 0) {
								String tmp = cell.getStringCellValue();
								System.out.println(tmp);

								if ((tmp != null) && (!(tmp.equals("")))) {

									bce[cB].setbClsExp(cell.getStringCellValue());
									cB++;

								}

							} else
								title[1] = cell.getStringCellValue();

							break;
						case 106: // 중분류 분류값
							if (i > 0) {
								String tmp = cell.getStringCellValue();
								System.out.println(tmp);

								if ((tmp != null) && (!(tmp.equals("")))) {

									mce[cM].setmCls(cell.getStringCellValue());

								}

							} else
								title[1] = cell.getStringCellValue();

							break;
						case 107: // 중분류 설명값
							if (i > 0) {
								String tmp = cell.getStringCellValue();
								System.out.println(tmp);

								if ((tmp != null) && (!(tmp.equals("")))) {

									mce[cM].setmClsExp(cell.getStringCellValue());
									cM++;

								}

							} else
								title[1] = cell.getStringCellValue();

							break;
						case 108: // 소분류(2) : string
							if (i > 0) {
								String tmp = cell.getStringCellValue();

								if ((tmp != null) && (!(tmp.equals("")))) {

									tw[w].setsClass(cell.getStringCellValue());

								}

							} else
								title[1] = cell.getStringCellValue();

							break;
						case 109: // 기술설명
							if (i > 0) {
								String tmp = cell.getStringCellValue().trim();
								if ((tmp != null) && (!(tmp.equals("")))) {

									tw[w].setTechExp(tmp);
									w++;
								}
							} else
								title[21] = cell.getStringCellValue();
							break;
						case 102: // wips on key
							if (i > 0)
								ds[i].setPdfNum(cell.getStringCellValue().trim());
							else
								title[22] = cell.getStringCellValue();
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
					ds[i] = new DataSet();
					tw[w] = new TechExp();
					bce[cB] = new BigClsExp();
					mce[cM] = new MidClsExp();
					while (cellIterator.hasNext()) {
						Cell cell = cellIterator.next();
						int k = cell.getColumnIndex();

						switch (k) {

						case 0: // 국가코드(3) : string
							boolean isAlreadyStored = false;
							if (i == 0) {
								title[2] = cell.getStringCellValue();
								break;
							}

							ds[i].setStrNationCode(cell.getStringCellValue().trim());

							for (int j = 0; j < nCnt; j++) {
								if (ds[i].getStrNationCode().equals(nds[j].getNation())) {
									nds[j].setNCount((nds[j].getNCount() + 1));
									isAlreadyStored = true;
									break;
								}
							}
							if (!isAlreadyStored) {
								nds[nCnt] = new NationDataSet();
								nds[nCnt].setNation(ds[i].getStrNationCode());
								nds[nCnt].setNCount(1);
								nCnt++;
							}

							break;
						case 4: // 발명의 명칭 : string(mysql 입력)
							if (i > 0) {
								String temp = cell.getStringCellValue().trim();
								String res = temp.replaceAll("\'", "");
								ds[i].setInvname(res);
							} else
								title[7] = cell.getStringCellValue();
							break;
						case 6: // 요약 : string(mysql 입력)
							if (i > 0) {
								String temp = cell.getStringCellValue().trim();
								String res = temp.replaceAll("\'", "");
								ds[i].setSummary(res);
							} else
								title[8] = cell.getStringCellValue();
							break;
						case 8: // 대표청구항 : string(mysql 입력)
							if (i > 0) {
								String temp = cell.getStringCellValue().trim();
								String res = temp.replaceAll("\'", "");
								ds[i].setRepclaim(res);
							} else
								title[9] = cell.getStringCellValue();
							break;
						case 15: // 출원번호 : string(mysql 입력)
							if (i > 0)
								ds[i].setPatnumber(cell.getStringCellValue().trim());
							else
								title[10] = cell.getStringCellValue();
							break;
						case 16: // 출원일 (4) : double -> 나중에 Date로 바꿀 것

							if (i > 0) {

								ds[i].setDateAppDate(cell.getStringCellValue());

							} else
								title[3] = cell.getStringCellValue();
							break;
						case 23: // 등록번호 : string(mysql 입력)
							if (i > 0) {
								if (cell.getCellTypeEnum() == CellType.NUMERIC) {
									ds[i].setRegnum(String.valueOf(cell.getNumericCellValue()));
								} else {
									ds[i].setRegnum(cell.getStringCellValue().trim());
								}

							} else
								title[11] = cell.getStringCellValue();
							break;
						case 24: // 등록일 : string or date(mysql 입력)
							if (i > 0)
								ds[i].setRegday(cell.getStringCellValue().trim());
							else
								title[12] = cell.getStringCellValue();
							break;
						case 27: // 출원인대표명화(7) : string
							if (i > 0) {

								ds[i].setStrAppName(cell.getStringCellValue()); // 오류 1

							} else
								title[6] = cell.getStringCellValue();
							break;
						case 28: // 출원인대표국적(5) : string
							if (i > 0) {
								ds[i].setStrAppNation(cell.getStringCellValue().trim());

							} else
								title[4] = cell.getStringCellValue();
							break;

						case 12: // 중분류(1) : string
							if (i > 0) {
								ds[i].setStrMidClassification(cell.getStringCellValue());

							} else
								title[0] = cell.getStringCellValue();
							break;
						case 13:
							if (i > 0) {
								String tmp = cell.getStringCellValue();
								ds[i].setStrSmallClassifiation(tmp);
								ds[i].setStrBigClassfication(tmp.substring(0, 1));

							} else
								title[0] = cell.getStringCellValue();
							break;

						case 37: // 발명자
							if (i > 0) {
								ds[i].setInventor(cell.getStringCellValue());

							} else
								title[14] = cell.getStringCellValue();
							break;
						case 43: // 우선권국가
							if (i > 0) {
								ds[i].setPreferNat(cell.getStringCellValue());

							} else
								title[15] = cell.getStringCellValue();
							break;
						case 44: // 우선권 주장일
							if (i > 0) {
								ds[i].setPreferDate(cell.getStringCellValue());

							} else
								title[16] = cell.getStringCellValue();
							break;
						case 19: // 공개 번호
							if (i > 0) {

								if (cell.getCellTypeEnum() == CellType.NUMERIC) {
									ds[i].setOpenNum(String.valueOf(cell.getNumericCellValue()));
								} else {
									ds[i].setOpenNum(cell.getStringCellValue());
								}

							} else
								title[17] = cell.getStringCellValue();
							break;
						case 20: // 공개일
							if (i > 0) {
								ds[i].setOpenDate(cell.getStringCellValue());

							} else
								title[18] = cell.getStringCellValue();
							break;
						case 85: // 상태
							if (i > 0) {
								ds[i].setState(cell.getStringCellValue());

							} else
								title[19] = cell.getStringCellValue();
							break;
						case 79: // 패밀리 개별국 문헌 수(6) : double -> 나중에 int로 바꿀 것
							if (i > 0) {
								ds[i].setNumFamilyLiterature(cell.getStringCellValue()); // 오류 2

							} else
								title[5] = cell.getStringCellValue();
							break;
						case 97: // 상세보기 링크 : string(mysql 입력)
							if (i > 0)
								ds[i].setDetail(cell.getStringCellValue().trim());
							else
								title[13] = cell.getStringCellValue();
							break;
						case 104: // 대분류 분류값 : string
							if (i > 0) {
								String tmp = cell.getStringCellValue();
								System.out.println(tmp);

								if ((tmp != null) && (!(tmp.equals("")))) {

									bce[cB].setbCls(cell.getStringCellValue());

								}

							} else
								title[1] = cell.getStringCellValue();

							break;
						case 105: // 대분류 설명값
							if (i > 0) {
								String tmp = cell.getStringCellValue();
								System.out.println(tmp);

								if ((tmp != null) && (!(tmp.equals("")))) {

									bce[cB].setbClsExp(cell.getStringCellValue());
									cB++;

								}

							} else
								title[1] = cell.getStringCellValue();

							break;
						case 106: // 중분류 분류값
							if (i > 0) {
								String tmp = cell.getStringCellValue();
								System.out.println(tmp);

								if ((tmp != null) && (!(tmp.equals("")))) {

									mce[cM].setmCls(cell.getStringCellValue());

								}

							} else
								title[1] = cell.getStringCellValue();

							break;
						case 107: // 중분류 설명값
							if (i > 0) {
								String tmp = cell.getStringCellValue();
								System.out.println(tmp);

								if ((tmp != null) && (!(tmp.equals("")))) {

									mce[cM].setmClsExp(cell.getStringCellValue());
									cM++;

								}

							} else
								title[1] = cell.getStringCellValue();

							break;
						case 108: // 소분류(2) : string
							if (i > 0) {
								String tmp = cell.getStringCellValue();

								if ((tmp != null) && (!(tmp.equals("")))) {

									tw[w].setsClass(cell.getStringCellValue());

								}

							} else
								title[1] = cell.getStringCellValue();

							break;
						case 109: // 기술설명
							if (i > 0) {
								String tmp = cell.getStringCellValue().trim();
								if ((tmp != null) && (!(tmp.equals("")))) {

									tw[w].setTechExp(tmp);
									w++;
								}
							} else
								title[21] = cell.getStringCellValue();
							break;
						case 102: // wips on key
							if (i > 0)
								ds[i].setPdfNum(cell.getStringCellValue().trim());
							else
								title[22] = cell.getStringCellValue();
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
			if (!isCreate) {
				query = "Create database " + DbLoginData.dbName;
				stmt.executeUpdate(query);
			}
			stmt.executeQuery("use " + DbLoginData.dbName + ";");

			query = "CREATE TABLE  IF NOT EXISTS visualization(sClass VARCHAR(10), " + " mClass VARCHAR(4), "
					+ " bClass VARCHAR(4), " + " natCode VARCHAR(3), " + " appDate Date, " + " appNat VARCHAR(3), "
					+ " numFamily VARCHAR(200), " + " appName VARCHAR(200) character set utf8, "
					+ " patNum VARCHAR(20) /* primary key */," + " openNum VARCHAR(20)," + " preferNat VARCHAR(100),"
					+ " preferDate VARCHAR(100)," + " inventor VARCHAR(200) character set utf8,"
					+ " serial VARCHAR(500) character set utf8" + ");";
			stmt.executeUpdate(query);
			query = "CREATE TABLE  IF NOT EXISTS tree(patNum VARCHAR(20) /* primary key */, "
					+ " invName VARCHAR(500) character set utf8, " + " pdfNum VARCHAR(100) character set utf8,"
					+ " summary VARCHAR(10000) character set utf8, " + " repClaim VARCHAR(10000) character set utf8, "
					+ " regNum VARCHAR(12)," + " regDay datetime, " + " detail VARCHAR(255) character set utf8,"
					+ " state VARCHAR(50) character set utf8," + " openDate datetime,"
					+ " serial VARCHAR(500) character set utf8" + ");";
			stmt.executeUpdate(query);
			query = "CREATE TABLE  IF NOT EXISTS ipcCpc(ipcCpc VARCHAR(20)  primary key , "
					+ " detail VARCHAR(500) character set utf8" + ");";
			stmt.executeUpdate(query);
			query = "CREATE TABLE  IF NOT EXISTS fileName(fName VARCHAR(5000) /* primary key */, "
					+ " serial VARCHAR(500) character set utf8" + ");";
			stmt.executeUpdate(query);
			query = "CREATE TABLE  IF NOT EXISTS techExp(sCls VARCHAR(20) /* primary key */, "
					+ " mCls VARCHAR(20) character set utf8," + " bCls VARCHAR(20) character set utf8,"
					+ " exp VARCHAR(500) character set utf8," + " serial VARCHAR(500) character set utf8" + ");";
			stmt.executeUpdate(query);
			query = "CREATE TABLE  IF NOT EXISTS BClsExp(" + " bCls VARCHAR(20) character set utf8,"
					+ " exp VARCHAR(500) character set utf8," + " serial VARCHAR(500) character set utf8" + ");";
			stmt.executeUpdate(query);
			query = "CREATE TABLE  IF NOT EXISTS MClsExp(" + " mCls VARCHAR(20) character set utf8,"
					+ " exp VARCHAR(500) character set utf8," + " serial VARCHAR(500) character set utf8" + ");";
			stmt.executeUpdate(query);

			java.util.Date appDay = null;
			java.util.Date regDay = null;
			java.util.Date openDay = null;

			String strRegDay = null;
			String strOpenDay = null;

			java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");

			for (i = 1; i < line_num; i++) {

				appDay = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(ds[i].getDateAppDate());

				/*
				 * System.out.println(ds[i].getStrSmallClassifiation() + "','" +
				 * ds[i].getStrMidClassification()+"','" + ds[i].getStrNationCode() + "','" +
				 * ds[i].getDateAppDate() + "','" + ds[i].getStrAppNation() + "','" +
				 * ds[i].getNumFamilyLiterature().intValue() + "','" + ds[i].getStrAppName() +
				 * "','" + ds[i].getPatnumber());
				 */

				query = "insert into visualization(sClass, mClass, bClass, natCode, appDate, appNat, numFamily, appName, patNum, openNum, preferNat, preferDate, inventor,serial)"
						+ " values('" + ds[i].getStrSmallClassifiation() + "','" + ds[i].getStrMidClassification()
						+ "','" + ds[i].getStrBigClassfication() + "','" + ds[i].getStrNationCode() + "','"
						+ dateFormat.format(appDay) + "','" + ds[i].getStrAppNation() + "','"
						+ ds[i].getNumFamilyLiterature() + "',\"" + ds[i].getStrAppName() + "\",'"
						+ ds[i].getPatnumber() + "','" + ds[i].getOpenNum() + "','" + ds[i].getPreferNat() + "','"
						+ ds[i].getPreferDate() + "',\"" + ds[i].getInventor() + "\",'" + uid + "');";

				stmt.executeUpdate(query);
			}

			for (i = 1; i < line_num; i++) {
				if (!ds[i].getRegday().equals("")) {
					regDay = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(ds[i].getRegday());
					strRegDay = dateFormat.format(regDay);
				} else {
					strRegDay = null;
				}

				if (!ds[i].getOpenDate().equals("")) {
					openDay = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(ds[i].getOpenDate());
					strOpenDay = dateFormat.format(openDay);
				} else {
					strOpenDay = null;
				}

				if ((strOpenDay == null) && (strRegDay != null)) {
					query = "insert into tree(patNum, invName,pdfNum, summary, repClaim, regNum, regDay, detail,state,openDate,serial)"
							+ " values('" + ds[i].getPatnumber() + "','" + ds[i].getInvname() + "','"
							+ ds[i].getPdfNum() + "','" + ds[i].getSummary() + "','" + ds[i].getRepclaim() + "','"
							+ ds[i].getRegnum() + "','" + strRegDay + "','" + ds[i].getDetail() + "','"
							+ ds[i].getState() + "'," + strOpenDay + ",'" + uid + "');";

				} else if ((strOpenDay != null) && (strRegDay == null)) {
					query = "insert into tree(patNum, invName,pdfNum, summary, repClaim, regNum, regDay, detail,state,openDate,serial)"
							+ " values('" + ds[i].getPatnumber() + "','" + ds[i].getInvname() + "','"
							+ ds[i].getPdfNum() + "','" + ds[i].getSummary() + "','" + ds[i].getRepclaim() + "','"
							+ ds[i].getRegnum() + "'," + strRegDay + ",'" + ds[i].getDetail() + "','" + ds[i].getState()
							+ "','" + strOpenDay + "','" + uid + "');";

				} else if ((strOpenDay == null) && (strRegDay == null)) {
					query = "insert into tree(patNum, invName,pdfNum, summary, repClaim, regNum, regDay, detail,state,openDate,serial)"
							+ " values('" + ds[i].getPatnumber() + "','" + ds[i].getInvname() + "','"
							+ ds[i].getPdfNum() + "','" + ds[i].getSummary() + "','" + ds[i].getRepclaim() + "','"
							+ ds[i].getRegnum() + "'," + strRegDay + ",'" + ds[i].getDetail() + "','" + ds[i].getState()
							+ "'," + strOpenDay + ",'" + uid + "');";

				} else {
					query = "insert into tree(patNum, invName,pdfNum, summary, repClaim, regNum, regDay, detail,state,openDate,serial)"
							+ " values('" + ds[i].getPatnumber() + "','" + ds[i].getInvname() + "','"
							+ ds[i].getPdfNum() + "','" + ds[i].getSummary() + "','" + ds[i].getRepclaim() + "','"
							+ ds[i].getRegnum() + "','" + strRegDay + "','" + ds[i].getDetail() + "','"
							+ ds[i].getState() + "','" + strOpenDay + "','" + uid + "');";

				}

				stmt.executeUpdate(query);
			}

			for (int j = 0; j < w; j++) {

				query = "insert into techExp(sCls,mCls,bCls,exp,serial)" + " values('" + tw[j].getsClass() + "','"
						+ tw[j].getsClass().substring(0, 2) + "','" + tw[j].getsClass().substring(0, 1) + "','"
						+ tw[j].getTechExp() + "','" + uid + "')";
				stmt.executeUpdate(query);
			}

			for (int cc = 0; cc < cB; cc++) {
				query = "insert into BClsExp(bCls,exp,serial)" + " values('" + bce[cc].getbCls() + "','"
						+ bce[cc].getbClsExp() + "','" + uid + "')";
				stmt.executeUpdate(query);
			}

			for (int cc = 0; cc < cM; cc++) {
				query = "insert into MClsExp(mCls,exp,serial)" + " values('" + mce[cc].getmCls() + "','"
						+ mce[cc].getmClsExp() + "','" + uid + "')";
				stmt.executeUpdate(query);
			}

			query = "insert into fileName(fName,serial)" + " values('" + fileName + "','" + uid + "')";
			stmt.executeUpdate(query);
			System.out.println(fileName);

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

		response.getWriter().println("<script>alert('엑셀 파일 성공적으로 업로드');location.href='./upload.jsp';</script>");

	}
	

}
