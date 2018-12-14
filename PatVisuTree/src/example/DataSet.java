package example;

import java.io.*;

public class DataSet
{
	private String strMidClassification;
	private String strSmallClassifiation;
	private String strNationCode;
	private String dateAppDate;
	private String strAppName;
	private String strAppNation;
	private String numFamilyLiterature;
	
	private String patnumber;
	private String invname;
	private String summary;
	private String repclaim;
	private String regnum;
	private String regday;
	private String comment;			//column 알아내기
	private String detail;
	
	private String state;
	private String openDate;
	private String openNum;
	private String preferNat;
	private String preferDate;
	private String inventor;
	
	private String exp;
	private String strBigClassfication;
	private String excelcode;
	private String pdfNum;
	
	public String getPatnumber() {
		return patnumber;
	}
	public void setPatnumber(String patnumber) {
		this.patnumber = patnumber;
	}
	public String getInvname() {
		return invname;
	}
	public void setInvname(String invname) {
		this.invname = invname;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getRepclaim() {
		return repclaim;
	}
	public void setRepclaim(String repclaim) {
		this.repclaim = repclaim;
	}
	public String getRegnum() {
		return regnum;
	}
	public void setRegnum(String regnum) {
		this.regnum = regnum;
	}
	public String getRegday() {
		return regday;
	}
	public void setRegday(String regday) {
		this.regday = regday;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	
	public String getStrMidClassification() {
		return strMidClassification;
	}
	public void setStrMidClassification(String strMidClassification) {
		this.strMidClassification = strMidClassification;
	}
	public String getStrSmallClassifiation() {
		return strSmallClassifiation;
	}
	public void setStrSmallClassifiation(String strSmallClassifiation) {
		this.strSmallClassifiation = strSmallClassifiation;
	}
	public String getStrNationCode() {
		return strNationCode;
	}
	public void setStrNationCode(String strNationCode) {
		this.strNationCode = strNationCode;
	}
	public String getDateAppDate() {
		return dateAppDate;
	}
	public void setDateAppDate(String dateAppDate) {
		this.dateAppDate = dateAppDate;
	}
	public String getStrAppName() {
		return strAppName;
	}
	public void setStrAppName(String strAppName) {
		this.strAppName = strAppName;
	}
	public String getStrAppNation() {
		return strAppNation;
	}
	public void setStrAppNation(String strAppNation) {
		this.strAppNation = strAppNation;
	}
	
	/*public static void PrintData(DataSet ds, int i, BufferedWriter fw) throws IOException {
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
		fw.write(ds.getStrAppName() + "\t");
		
		fw.write(ds.getPatnumber() + "\t\t");
		fw.write(ds.getInvname() + "\t\t");
		fw.write(ds.getSummary() + "\t\t");
		fw.write(ds.getRepclaim() + "\t\t");
		fw.write(ds.getRegnum() + "\t\t");
		fw.write(ds.getRegday() + "\t\t");
		fw.write(ds.getComment() + "\t\t");
		fw.write(ds.getDetail() + "\t\t");
		fw.newLine();
		fw.flush();
	}*/
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getOpenDate() {
		return openDate;
	}
	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}
	public String getOpenNum() {
		return openNum;
	}
	public void setOpenNum(String openNum) {
		this.openNum = openNum;
	}
	public String getPreferNat() {
		return preferNat;
	}
	public void setPreferNat(String preferNat) {
		this.preferNat = preferNat;
	}
	public String getPreferDate() {
		return preferDate;
	}
	public void setPreferDate(String preferDate) {
		this.preferDate = preferDate;
	}
	public String getInventor() {
		return inventor;
	}
	public void setInventor(String inventor) {
		this.inventor = inventor;
	}
	public String getStrBigClassfication() {
		return strBigClassfication;
	}
	public void setStrBigClassfication(String strBigClassfication) {
		this.strBigClassfication = strBigClassfication;
	}
	public String getExcelcode() {
		return excelcode;
	}
	public void setExcelcode(String excelcode) {
		this.excelcode = excelcode;
	}
	public String getExp() {
		return exp;
	}
	public void setExp(String exp) {
		this.exp = exp;
	}
	public String getPdfNum() {
		return pdfNum;
	}
	public void setPdfNum(String pdfNum) {
		this.pdfNum = pdfNum;
	}
	public String getNumFamilyLiterature() {
		return numFamilyLiterature;
	}
	public void setNumFamilyLiterature(String numFamilyLiterature) {
		this.numFamilyLiterature = numFamilyLiterature;
	}
}