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


public class Bot extends Obstacles{

    public String name;

    public int team;

    public int health;
    
    public Color flagColor;

    public Chassis chassi = new Chassis();
    public Weapon weapon = new Weapon();
    public Power power = new Power();
    public Comp comp = new Comp();

    public Landscape terrain;
    
    public Bot target;
    public int targetDistance;
    

    /**
     *режим работы бота
     * 
     * 0-мертв
     * 1-поиск цели
     * 2-движение к target
     * 3-прицеливание (оценка дистанции)
     * 4-стрельба
     * 5-самотестирование
     * 6-убегание
     */
    public int botMode;

    public void Bot(String name, int team, int X, int Y, Color color){
        this.name=name;
        this.team = team;
        this.posX=X; this.posY=Y;
        this.flagColor=color;
        this.botMode=5;
        this.target=null;
        this.targetDistance=9999;
    }
    
    public void see(Bot enemyBot){
        int X1 = enemyBot.posX;
        int Y1 = enemyBot.posY;
        int X = this.posX;
        int Y = this.posY;
        
        //System.out.println(this.name+" видит "+enemyBot.toString()+" в "+X1+"-"+Y1);
        
        //Вычисляем дистанцию до бота, если она меньше предыдущей, выбираем его в качестве цели.
        double distance=Math.sqrt(Math.pow((X1-X),2)+Math.pow((Y1-Y),2));
        if((int)distance <= this.targetDistance)
        {
            this.target=enemyBot;
            this.targetDistance = (int) distance;
            //посмотрим, может можно стрельнуть
            this.botMode=3;
            
            //System.out.println(this.name+" выбрал целью "+enemyBot.toString());
            
        }
    }
    public void aim(){
        if (this.weapon.range>this.targetDistance)
        {
        //System.out.println(this.name+" прицелился в "+this.target.toString());
            //Можно стрелять
            this.botMode = 4;
        }
        else
        {
        //System.out.println(this.name+" видит, что до цели далеко ");
        
            //далеко, надо шагнуть ближе
            this.botMode = 2;
        }
        
    }
    
    public void move(){
        int surface = terrain.getSurface(this.posX, this.posY);
        //размер одного хода. Зависит от скорости шасси
        float step = this.targetDistance/this.chassi.speed;
        //длины проекций вектора движения.
        float vectorX=(this.target.posX-this.posX);
        float vectorY=(this.target.posY-this.posY);
        
        //кол-во шагов по каждой оси
        float newX = this.posX+(vectorX/(step));
        float newY = this.posY+(vectorY/(step));

        //шагаем
        Object obstacle = terrain.getObstacle((int)newX, (int)newY);
        if(obstacle==null)
        {
            terrain.setObstacle(posX, posY, null);
            this.posX=(int)newX;
            this.posY=(int)newY;
            terrain.setObstacle(posX, posY, this);
        }
        
        //System.out.println(this.name+" делает шаг в "+newX+"-"+newY);
        
        this.targetDistance=99999;
        //посмотрим, может есть кто-нть поближе
        this.botMode=1;
    }

    public void shoot(){
        
        //Это будут пули. пока не используется
        //Projectile projectile;
        //projectile = new Projectile(this.posX, this.posY, this.target.posX, this.target.posY, this.weapon.speed, this.weapon.damage);
        
        //System.out.println(this.name+" стреляет.");
        
        //оружие стреляет и попадает со 100% вероятностью
        int resultOfFiring = this.target.chassi.durability-this.weapon.damage;
        if(resultOfFiring<=0)
        {
            
        //System.out.println(this.name+" убил "+this.target.toString());

            this.target.botMode=0;
            this.targetDistance=99999;
        }
        else
        {

        //System.out.println(this.name+" ранил "+this.target.toString());

            this.target.chassi.durability-=this.weapon.damage;
        }
        //после стрельбы надо оглядеться вокруг, может кто-то ближе подошел
        this.targetDistance=99999;
        this.botMode=1;

    }
    
    public void selftest(){
        
        //нужно посчитать, сколько жизни у нас осталось
        
        
        //после всех тестов, посмотрим вокруг.
        this.botMode=1;
    }
    
    public void die(){
        //System.out.println(this.name+" УМЕР");
    }
    
    /**
     *Описывает цвета каждого из режимов работы бота
     * @return
     */
    public Color getStatusColor()
    {
        Color C = Color.WHITE;
        
        switch(this.botMode){
            case 0: C=Color.black;break;
            case 1: C=Color.white;break;
            case 2: C=Color.green;break;
            case 3: C=Color.yellow;break;
            case 4: C=Color.red;break;
            case 5: C=Color.magenta;break;
        }
        return C;
    }
 
}
