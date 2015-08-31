package es.old;

/**
 *
 * @author Andrej
 */
public class EsPoint {
    
    private int x;
    private int y;
    
    // TODO: color, size for a better evolution
    public EsPoint(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
