/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assault.equipment;

/**
 *
 * @author pavel.tretyakov
 */

    public class Comp{
        public String name;
        public int viewDiastance;
        public void deskComp(){
            this.name="Десктоп";
            this.viewDiastance=3;
        }
        public void militaryComp(){
            this.name="Военный компьютер";
            this.viewDiastance=5;
        }
        public void nasaComp(){
            this.name="Спутниковое управление";
            this.viewDiastance=15;
        }
    }
