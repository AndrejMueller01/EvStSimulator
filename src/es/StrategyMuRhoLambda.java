package es;

import es.ui.StrategyForm;
import es.util.Candidate;
import es.util.Quality;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by AM on 01.09.2015.
 */
public class StrategyMuRhoLambda extends Strategy {

    private final double ALPHA = 1.2;
    private final int MALE = 0;
    private final int FEMALE = 1;
    private ArrayList<Candidate> children;


    public StrategyMuRhoLambda(StrategyForm form) {
        setName("muRhoLambda");
        setEpsilon(0);
        setPopulationSize(50);
        setIterations(1000);
        setForm(form);
        setPreviousQuality(9999);
        setLambda(getPopulationSize() * 10);
        children = new ArrayList<>();
    }

    @Override
    public void adjustGui() {
        getForm().targetStringTextField.setText("helloworld");
        getForm().iterationsTextField.setText("" + getIterations());
        getForm().populationSizeTextField.setText("" + getPopulationSize());
        getForm().populationSizeTextField.setEditable(true);

    }

    @Override
    public void reproductionStep(Candidate father, Candidate mother) {

        Candidate child1 = new Candidate();
        Candidate child2 = new Candidate();

        ArrayList<Integer> reproductionTracker = new ArrayList<>();

        char[] childCArray1 = new char[getTargetString().length()];
        char[] childCArray2 = new char[getTargetString().length()];


        for (int j = 0; j < getTargetString().length(); j++) {
            if ((Math.random() * 2.0) <= 1.0) {
                childCArray1[j] = father.getValue().toCharArray()[j];
                reproductionTracker.add(MALE);
            } else {
                childCArray1[j] = mother.getValue().toCharArray()[j];
                reproductionTracker.add(FEMALE);
            }
        }

        for (int j = 0; j < getTargetString().length(); j++) {
            if (reproductionTracker.get(j) == MALE) {
                childCArray2[j] = mother.getValue().toCharArray()[j];
            } else {
                childCArray2[j] = father.getValue().toCharArray()[j];
            }
        }

        child1.setStepWidth(calculateStepWidth(father, mother));
        child2.setStepWidth(calculateStepWidth(father, mother));

        child1.setValue(new String(childCArray1));
        child2.setValue(new String(childCArray2));

        children.add(child1);
        children.add(child2);

    }

    private double calculateStepWidth(Candidate father, Candidate mother) {
        if ((Math.random() * 2.0) <= 1.0) {
            double random = (Math.random() * 3.0);
            if (random <= 1.0) {
                return (father.getStepWidth() * ALPHA);
            } else if (random >= 2.0) {
                return (father.getStepWidth() / ALPHA);
            } else {
                return (father.getStepWidth());
            }
        } else {
            double random = (Math.random() * 3.0);
            if (random <= 1.0) {
                return (mother.getStepWidth() * ALPHA);
            } else if (random >= 2.0) {
                return (mother.getStepWidth() / ALPHA);
            } else {
                return (mother.getStepWidth());
            }
        }
    }

    @Override
    public String mutationStep(String candidate) {
        return null;
    }

    @Override
    public Candidate mutationStep(Candidate candidate) {
        double stdDeviation = candidate.getStepWidth();
        char a = 'a';
        char z = 'z';

        Random r = new Random();
        char[] characters = candidate.getValue().toCharArray();
        int pos = (int) (Math.random() * candidate.getValue().length());

        int value = characters[pos] + Math.abs((int) Math.round(r.nextGaussian() * stdDeviation));
        while (value > z)
            value -= 26;
        characters[pos] = (char) value;

        candidate.setValue(String.valueOf(characters));
        candidate.setQuality(Quality.function(candidate.getValue(), getTargetString()));
        return candidate;
    }

    @Override
    public boolean selectionStep(String candidate) {
        return false;
    }

    @Override
    public ArrayList<Candidate> selectionStep(ArrayList<Candidate> candidates) {

        ArrayList<Candidate> newCandidates = new ArrayList<>();

        int quality = Integer.MAX_VALUE;
        int bestQualityIndex = -1;

        for (int j = 0; j < getPopulationSize(); j++) {
            for (int i = 0; i < candidates.size(); i++) {
                if (candidates.get(i).getQuality() < quality) {
                    bestQualityIndex = i;
                    quality = candidates.get(i).getQuality();
                }
            }
            Candidate newCandy = new Candidate(candidates.get(bestQualityIndex).getValue(), candidates.get(bestQualityIndex).getStepWidth());
            newCandy.setQuality(Quality.function(newCandy.getValue(), getTargetString()));
            newCandidates.add(newCandy);

            candidates.remove(bestQualityIndex);
            quality = Integer.MAX_VALUE;
        }

        return newCandidates;
    }

    @Override
    public String evolution() {
        reset();
        init();

        ArrayList<Candidate> mutatedChildren = new ArrayList<>();
        ArrayList<Candidate> newParents = null;
        int iteration = 0;

        for (int i = 0; i < getIterations(); i++) {
            children.clear();
            mutatedChildren.clear();
            for (int j = 0; j < (getLambda() / 2); j++) {
                newParents = rouletteWheel();
                reproductionStep(newParents.get(0), newParents.get(1));
            }
            for (int j = 0; j < getLambda(); j++) {
                mutatedChildren.add(mutationStep(children.get(j)));
            }
            newParents = selectionStep(mutatedChildren);
            setInitialConfiguration(newParents);
            if (newParents.get(0).getQuality() == 0) {
                iteration = i;
                break;
            }
            iteration = i;
        }

        for (int i = 0; i < newParents.size(); i++) {
            Candidate candidate = newParents.get(i);
            getForm().resultList.append("candidate " + (i + 1) + ": " + candidate.getValue() + " - quality " + candidate.getQuality() + "\n");
        }

        return "Found candidate '" + newParents.get(0).getValue() + "' with quality " + newParents.get(0).getQuality() + " (iteration " + (iteration + 1) + ") !";
    }

    private void reset() {
        setEpsilon(getEpsilon());
        setStepSize(1.0);
        setPopulationSize(Integer.parseInt(getForm().populationSizeTextField.getText()));
        setIterations(Integer.parseInt(getForm().iterationsTextField.getText()));
        setPreviousQuality(9999);
    }
}
