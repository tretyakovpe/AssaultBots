package assault.game;


public final class Constants 
{
    private Constants() {}
    public static String NAME = "Assault Bots";
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 850;
    
    public static final int CELL_SIZE = 15; //размер ячейки игрового поля
    //размер игрового поля в ячейках
    public static final int WORLD_SIZE = WINDOW_HEIGHT / CELL_SIZE;
}
