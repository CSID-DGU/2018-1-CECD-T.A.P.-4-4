package data;

public class SkillData {
	private String ipcpc;
	private String name;
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}



	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	private int num;

	private Double aver;
	private Double flowV;
	
	
	public SkillData() {
		super();
		// TODO Auto-generated constructor stub
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



	

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
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
	
	
	
}
