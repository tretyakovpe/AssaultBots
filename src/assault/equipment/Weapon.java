/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assault.equipment;

import java.awt.Color;

/**
 *
 * @author pavel.tretyakov
 */
    public class Weapon extends Equipment{
        public String name;
        public int speed;
        public int range;
        public int damage;
        public Color color;
        public void cannon(){
            this.name = "Пушка";
            this.speed = 5;
            this.range = 3;
            this.damage = 3;
            this.color=Color.green;
        }
        public void laser(){
            this.name = "Лазер";
            this.speed = 5;
            this.range = 7;
            this.damage = 1;
            this.color=Color.PINK;
        }
        public void plasma(){
            this.name = "Плазма";
            this.speed = 1;
            this.range = 4;
            this.damage = 5;
            this.color=Color.orange;
        }
    }
