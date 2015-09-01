package es;

import es.ui.StrategyForm;
import es.util.Quality;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Johannes on 31/08/2015.
 */
public class StrategyOnePlusOne extends Strategy {
    private final double ALPHA = 0.85;

    public StrategyOnePlusOne(StrategyForm form) {
        setName("1+1");
		setForm(form);
		setEpsilon(0);
		setStepSize(1.0);
		setPopulationSize(1);
		setIterations(100000);
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
        double stdDeviation = getStepSize()*12;
        char a = 'a';
        char z = 'z';

        Random r = new Random();
        char[] characters = candidate.toCharArray();
		int pos = (int) (Math.random() * candidate.length());

		int value = characters[pos] + Math.abs((int) Math.round(r.nextGaussian() * stdDeviation));
		while (value > z)
			value -= 26;
		characters[pos] = (char) value;

		/*
        for (int i = 0; i < candidate.length(); i++) {
            int value = characters[pos] + Math.abs((int) Math.round(r.nextGaussian() * stdDeviation));
            while (value > z)
                value -= 26;
            while (value < a)
                value += 26;
			characters[i] = (char) value;
        }
        */
		String output = String.valueOf(characters);
		//System.out.println(output);
		return output;
    }

    @Override
    public boolean selectionStep(String candidate) {
        int distance = Quality.function(candidate, getTargetString());
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
		reset();
        init();
        String temp = getInitialConfiguration().get(0);

        int i;
		int prevQuality = 9999;
		int currQuality = 9999;
		int run = 0;
		int[] bestValues = {0, 0};
		do {
			int numberOfPositiveMutations = 0;

			for (i = 0; i < getIterations(); i++) {
				//System.out.println("#" + i + ": temp = " + temp + "(" + LevenshteinDistance.distance(temp, getTargetString()) + "); previousParent = " + previousParent + "(" + LevenshteinDistance.distance(previousParent, getTargetString()) + ")");

				String afterMutation = mutationStep(temp);
				//System.out.println("#" + i + ": afterMutation = " + afterMutation);

				if (selectionStep(afterMutation)) {
					temp = afterMutation;
					numberOfPositiveMutations++;
					getForm().resultList.append("run " + (run + 1) + " / iteration " + (i + 1) + ": " + temp + " - quality " + Quality.function(temp, getTargetString()) + "\n");
					bestValues[0] = run + 1;
					bestValues[1] = i + 1;
				}
			}
			prevQuality = currQuality;
			currQuality = Quality.function(temp, getTargetString());
			if (((float) numberOfPositiveMutations / i) < 0.2) {
				setStepSize(getStepSize() * ALPHA);
			} else {
				setStepSize(getStepSize() / ALPHA);
			}
			run++;
			System.out.println("prevQ: " + prevQuality + "; currQ: " + currQuality + "; stepSize: " + getStepSize() + "; epsilon: " + getEpsilon());
		} while(prevQuality - currQuality > getEpsilon());
        return "Reached '" + temp + "' (distance of " + Quality.function(temp, getTargetString()) + ") in " + bestValues[1] + " iterations (run #" + bestValues[0] + ").";
    }

	private void reset() {
		setEpsilon(getEpsilon());
		setStepSize(1.0);
		setPopulationSize(Integer.parseInt(getForm().populationSizeTextField.getText()));
		setIterations(Integer.parseInt(getForm().iterationsTextField.getText()));
		setPreviousQuality(9999);
	}
}
