package data;

import java.util.List;

public class GScore {
	private Double score;
	private String grade;
	private String patNum;
	
	
	public GScore() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @return the patNum
	 */
	public String getPatNum() {
		return patNum;
	}

	/**
	 * @param patNum the patNum to set
	 */
	public void setPatNum(String patNum) {
		this.patNum = patNum;
	}

	public GScore(Double score, String grade) {
		
		this.score = score;
		this.grade = grade;
		
	}
	public Double getScore() {
		return score;
	}
	public void setScore(Double score) {
		this.score = score;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	
	
	
}
