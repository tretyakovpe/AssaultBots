/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assault.bots;

import java.awt.Color;

/**
 *
 * @author pavel.tretyakov
 */
    /**
     *Шасси бота.
     * @param durability запас прочности всего бота
     * @param speed скорость перемещения
     */
    public class Chassis{

        public String name;
        public int speed;
        public int durability;
        public Color color;
        public void truck(){
                this.name = "Гусеницы";
                this.durability = 20;
                this.speed = 1;
                this.color=Color.PINK;
        }
        public void wheel(){
                this.name = "Колеса";
                this.durability = 18;
                this.speed = 1;
                this.color=Color.black;
        }
        public void antigrav(){
                this.name = "Антигравы";
                this.durability = 15;
                this.speed = 1;
                this.color=Color.cyan;
        }
    }
