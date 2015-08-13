/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assault.bots;
import assault.equipment.*;
/**
 *
 * @author pavel.tretyakov
 */
public class BotRemains extends Obstacles{
    
    private Equipment part;

    public BotRemains(Equipment part) {
        System.out.println(part.name);
        this.part=part;
    }

    public Equipment getPart() {
        return this.part;
    }

    
}
