/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assault.equipment;

import java.awt.Color;
import java.io.*;
import javax.imageio.ImageIO;

/**
 *
 * @author pavel.tretyakov
 */
    /**
     *Шасси бота.
     * @param durability запас прочности всего бота
     * @param speed скорость перемещения
     */
    public class Body extends Equipment{

        public String name;
        public int speed;
        public Color color;
        public void truck(){
                this.name = "Гусеницы";
                this.durability = 20;
                this.speed = 3;
                this.color=Color.PINK;
                try {                
                   this.image = ImageIO.read(new File("assets/trucks.gif"));
                } catch (IOException ex) {
                     // handle exception...
                }
        }
        public void wheel(){
                this.name = "Колеса";
                this.durability = 18;
                this.speed = 6;
                this.color=Color.black;
                try {                
                   this.image = ImageIO.read(new File("assets/wheels.gif"));
                } catch (IOException ex) {
                     // handle exception...
                }
        }
        public void antigrav(){
                this.name = "Антигравы";
                this.durability = 15;
                this.speed = 9;
                this.color=Color.cyan;
                try {                
                   this.image = ImageIO.read(new File("assets/antigrav.gif"));
                } catch (IOException ex) {
                     // handle exception...
                }
        }
    }
