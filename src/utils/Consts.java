package utils;
public class Consts {
    // ----------- img path ------------

    public final static String MAP_FOLDER = "./resources/map/";
    public final static String IMG_FOLDER = "./resources/img/";

    public final static String DESTR_BLK_IMG_PATH = IMG_FOLDER + "break_brick.jpg";
    public final static ImagePanel DESTR_BLK_IMG = new ImagePanel(DESTR_BLK_IMG_PATH);
    public final static String SOLID_BLK_IMG_PATH= IMG_FOLDER + "solid_brick.jpg";
    public final static ImagePanel SOLID_BLK_IMG = new ImagePanel(SOLID_BLK_IMG_PATH);
    public final static String EMPTY_BLK_IMG_PATH = IMG_FOLDER + "empty_brick.jpg";
    public final static ImagePanel EMPTY_BLK_IMG = new ImagePanel(EMPTY_BLK_IMG_PATH);

    public final static String T1_LEFT_IMG_PATH = IMG_FOLDER + "player1_tank_left.png";
    public final static ImagePanel T1_LEFT_IMG = new ImagePanel(T1_LEFT_IMG_PATH);
    public final static String T1_RIGHT_IMG_PATH = IMG_FOLDER + "player1_tank_right.png";
    public final static ImagePanel T1_RIGHT_IMG = new ImagePanel(T1_RIGHT_IMG_PATH);
    public final static String T1_UP_IMG_PATH = IMG_FOLDER + "player1_tank_up.png";
    public final static ImagePanel T1_UP_IMG = new ImagePanel(T1_UP_IMG_PATH);
    public final static String T1_DOWN_IMG_PATH = IMG_FOLDER + "player1_tank_down.png";
    public final static ImagePanel T1_DOWN_IMG = new ImagePanel(T1_DOWN_IMG_PATH);
    public final static String T2_LEFT_IMG_PATH = IMG_FOLDER + "player2_tank_left.png";
    public final static ImagePanel T2_LEFT_IMG = new ImagePanel(T2_LEFT_IMG_PATH);
    public final static String T2_RIGHT_IMG_PATH = IMG_FOLDER + "player2_tank_right.png";
    public final static ImagePanel T2_RIGHT_IMG = new ImagePanel(T2_RIGHT_IMG_PATH);
    public final static String T2_UP_IMG_PATH = IMG_FOLDER + "player2_tank_up.png";
    public final static ImagePanel T2_UP_IMG = new ImagePanel(T2_UP_IMG_PATH);
    public final static String T2_DOWN_IMG_PATH = IMG_FOLDER + "player2_tank_down.png";
    public final static ImagePanel T2_DOWN_IMG = new ImagePanel(T2_DOWN_IMG_PATH);

    public final static String BULLET_IMG_PATH = IMG_FOLDER + "bullet.png";
    public final static ImagePanel BULLET_IMG = new ImagePanel(BULLET_IMG_PATH);
    // ----------- render setting  ------------

    public final static int INIT_X_PIX = 500;
    public final static int INIT_Y_PIX = 500;

    public final static int FRAME_RATE = 140;
    public final static int FRAME_INTERV = (int)Math.round(1000.0 / FRAME_RATE);

    // ----------- game elements setting  ------------

    public final static int INIT_TANK_HP = 50;
    public final static int INIT_TANK_DAMAGE = 4; // damage per hit
    public final static float TANK_OFFSET_TO_GUN = 0.23f;
    public final static float TANK_COLLIDE_BOUNCE_DIST = .4f;
    public final static float TANK_SIZE_RATIO = .8f;

    public final static float INIT_TANK_SPEED = 1.25f; // blopcks per second

    public final static int INIT_DESTR_BLK_HP = 16;
    
    // ----------- weapon setting  ------------
    public final static int INIT_BULLET_DAMAGE = 8;
    public final static float INIT_BULLET_SPEED = 7f;
    public final static Coord INIT_BULLET_SIZE = new Coord(.2f, .2f);
    public final static int INIT_BULLET_FIRE_INTERV = 300; // 300 ms


    // ------------ networking settings ------------
    public final static String SERV_IP = "172.16.101.142";
    public final static int MOVABLE_POS_UPD_INTERV = 20; // ms
}   
