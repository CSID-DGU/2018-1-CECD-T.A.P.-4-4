<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="example.DataSet" %>
<%@ page import="example.CompanyDataSet" %>
<%@ page import="com.oreilly.servlet.MultipartRequest,
				com.oreilly.servlet.multipart.DefaultFileRenamePolicy,
				javax.json.*,
				javax.servlet.*,
				java.io.*,
				java.util.*,
				java.text.*,
				org.apache.poi.ss.usermodel.*,
				org.apache.poi.hssf.usermodel.*,
				org.apache.poi.openxml4j.opc.OPCPackage,
				org.apache.poi.xssf.usermodel.*,
				java.sql.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Upload Parsing</title>
<link rel ="stylesheet" type="text/css" href="./style/style.css">
<link rel ="stylesheet" type="text/css" href="./style/bootstrap.css?ver=1">
<script src = "./js/implements/createDIVDynamically.js" charset="UTF-8"></script>
<script src = "./js/implements/createSVGDynamically.js" charset="UTF-8"></script>

<script src = "./js/plugins/D3jsVersion4/d3.v4.min.js" charset="utf-8"></script>
<script src = "./js/plugins/jQuery3.1.0/jquery.min.js" charset="utf-8"></script>
<script src = "./js/plugins/Donut3D/Donut3D.js"></script>
</head>
<body>
	<%
		int line_num = 0, i = 0;
		int pivotCount = 5;
		int tabCount = 11;
		String []title = new String[14];
		DataSet []ds = new DataSet[100010];								
		CompanyDataSet [][]cds = new CompanyDataSet[1000][5];			
		ArrayList<HashMap<String, Integer>> dsy = new ArrayList<HashMap<String, Integer>>();
		Map<String, Integer> nds = new HashMap<String, Integer>();
		ArrayList<HashMap<String, Integer>> scy = new ArrayList<HashMap<String, Integer>>();		
		
		String imgSrc = "./img/dooho_main_logo.jpg";
		String imgLeft = "./img/dooho_main_left.jpg";
		String imgRight = "./img/dooho_main_right.jpg";
		String indexLink = "./index.jsp";
		String filePath = (String)request.getAttribute("filePath");
		String ext = (String)request.getAttribute("ext");
		String fileName = filePath.substring(filePath.lastIndexOf('\\') + 1, filePath.lastIndexOf('.') - 1);
		String savePath = request.getServletContext().getRealPath("/");
		String sqlURL = "jdbc:mysql://localhost:3306/patdata?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false";
		BufferedWriter fw = null;

		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write("<a href=\""+indexLink+"\">");
		response.getWriter().write("<img src=\""+imgSrc+"\">");
		response.getWriter().write("</a>");
		//response.getWriter().write("<img src=\""+imgLeft+"\">");
		//response.getWriter().write("<img src=\""+imgRight+"\">");
		response.getWriter().write("<br><br>");
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs;
		
		if(ext.equals("xls") || ext.equals("XLS")) {
			try {
				FileInputStream file2;
				file2 = new FileInputStream(filePath);

				HSSFWorkbook workbook = new HSSFWorkbook(file2);

				HSSFSheet sheet = workbook.getSheetAt(0);

				Iterator<Row> rowIterator = sheet.iterator();
				fw = new BufferedWriter(new FileWriter(savePath + "outputTest.txt"));
				while (rowIterator.hasNext()){
					Row row = rowIterator.next();
					Iterator<Cell> cellIterator = row.cellIterator();
					ds[i] = new DataSet();
					while (cellIterator.hasNext()){
						Cell cell = cellIterator.next();
						int k = cell.getColumnIndex();
						switch(k) {
						case 0:				
							boolean isAlreadyStored = false;
							if (i == 0) {
								title[2] = cell.getStringCellValue();
								break;
							}
							ds[i].setStrNationCode(cell.getStringCellValue().trim());
							if(nds.containsKey(ds[i].getStrNationCode())){
								nds.replace(ds[i].getStrNationCode(), nds.get(ds[i].getStrNationCode())+1);
							}
							else{
								nds.put(ds[i].getStrNationCode(), 1);
							}
							break;
						case 2:				
							if( i == 0 ) {
								title[7] = cell.getStringCellValue();
							}
							else {
								String temp = cell.getStringCellValue().trim();
								String res = temp.replaceAll("\'", "");
								ds[i].setInvname(res);
							}
							break;
						case 3:				
							if( i == 0 ) {
								title[8] = cell.getStringCellValue();
							}
							else {
								String temp = cell.getStringCellValue().trim();
								String res = temp.replaceAll("\'", "");
								ds[i].setSummary(res);
							}
							break;
						case 4:				
							if( i == 0 ) {
								title[9] = cell.getStringCellValue();
							}
							else {
								String temp = cell.getStringCellValue().trim();
								String res = temp.replaceAll("\'", "");
								ds[i].setRepclaim(res);
							}
							break;
						case 7:				
							if( i == 0 ) {
								title[10] = cell.getStringCellValue();
							}
							else {
								ds[i].setPatnumber(cell.getStringCellValue().trim());
							}
							break;
						case 8:				
							if(i == 0) {
								title[3] = cell.getStringCellValue();
							}
							else {
								ds[i].setDateAppDate(cell.getStringCellValue().trim());
							}
							break;
						case 9:				
							if( i == 0 ) {
								title[11] = cell.getStringCellValue();
							}
							else {
								ds[i].setRegnum(cell.getStringCellValue().trim());
							}
							break;
						case 10:			
							if( i == 0 ) {
								title[12] = cell.getStringCellValue();
							}
							else {
								ds[i].setRegday(cell.getStringCellValue().trim());
							}
							break;
						case 12:			
							if (i > 0) {
								String temp = cell.getStringCellValue().trim();
								String res = temp.replaceAll("\'", "");
								ds[i].setStrAppName(res);
							}
							else title[6] = cell.getStringCellValue();
							break;
						case 14:			
							if (i > 0) ds[i].setStrAppNation(cell.getStringCellValue().trim());
							else title[4] = cell.getStringCellValue();
							break;
						case 15:			
							if (i > 0) ds[i].setStrMidClassification(cell.getStringCellValue());
							else title[0] = cell.getStringCellValue();
							break;
						case 16:			
							if (i > 0) ds[i].setStrSmallClassification(cell.getStringCellValue());
							else title[1] = cell.getStringCellValue();
							break;
						case 24:			
							if (i > 0) ds[i].setMemberNumber(cell.getNumericCellValue());
							break;
						case 56:			
							if (i > 0) ds[i].setNumFamilyLiterature(cell.getNumericCellValue());
							else title[5] = cell.getStringCellValue();
							break;
						case 78:			
							if ( i == 0 ) {
								title[13] = cell.getStringCellValue();
							}
							else {
								ds[i].setDetail(cell.getStringCellValue().trim());
							}
							break;
						default:
							break;
						}
					}
					if(i == 0) 	{
						for(int k=0;k<14;k++) {
							fw.write(title[k]);
							fw.write("\t\t");
						}
						fw.newLine();
						fw.flush();
					}
					else {
						example.DataSet.PrintData(ds[i], i, fw);
					}
					i++;
				}
				file2.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				fw.close();
			}
			line_num = i;
		}
		else if(ext.equals("xlsx") || ext.equals("XLSX")) {
			try {
				OPCPackage opcPackage = OPCPackage.open(filePath);
				XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);
				opcPackage.close();

				XSSFSheet sheet = workbook.getSheetAt(0);

				Iterator<Row> rowIterator = sheet.iterator();
				while (rowIterator.hasNext()){
					Row row = rowIterator.next();
					Iterator<Cell> cellIterator = row.cellIterator();
					fw = new BufferedWriter(new FileWriter(savePath + "outputTest2.txt"));
					ds[i] = new DataSet();
					while (cellIterator.hasNext()){
						Cell cell = cellIterator.next();
						int k = cell.getColumnIndex();
						switch(k) {
						case 0:				
							boolean isAlreadyStored = false;
							if (i == 0) {
								title[2] = cell.getStringCellValue();
								break;
							}
							ds[i].setStrNationCode(cell.getStringCellValue().trim());
							if(nds.containsKey(ds[i].getStrNationCode())){
								nds.replace(ds[i].getStrNationCode(), nds.get(ds[i].getStrNationCode())+1);
							}
							else{
								nds.put(ds[i].getStrNationCode(), 1);
							}
							break;
						case 2:				
							if( i > 0 ) {
								String temp = cell.getStringCellValue().trim();
								String res = temp.replaceAll("\'", "");
								ds[i].setInvname(res);
							}
							else title[7] = cell.getStringCellValue();
							break;
						case 3:				
							if( i > 0 ) {
								String temp = cell.getStringCellValue().trim();
								String res = temp.replaceAll("\'", "");
								ds[i].setSummary(res);
							}
							else title[8] = cell.getStringCellValue();
							break;
						case 4:				
							if( i > 0 ) {
								String temp = cell.getStringCellValue().trim();
								String res = temp.replaceAll("\'", "");
								ds[i].setRepclaim(res);
							}
							else title[9] = cell.getStringCellValue();
							break;
						case 7:				
							if( i > 0 ) ds[i].setPatnumber(cell.getStringCellValue().trim());
							else title[10] = cell.getStringCellValue();
							break;
						case 8:				
							if(i == 0) {
								title[3] = cell.getStringCellValue();
							}
							else {
								ds[i].setDateAppDate(cell.getStringCellValue().trim());
							}
							break;
						case 9:				
							if( i > 0 ) ds[i].setRegnum(cell.getStringCellValue().trim());
							else title[11] = cell.getStringCellValue();
							break;
						case 10:			
							if( i > 0 ) ds[i].setRegday(cell.getStringCellValue().trim());
							else title[12] = cell.getStringCellValue();
							break;
						case 12:			
							if (i > 0) {
								String temp = cell.getStringCellValue().trim();
								String res = temp.replaceAll("\'", "");
								ds[i].setStrAppName(res);
							}
							else title[6] = cell.getStringCellValue();
							break;
						case 14:			
							if (i > 0) ds[i].setStrAppNation(cell.getStringCellValue().trim());
							else title[4] = cell.getStringCellValue();
							break;
						case 15:			
							if (i > 0) ds[i].setStrMidClassification(cell.getStringCellValue());
							else title[0] = cell.getStringCellValue();
							break;
						case 16:			
							if (i > 0) ds[i].setStrSmallClassification(cell.getStringCellValue());
							else title[1] = cell.getStringCellValue();
							break;
						case 24:			
							if (i > 0) ds[i].setMemberNumber(cell.getNumericCellValue());
							break;
						case 56:			
							if (i > 0) ds[i].setNumFamilyLiterature(cell.getNumericCellValue());
							else title[5] = cell.getStringCellValue();
							break;
						case 78:			
							if ( i > 0 ) ds[i].setDetail(cell.getStringCellValue().trim());
							else title[13] = cell.getStringCellValue();
							break;
						default:
							break;
						}
					}
					if(i == 0) 	{
						for(int k=0;k<14;k++) {
							fw.write(title[k]);
							fw.write("\t\t");
						}
						fw.newLine();
						fw.flush();
					}
					else {
						example.DataSet.PrintData(ds[i], i, fw);
					}
					i++;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				fw.close();
			}
			line_num = i;
		}
		
		//sql update
		try{
			String id = "root";
			String pw = "1234";
			String query = "show databases;";
			boolean isCreate = false;
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(sqlURL, id, pw);
			
			//Check DB create or not
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			if(stmt.execute(query)){
				rs = stmt.getResultSet();
			}
			while(rs.next()){
				if(rs.getString("Database").equals("patdata")){
					isCreate = true;
					break;
				}
			}
			if(!isCreate) {
				query = "Create database patdata";
				stmt.executeUpdate(query);
			}
			stmt.executeQuery("use patdata;");
			
			//Check table create or not
			int tabIsCreate = 0;
			query = "show tables from patdata;";
			stmt.executeQuery(query);
			if(stmt.execute(query)){
				rs = stmt.getResultSet();
			}
			while(rs.next()){
				if(rs.getString(1).equals("visualization")){
					tabIsCreate += 1;
				}
				if(rs.getString(1).equals("tree")){
					tabIsCreate += 2;
				}
			}
			if(tabIsCreate < 3){
				if(tabIsCreate == 0){
					query = "CREATE TABLE visualization(sClass VARCHAR(3), "+
							" mClass VARCHAR(4), " +
							" natCode VARCHAR(3), " +
							" appDate VARCHAR(11), " +
							" appNat VARCHAR(3), " +
							" numFamily Integer, " +
							" appName VARCHAR(200) character set utf8, " + 
							" patNum VARCHAR(20));";
					stmt.executeUpdate(query);
					query = "CREATE TABLE tree(patNum VARCHAR(20), "+
							" invName VARCHAR(500) character set utf8, " +
							" summary VARCHAR(10000) character set utf8, " +
							" repClaim VARCHAR(10000) character set utf8, " +
							" regNum VARCHAR(12), " +
							" regDay VARCHAR(11), " +
							" detail VARCHAR(255));";
					stmt.executeUpdate(query);
				}
				if(tabIsCreate == 1){
					query = "CREATE TABLE tree(patNum VARCHAR(20), "+
							" invName VARCHAR(500) character set utf8, " +
							" summary VARCHAR(10000) character set utf8, " +
							" repClaim VARCHAR(10000) character set utf8, " +
							" regNum VARCHAR(12), " +
							" regDay VARCHAR(11), " +
							" detail VARCHAR(255));";
					stmt.executeUpdate(query);		
				}
				if(tabIsCreate == 2){
					query = "CREATE TABLE visualization(sClass VARCHAR(3), "+
							" mClass VARCHAR(4), " +
							" natCode VARCHAR(3), " +
							" appDate VARCHAR(11), " +
							" appNat VARCHAR(3), " +
							" numFamily Integer, " +
							" appName VARCHAR(200) character set utf8, " + 
							" patNum VARCHAR(20));";
					stmt.executeUpdate(query);
				}
			}
			
			for(i=1;i<line_num;i++){
				query = "insert into visualization(sClass, mClass, natCode, appDate, appNat, numFamily, appName, patNum)" +  
						" values('" + ds[i].getStrSmallClassification() + "','" + ds[i].getStrMidClassification()+"','" + ds[i].getStrNationCode() + "','" + ds[i].getDateAppDate() + "','" + ds[i].getStrAppNation() + "','" + ds[i].getNumFamilyLiterature().intValue() + "','" + ds[i].getStrAppName() + "','" + ds[i].getPatnumber() + "');";
				stmt.executeUpdate(query);
			}
			
			for(i=1;i<line_num;i++){
				query = "insert into tree(patNum, invName, summary, repClaim, regNum, regDay, detail)" +  
						" values('" + ds[i].getPatnumber() + "','" + ds[i].getInvname()+"','" + ds[i].getSummary() + "','" + ds[i].getRepclaim() + "','" + ds[i].getRegnum() + "','" + ds[i].getRegday() + "','" + ds[i].getDetail() + "');";
				stmt.executeUpdate(query);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally{
			if(stmt != null) {
				try{
					stmt.close();
				} catch(SQLException e){
					e.printStackTrace();
				}
			}
			if(conn != null) {
				try{
					conn.close();
				} catch(SQLException e){
					e.printStackTrace();
				}
			}
		}
		
		Map<String, Integer> m = new HashMap<String, Integer>();
		JsonBuilderFactory fac = Json.createBuilderFactory(m);
		ArrayList<JsonArrayBuilder> JsonarrList = new ArrayList<JsonArrayBuilder>();									
		ArrayList<ArrayList<String>> JsonList = new ArrayList<ArrayList<String>>();										
		ArrayList<ArrayList<String>> SvgList = new ArrayList<ArrayList<String>>();										
																														
																														
		
		
		for(i=0;i<10;i++){
			JsonList.add(new ArrayList<String>());
			SvgList.add(new ArrayList<String>());
		}
		
		int svgCnt = 0, jsonCnt = 0;
		int JsonarrListSize = 0;
		
		int maxYear=-99999, minYear=99999, temp;
		for(i=1;i<line_num;i++) {
			temp = Integer.parseInt(ds[i].getDateAppDate().substring(0,4));
			if(temp > maxYear) {
				maxYear = temp;
			}
			if(temp < minYear) {
				minYear = temp;
			}
		}
		int yearPivot = (maxYear-minYear)/5;
		ArrayList<Integer> listYear = new ArrayList<Integer>();
		ArrayList<String> listClassification = new ArrayList<String>();
		ArrayList<String> listNationCode = new ArrayList<String>();
		ArrayList<String> listCompany = new ArrayList<String>();		
		for(i=minYear;i<=maxYear;i++){
			listYear.add(i-minYear, i);
		}
		for(i=1;i<line_num;i++){
			if(!listClassification.contains(ds[i].getStrSmallClassification())){
				listClassification.add(ds[i].getStrSmallClassification());
			}
			if(!listNationCode.contains(ds[i].getStrNationCode())){
				listNationCode.add(ds[i].getStrNationCode());
			}
			if(!listCompany.contains(ds[i].getStrAppName())){
				listCompany.add(ds[i].getStrAppName());
			}
		}
		Collections.sort(listNationCode, new Comparator<String>(){
			public int compare(String obj1, String obj2){
				return obj1.compareToIgnoreCase(obj2);
			}
		});
		Collections.sort(listCompany, new Comparator<String>(){
			public int compare(String obj1, String obj2){
				return obj1.compareToIgnoreCase(obj2);
			}
		});
		Collections.sort(listClassification, new Comparator<String>(){
			public int compare(String obj1, String obj2){
				return obj1.compareToIgnoreCase(obj2);
			}
		});
		
		//컬러 세팅 부분
		{
			JsonArrayBuilder colorJAM = Json.createArrayBuilder();
			for(i=0;i<6;i++){
				JsonObjectBuilder job = fac.createObjectBuilder();
				switch(i){
				case 0:		//기계
					job.add("classification", "AAC");
					job.add("color", "green");
					job.add("100", "#97A719");
					job.add("70", "#b6c15e");
					job.add("50", "#cbd38c");
					job.add("30", "#e0e4ba");
					job.add("20", "#eaedd1");
					break;
				case 1:		//화학
					job.add("classification", "ABA");
					job.add("color", "orange");
					job.add("100", "#eb690b");
					job.add("70", "#f19654");
					job.add("50", "#f5b485");
					job.add("30", "#f9d2b5");
					job.add("20", "#fbe1ce");
					break;
				case 2:		//전기전자
					job.add("classification", "AAA");
					job.add("color", "blue green");
					job.add("100", "#376a86");
					job.add("70", "#7396aa");
					job.add("50", "#9bb4c2");
					job.add("30", "#c3d2da");
					job.add("20", "#d7e1e7");
					break;
				case 3:		//정보통신
					job.add("classification", "AAB");
					job.add("color", "purple");
					job.add("100", "#7e5c7f");
					job.add("70", "#a48da5");
					job.add("50", "#beadbf");
					job.add("30", "#d8ced8");
					job.add("20", "#e5dee5");
					break;
				case 4:		//기타
					job.add("classification", "etc");
					job.add("color", "gold");
					job.add("100", "#c1ac74");
					job.add("70", "#d3c59d");
					job.add("50", "#e0d5b9");
					job.add("30", "#ece6d5");
					job.add("20", "#f3eee3");
					break;
				case 5:
					job.add("classification", "blank");
					job.add("color", "black");
					job.add("100", "#595959");
					job.add("80", "#7f7f7f");
					job.add("50", "#bfbfbf");
					job.add("30", "#d9d9d9");
					job.add("10", "#eeeeee");
					break;
				default:
					break;
				}
				colorJAM.add(job);
			}
			
			try{
				BufferedWriter jsonBW = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(request.getServletContext().getRealPath("/") + "ColorSettings.json"), "UTF-8"));
				jsonBW.write(colorJAM.build().toString());
				jsonBW.flush();
				jsonBW.close();
			}
			catch(IOException ie){
				ie.printStackTrace();
			}
		}
		
		
		Map<String, Integer> patYear = new HashMap<String, Integer>();
		ArrayList<HashMap<String, Integer>> nCodeNation = new ArrayList<HashMap<String, Integer>>();
		
		for(i=0;i<listNationCode.size();i++){
			nCodeNation.add(new HashMap<String, Integer>());
			for(int j=minYear;j<=maxYear;j++){
				nCodeNation.get(i).put(String.valueOf(j), 0);			
			}
		}
		for(i=minYear;i<=maxYear;i++){
			patYear.put(String.valueOf(i), 0);
		}
		for(i=1;i<line_num;i++){
			String tmp = ds[i].getDateAppDate().substring(0,4);		
			String tmp2 = ds[i].getStrNationCode();					
			patYear.replace(tmp, patYear.get(tmp) + 1);				
			nCodeNation.get(listNationCode.indexOf(tmp2)).replace(tmp, nCodeNation.get(listNationCode.indexOf(tmp2)).get(tmp) + 1);
		}
		
		TreeMap<String, Integer> tmPat = new TreeMap<String, Integer>(patYear);
		
		ArrayList<TreeMap<String, Integer>> tmnCodeNation = new ArrayList<TreeMap<String, Integer>>();
		for(i=0;i<listNationCode.size();i++){
			tmnCodeNation.add(i, new TreeMap<String, Integer>(nCodeNation.get(i)));
		}
		
		
		{
			SvgList.get(0).add("svg0");
			svgCnt++;
			JsonList.get(0).add("result1");
			jsonCnt++;
			JsonarrList.add(0, fac.createArrayBuilder());
			Iterator<String> itKey = tmPat.keySet().iterator();
			while(itKey.hasNext()) {
				String key = itKey.next();
				
				JsonarrList.get(JsonarrListSize).add(
						Json.createObjectBuilder()
						.add("year", key)
						.add("count", tmPat.get(key)));
			}
		}
		
		
		{
			SvgList.get(0).add("svg1");
			svgCnt++;
			JsonList.get(0).add("result0");
			jsonCnt++;
			JsonarrListSize++;
			JsonarrList.add(1, fac.createArrayBuilder());
			Iterator<String> itKey = tmPat.keySet().iterator();
			String Prev = null;
			while(itKey.hasNext()) {
				int val;
				
				if(Prev == null){
					Prev = itKey.next();
				}
				else{
					String key = itKey.next();
					if(tmPat.get(Prev) == 0 && tmPat.get(key) > 0) val = (int)((double)tmPat.get(key) * 100);
					else val = (int)(((double)tmPat.get(key) / (double)tmPat.get(Prev)) * 100);
					JsonarrList.get(JsonarrListSize).add(
							Json.createObjectBuilder()
							.add("year", String.valueOf(Integer.parseInt(key) + 1))
							.add("percentage", val));
					Prev = key;
				}
			}
			JsonarrList.get(0).add(Json.createObjectBuilder().add("classification", listClassification.get(listClassification.size()-1)));
			
			SvgList.get(0).add("svg2");
			svgCnt++;
			JsonList.get(0).add("result0");
		}
		
		
		for(i=0;i<listNationCode.size();i++){
			String key;
			int val;
			
			SvgList.get(0).add("svg"+String.valueOf(svgCnt));
			svgCnt++;
			JsonList.get(0).add("result"+String.valueOf(jsonCnt));
			jsonCnt++;
			JsonarrListSize++;
			JsonarrList.add(JsonarrListSize, fac.createArrayBuilder());
			
			for(Map.Entry<String, Integer> entry: tmnCodeNation.get(i).entrySet()){			
				key = entry.getKey();
				val = entry.getValue();
				JsonarrList.get(JsonarrListSize).add(
						Json.createObjectBuilder()
							.add("year", key)
							.add("count", val));
			}
			JsonarrList.get(JsonarrListSize).add(Json.createObjectBuilder().add("classification", listClassification.get(i)));
			
			SvgList.get(0).add("svg"+String.valueOf(svgCnt));
			svgCnt++;
			JsonList.get(0).add("result"+String.valueOf(jsonCnt));
			jsonCnt++;
			JsonarrListSize++;
			JsonarrList.add(JsonarrListSize, fac.createArrayBuilder());
			
			
			Iterator<String> itKey = tmnCodeNation.get(i).keySet().iterator();
			String Prev = itKey.next();
			while(itKey.hasNext()){
				key = itKey.next();
				if(tmnCodeNation.get(i).get(Prev) == 0 && tmnCodeNation.get(i).get(key) > 0) val = (int)((double)tmnCodeNation.get(i).get(key) * 100);
				else val = (int)(((double)tmnCodeNation.get(i).get(key) / (double)tmnCodeNation.get(i).get(Prev))*100);
				JsonarrList.get(JsonarrListSize).add(
						Json.createObjectBuilder()
							.add("year", String.valueOf(Integer.parseInt(key)+1))			
							.add("percentage", val));
				Prev = key;
			}
		}
		
		
		{
			TreeMap<String, Integer> TreeNDS = new TreeMap<String, Integer>(nds);
			ArrayList<HashMap<String, Integer>>PatentNationIO = new ArrayList<HashMap<String, Integer>>();	
			ArrayList<HashMap<String, Integer>>NationsSummary = new ArrayList<HashMap<String, Integer>>();
			
			
			SvgList.get(1).add("svg"+String.valueOf(svgCnt));
			svgCnt++;
			JsonList.get(1).add("result"+String.valueOf(jsonCnt));
			jsonCnt++;
			JsonarrListSize++;
			JsonarrList.add(JsonarrListSize, fac.createArrayBuilder());
			
			
			i = 0;
			for(Map.Entry<String, Integer> entry: TreeNDS.entrySet()){
				String entryKey = entry.getKey();
				int entryVal = entry.getValue();
				JsonarrList.get(JsonarrListSize).add(
						Json.createObjectBuilder()
						.add("label", entryKey)
						.add("classification", listClassification.get(i))
						.add("value", entryVal));
				i++;
			}
			
			for(i=0;i<listNationCode.size();i++){
				PatentNationIO.add(new HashMap<String, Integer>());			
				NationsSummary.add(new HashMap<String, Integer>());
			}
			
			for(i=1;i<line_num;i++){
				String NationCode = ds[i].getStrNationCode();				
				String AppNation = ds[i].getStrAppNation();					
				int idx = listNationCode.indexOf(NationCode);				
				if(NationCode.equals(AppNation)){
					if(PatentNationIO.get(idx).containsKey(NationCode+"Native"))
						PatentNationIO.get(idx).replace(NationCode+"Native", PatentNationIO.get(idx).get(NationCode+"Native")+1); 
					else
						PatentNationIO.get(idx).put(NationCode+"Native", 1);
				}
				else{
					if(PatentNationIO.get(idx).containsKey(NationCode+"Foreign"+AppNation))
						PatentNationIO.get(idx).replace(NationCode+"Foreign"+AppNation, PatentNationIO.get(idx).get(NationCode+"Foreign"+AppNation)+1); 
					else
						PatentNationIO.get(idx).put(NationCode+"Foreign"+AppNation, 1);
				}
			}
			
			
			for(i=0;i<listNationCode.size();i++){
				int natives = 0, foriegn = 0;
				for(Map.Entry<String, Integer> e : PatentNationIO.get(i).entrySet()){
					String key = e.getKey();
					if(key.contains("Native")){
						natives = e.getValue();
					}
					else{
						foriegn += e.getValue();
					}
				}
				NationsSummary.get(i).put(listNationCode.get(i)+"Native", natives);
				NationsSummary.get(i).put(listNationCode.get(i)+"Foreign", foriegn);
			}
			
			
			JsonList.get(1).add("result"+String.valueOf(jsonCnt));
			jsonCnt++;
			JsonarrListSize++;
			JsonarrList.add(JsonarrListSize, fac.createArrayBuilder());
			
			JsonObjectBuilder job = fac.createObjectBuilder();
			job.add("CntNation", listNationCode.size());
			JsonarrList.get(JsonarrListSize).add(job);
			
			job = fac.createObjectBuilder();
			for(i=0;i<listNationCode.size();i++){
				job.add(listNationCode.get(i), listNationCode.get(i));
			}
			JsonarrList.get(JsonarrListSize).add(job);
			
			
			for(i=0;i<listNationCode.size();i++){
				job = fac.createObjectBuilder();
				String NationsOfNative = listNationCode.get(i)+"Native";
				String NationsOfForeign = listNationCode.get(i)+"Foreign";
				job.add(NationsOfNative, NationsSummary.get(i).get(NationsOfNative));
				job.add(NationsOfForeign, NationsSummary.get(i).get(NationsOfForeign));
				JsonarrList.get(JsonarrListSize).add(job);
			}
			
			job = fac.createObjectBuilder();
			for(i=0;i<listNationCode.size();i++){
				job.add(listNationCode.get(i), listClassification.get(i));
			}
			JsonarrList.get(JsonarrListSize).add(job);
			
			
			for(i=0;i<listNationCode.size();i++){
				job = fac.createObjectBuilder();
				for(Map.Entry<String, Integer> e : PatentNationIO.get(i).entrySet()){
					String key = e.getKey();
					if(key.contains("Native")) continue;
					int val = e.getValue();
					job.add(key, val);
				}
				JsonarrList.get(JsonarrListSize).add(job);
			}			
		}
		
		{
			ArrayList<HashMap<String, Integer>> hmNation = new ArrayList<HashMap<String, Integer>>();
			
			for(i=0;i<listNationCode.size();i++){
				hmNation.add(new HashMap<String, Integer>());
				for(int j=0;j<listYear.size();j++){
					hmNation.get(i).put("I"+String.valueOf(listYear.get(j)), 0);
					hmNation.get(i).put("O"+String.valueOf(listYear.get(j)), 0);
				}
			}
			
			for(i=1;i<line_num;i++){
				String dist = ds[i].getStrNationCode();
				String appNation = ds[i].getStrAppNation();
				String tmp = ds[i].getDateAppDate().substring(0,4);
				if(dist.equals(appNation))
					hmNation.get(listNationCode.indexOf(dist)).replace("I"+tmp, hmNation.get(listNationCode.indexOf(dist)).get("I"+tmp)+1);
				else
					hmNation.get(listNationCode.indexOf(dist)).replace("O"+tmp, hmNation.get(listNationCode.indexOf(dist)).get("O"+tmp)+1);
			}
						
			DateFormat df = new SimpleDateFormat("yyyy");
			java.util.Date d = new java.util.Date();
			int todayYear = Integer.parseInt(df.format(d));
						
			ArrayList<TreeMap<String, Integer>> tmNYCodeNations = new ArrayList<TreeMap<String, Integer>>();			
			for(i=0;i<listNationCode.size();i++){
				tmNYCodeNations.add(i, new TreeMap<String, Integer>(hmNation.get(i)));
			}
			
			Iterator<String> itKey;
			for(i=0;i<listNationCode.size();i++){
				itKey = tmNYCodeNations.get(i).keySet().iterator();
				SvgList.get(2).add("svg"+String.valueOf(svgCnt));
				svgCnt++;
				JsonList.get(2).add("result"+String.valueOf(jsonCnt));
				jsonCnt++;
				JsonarrListSize++;
				JsonarrList.add(JsonarrListSize, fac.createArrayBuilder());
				while(itKey.hasNext()){
					String key = itKey.next();
					int val = tmNYCodeNations.get(i).get(key);
					JsonarrList.get(JsonarrListSize).add(
							Json.createObjectBuilder()
							.add("year", key)
							.add("value", val)
					);
				}
				JsonarrList.get(JsonarrListSize).add(Json.createObjectBuilder().add("classification", listClassification.get(i)));
			}
		}
		
		
		{
			
			ArrayList<HashMap<String, Integer>> aryhmPatentCntByNationPerYear = new ArrayList<HashMap<String, Integer>>();
		
			ArrayList<HashMap<String, Integer>> aryhmPatentAppCntByNationPerYear = new ArrayList<HashMap<String, Integer>>();
			
			for(i=0;i<listNationCode.size();i++){
				aryhmPatentCntByNationPerYear.add(new HashMap<String, Integer>());
				aryhmPatentAppCntByNationPerYear.add(new HashMap<String, Integer>());
			}
			
			for(i=1;i<line_num;i++){
				
				int nationIdx = listNationCode.indexOf(ds[i].getStrNationCode());
				
				
				int yearIdx = (Integer.parseInt(ds[i].getDateAppDate().substring(0,4)) - minYear) / yearPivot;
				
				Double dblPatentMemberNumber = ds[i].getMemberNumber();
				
				int patentMemberNumber = dblPatentMemberNumber.intValue();
				
				
				if(aryhmPatentCntByNationPerYear.get(nationIdx).containsKey("year"+String.valueOf(yearIdx))){
					aryhmPatentCntByNationPerYear.get(nationIdx)
						.replace("year"+String.valueOf(yearIdx), aryhmPatentCntByNationPerYear.get(nationIdx).get("year"+String.valueOf(yearIdx))+1);
				}
				else{
					aryhmPatentCntByNationPerYear.get(nationIdx).put("year"+String.valueOf(yearIdx), 1);
				}
				
				
				if(aryhmPatentAppCntByNationPerYear.get(nationIdx).containsKey("year"+String.valueOf(yearIdx))){
					aryhmPatentAppCntByNationPerYear.get(nationIdx)
						.replace("year"+String.valueOf(yearIdx), aryhmPatentAppCntByNationPerYear.get(nationIdx).get("year"+String.valueOf(yearIdx))+patentMemberNumber);
				}
				else{
					aryhmPatentAppCntByNationPerYear.get(nationIdx).put("year"+String.valueOf(yearIdx), patentMemberNumber);
				}
			}
			
			
			JsonObjectBuilder job;
			
			for(i=0;i<listNationCode.size();i++){
				
				SvgList.get(3).add("svg"+String.valueOf(svgCnt));
				svgCnt++;
				JsonList.get(3).add("result"+String.valueOf(jsonCnt));
				jsonCnt++;
				JsonarrListSize++;
				JsonarrList.add(JsonarrListSize, fac.createArrayBuilder());
				for(int j=1;j<=5;j++){
					job = fac.createObjectBuilder();
					int x = aryhmPatentCntByNationPerYear.get(i).get("year"+String.valueOf(j));
					int y = aryhmPatentAppCntByNationPerYear.get(i).get("year"+String.valueOf(j));
					job.add("Year", j);
					job.add("x", x);
					job.add("y", y);
					JsonarrList.get(JsonarrListSize).add(job);
				}
				job = fac.createObjectBuilder();
				job.add("classification", listClassification.get(i));
				JsonarrList.get(JsonarrListSize).add(job);
			}
		}
		
		
		{
			Map<String, String> cnList = new HashMap<String, String>();
			List<String> clist = new ArrayList<String>();
			
			for(i=1;i<line_num;i++) {
				String compName = ds[i].getStrAppName();
				String nationCode = ds[i].getStrAppNation();
				if(!cnList.containsKey(compName)){
					cnList.put(compName, nationCode);
					clist.add(compName);
				}
			}
			
			i = 0;
			ArrayList<HashMap<String, Integer>> patTypeByCompany = new ArrayList<HashMap<String, Integer>>();
			for(Map.Entry<String, String> e : cnList.entrySet()){
				for(int j=0;j<listNationCode.size();j++){
					cds[i][j] = new CompanyDataSet();
					cds[i][j].setStrCompName(e.getKey());
					cds[i][j].setStrAppNation(e.getValue());
					cds[i][j].setNumValue(0);
					cds[i][j].setNumOfRecentIncreasePat(0);
				}
				patTypeByCompany.add(new HashMap<String, Integer>());
				i++;
			}
			for(i=1;i<line_num;i++){
				String patNation = ds[i].getStrNationCode();
				String compName = ds[i].getStrAppName();
				int todayYear = Calendar.getInstance().get(Calendar.YEAR);				
				int pYear = Integer.parseInt(ds[i].getDateAppDate().substring(0,4));	
				if(todayYear-5 >= pYear) cds[clist.indexOf(compName)][0].setNumOfRecentIncreasePat(cds[clist.indexOf(compName)][0].getNumOfRecentIncreasePat()+1);
				for(int j=0;j<listNationCode.size();j++){
					if(patTypeByCompany.get(clist.indexOf(compName))
							.containsKey(ds[i].getStrSmallClassification())){
						patTypeByCompany.get(clist.indexOf(compName))
										.replace(ds[i].getStrSmallClassification(), patTypeByCompany.get(clist.indexOf(compName)).get(ds[i].getStrSmallClassification()));
					}
					else {
						patTypeByCompany.get(clist.indexOf(compName))
										.put(ds[i].getStrSmallClassification(), 1);
					}
					cds[clist.indexOf(compName)][listNationCode.indexOf(patNation)].setNumValue(cds[clist.indexOf(compName)][listNationCode.indexOf(patNation)].getNumValue()+1);
				}
			}
			for(i=0;i<clist.size();i++){
				int maxValueOfNation = 0;
				int sumOfPatValue = 0;
				for(int j=0;j<listNationCode.size();j++){
					if(maxValueOfNation < cds[i][j].getNumValue()){
						cds[i][0].setStrTotalNation(listNationCode.get(j));
						maxValueOfNation = cds[i][j].getNumValue();
					}
					sumOfPatValue += cds[i][j].getNumValue();
				}
				cds[i][0].setNumOfRecentIncreasePat((int)((double)cds[i][0].getNumOfRecentIncreasePat() / (double)sumOfPatValue * 100));
			}
			JsonList.get(4).add("result"+String.valueOf(jsonCnt));
			jsonCnt++;
			JsonarrListSize++;
			JsonarrList.add(JsonarrListSize, fac.createArrayBuilder());
			
			JsonObjectBuilder job = fac.createObjectBuilder();
			job.add("cntNations", listNationCode.size());
			JsonarrList.get(JsonarrListSize).add(job);
			for(i=0;i<listNationCode.size();i++){
				job.add(listNationCode.get(i), listNationCode.get(i));
			}
			JsonarrList.get(JsonarrListSize).add(job);
			for(i=0;i<clist.size();i++){
				job.add("CompanyName", clist.get(i));
				job.add("AppNation", cnList.get(clist.get(i)));
				int totalN = 0;
				for(int j=0;j<listNationCode.size();j++){
					job.add(listNationCode.get(j), cds[i][j].getNumValue());
					totalN += cds[i][j].getNumValue();
				}
				for(int j=0;j<listNationCode.size();j++){
					job.add("per"+listNationCode.get(j) , (int)((double)cds[i][j].getNumValue()/(double)totalN*100));
				}
				job.add("TotalNation", cds[i][0].getStrTotalNation());
				int max = 0;
				String maxString = null;
				for(Map.Entry<String, Integer> e : patTypeByCompany.get(i).entrySet()){
					if(Math.max(max, e.getValue()) == e.getValue()){
						maxString = e.getKey();
						max = e.getValue();
					}
				}
				job.add("midClassification", maxString.substring(0,2));
				job.add("smallClassification", maxString);
				job.add("RecentPatent", cds[i][0].getNumOfRecentIncreasePat());
				JsonarrList.get(JsonarrListSize).add(job);
			}
			

			ArrayList<HashMap<String, Integer>> NationCodeByCompanyList = new ArrayList<HashMap<String, Integer>>();
			ArrayList<HashMap<String, Integer>> SmallClassificationByCompanyList = new ArrayList<HashMap<String, Integer>>();
			for(i=0;i<listCompany.size();i++){
				NationCodeByCompanyList.add(new HashMap<String, Integer>());
				SmallClassificationByCompanyList.add(new HashMap<String, Integer>());
			}
			for(i=0;i<listCompany.size();i++){
				for(int j=0;j<listNationCode.size();j++){
					NationCodeByCompanyList.get(i).put(listNationCode.get(j), 0);
				}
				for(int j=0;j<listClassification.size();j++){
					SmallClassificationByCompanyList.get(i).put(listClassification.get(j), 0);
				}
			}
			for(i=1;i<line_num;i++){
				int idx = listCompany.indexOf(ds[i].getStrAppName());
				String nationCode = ds[i].getStrNationCode();
				String classificaiton = ds[i].getStrSmallClassification();
				

				NationCodeByCompanyList.get(idx).replace(nationCode, NationCodeByCompanyList.get(idx).get(nationCode)+1);
				SmallClassificationByCompanyList.get(idx).replace(classificaiton, SmallClassificationByCompanyList.get(idx).get(classificaiton)+1);
			}
			

			SvgList.get(5).add("svg"+String.valueOf(svgCnt));
			svgCnt++;
			JsonList.get(5).add("result"+String.valueOf(jsonCnt));
			jsonCnt++;
			JsonarrListSize++;
			JsonarrList.add(JsonarrListSize, fac.createArrayBuilder());
			
			job = fac.createObjectBuilder();
			job.add("NationSize", listNationCode.size());
			for(i=0;i<listNationCode.size();i++){
				job.add(listNationCode.get(i), listNationCode.get(i));
			}
			JsonarrList.get(JsonarrListSize).add(job);
			
			job = fac.createObjectBuilder();
			for(i=0;i<listClassification.size();i++){
				job.add(listClassification.get(i), listClassification.get(i));
			}
			JsonarrList.get(JsonarrListSize).add(job);
			
			job = fac.createObjectBuilder();
			job.add("ClassSize", listClassification.size());
			for(i=0;i<listClassification.size();i++){
				job.add(listClassification.get(i), listClassification.get(i));
			}
			JsonarrList.get(JsonarrListSize).add(job);
			
			for(i=0;i<listCompany.size();i++){
				job = fac.createObjectBuilder();
				job.add("CompanyName", listCompany.get(i));
				for(Map.Entry<String, Integer> e : NationCodeByCompanyList.get(i).entrySet()){
					String key = e.getKey();
					int val = e.getValue();
					job.add("Nation"+key, val);
				}
				for(Map.Entry<String, Integer> e : SmallClassificationByCompanyList.get(i).entrySet()){
					String key = e.getKey();
					int val = e.getValue();
					job.add("Class"+key, val);
				}
				JsonarrList.get(JsonarrListSize).add(job);
			}
		}

		Map<String, Integer> smallClassification = new HashMap<String, Integer>();
		

		{
			ArrayList<HashMap<String, Integer>> listClassificationPerYear = new ArrayList<HashMap<String, Integer>>();
			List<String> strListYear = new ArrayList<String>();
			for(i=minYear;i<=maxYear;i++) {
				strListYear.add("I"+String.valueOf(i));
				strListYear.add("O"+String.valueOf(i));
			}
			for(i=0;i<listClassification.size();i++){
				listClassificationPerYear.add(new HashMap<String, Integer>());
			}
			for(i=0;i<listClassification.size();i++){
				for(int j=0;j<strListYear.size();j++){
					listClassificationPerYear.get(i).put(strListYear.get(j), 0);
				}
			}
			
			for(i=1;i<line_num;i++){
				int idx = listClassification.indexOf(ds[i].getStrSmallClassification());
				String year = ds[i].getDateAppDate().substring(0,4);
				String appNation = ds[i].getStrAppNation();
				
				if(appNation.equals("KR")){
					listClassificationPerYear.get(idx).replace("I"+year, listClassificationPerYear.get(idx).get("I"+year)+1);
				}
				else{
					listClassificationPerYear.get(idx).replace("O"+year, listClassificationPerYear.get(idx).get("O"+year)+1);
				}
			}
			ArrayList<TreeMap<String, Integer>> tmListClassificationPerYear = new ArrayList<TreeMap<String, Integer>>();
			for(i=0;i<listClassificationPerYear.size();i++){
				tmListClassificationPerYear.add(i, new TreeMap<String, Integer>(listClassificationPerYear.get(i)));
			}
			
			for(i=0;i<tmListClassificationPerYear.size();i++){
				Iterator<String> it = tmListClassificationPerYear.get(i).keySet().iterator();
				SvgList.get(7).add("svg"+String.valueOf(svgCnt));
				svgCnt++;
				JsonList.get(7).add("result"+String.valueOf(jsonCnt));
				jsonCnt++;
				JsonarrListSize++;
				JsonarrList.add(JsonarrListSize, fac.createArrayBuilder());
				while(it.hasNext()){
					String key = it.next();
					int value = tmListClassificationPerYear.get(i).get(key);
					JsonarrList.get(JsonarrListSize).add(
							Json.createObjectBuilder()
								.add("year", key)
								.add("value", value)
					);
				}
				JsonarrList.get(JsonarrListSize).add(Json.createObjectBuilder().add("classification", listClassification.get(i)));
			}
		}
		

		{
			for(i=0;i<pivotCount;i++){
				dsy.add(new HashMap<String, Integer>());
			}
			
			for (i=1;i<line_num;i++) {
				temp = Integer.parseInt(ds[i].getDateAppDate().substring(0,4));
				boolean isExists = false;
				int dist_dsyi = 0;
				for(int j=0;j<pivotCount;j++){
					if(j != pivotCount - 1){
						if(minYear + j * yearPivot <= temp && temp < minYear + (j+1)*yearPivot){
							dist_dsyi = j;
							break;
						}
					}
					else{
						dist_dsyi = pivotCount-1;
					}
				}
				if(dsy.get(dist_dsyi).containsKey(ds[i].getStrSmallClassification())){
					dsy.get(dist_dsyi).replace(ds[i].getStrSmallClassification(), dsy.get(dist_dsyi).get(ds[i].getStrSmallClassification())+1);
				}
				else{
					dsy.get(dist_dsyi).put(ds[i].getStrSmallClassification(), 1);
				}
			}
			
			for(i=0;i<pivotCount;i++) {
				int j = 0;
				
				SvgList.get(8).add("svg"+String.valueOf(svgCnt));
				svgCnt++;
				JsonList.get(8).add("result"+String.valueOf(jsonCnt));
				jsonCnt++;
				JsonarrListSize++;
				JsonarrList.add(JsonarrListSize, fac.createArrayBuilder());
				for(Map.Entry<String, Integer> e : dsy.get(i).entrySet()){
					String key = e.getKey();
					int val = e.getValue();
					if(smallClassification.containsKey(key))
						smallClassification.replace(key, smallClassification.get(key) + val);
					else
						smallClassification.put(key, val);
					if(j == 0){
						int prevYear = (minYear+yearPivot * i), nextYear = (minYear + (yearPivot * (i+1)));
						if(i == pivotCount-1 && nextYear < maxYear) nextYear = maxYear;
						JsonarrList.get(JsonarrListSize).add(
									Json.createObjectBuilder()
									.add("title", String.valueOf(prevYear) + " ~ " + String.valueOf(nextYear)+"년도 기술구분")
									.add("istitle", 1)
								);
					}
					JsonarrList.get(JsonarrListSize).add(
								Json.createObjectBuilder()
								.add("label", key)
								.add("value", val));
					j++;
				}
			}
		}
		

		{
			SvgList.get(6).add("svg"+String.valueOf(svgCnt));
			svgCnt++;
			JsonList.get(6).add("result"+String.valueOf(jsonCnt));
			jsonCnt++;
			JsonarrListSize++;
			JsonarrList.add(JsonarrListSize, fac.createArrayBuilder());
			for(Map.Entry<String, Integer> e : smallClassification.entrySet()) {
				JsonarrList.get(JsonarrListSize).add(
						Json.createObjectBuilder()
						.add("label", e.getKey())
						.add("value", e.getValue())
						);
			}
			
			for(i=0;i<=maxYear-minYear + 1;i++){
				scy.add(new HashMap<String, Integer>());
				for(int j=0;j<listClassification.size();j++){
					scy.get(i).put(listClassification.get(j), 0);
				}
			}
			
			int classCnt = 0;
			for(i=1;i<line_num;i++){
				int idx = Integer.parseInt(ds[i].getDateAppDate().substring(0,4)) - minYear;
				String classification = ds[i].getStrSmallClassification();
				scy.get(idx).replace(classification, scy.get(idx).get(classification)+1);
			}
			
			SvgList.get(6).add("svg"+String.valueOf(svgCnt));
			svgCnt++;
			JsonList.get(6).add("result"+String.valueOf(jsonCnt));
			jsonCnt++;
			JsonarrListSize++;
			JsonarrList.add(JsonarrListSize, fac.createArrayBuilder());
			for(i=0;i<listClassification.size();i++){
				JsonObjectBuilder job = fac.createObjectBuilder();
				job.add("classification", listClassification.get(i));
				job.add("minYear", minYear);
				job.add("maxYear", maxYear);
				for(int j=0;j<listYear.size();j++){
					job.add(String.valueOf(j + minYear), scy.get(j).get(listClassification.get(i)));
				}
				JsonarrList.get(JsonarrListSize).add(job);
			}
		}
		

		{
			ArrayList<HashMap<String, Integer>> SmallClassificationByNationCode = new ArrayList<HashMap<String, Integer>>();
			for(i=0;i<listClassification.size();i++){
				SmallClassificationByNationCode.add(new HashMap<String, Integer>());
			}
			for(i=0;i<listClassification.size();i++){
				for(int j=0;j<listNationCode.size();j++){
					SmallClassificationByNationCode.get(i).put(listNationCode.get(j), 0);
				}
			}
			
			for(i=1;i<line_num;i++){
				String nationCode = ds[i].getStrNationCode();
				int idx = listClassification.indexOf(ds[i].getStrSmallClassification());
				SmallClassificationByNationCode.get(idx).replace(nationCode, SmallClassificationByNationCode.get(idx).get(nationCode)+1);
			}
			
			SvgList.get(9).add("svg"+String.valueOf(svgCnt));
			svgCnt++;
			JsonList.get(9).add("result"+String.valueOf(jsonCnt));
			jsonCnt++;
			JsonarrListSize++;
			JsonarrList.add(JsonarrListSize, fac.createArrayBuilder());
			
			JsonObjectBuilder job = fac.createObjectBuilder();
			job.add("NationSize", listNationCode.size());
			for(i=0;i<listNationCode.size();i++){
				job.add(listNationCode.get(i), listNationCode.get(i));
			}
			JsonarrList.get(JsonarrListSize).add(job);
			
			job = fac.createObjectBuilder();
			job.add("ClassSize", listClassification.size());
			for(i=0;i<listClassification.size();i++){
				job.add(listClassification.get(i), listClassification.get(i));
			}
			JsonarrList.get(JsonarrListSize).add(job);
			

			for(i=0;i<listClassification.size();i++){
				for(Map.Entry<String, Integer> e : SmallClassificationByNationCode.get(i).entrySet()){
					job = fac.createObjectBuilder();
					String key = e.getKey();
					int val = e.getValue();
					job.add("Classification", listClassification.get(i));
					job.add("NationCode", key);
					job.add("Value", val);
					JsonarrList.get(JsonarrListSize).add(job);
				}
			}
		}
		
		for(i=0;i<JsonarrList.size();i++){
			try{
				BufferedWriter jsonBW = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(request.getServletContext().getRealPath("/") + "result"+String.valueOf(i)+".json"), "UTF-8"));
				jsonBW.write(JsonarrList.get(i).build().toString());
				jsonBW.flush();
				jsonBW.close();
			}
			catch(IOException ie){
				ie.printStackTrace();
			}
		}
		%>
		<div class="tab">
			<button class = "tablinks" onClick="openChart(event, 'Chart1')" id="defaultOpen">Chart1</button>
			<button class = "tablinks" onClick="openChart(event, 'Chart2')">Chart2</button>
			<button class = "tablinks" onClick="openChart(event, 'Chart3')">Chart3</button>
			<button class = "tablinks" onClick="openChart(event, 'Chart4')">Chart4</button>
			<button class = "tablinks" onClick="openChart(event, 'Chart5')">Chart5</button>
			<button class = "tablinks" onClick="openChart(event, 'Chart6')">Chart6</button>
			<button class = "tablinks" onClick="openChart(event, 'Chart7')">Chart7</button>
			<button class = "tablinks" onClick="openChart(event, 'Chart8')">Chart8</button>
			<button class = "tablinks" onClick="openChart(event, 'Chart9')">Chart9</button>
			<button class = "tablinks" onClick="openChart(event, 'Chart10')">Chart10</button>
		</div>
		<div id = "Chart1" class="tabcontent">
		</div>
		<div id = "Chart2" class="tabcontent">
		</div>
		<div id = "Chart3" class="tabcontent">
		</div>
		<div id = "Chart4" class="tabcontent">
		</div>
		<div id = "Chart5" class="tabcontent">
		</div>
		<div id = "Chart6" class="tabcontent">
		</div>
		<div id = "Chart7" class="tabcontent">
		</div>
		<div id = "Chart8" class="tabcontent">
		</div>
		<div id = "Chart9" class="tabcontent">
		</div>
		<div id = "Chart10" class="tabcontent">
		</div>
		<script type = "text/javascript">
			divCreation('Chart1', 'sheet1Chart0');		
			divCreation('Chart1', 'sheet1Chart1');
			divCreation('Chart1', 'sheet1Chart2');
			svgCreation('sheet1Chart0', '연도별 특허 증가율', 720, 480, 'svg0');
			svgCreation('sheet1Chart1', '연도별 특허 출원건수', 720, 480, 'svg1');
			svgCreation('sheet1Chart2', '연도별 특허 출원건수(세부)', 720, 480, 'svg2');

		</script>
		<script src = "./js/implements/BarPolygonalChart1.js" charset="utf-8"></script>
		<script src = "./js/implements/patentNationDonut2.js" charset="utf-8"></script>
		<script src = "./js/implements/patentCompareNationChart3.js" charset="utf-8"></script>
		<script src = "./js/implements/VectorBubbleChart4.js" charset="utf-8"></script>
		<script src = "./js/implements/tableImplement5.js" charset="utf-8"></script>
		<script src = "./js/implements/PositiveNegativeBarChart6.js" charset="utf-8"></script>
		<script src = "./js/implements/smallClass3DDonut7.js" charset="utf-8"></script>
		<script src = "./js/implements/BarPolygonalChart8.js" charset="utf-8"></script>
		<script src = "./js/implements/patentYearDonut9.js" charset="utf-8"></script>
		<script src = "./js/implements/PositioningBubbleChart10.js" charset="utf-8"></script>
		<script src = "./js/implements/CircleGrouping.js" charset="utf-8"></script>
		<%
			int numberOfDiv = 3;
			String svg1, json1, svg2, json2;
			for(i=0;i<JsonarrList.size();i++){
				switch(i){
				case 0:
					for(int j=3;j<JsonList.get(i).size();j+=2){
						svg1 = SvgList.get(i).get(j);
						json1 = JsonList.get(i).get(j);
						svg2 = SvgList.get(i).get(j+1);
						json2 = JsonList.get(i).get(j+1);
						%>
						<script type = "text/javascript">
							//<div> 태그 생성
							divCreation('Chart1','sheet1Chart'+'<%=numberOfDiv%>');
							svgCreation('sheet1Chart'+'<%=numberOfDiv%>', '<%=listNationCode.get((j-3)/2)%>', 720, 480, '<%=svg1%>');
							divCreation('Chart1','sheet1Chart'+'<%=(numberOfDiv+1)%>');
							svgCreation('sheet1Chart'+'<%=(numberOfDiv+1)%>', '<%=listNationCode.get((j-3)/2)%>', 720, 480, '<%=svg2%>');
							dataGraph_sheet1('<%=svg1%>', '<%=svg2%>', '<%=json1%>', '<%=json2%>');
							<%numberOfDiv += 2;%>
						</script>
						<script src = "./js/implements/BarPolygonalChart1.js"></script>
						<%
					}
					break;
				case 1:
					svg1 = SvgList.get(i).get(0);
					json1 = JsonList.get(i).get(0);
					json2 = JsonList.get(i).get(1);
					
					%>
					<script type = "text/javascript">
						divCreation('Chart2', 'sheet2Chart'+'<%=numberOfDiv%>');
						svgCreation('sheet2Chart'+'<%=numberOfDiv%>', '특허 출원인의 출신', 1300, 1300, '<%=svg1%>');
						donutDataGraph_Sheet2('<%=svg1%>', '<%=json1%>', '<%=json2%>');
						<%numberOfDiv+=1;%>
					</script>
					<script src = "./js/implements/patentNationDonut2.js" charset="utf-8"></script>
					<%
					break;
				case 2:
					for(int j=0;j<JsonList.get(i).size();j++){
						svg1 = SvgList.get(i).get(j);
						json1 = JsonList.get(i).get(j);
					%>
					<script type = "text/javascript">
						divCreation('Chart3', 'sheet3Chart'+'<%=numberOfDiv%>');
						svgCreation('sheet3Chart'+'<%=numberOfDiv%>', '<%=listNationCode.get(j)%>', 720, 480, '<%=svg1%>');
						inner_outer_Sheet3('<%=svg1%>', '<%=json1%>');
						<%numberOfDiv+=1;%>
					</script>
					<script src = "./js/implements/patentCompareNationChart3.js" charset="utf-8"></script>
					<%
					}
					break;
				case 3:
					for(int j=0;j<JsonList.get(i).size();j++){
						svg1 = SvgList.get(i).get(j);
						json1 = JsonList.get(i).get(j);
					%>
					<script type = "text/javascript">
						divCreation('Chart4', 'sheet4Chart'+'<%=numberOfDiv%>');
						svgCreation('sheet4Chart'+'<%=numberOfDiv%>', '<%=listNationCode.get(j)%>', 720, 480, '<%=svg1%>');
						VectorBubbleChart_Sheet4('<%=svg1%>', '<%=json1%>');
						<%numberOfDiv+=1;%>
					</script>
					<script src = "./js/implements/VectorBubbleChart4.js" charset="utf-8"></script>
					<%
					}
					break;
				case 4:
					json1 = JsonList.get(i).get(0);
					%>
					<script type = "text/javascript">
						divCreation('Chart5', 'sheet5Chart'+'<%=numberOfDiv%>');
						dataTable_Sheet5('sheet5Chart'+'<%=numberOfDiv%>', '<%=json1%>');
						<%numberOfDiv+=1;%>
					</script>
					<script src = "./js/implements/tableImplement5.js" charset="utf-8"></script>
					<%
					break;
				case 5:
					svg1 = SvgList.get(i).get(0);
					json1 = JsonList.get(i).get(0);
					%>
					<script type = "text/javascript">
						divCreation('Chart6', 'sheet6Chart'+'<%=numberOfDiv%>');
						svgCreation('sheet6Chart'+'<%=numberOfDiv%>', '회사별 특허시장 및 특허분야 점유율', 1300, 1300, '<%=svg1%>');
						PositiveNegativeBar_Sheet6('sheet6Chart'+<%=numberOfDiv%>, '<%=svg1%>', '<%=json1%>');
						<%numberOfDiv+=1;%>
					</script>
					<script src = "./js/implements/PositiveNegativeBarChart6.js" charset="utf-8"></script>
					<%
					break;
				case 6:
					svg1 = SvgList.get(i).get(0);
					json1 = JsonList.get(i).get(0);
					%>
					<script type = "text/javascript">
						divCreation('Chart7', 'sheet7Chart'+'<%=numberOfDiv%>');
						svgCreation('sheet7Chart'+'<%=numberOfDiv%>', '소분류 3d donut', 720, 480, '<%=svg1%>');
						smallClass3DDonut_Sheet7('<%=svg1%>', '<%=json1%>');
						<%numberOfDiv+=1;%>
					</script>
					<script src = "./js/implements/smallClass3DDonut7.js" charset="utf-8"></script>
					<%
					svg1 = SvgList.get(i).get(1);
					json1 = JsonList.get(i).get(1);
					%>
					<script type = "text/javascript">
						divCreation('Chart7', 'sheet7Chart'+'<%=numberOfDiv%>');
						svgCreation('sheet7Chart'+'<%=numberOfDiv%>', '소분류 연도별 추이', 720, 480, '<%=svg1%>');
						polygonalChart_Sheet7('<%=svg1%>', '<%=json1%>');
						<%numberOfDiv+=1;%>
					</script>
					<script src = "./js/implements/smallClass3DDonut7.js" charset="utf-8"></script>
					<%
					break;
				case 7://여기처럼 만들 것
					for(int j=0;j<JsonList.get(i).size();j++){
						svg1 = SvgList.get(i).get(j);
						json1 = JsonList.get(i).get(j);
					%>
					<script type = "text/javascript">
						divCreation('Chart8', 'sheet8Chart'+'<%=numberOfDiv%>');
						svgCreation('sheet8Chart'+'<%=numberOfDiv%>', '<%=listClassification.get(j)%>', 720, 480, '<%=svg1%>');
						BarPolygonalChart_Sheet8('<%=svg1%>', '<%=json1%>');
						<%numberOfDiv+=1;%>
					</script>
					<script src = "./js/implements/BarPolygonalChart8.js" charset="utf-8"></script>
					<%
					}
					break;
				case 8:
					for(int j=0;j<JsonList.get(i).size();j++){
						int stYear = minYear + yearPivot * j;
						int endYear = minYear + (yearPivot * (j+1));
						if(j == JsonList.get(i).size()-1 && endYear < maxYear) endYear = maxYear;
						String strYear = String.valueOf(stYear)+" ~ " + String.valueOf(endYear);
						svg1 = SvgList.get(i).get(j);
						json1 = JsonList.get(i).get(j);
					%>
					<script type = "text/javascript">
						divCreation('Chart9', 'sheet9Chart'+'<%=numberOfDiv%>');
						svgCreation('sheet9Chart'+'<%=numberOfDiv%>', '<%=strYear%>', 720, 480, '<%=svg1%>');
						patentYearDonut_Sheet9('<%=svg1%>', '<%=json1%>');
						<%numberOfDiv+=1;%>
					</script>
					<script src = "./js/implements/patentYearDonut9.js" charset="utf-8"></script>
					<%
					}
					break;
				case 9:
					svg1 = SvgList.get(i).get(0);
					json1 = JsonList.get(i).get(0);
					%>
					<script type = "text/javascript">
						divCreation('Chart10', 'sheet10Chart'+'<%=numberOfDiv%>');
						svgCreation('sheet10Chart'+'<%=numberOfDiv%>', '국가별 대비 특허', 720, 480, '<%=svg1%>');
						PositioningBubbleChart_Sheet10('<%=svg1%>', '<%=json1%>');
						<%numberOfDiv+=1;%>
					</script>
					<script src = "./js/implements/PositioningBubbleChart10.js" charset="utf-8"></script>
					<%
					break;
				default:
					break;
				}
			}
			%>
			<script src = "./js/implements/TabCtrl.js" charset = "utf-8"></script>
</body>
</html>