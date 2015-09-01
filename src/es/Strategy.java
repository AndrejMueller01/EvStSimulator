package es;

import es.ui.StrategyForm;
import es.util.LevenshteinDistance;
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
    private ArrayList<String> initialConfiguration;

    private StrategyForm form;

    public abstract void adjustGui();

    public abstract String reproductionStep(String father, String mother);

    public abstract String mutationStep(String candidate);

    public abstract boolean selectionStep(String candidate);

    public abstract ArrayList<String> selectionStep(ArrayList<String> candidates);

    public abstract String evolution();

    public void init() {
        setInitialConfiguration(new ArrayList<String>());
        int length = targetString.length();
        RandomString rs = new RandomString(length);
        for (int i = 0; i < populationSize; i++) {
            getInitialConfiguration().add(rs.nextString());
        }
    }

    // roulette wheel
    public ArrayList<String> rouletteWheel() {
        System.out.println("___________________________________ROULETTE WHEEL_________________________________________");
        ArrayList<Integer> invLevenshteinValues = new ArrayList<>();
        int sumOfInvLevValues = 0;

        for (int i = 0; i < populationSize; i++) {
            invLevenshteinValues.add((targetString.length()) - LevenshteinDistance.distance(getInitialConfiguration().get(i), targetString));
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
                    System.out.println("P1: RandomValue: " + temp + " String: " + getInitialConfiguration().get(indexP1) + " index: " + i);

                }
            }
            if (temp >= randomSelection2) {
                if (indexP2 == -1) {
                    indexP2 = i;
                    System.out.println("P2: RandomValue: " + temp + " String: " + getInitialConfiguration().get(indexP2) + " index: " + i);

                }
            }
        }

        ArrayList<String> parents = new ArrayList<>();

        parents.add(getInitialConfiguration().get(indexP1));
        parents.add(getInitialConfiguration().get(indexP2));

        System.out.println("RouletteWheel: P1: " + parents.get(0) + " P2: " + parents.get(1));
        System.out.println("__________________________________________________________________________________________");

        return parents;
    }

    public ArrayList<String> findBestParents() {

        if (populationSize < 2) {
            return null;
        }

        int tempDistance = 999;
        int tempIndex = -1;
        int tempIndex2 = -1;


        // find the best one
        for (int i = 0; i < populationSize; i++) {

            if (tempDistance > LevenshteinDistance.distance(getInitialConfiguration().get(i), targetString)) {
                tempDistance = LevenshteinDistance.distance(getInitialConfiguration().get(i), targetString);
                tempIndex = i;
            }
        }

        tempDistance = 999;
        // find the 2nd best one
        for (int i = 0; i < populationSize; i++) {
            if (i != tempIndex)
                if (tempDistance > LevenshteinDistance.distance(getInitialConfiguration().get(i), targetString)) {
                    tempDistance = LevenshteinDistance.distance(getInitialConfiguration().get(i), targetString);
                    tempIndex2 = i;
                }
        }

        ArrayList<String> parents = new ArrayList<>();
        parents.add(getInitialConfiguration().get(tempIndex));
        parents.add(getInitialConfiguration().get(tempIndex2));

        System.out.println("Best parents: (1: " + parents.get(0) + ") (2: " + parents.get(1) + ")");
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

    public ArrayList<String> getInitialConfiguration() {
        return initialConfiguration;
    }

    public void setInitialConfiguration(ArrayList<String> initialConfiguration) {
        if (this.initialConfiguration == null)
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

    public int getMu() {
        return mu;
    }

    public void setMu(int mu) {
        this.mu = mu;
    }
}
