package es;

import es.ui.StrategyForm;
import es.util.Candidate;
import es.util.Quality;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by AM on 01.09.2015.
 */
public class StrategyMuPlusLambda extends Strategy {
    private final double ALPHA = 1.05;
	private ArrayList<Integer> stepSizes;


	public StrategyMuPlusLambda(StrategyForm form) {
        setName("mu+lambda");
        setEpsilon(0);
        stepSizes = new ArrayList<>();
        setPopulationSize(50);
        setIterations(1000);
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
    public Candidate reproductionStep(Candidate father, Candidate mother) {
		Candidate child = new Candidate();
        char[] childCArray = new char[getTargetString().length()];

        // random for now
        for (int j = 0; j < getTargetString().length(); j++) {
			if ((Math.random() * 2.0) <= 1.0) {
				childCArray[j] = father.getValue().toCharArray()[j];
			}
			else {
				childCArray[j] = mother.getValue().toCharArray()[j];
			}
		}

		if ((Math.random() * 2.0) <= 1.0) {
			if ((Math.random() * 2.0) <= 1.0) {
				child.setStepWidth(father.getStepWidth() * ALPHA);
			}else{
				child.setStepWidth(father.getStepWidth() / ALPHA);
			}
		}else {
			if ((Math.random() * 2.0) <= 1.0) {
				child.setStepWidth(mother.getStepWidth() * ALPHA);
			} else {
				child.setStepWidth(mother.getStepWidth() / ALPHA);
			}
		}

		child.setValue(new String(childCArray));

        return child;
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
		candidate.setValue(String.valueOf(characters));
		candidate.setQuality(Quality.function(candidate.getValue(), getTargetString()));
		//System.out.println(output);
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

		stepSizes.clear();
		for(int i = 0; i < getPopulationSize(); i++)
			stepSizes.add(1);

        ArrayList<Candidate> children = new ArrayList<>();
        ArrayList<Candidate> mutatedChildren = new ArrayList<>();
        ArrayList<Candidate> newParents = null;

		for (int i = 0; i < getIterations(); i++) {
			children.clear();
			mutatedChildren.clear();
			for (int j = 0; j < getMu(); j++) {
				newParents = rouletteWheel();
				children.add(reproductionStep(newParents.get(0), newParents.get(1)));
				mutatedChildren.add(mutationStep(children.get(j)));
			}
			newParents = selectionStep(mutatedChildren);
			setInitialConfiguration(newParents);
			if(newParents.get(0).getQuality() == 0)
				break;
		}

		for (int i = 0; i < newParents.size(); i++) {
			Candidate candidate = newParents.get(i);
			getForm().resultList.append("candidate " + (i + 1) + ": " + candidate.getValue() + " - quality " + candidate.getQuality() + "\n");
			//getForm().resultList.append( i + ": " + newParents.get(i).getValue()+"\n");
        }

        return "Found candidate '" + newParents.get(0).getValue() + "' with quality " + newParents.get(0).getQuality();
    }
	private void reset() {
		setEpsilon(getEpsilon());
		setStepSize(1.0);
		setPopulationSize(Integer.parseInt(getForm().populationSizeTextField.getText()));
		setIterations(Integer.parseInt(getForm().iterationsTextField.getText()));
		setPreviousQuality(9999);
	}
}
