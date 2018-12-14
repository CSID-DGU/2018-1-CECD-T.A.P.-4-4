package data;

public class PriData {

	private String name;
	private String patNum;
	private String sum;
	private String claim;
	private String registerDate;
	private String registerCheck;
	private String appDate;
	private String citedPat;
	private String family; //패밀리 정보(정돈 안되어 있음)
	private String skillNum; //기술 번호
	private String ipcpc; // 산업 번호
	private String appName;
	
	private int year;
	private int cited;
	private int leftyear;
	
	public PriData() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @return the appName
	 */
	public String getAppName() {
		return appName;
	}

	/**
	 * @param appName the appName to set
	 */
	public void setAppName(String appName) {
		this.appName = appName;
	}

	public int getYear() {
		return year;
	}

	/**
	 * @return the ipcpc
	 */
	public String getIpcpc() {
		return ipcpc;
	}

	/**
	 * @param ipcpc the ipcpc to set
	 */
	public void setIpcpc(String ipcpc) {
		this.ipcpc = ipcpc;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getName() {
		return name;
	}
	
	public String getSkillNum() {
		return skillNum;
	}

	public void setSkillNum(String skillNum) {
		this.skillNum = skillNum;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getPatNum() {
		return patNum;
	}
	public void setPatNum(String patNum) {
		this.patNum = patNum;
	}
	public String getSum() {
		return sum;
	}
	public void setSum(String sum) {
		this.sum = sum;
	}
	public String getClaim() {
		return claim;
	}
	public void setClaim(String claim) {
		this.claim = claim;
	}
	public String getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}
	public String getRegisterCheck() {
		return registerCheck;
	}
	public void setRegisterCheck(String registerCheck) {
		this.registerCheck = registerCheck;
	}
	public String getAppDate() {
		return appDate;
	}
	public void setAppDate(String appDate) {
		this.appDate = appDate;
	}
	public String getCitedPat() {
		return citedPat;
	}
	public void setCitedPat(String citedPat) {
		this.citedPat = citedPat;
	}
	public String getFamily() {
		return family;
	}
	public void setFamily(String family) {
		this.family = family;
	}
	public int getCited() {
		return cited;
	}
	public void setCited(int cited) {
		this.cited = cited;
	}
	public int getLeftyear() {
		return leftyear;
	}
	public void setLeftyear(int leftyear) {
		this.leftyear = leftyear;
	}
	
	
}
