package servidor;

/**
 * Classe para identificar onde foram colocadas as diferentes fichas.
 * 
 * @author jncfa, srpg
 */
public class Point {
    private int x;
    private int y;

    public Point() {
        this.x = 0;
        this.y = 0;
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}