package assault.game;


public class Score
{

    public Score() {
        this.scoreBlue = 0;
        this.scoreRed = 0;
    }
    private int scoreBlue;
    private int scoreRed;

    public int getScoreBlue()
    {
        return scoreBlue;
    }

    public int getScoreRed()
    {
        return scoreRed;
    }

    public void setScoreBlue(int scoreBlue)
    {
        this.scoreBlue = scoreBlue;
    }

    public void setScoreRed(int scoreRed)
    {
        this.scoreRed = scoreRed;
    }
    
    public void increaseBlue()
    {
        scoreBlue++;
    }
    
    public void increaseRed()
    {
        scoreRed++;
    }
}
