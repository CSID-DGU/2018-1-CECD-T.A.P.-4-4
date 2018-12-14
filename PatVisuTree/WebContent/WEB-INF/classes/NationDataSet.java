package example;

public class NationDataSet {
	private String Nation;
	private int nCount;
	private boolean isEtcInclude;
	public void setNation(String Nation) {
		this.Nation = Nation;
	}
	public void setNCount(int nCount) {
		this.nCount = nCount;
	}
	public void setEtcInclude(boolean isEtcInclude) {
		this.isEtcInclude = isEtcInclude;
	}
	public String getNation() {
		return this.Nation;
	}
	public int getNCount() {
		return this.nCount;
	}
	public boolean getEtcInclude() {
		return this.isEtcInclude;
	}
}