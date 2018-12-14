package example;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.File;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Servlet implementation class Process
 */
@WebServlet("/Process")
public class Process extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static final String savePath = "D:/";
	static BufferedWriter fw = null;//parsing test
	private DataSet []ds = null;
	private NationDataSet []nds = null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Process() {
        super();
        
        ds = new DataSet[100010];
        nds = new NationDataSet[200];
		
        // TODO Auto-generated constructor stub
    }
    
  /*  private static void PrintData(DataSet ds, int i) throws IOException {
		//Date d = ds.getDateAppDate();
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
		
		fw.write(ds.getStrMidClassification() + "\t\t");
		fw.write(ds.getStrSmallClassifiation() + "\t\t");
		fw.write(ds.getStrNationCode() + "\t\t");
		fw.write(ds.getDateAppDate()+"\t\t");
		fw.write(ds.getStrAppNation() + "\t\t\t");
		int familyNum = ds.getNumFamilyLiterature().intValue();
		String familyString = Integer.toString(familyNum);
		fw.write(familyString  + "\t\t\t");
		fw.write(ds.getStrAppName());
		fw.newLine();
		fw.flush();
	}
*/
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/x-json; charset=utf-8");
		
		String cmd = (String)request.getAttribute("cmd");

		System.out.println(cmd);
		if (cmd == null || cmd.equals("parsingExcel")) {
			parsingExcel(request,response);
		} else if(cmd.equals("printClassData")) {
			System.out.println("ddd");
			printClassData(request,response);
		} else if(cmd.equals("printData")) {
			printData(request,response);
		}
	}

	private void printData(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/x-json; charset=utf-8");
		
		String arg = "/detail.jsp";
		request.setAttribute("summing", ds[0].getStrAppNation());
		request.setAttribute("name", ds[0].getStrAppName());
	
		
		RequestDispatcher rd = request.getRequestDispatcher(arg);
		rd.forward(request, response);
		
	}

	private void printClassData(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/x-json; charset=utf-8");
		
		String arg =  "/tree.jsp";
		String[] middle = {"AA","AB","AC"};
		String[][] small = {{"AAA","AAB","AAC"},{"ABA","ABB","ABC"},
				{"ACA","ACB","ACC"}
		};
		request.setAttribute("cmd", "ClassData");
		request.setAttribute("middle", middle);
		request.setAttribute("small", small);
		
		RequestDispatcher rd = request.getRequestDispatcher(arg);
		rd.forward(request, response);
		
		
	}

	public void parsingExcel(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		int line_num = 0;
		int i = 0, nCnt = 0;
		String []title = new String[7];
		String filePath = (String)request.getAttribute("filePath");
		String ext = (String)request.getAttribute("ext");
		
		
		
		try{
			File f = new File("E:/");
			if(!f.exists())f.mkdirs();
		}
		catch(Exception e){
			response.getWriter().write(e.toString());
		}
		
		if(ext.equals("xls") || ext.equals("XLS")) {
			try {
				FileInputStream file2;
				file2 = new FileInputStream(filePath);

				HSSFWorkbook workbook = new HSSFWorkbook(file2);

				HSSFSheet sheet = workbook.getSheetAt(0);

				Iterator<Row> rowIterator = sheet.iterator();
	
				while (rowIterator.hasNext()){
					Row row = rowIterator.next();
					Iterator<Cell> cellIterator = row.cellIterator();
					ds[i] = new DataSet();
					while (cellIterator.hasNext()){
						Cell cell = cellIterator.next();
						int k = cell.getColumnIndex();
						switch(k) {
						case 0:				//국가코드(3) : string
							boolean isAlreadyStored = false;
							if (i == 0) {
								title[2] = cell.getStringCellValue();
								break;
							}
							ds[i].setStrNationCode(cell.getStringCellValue().trim());
							for(int j=0;j<nCnt;j++){
								if(ds[i].getStrNationCode().equals(nds[j].getNation())) {
									nds[j].setNCount((nds[j].getNCount()+1));
									isAlreadyStored = true;
									break;
								}
							}
							if(!isAlreadyStored) {
								nds[nCnt] = new NationDataSet();
								nds[nCnt].setNation(ds[i].getStrNationCode());
								nds[nCnt].setNCount(1);
								nCnt++;
							}
							break;
						case 8:				//출원일 (4) : double -> 나중에 Date로 바꿀 것
							if (i > 0) ds[i].setDateAppDate(cell.getStringCellValue());
							else title[3] = cell.getStringCellValue();
							break;
						case 12:			//출원인대표명화(7) : string
							if (i > 0) ds[i].setStrAppName(cell.getStringCellValue());
							else title[6] = cell.getStringCellValue();
							break;
						case 14:			//출원인대표국적(5) : string
							if (i > 0) ds[i].setStrAppNation(cell.getStringCellValue().trim());
							else title[4] = cell.getStringCellValue();
							break;
						case 15:			//중분류(1) : string
							if (i > 0) ds[i].setStrMidClassification(cell.getStringCellValue());
							else title[0] = cell.getStringCellValue();
							break;
						case 16:			//소분류(2) : string
							if (i > 0) ds[i].setStrSmallClassifiation(cell.getStringCellValue());
							else title[1] = cell.getStringCellValue();
							break;
						case 56:			//패밀리 개별국 문헌 수(6) : double -> 나중에 int로 바꿀 것
							if (i > 0) ds[i].setNumFamilyLiterature(cell.getNumericCellValue());
							else title[5] = cell.getStringCellValue();
							break;
						default:
							break;
						}
					}
					if(i == 0) 	{
						for(int k=0;k<7;k++) {
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
				fw = new BufferedWriter(new FileWriter(savePath + "outputTest2.txt"));
				while (rowIterator.hasNext()){
					Row row = rowIterator.next();
					Iterator<Cell> cellIterator = row.cellIterator();
					ds[i] = new DataSet();
					while (cellIterator.hasNext()){
						Cell cell = cellIterator.next();
						int k = cell.getColumnIndex();
						switch(k) {
						case 0:				//국가코드(3) : string
							boolean isAlreadyStored = false;
							if (i == 0) {
								title[2] = cell.getStringCellValue();
								break;
							}
							
							ds[i].setStrNationCode(cell.getStringCellValue().trim());
							for(int j=0;j<nCnt;j++){
								if(ds[i].getStrNationCode().equals(nds[j].getNation())) {
									nds[j].setNCount((nds[j].getNCount()+1));
									isAlreadyStored = true;
									break;
								}
							}
							if(!isAlreadyStored) {
								nds[nCnt] = new NationDataSet();
								nds[nCnt].setNation(ds[i].getStrNationCode());
								nds[nCnt].setNCount(1);
								nCnt++;
							}
							
							break;
						case 8:				//출원일 (4) : double -> 나중에 Date로 바꿀 것
						
							if (i > 0){
								
								ds[i].setDateAppDate(cell.getStringCellValue()); 
								
							}
							else title[3] = cell.getStringCellValue();
							break;
						case 12:			//출원인대표명화(7) : string
							if (i > 0){
								
								/* ds[i].setStrAppName(cell.getStringCellValue()); */
								
							}
							else title[6] = cell.getStringCellValue();
							break;
						case 14:			//출원인대표국적(5) : string
							if (i > 0) {
								ds[i].setStrAppNation(cell.getStringCellValue().trim());
								
							}
							else title[4] = cell.getStringCellValue();
							break;
						case 15:			//중분류(1) : string
							if (i > 0){
								ds[i].setStrMidClassification(cell.getStringCellValue());
								
							}
							else title[0] = cell.getStringCellValue();
							break;
						case 16:			//소분류(2) : string
							if (i > 0){
								ds[i].setStrSmallClassifiation(cell.getStringCellValue());
							}
								
							else title[1] = cell.getStringCellValue();
							break;
						case 56:			//패밀리 개별국 문헌 수(6) : double -> 나중에 int로 바꿀 것
							if (i > 0) {
								/* ds[i].setNumFamilyLiterature(cell.getNumericCellValue()); */
								
							}
							else title[5] = cell.getStringCellValue();
							break;
						default:
							break;
						}
					}
					if(i == 0) 	{
						for(int k=0;k<7;k++) {
							System.out.println(title[k]);
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
	
		//test 코드
		String arg = "/Process";
		request.setAttribute("cmd", "printClassData");
		RequestDispatcher rd = request.getRequestDispatcher(arg);
		rd.forward(request, response);
	}
	
}

