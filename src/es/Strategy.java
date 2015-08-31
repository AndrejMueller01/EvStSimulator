package es;

import es.ui.StrategyForm;
import es.util.RandomString;

import java.util.ArrayList;

/**
 * Created by Johannes on 31/08/2015.
 */
public abstract class Strategy {
	private String name;
	private double stepSize;
	private double epsilon;
	private int previousQuality;
	private int populationSize;
	private int selectionSize;
	private int iterations;
	private String targetString;
	private ArrayList<String> initialConfiguration;

	private StrategyForm form;

	public abstract void adjustGui();

	public abstract String reproductionStep (String father, String mother);
	public abstract String mutationStep (String candidate);
	public abstract boolean selectionStep (String candidate);

	public abstract String evolution();

	public void init() {
		setInitialConfiguration(new ArrayList<String>());
		int length = targetString.length();
		RandomString rs = new RandomString(length);
		for(int i = 0; i < populationSize; i++) {
			getInitialConfiguration().add(rs.nextString());
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getStepSize() {
		return stepSize;
	}

	public void setStepSize(double stepSize) {
		this.stepSize = stepSize;
	}

	public double getEpsilon() {
		return epsilon;
	}

	public void setEpsilon(double epsilon) {
		this.epsilon = epsilon;
	}

	public int getPopulationSize() {
		return populationSize;
	}

	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
	}

	public int getSelectionSize() {
		return selectionSize;
	}

	public void setSelectionSize(int selectionSize) {
		this.selectionSize = selectionSize;
	}

	public int getIterations() {
		return iterations;
	}

	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	public String getTargetString() {
		return targetString;
	}

	public void setTargetString(String targetString) {
		this.targetString = targetString;
	}

	public ArrayList<String> getInitialConfiguration() {
		return initialConfiguration;
	}

	public void setInitialConfiguration(ArrayList<String> initialConfiguration) {
		if(this.initialConfiguration == null)
			this.initialConfiguration = new ArrayList<String>();
		this.initialConfiguration.clear();
		this.initialConfiguration.addAll(initialConfiguration);
	}

	public StrategyForm getForm() {
		return form;
	}

	public void setForm(StrategyForm form) {
		this.form = form;
	}

	public int getPreviousQuality() {
		return previousQuality;
	}

	public void setPreviousQuality(int previousQuality) {
		this.previousQuality = previousQuality;
	}
}
