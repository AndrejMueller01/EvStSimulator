package es;

import es.ui.StrategyForm;
import es.util.Quality;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by AM on 01.09.2015.
 */
public class StrategyMuPlusLambda extends Strategy {
    private final double ALPHA = 0.85;

    public StrategyMuPlusLambda(StrategyForm form) {
        setName("mu+lambda");
        setEpsilon(0);
        setStepSize(1);
        setPopulationSize(50);
        setIterations(100000);
        setForm(form);
        setPreviousQuality(9999);
        setMu(getPopulationSize() * 10);
    }

    @Override
    public void adjustGui() {
        getForm().targetStringTextField.setText("helloworld");
        getForm().iterationsTextField.setText("" + getIterations());
        getForm().populationSizeTextField.setText("" + getPopulationSize());
        getForm().populationSizeTextField.setEditable(true);

    }

    @Override
    public String reproductionStep(String father, String mother) {

        char[] child1 = new char[getTargetString().length()];

        // random for now
        for (int j = 0; j < getTargetString().length(); j++)
            if ((Math.random() * 2.0) <= 1.0)
                child1[j] = father.toCharArray()[j];
            else {
                child1[j] = mother.toCharArray()[j];
            }
        return new String(child1);
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
        return false;
    }

    @Override
    public ArrayList<String> selectionStep(ArrayList<String> candidates) {

        ArrayList<Integer> qualities = new ArrayList<>();
        ArrayList<String> bestChildren = new ArrayList<>();
        int quality = Integer.MAX_VALUE;

        for (int i = 0; i < candidates.size(); i++) {
            qualities.add(Quality.distance(candidates.get(i), getTargetString()));
        }

        int bestQualityIndex = -1;

        for (int j = 0; j < getPopulationSize(); j++) {

            for (int i = 0; i < candidates.size(); i++) {

                if (qualities.get(i) < quality) {
                    bestQualityIndex = i;
                    quality = qualities.get(i);
                }
            }
            bestChildren.add(candidates.get(bestQualityIndex));
            System.out.println("remove quality: " + qualities.get(bestQualityIndex) + " candidate: " + candidates.get(bestQualityIndex));

            candidates.remove(bestQualityIndex);
            qualities.remove(bestQualityIndex);
            quality = Integer.MAX_VALUE;
        }
        return bestChildren;
    }

    @Override
    public String evolution() {
		reset();
        init();
		
        ArrayList<String> parents;
        ArrayList<String> children = new ArrayList<>();
        ArrayList<String> mutatedChildren = new ArrayList<>();


        for (int i = 0; i < (getMu()); i++) {
            parents = rouletteWheel();
            children.add(reproductionStep(parents.get(0), parents.get(1)));
            System.out.println("New child: " + children.get(i));
            mutatedChildren.add(mutationStep(children.get(i)));
        }
        ArrayList<String> newParents = selectionStep(mutatedChildren);
        for (int i = 0; i < newParents.size(); i++) {
            System.out.println("new Parent " + i + ": " + newParents.get(i));
        }


        return null;
 /*
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
    */
    }
	private void reset() {
		setEpsilon(getEpsilon());
		setStepSize(1.0);
		setPopulationSize(Integer.parseInt(getForm().populationSizeTextField.getText()));
		setIterations(Integer.parseInt(getForm().iterationsTextField.getText()));
		setPreviousQuality(9999);
	}
}
