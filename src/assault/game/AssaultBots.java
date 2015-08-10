package assault.game;
import assault.bots.Bot;
import assault.bots.BlueBot;
import assault.bots.RedBot;
import assault.bots.Landscape;
import assault.bots.Tower;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import static assault.game.Constants.*;

/**
 * 
 * @author pavel.tretyakov
 * 
 * Правила: две команды ботов. Каждый бот состоит из шасси, оружия, источника энергии,
 * и мозгов. 
 * Реализовано: шасси, оружие.
 * Каждый бот в процессе игры волен выбирать себе цель и решать, двигаться к ней, стрелять в неё,
 * убегать от неё, ждать в засаде и т.д.
 * Реализовано: находим ближайшего противника и бежим к нему, по пути смотрим более близкие цели
 * при достижении дистанции выстрела - стреляем, что есть мочи.
 * 
 * 
 */
public class AssaultBots extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	
	private boolean running;

        public int optionArmySize = 2; //кол-во ботов в команде	

        public Bot[] armyBlue = new Bot[optionArmySize];
        public Bot[] armyRed = new Bot[optionArmySize];
        
        public Landscape land = new Landscape(WORLD_SIZE,WORLD_SIZE);
        
        public Tower tower;
        
        public int respawnBlueX=1,respawnBlueY=1; //позиция для респа синих
        public int respawnRedX=WORLD_SIZE-2,respawnRedY=WORLD_SIZE-2; //позиция для респа красных
        
        private Score score;
        
        private boolean leftPressed = false;
	private boolean rightPressed = false;

    public AssaultBots() {
    }
	
	public void start() {
		running = true;
                new Thread(this).start();
	}
	
        @Override
	public void run() {
		long lastTime = System.currentTimeMillis();
		long delta;
		init();
		
		
		while(running) {
			delta = 250L; //System.currentTimeMillis() - lastTime;
			lastTime = System.currentTimeMillis();	
			update(delta);
			render();
                        
                        //running=false;
                        
                        
                        
		}
	}
	
	public void init() {
		addKeyListener(new KeyInputHandler());
                landscapeInit();
                Bot.terrain = land;                    
                towerSetup();
                for (int i=0; i<optionArmySize; i++)
                {
                    armyBlue[i] = new BlueBot(score);
                    armyBlue[i].spawn(i);
                    armyRed[i] = new RedBot(score);
                    armyRed[i].spawn(i);
                }
        }
	
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			requestFocus();
			return;
		}
		Graphics g = bs.getDrawGraphics(); 
                drawLandscape(g);
                drawTower(g);
                    
                for(int i=0; i<optionArmySize; i++)
                {
                    drawBot(g, armyBlue[i]);
                    drawBot(g,armyRed[i]);
                    drawShoot(g, armyBlue[i]);
                    drawShoot(g,armyRed[i]);
                }
                drawHud(g);
                g.dispose();
                bs.show();
                }

        private void drawTower(Graphics g){
            g.setColor(Color.yellow);
            g.fillOval(tower.posX*CELL_SIZE, tower.posY*CELL_SIZE,CELL_SIZE,CELL_SIZE);
            g.setColor(Color.red);
            g.drawOval(tower.posX*CELL_SIZE+2, tower.posY*CELL_SIZE+2,CELL_SIZE-4,CELL_SIZE-4);
        }

        private void drawLandscape(Graphics g){
            for(int y=0; y<WORLD_SIZE; y++)
            {
                for(int x=0; x<WORLD_SIZE; x++)
                {
                    int surface = land.getSurface(x, y);
                    Object obstacle = land.getObstacle(x, y);
                    int[][] Surface = land.getWholeSurface();
                    
                    
                    float hue,saturation,brightness;
                    hue = 0.4f;
                    saturation = 0.5f;
                    brightness = (float) surface*0.025f+0.1f;
                    
                    Color C = Color.getHSBColor(hue,saturation,brightness);
                    g.setColor(C);
                    g.fillRect(x*CELL_SIZE, y*CELL_SIZE, CELL_SIZE, CELL_SIZE);

                    if (obstacle!=null)
                    {
                        g.setColor(Color.BLACK);
                        g.fillRect(x*CELL_SIZE, y*CELL_SIZE, CELL_SIZE, CELL_SIZE);
                    }
                }
            }
        }
        
	private void drawBot(Graphics g, Bot bot)
        {
            //Дальность оружия
/*            g.setColor(bot.weapon.color);
            g.drawOval((bot.posX*CELL_SIZE)-(bot.weapon.range*CELL_SIZE)/2, (bot.posY*CELL_SIZE)-(bot.weapon.range*CELL_SIZE)/2, bot.weapon.range*CELL_SIZE, bot.weapon.range*CELL_SIZE);
*/      //Цвет команды
            g.setColor(bot.flagColor);
            g.fillRoundRect(bot.posX*CELL_SIZE, bot.posY*CELL_SIZE, CELL_SIZE, CELL_SIZE,5,5);
            g.setColor(Color.white);
            g.drawRoundRect(bot.posX*CELL_SIZE, bot.posY*CELL_SIZE, CELL_SIZE, CELL_SIZE,5,5);
        //Статус
            g.setColor(bot.getStatusColor());
            g.fillOval(bot.posX*CELL_SIZE+5, bot.posY*CELL_SIZE+5,9,9);
            g.setColor(Color.white);
            g.drawOval(bot.posX*CELL_SIZE+5, bot.posY*CELL_SIZE+5,9,9);
        }
        
        private void drawShoot(Graphics g, Bot bot){
        //Стрельба
            if(bot.botMode==4)
            {
                g.setColor(bot.flagColor);
                g.drawLine(bot.posX*CELL_SIZE+5, bot.posY*CELL_SIZE+5, bot.target.posX*CELL_SIZE+5, bot.target.posY*CELL_SIZE+5);
                g.fillOval(bot.target.posX*CELL_SIZE+5, bot.target.posY*CELL_SIZE+5,20,20);
            }
            
/*            //Целеуказатель
            g.setColor(bot.flagColor);
            if (bot.target!=null)
            {
                g.drawLine(bot.posX*CELL_SIZE, bot.posY*CELL_SIZE, bot.target.posX*CELL_SIZE, bot.target.posY*CELL_SIZE);
            }
*/            
        }
        /**
         * Рисует панельку с элементами информации
         * @param g графический контекст
         */
        private void drawHud(Graphics g){
         
            //панелька
            g.setColor(Color.white);
            g.fillRect(0, 800, 800, 50);
            
            //пишем счёт команд
            g.setColor(armyBlue[0].flagColor);
            g.fillRect(10, 810, 100, 30);
            g.setColor(Color.yellow);
            //g.drawString(String.valueOf(score.getScoreBlue()), 20, 830);
            
            g.setColor(armyRed[0].flagColor);
            g.fillRect(120, 810, 100, 30);
            g.setColor(Color.yellow);
            //g.drawString(String.valueOf(score.getScoreRed()), 130, 830);
            
        }
        
	public void update(long delta) {
            
            for(int i=0; i<optionArmySize; i++)
            {
                armyBlue[i].doAction(armyRed, i);
                armyRed[i].doAction(armyBlue, i);
            }
            
            try {
                Thread.sleep(delta);
            } catch (InterruptedException ex) {
                Logger.getLogger(AssaultBots.class.getName()).log(Level.SEVERE, null, ex);
            }
	}
/*        public void spawnBlue(int i){
            Random r = new Random();
            int posX = r.nextInt(optionArmySize)+respawnBlueX;
            int posY = r.nextInt(optionArmySize)+respawnBlueY;
            armyBlue[i].Bot("B-"+i, 0, posX, posY, Color.blue);
            armyBlue[i].terrain = land;
            land.setObstacle(posX, posY, armyBlue[i]);
            switch (r.nextInt(3)){
                case 0:
                    armyBlue[i].body.truck();break;
                case 1:
                    armyBlue[i].body.wheel();break;
                case 2:
                    armyBlue[i].body.antigrav();break;
            }
            switch (r.nextInt(3)){
                case 0:
                    armyBlue[i].weapon.cannon();break;
                case 1:
                    armyBlue[i].weapon.laser();break;
                case 2:
                    armyBlue[i].weapon.plasma();break;
            }
        }

        public void spawnRed(int i){
            Random r = new Random();
            int posX = respawnRedX-r.nextInt(optionArmySize);
            int posY = respawnRedY-r.nextInt(optionArmySize);
            armyRed[i].Bot("R-"+i, 1, posX, posY, Color.red);
            armyRed[i].terrain=land;
            land.setObstacle(posX, posY, armyRed[i]);
            switch (r.nextInt(3)){
                case 0:
                    armyRed[i].body.truck();break;
                case 1:
                    armyRed[i].body.wheel();break;
                case 2:
                    armyRed[i].body.antigrav();break;
            }
            switch (r.nextInt(3)){
                case 0:
                    armyRed[i].weapon.cannon();break;
                case 1:
                    armyRed[i].weapon.laser();break;
                case 2:
                    armyRed[i].weapon.plasma();break;
            }
        }
*/
        private void landscapeInit(){
            Random random = new Random();
            for(int y=0; y<WORLD_SIZE; y++)
            {
                for(int x=0; x<WORLD_SIZE; x++)
                {
                    int r=random.nextInt(5)+1;
                    land.setSurface(x, y, r);
                }
            }
        }
        
        private void towerSetup(){
            Random r = new Random();
            int posX = r.nextInt(WORLD_SIZE);
            int posY = r.nextInt(WORLD_SIZE);
            tower = new Tower(posX, posY);
            land.setObstacle(posX, posY, tower);
        }
        
	public static void main(String[] args) {
		AssaultBots game = new AssaultBots();
		game.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		JFrame frame = new JFrame(NAME);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(game, BorderLayout.CENTER);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
		game.start();
	}
	
	private class KeyInputHandler extends KeyAdapter {
                @Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				leftPressed = true;
                                running=true;
                                
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				rightPressed = true;
                                running=false;
                                
			}
		} 
		
                @Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				leftPressed = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				rightPressed = false;
			}
		}
	}
}