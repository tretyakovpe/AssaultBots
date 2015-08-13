/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assault.equipment;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author pavel.tretyakov
 */
    public class Power extends Equipment{
        public String name;
        public int power;

        public void nuclearReactor() {
            this.name = "Ядерный реактор";
            this.power = 100;
            this.durability = 10;
            try {                
               this.image = ImageIO.read(new File("assets/nuclear.gif"));
            } catch (IOException ex) {
                 // handle exception...
            }
        }
        public void dieselEngine(){
            this.name = "Дизель-генератор";
            this.power = 10;
            this.durability = 100;
            try {                
               this.image = ImageIO.read(new File("assets/diesel.gif"));
            } catch (IOException ex) {
                 // handle exception...
            }
        }
        public void tousandChinese(){
            this.name = "Тысяча китайцев";
            this.power = 50;
            this.durability = 50;
            try {                
               this.image = ImageIO.read(new File("assets/chinese.gif"));
            } catch (IOException ex) {
                 // handle exception...
            }
        }
    }
