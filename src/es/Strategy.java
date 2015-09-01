package es;

import es.ui.StrategyForm;
import es.util.Candidate;
import es.util.Quality;
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
    private int mu;
    private String targetString;
    private ArrayList<Candidate> initialConfiguration;

    private StrategyForm form;

    public abstract void adjustGui();

    public abstract Candidate reproductionStep(Candidate father, Candidate mother);

    public abstract String mutationStep(String candidate);

	public abstract Candidate mutationStep(Candidate candidate);

	public abstract boolean selectionStep(String candidate);

    public abstract ArrayList<Candidate> selectionStep(ArrayList<Candidate> candidates);

    public abstract String evolution();

    public void init() {
        setInitialConfiguration(new ArrayList<Candidate>());
        int length = targetString.length();
        RandomString rs = new RandomString(length);
        for (int i = 0; i < populationSize; i++) {
            getInitialConfiguration().add(new Candidate(rs.nextString(), 1));
        }
    }

    // roulette wheel
    public ArrayList<Candidate> rouletteWheel() {
        //System.out.println("___________________________________ROULETTE WHEEL_________________________________________");
        ArrayList<Integer> invLevenshteinValues = new ArrayList<>();
        int sumOfInvLevValues = 0;

        for (int i = 0; i < populationSize; i++) {
            invLevenshteinValues.add((targetString.length()) - Quality.distance(getInitialConfiguration().get(i).getValue(), targetString));
            sumOfInvLevValues += invLevenshteinValues.get(i);
        }

        double randomSelection1 = Math.random() * (double) sumOfInvLevValues;
        double randomSelection2 = Math.random() * (double) sumOfInvLevValues;

        int temp = 0;
        int indexP1 = -1;
        int indexP2 = -1;

        for (int i = 0; i < populationSize; i++) {
            temp += invLevenshteinValues.get(i);
            if (temp >= randomSelection1) {
                if (indexP1 == -1) {
                    indexP1 = i;
                    //System.out.println("P1: RandomValue: " + temp + " String: " + getInitialConfiguration().get(indexP1) + " index: " + i);

                }
            }
            if (temp >= randomSelection2) {
                if (indexP2 == -1) {
                    indexP2 = i;
                    //System.out.println("P2: RandomValue: " + temp + " String: " + getInitialConfiguration().get(indexP2) + " index: " + i);

                }
            }
        }

        ArrayList<Candidate> parents = new ArrayList<>();
		Candidate candidate1 = getInitialConfiguration().get(indexP1);
		Candidate candidate2 = getInitialConfiguration().get(indexP2);

		parents.add(candidate1);
        parents.add(candidate2);

        //System.out.println("RouletteWheel: P1: " + parents.get(0) + " P2: " + parents.get(1));
        //System.out.println("__________________________________________________________________________________________");

        return parents;
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

    public ArrayList<Candidate> getInitialConfiguration() {
        return initialConfiguration;
    }

    public void setInitialConfiguration(ArrayList<Candidate> initialConfiguration) {
        if (this.initialConfiguration == null)
            this.initialConfiguration = new ArrayList<Candidate>();
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

    public int getMu() {
        return mu;
    }

    public void setMu(int mu) {
        this.mu = mu;
    }
}
