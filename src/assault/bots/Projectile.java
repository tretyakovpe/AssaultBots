/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assault.bots;

import java.awt.Graphics;

/**
 *
 * @author pavel.tretyakov
 */
public class Projectile {

    public int posX;
    public int posY;
    public int destX;
    public int destY;
    
    public int speed;
    public int damage;
    
    public Projectile(int posX, int posY, int destX, int destY, int speed, int damage) {
        this.posX = posX;
        this.posY = posY;
        this.destX = destX;
        this.destY = destY;
        this.speed = speed;
        this.damage = damage;
    }
    
    public void draw(Graphics g){
        g.fillOval(posX, posX, 1, 1);
    }
            
}
