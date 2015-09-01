package es;

import es.ui.StrategyForm;
import es.util.LevenshteinDistance;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Johannes on 31/08/2015.
 */
public class StrategyOnePlusOne extends Strategy {
    private final double ALPHA = 0.85;

    public StrategyOnePlusOne(StrategyForm form) {
        setName("1+1");
        setEpsilon(0.05);
        setStepSize(1);
        setPopulationSize(1);
        setIterations(100000);
        setForm(form);
        setPreviousQuality(9999);
    }

    @Override
    public void adjustGui() {
        getForm().targetStringTextField.setText("helloworld");
        getForm().iterationsTextField.setText("" + getIterations());
        getForm().populationSizeTextField.setText("" + getPopulationSize());
        getForm().populationSizeTextField.setEditable(false);
    }

    @Override
    public String reproductionStep(String father, String mother) {
        return null;
    }

    @Override
    public String mutationStep(String candidate) {
        double stdDeviation = getStepSize();
        char a = 'a';
        char z = 'z';

        Random r = new Random();
        char[] characters = candidate.toCharArray();
        for (int i = 0; i < candidate.length(); i++) {
            int value = characters[i] + (int) Math.round(r.nextGaussian() * stdDeviation);
            while (value > z)
                value -= 26;
            while (value < a)
                value += 26;
            characters[i] = (char) value;
        }
        return String.valueOf(characters);
    }

    @Override
    public boolean selectionStep(String candidate) {
        int distance = LevenshteinDistance.distance(candidate, getTargetString());
        if (distance < getPreviousQuality()) {
            setPreviousQuality(distance);
            return true;
        } else
            return false;
    }

    @Override
    public ArrayList<String> selectionStep(ArrayList<String> candidates) {
        return null;
    }

    @Override
    public String evolution() {
        init();
        String temp = getInitialConfiguration().get(0);
        String previousParent = temp;

        int numberOfPositiveMutations = 0;

        getForm().resultList.append("Iteration " + 0 + ": " + temp + "\n");

        int i;
        for (i = 0; i < getIterations(); i++) {
            //System.out.println("#" + i + ": temp = " + temp + "(" + LevenshteinDistance.distance(temp, getTargetString()) + "); previousParent = " + previousParent + "(" + LevenshteinDistance.distance(previousParent, getTargetString()) + ")");

            String afterMutation = mutationStep(temp);
            //System.out.println("#" + i + ": afterMutation = " + afterMutation);

            if (selectionStep(afterMutation)) {
                previousParent = temp;
                temp = afterMutation;
                numberOfPositiveMutations++;
                getForm().resultList.append("Iteration " + (i + 1) + ": " + temp + "\n");
                System.out.println("" + i + " " + temp);

                if (i > 0) {
                    if (Math.abs(LevenshteinDistance.distance(previousParent, getTargetString()) - LevenshteinDistance.distance(temp, getTargetString())) <= getEpsilon()) {
                        break;
                    } else {
                        if (numberOfPositiveMutations < (numberOfPositiveMutations / i)) {
                            setStepSize(getStepSize() * ALPHA);
                        } else {
                            setStepSize(getStepSize() / ALPHA);
                        }
                    }
                }
            }
        }
        return "Reached '" + temp + "' (distance of " + LevenshteinDistance.distance(temp, getTargetString()) + ") in " + i + " iterations.";
    }
}
