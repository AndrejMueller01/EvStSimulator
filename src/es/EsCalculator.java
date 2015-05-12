package es;

import java.util.ArrayList;

/**
 *
 * @author Andrej
 */
public class EsCalculator {

    private ArrayList<EsPoint> parents;
    private ArrayList<EsPoint> children;
    private int outputPanelWidth;
    private int outputPanelHeight;
    
    private EsPoint targetPoint;
    private int sizeOfPopulation;
    
    public EsCalculator() {
    }

    public EsCalculator(int outputPanelWidth, int outputPanelHeight, EsPoint targetPoint, int sizeOfPopulation) {
        this.targetPoint = targetPoint;
        this.sizeOfPopulation = sizeOfPopulation;
        this.outputPanelWidth = outputPanelWidth;
        this.outputPanelHeight = outputPanelHeight;
        
        parents = new ArrayList<>();
        children = new ArrayList<>();
    }

    public void init() {
        setInitialPopulation();
    }
    
    public void step(){
        // TODO: calculate sizeOfPopulation * 10 children with random parents
        // TODO: choose the "best" sizeOfPopulation children
        // TODO optional: stepsize etc
        // TODO: find a proper stop criterium
    }
    
    private void setInitialPopulation() {

        for (int i = 0; i < sizeOfPopulation; i++) {
            int x = (int) (Math.random()* outputPanelWidth);
            int y = (int) (Math.random() * outputPanelHeight);
            parents.add(new EsPoint(x, y));
        }
        
    }
    
    public ArrayList<EsPoint> getParents(){
        return parents;
    }
   
}
