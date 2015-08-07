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


public class Bot {

    /**
     *название бота
     */
    public String name;

    /**
     *идентификатор команды, пока не используется
     */
    public int team;

    /**
     *положение на поле по горизонтали в клетках поля
     */
    public int posX;

    /**
     *положение на поле по вертикали в клетках поля
     */
    public int posY;

    /**
     *цвет флага команды
     */
    public Color flagColor;

    /**
     *установленное шасси
     */
    public Chassis chassi = new Chassis();

    /**
     *установленное оружие
     */
    public Weapon weapon = new Weapon();

    /**
     *установленный источник питания
     */
    public Power power = new Power();

    /**
     *установленный мозг
     */
    public Comp comp = new Comp();
    
    /**
     *текущая цель
     */
    public Bot target;

    /**
     *расстояние до текущей цели
     */
    public int targetDistance;

    /**
     *флаг живости/неживости
     */
    public boolean isDead=false;

    /**
     *режим работы бота
     * 
     * 0-мертв
     * 1-поиск цели
     * 2-движение к target
     * 3-прицеливание (оценка дистанции)
     * 4-стрельба
     * 
     * 
     */
    public int botMode=1;

    /**
     * Создание бота
     * @param name
     * @param team
     * @param X
     * @param Y
     * @param color 
     */
    public void Bot(String name, int team, int X, int Y, Color color){
        this.name=name;
        this.team = team;
        this.posX=X; this.posY=Y;
        this.flagColor=color;
        this.botMode=1;
        this.target=null;
        this.targetDistance=9999;
    }
    
    /**
     * Оценить дистанцию до объекта
     * объект с минимальной дистанцией записать в цели
     * 
     * @param enemyBot цель, которую надо посмотреть
     */
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
    
    /**
     * Перемещение в сторону цели
     */
    public void move(){
         //размер одного хода. Зависит от скорости шасси
        float step = this.targetDistance/this.chassi.speed;
        //длины проекций вектора движения.
        float vectorX=this.target.posX-this.posX;
        float vectorY=this.target.posY-this.posY;
        
        //кол-во шагов по каждой оси
        float newX = this.posX+(vectorX/(step));
        float newY = this.posY+(vectorY/(step));

        //шагаем
        this.posX=(int)newX;
        this.posY=(int)newY;
        
        //System.out.println(this.name+" делает шаг в "+newX+"-"+newY);
        
        this.targetDistance=99999;
        //посмотрим, может есть кто-нть поближе
        this.botMode=1;
    }
    /**
     * Оценка возможности ведения огня по цели
     */
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

    /**
     * Стреляем
     */
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
    
    /**
     *
     */
    public void die(){
        //System.out.println(this.name+" УМЕР");
    }
    
    /**
     *Шасси бота.
     * @param durability запас прочности всего бота
     * @param speed скорость перемещения
     */
    public class Chassis{

        public String name;
        public int speed;
        public int durability;
        public Color color;
        public void truck(){
                this.name = "Гусеницы";
                this.durability = 20;
                this.speed = 1;
                this.color=Color.PINK;
        }
        public void wheel(){
                this.name = "Колеса";
                this.durability = 18;
                this.speed = 2;
                this.color=Color.black;
        }
        public void antigrav(){
                this.name = "Антигравы";
                this.durability = 15;
                this.speed = 3;
                this.color=Color.cyan;
        }
    }

    public class Weapon{
        public String name;
        public int speed;
        public int range;
        public int damage;
        public Color color;
        public void cannon(){
            this.name = "Пушка";
            this.speed = 5;
            this.range = 10;
            this.damage = 3;
            this.color=Color.green;
        }
        public void laser(){
            this.name = "Лазер";
            this.speed = 5;
            this.range = 30;
            this.damage = 1;
            this.color=Color.PINK;
        }
        public void plasma(){
            this.name = "Плазма";
            this.speed = 1;
            this.range = 15;
            this.damage = 5;
            this.color=Color.orange;
        }
    }

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
