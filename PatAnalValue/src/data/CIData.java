package data;

public class CIData {

	private String CIName;
	private String ipcpc;
	private String skillNum;
	
	private Double aver;
	private Double flowV;
	
	private int year;
	/**
	 * 
	 */
	public CIData() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getCIName() {
		return CIName;
	}
	public void setCIName(String cIName) {
		CIName = cIName;
	}
	public Double getAver() {
		return aver;
	}
	public void setAver(Double aver) {
		this.aver = aver;
	}
	public Double getFlowV() {
		return flowV;
	}
	public void setFlowV(Double flowV) {
		this.flowV = flowV;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	/**
	 * @return the ipcpc
	 */
	public String getIpcpc() {
		return ipcpc;
	}
	/**
	 * @return the skillNum
	 */
	public String getSkillNum() {
		return skillNum;
	}
	/**
	 * @param skillNum the skillNum to set
	 */
	public void setSkillNum(String skillNum) {
		this.skillNum = skillNum;
	}
	/**
	 * @param ipcpc the ipcpc to set
	 */
	public void setIpcpc(String ipcpc) {
		this.ipcpc = ipcpc;
	}
	
	
}
