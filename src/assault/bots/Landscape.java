/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assault.bots;

/**
 *
 * @author pavel.tretyakov
 */
public class Landscape {

    private int x;
    private int y;
    private int[][] surface;
    private Object[][] obstacles;

    public Landscape(int width, int height){
        this.x=width;
        this.y=height;
        this.surface = new int[x][y];
        this.obstacles= new Object[x][y];
    }
            
    public Object getObstacle(int x, int y) {
        return obstacles[x][y];
    }

    public int getSurface(int x, int y) {
        return surface[x][y];
    }
    
    public int[][] getWholeSurface(){
        return this.surface;
    }

    public void setSurface(int x, int y, int surface) {
        this.surface[x][y] = surface;
    }

    public void setObstacle(int x, int y, Object obstacle) {
        this.obstacles[x][y] = obstacle;
    }
    
}
