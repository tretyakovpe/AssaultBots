package assault.bots;

import java.awt.Color;
import static assault.game.Constants.*;
import assault.game.Score;


public class RedBot extends Bot 
{
    //позиция для респа красных
    protected int respawnX = WORLD_SIZE - 1;
    protected int respawnY = WORLD_SIZE - 1;
    
    public RedBot(Score score) 
    {
        super(score);
        flagColor = Color.red;
    }
    
    @Override
    public void init(String name, int team, int X, int Y)
    {
        super.init("R-" + name, team, X, Y, flagColor);
        System.out.println("R-" + name+" появился в "+X+"-"+Y);
    }
    
    @Override
    public void die() 
    {
        super.die();
        score.increaseBlue();
    }
}
