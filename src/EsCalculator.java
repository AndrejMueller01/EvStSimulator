package es;

import java.util.ArrayList;

/**
 *
 * @author Andrej
 */
public class EsCalculator {

    private ArrayList<EsPoint> parents;
    private ArrayList<EsPoint> children;
    private ArrayList<Double> distances;
    private int outputPanelWidth;
    private int outputPanelHeight;

    private EsPoint targetPoint;
    private EsPoint alphaMale;
    private int sizeOfPopulation;
    
    private double step_width = 1;
    
    private final double ALPHA = 1.05;
    private final int DISTANCE_THRESHOLD = 20;
    private final int ALPHA_MALE_MODE = 1;
    private final int CHILDREN_MULT = 10;

    public EsCalculator() {
    }

    public EsCalculator(int outputPanelWidth, int outputPanelHeight, EsPoint targetPoint, int sizeOfPopulation) {
        this.targetPoint = targetPoint;
        this.sizeOfPopulation = sizeOfPopulation;
        this.outputPanelWidth = outputPanelWidth;
        this.outputPanelHeight = outputPanelHeight;

        parents = new ArrayList<>();
        children = new ArrayList<>();
        distances = new ArrayList<>();
    }

    public void init() {
        setInitialPopulation();
    }

    private EsPoint reproduce(EsPoint a, EsPoint b) {
        double xWithStepSize = ((a.getX()*step_width + b.getX()) / 2.0 );
        double yWithStepSize = ((a.getY() + b.getY()) / 2.0 );
        
        return new EsPoint((int)xWithStepSize, (int)yWithStepSize);
    }

    private boolean calculateChildren(int mode) {
        boolean convergence = false;
        if (mode == ALPHA_MALE_MODE) {
            convergence = findAlphaMale();

            for (int i = 0; i < parents.size() * CHILDREN_MULT; i++) {
                int randomIndex = (int) (Math.random() * parents.size());
                children.add(reproduce(alphaMale, parents.get(randomIndex)));
                distances.add(getPointDistance(alphaMale, parents.get(randomIndex)));
            }
        } else {
            // TODO
        }
        return convergence;
    }

    public void step() {
        // TODO: calculate sizeOfPopulation * 10 children with random parents
        if (calculateChildren(ALPHA_MALE_MODE)) {
            // TODO: choose the "best" sizeOfPopulation children
            parents.clear();
            
            if(Math.random() > 0.5)
                step_width *= ALPHA;
            else
                step_width /= ALPHA;
            
            for (int i = 0; i < sizeOfPopulation; i++) {
                parents.add(children.get(i));
            }
            children.clear();
        }
        // TODO optional: stepsize etc
        // TODO: find a proper stop criterium

    }

    private boolean findAlphaMale() {
        double distance = 1000;
        double maxDistance = -1;
        for (EsPoint parent : parents) {
            double tempDistance = getPointDistance(targetPoint, parent);
            if (distance > tempDistance) {
                maxDistance += tempDistance;
                distance = tempDistance;
                alphaMale = parent;
            }
        }
        if(maxDistance <= DISTANCE_THRESHOLD)
            return false;
        return true;
    }

    private double getPointDistance(EsPoint a, EsPoint b) {
        double result = Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
        return result;
    }

    private void setInitialPopulation() {

        for (int i = 0; i < sizeOfPopulation; i++) {
            int x = (int) (Math.random() * outputPanelWidth);
            int y = (int) (Math.random() * outputPanelHeight);
            parents.add(new EsPoint(x, y));
        }
    }

    public ArrayList<EsPoint> getParents() {
        return parents;
    }

}
