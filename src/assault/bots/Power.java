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
    public class Power{
        public String name;
        public int power;
        public int durability;

        public void nuclearReactor() {
            this.name = "Ядерный реактор";
            this.power = 100;
            this.durability = 10;
        }
        public void dieselEngine(){
            this.name = "Дизель-генератор";
            this.power = 10;
            this.durability = 100;
        }
        public void tousandChinese(){
            this.name = "Тысяча китайцев";
            this.power = 50;
            this.durability = 50;
        }
    }
