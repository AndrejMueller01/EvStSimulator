package es.util;

/**
 * Created by Johannes on 01/09/2015.
 */
public class Candidate {

	private String value;
	private double stepWidth;
	private int quality;

	public Candidate(){

	}

	public Candidate(String value, double stepWidth){
		this.value = value;
		this.stepWidth = stepWidth;
		this.setQuality(Integer.MAX_VALUE);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public double getStepWidth() {
		return stepWidth;
	}

	public void setStepWidth(double stepWidth) {
		this.stepWidth = stepWidth;
	}

	public int getQuality() {
		return quality;
	}

	public void setQuality(int quality) {
		this.quality = quality;
	}
}
