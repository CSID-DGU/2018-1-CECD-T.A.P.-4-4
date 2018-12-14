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
	private Double numFamilyLiterature;
	
	private String patnumber;
	private String invname;
	private String summary;
	private String repclaim;
	private String regnum;
	private String regday;
	private String comment;			//column 알아내기
	private String detail;
	
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
	public Double getNumFamilyLiterature() {
		return numFamilyLiterature;
	}
	public void setNumFamilyLiterature(Double numFamilyLiterature) {
		this.numFamilyLiterature = numFamilyLiterature;
	}
	public static void PrintData(DataSet ds, int i, BufferedWriter fw) throws IOException {
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
	}
}