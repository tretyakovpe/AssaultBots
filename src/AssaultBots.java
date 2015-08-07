import assault.bots.Obstacles;
import assault.bots.Bot;
import assault.bots.Landscape;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
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
	
	public final static int windowWidth = 800;
	public final static int windowHeight = 850;
	public static String NAME = "Assault Bots";
	
        
        public int cellSize=12; //размер ячейки игрового поля
        public int worldSize=windowWidth/cellSize; //размер игрового поля в ячейках
        public int optionArmySize = 5; //кол-во ботов в команде
        
        public Bot[] armyBlue = new Bot[optionArmySize];
        public Bot[] armyRed = new Bot[optionArmySize];
        
        public Landscape land = new Landscape(worldSize,worldSize);
        
        public int respawnBlueX=1,respawnBlueY=1; //позиция для респа синих
        public int respawnRedX=worldSize-2,respawnRedY=worldSize-2; //позиция для респа красных
        
        private int scoreBlue;
        private int scoreRed;
        
        
        private boolean leftPressed = false;
	private boolean rightPressed = false;
	
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
		}
	}
	
	public void init() {
		addKeyListener(new KeyInputHandler());
                landscapeInit();
                for (int i=0; i<optionArmySize; i++)
                {
                    armyBlue[i] = new Bot();
                    spawnBlue(i);
                    armyRed[i] = new Bot();
                    spawnRed(i);
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
                    
                for(int i=0; i<optionArmySize; i++)
                {
                    drawBot(g, armyBlue[i]);
                    drawBot(g,armyRed[i]);
                }
                drawHud(g);
                g.dispose();
                bs.show();
                }

        private void drawLandscape(Graphics g){
            for(int y=0; y<worldSize; y++)
            {
                for(int x=0; x<worldSize; x++)
                {
                    int surface = land.getSurface(x, y);
                    int[][] Surface = land.getWholeSurface();
                    float hue,saturation,brightness;
                    hue = 0.4f;
                    saturation = 0.5f;
                    brightness = (float) Surface[x][y]*0.1f;
                    
                    Color C = Color.getHSBColor(hue,saturation,brightness);
                    g.setColor(C);
                    g.fillRect(x*cellSize, y*cellSize, cellSize, cellSize);
                }
            }
        }
        
	private void drawBot(Graphics g, Bot bot)
        {
            //Дальность оружия
/*            g.setColor(bot.weapon.color);
            g.drawOval((bot.posX*cellSize)-(bot.weapon.range*cellSize)/2, (bot.posY*cellSize)-(bot.weapon.range*cellSize)/2, bot.weapon.range*cellSize, bot.weapon.range*cellSize);
*/      //Цвет команды
            g.setColor(bot.flagColor);
            g.fillRoundRect(bot.posX*cellSize, bot.posY*cellSize, cellSize, cellSize,5,5);
            g.setColor(Color.white);
            g.drawRoundRect(bot.posX*cellSize, bot.posY*cellSize, cellSize, cellSize,5,5);
        //Статус
            g.setColor(bot.getStatusColor());
            g.fillOval(bot.posX*cellSize+5, bot.posY*cellSize+5,9,9);
            g.setColor(Color.white);
            g.drawOval(bot.posX*cellSize+5, bot.posY*cellSize+5,9,9);

        //Стрельба
            if(bot.botMode==4)
            {
                g.setColor(bot.flagColor);
                g.drawLine(bot.posX*cellSize+5, bot.posY*cellSize+5, bot.target.posX*cellSize+5, bot.target.posY*cellSize+5);
            }
            
/*            //Целеуказатель
            g.setColor(bot.flagColor);
            if (bot.target!=null)
            {
                g.drawLine(bot.posX*cellSize, bot.posY*cellSize, bot.target.posX*cellSize, bot.target.posY*cellSize);
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
            g.drawString(String.valueOf(scoreBlue), 20, 830);
            
            g.setColor(armyRed[0].flagColor);
            g.fillRect(120, 810, 100, 30);
            g.setColor(Color.yellow);
            g.drawString(String.valueOf(scoreRed), 130, 830);
            
        }
        
	public void update(long delta) {
            
            for(int i=0; i<optionArmySize; i++)
            {
                for(int j=0; j<optionArmySize; j++)
                {
                    switch (armyBlue[i].botMode)
                    {
                        case 0: armyBlue[i].die(); scoreRed++; spawnBlue(i);break;
                        case 1: armyBlue[i].see(armyRed[j]);break;
                        case 2: armyBlue[i].move();break;
                        case 3: armyBlue[i].aim();break;
                        case 4: armyBlue[i].shoot();break;
                        case 5: armyBlue[i].selftest();break;
                    }

                    switch (armyRed[i].botMode)
                    {
                        case 0: armyRed[i].die(); scoreBlue++; spawnRed(i);break;
                        case 1: armyRed[i].see(armyBlue[j]);break;
                        case 2: armyRed[i].move();break;
                        case 3: armyRed[i].aim();break;
                        case 4: armyRed[i].shoot();break;
                        case 5: armyRed[i].selftest();break;
                    }
                }
            }
            
            try {
                Thread.sleep(delta);
            } catch (InterruptedException ex) {
                Logger.getLogger(AssaultBots.class.getName()).log(Level.SEVERE, null, ex);
            }
	}
        public void spawnBlue(int i){
            Random r = new Random();
            int posX = r.nextInt(optionArmySize)+respawnBlueX;
            int posY = r.nextInt(optionArmySize)+respawnBlueY;
            armyBlue[i].Bot("B-"+i, 0, posX, posY, Color.blue);
            armyBlue[i].terrain = land;
            switch (r.nextInt(3)){
                case 0:
                    armyBlue[i].chassi.truck();break;
                case 1:
                    armyBlue[i].chassi.wheel();break;
                case 2:
                    armyBlue[i].chassi.antigrav();break;
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
            switch (r.nextInt(3)){
                case 0:
                    armyRed[i].chassi.truck();break;
                case 1:
                    armyRed[i].chassi.wheel();break;
                case 2:
                    armyRed[i].chassi.antigrav();break;
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

        private void landscapeInit(){
            Random random = new Random();
            for(int y=0; y<worldSize; y++)
            {
                for(int x=0; x<worldSize; x++)
                {
                    int r=random.nextInt(5)+1;
                    land.setSurface(x, y, r);
                }
            }
        }
        
	public static void main(String[] args) {
		AssaultBots game = new AssaultBots();
		game.setPreferredSize(new Dimension(windowWidth, windowHeight));
		JFrame frame = new JFrame(AssaultBots.NAME);
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