import com.sun.source.tree.DoWhileLoopTree;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static jdk.internal.misc.VM.initLevel;

public class Model extends JPanel  implements ActionListener {
    private Dimension d;
    private final Font smallFont = new Font("Arial", Font.BOLD ,14);
    private boolean inGame = false;
    private boolean dying = false;
    private final int BLOCK_SIZE = 24;
    private final int N_BLOCK = 15;
    private final int SCREEN_SIZE = N_BLOCK* BLOCK_SIZE;
    private final int MAX_GHOSTS = 12;
    private final int speed_pacman =6;
    private int N_GHOSTS = 6;
    private int live, score;
    private int []dx,dy;
    private int [] ghost_x, ghost_y, ghost_dx, ghost_dy, ghostSpeed;
    private Image heart, ghost;
    private Image UP,DOWN,LEFT, RIGHT;
    private  int pacman_x, pacman_y,pacman_dx,pacman_dy;
    private int req_dx, req_dy;
    private final int validSpeed [] = {1,2,3,4,6,8};
    private final int maxSpeed =6;
    private int currentSpeed = 3;
    private short [] screenData;
    private Timer timer;
    private final short levelData[] ={
            19,18,18,18,18,18,18,18,18,18,18,18,18,18,22,// 0 = barrier ;1 = left border; 2 = top border; 4 = right border
            17,16,16,16,16,24,16,16,16,16,16,16,16,16,20,// 8 = bottom border; 16 = white dots
            25,24,24,24,28,0 ,17,16,16,16,16,16,16,16,20,
            0 ,0 ,0 ,0 ,0 ,0 ,17,16,16,16,16,16,16,16,20,
            19,18,18,18,18,18,16,16,16,16,24,24,24,24,20,
            17,16,16,16,16,16,16,16,16,20,0 ,0 ,0 ,0 ,21,
            17,16,16,16,16,16,16,16,16,20,0 ,0 ,0 ,0 ,21,
            17,16,16,16,24,16,16,16,16,20,0 ,0 ,0 ,0 ,21,
            17,16,16,20,0 ,17,16,16,16,16,18,18,18,18,20,
            17,24,24,28,0 ,25,24,24,16,16,16,16,16,16,20,
            21,0 ,0 ,0 ,0 ,0 ,0 ,0 ,17,16,16,16,16,16,20,
            17,18,18,22,0 ,19,18,18,16,16,16,16,16,16,20,
            17,16,16,20,0 ,17,16,16,16,16,16,16,16,16,20,
            17,16,16,20,0 ,17,16,16,16,16,16,16,16,16,20,
            25,24,24,24,26,24,24,24,24,24,24,24,24,24,28,
    };
    public  Model()
    {
        loadImages();
        initVariables();
        addKeyListener(new TAdapter());
        setFocusable(true);
        initGame();
    }
    private void loadImages(){
        DOWN = new ImageIcon("C:/Users/This-PC/Downloads/Pacman/images/down.gif").getImage();
        LEFT = new ImageIcon("C:/Users/This-PC/Downloads/Pacman/images/left.gif").getImage();
        RIGHT = new ImageIcon("C:/Users/This-PC/Downloads/Pacman/images/right.gif").getImage();
        UP = new ImageIcon("C:/Users/This-PC/Downloads/Pacman/images/up.gif").getImage();
        ghost = new ImageIcon("C:/Users/This-PC/Downloads/Pacman/images/ghost.gif").getImage();
        heart = new ImageIcon("C:/Users/This-PC/Downloads/Pacman/images/heart.gif").getImage();
    }
    private  void initVariables()
    {
        screenData = new short[N_BLOCK* N_BLOCK];
        d = new Dimension(400,400);
        ghost_x = new int[MAX_GHOSTS];
        ghost_dx = new int[MAX_GHOSTS];
        ghost_y = new int[MAX_GHOSTS];
        ghost_dy = new int[MAX_GHOSTS];
        ghostSpeed = new int [MAX_GHOSTS];
        dx = new int[4];
        dy = new int[4];
        timer = new Timer(40, this);
        timer.restart();
    }
    public void  initGame()
    {
        live = 3;
        score = 0;
        initLevel();
        N_GHOSTS = 6;
        currentSpeed =3;
    }

    private void playGame(Graphics)
    {

    }
    private void initLevel(){
        int i;
        for(i =0; i < N_BLOCK*N_BLOCK;i++)
        {
            screenData[i] = levelData[i];
        }
    }
    private void continueLevel()
    {
        int dx=1;
        int random;
        for(int i =0; i < N_GHOSTS;i++)
        {
            ghost_y[i] = 4*BLOCK_SIZE;
            ghost_x[i] = 4*BLOCK_SIZE;
            ghost_dy[i] =0;
            ghost_dx[i] = dx;
            dx = -dx;
            random = (int)((Math.random() * (currentSpeed+1)));
            if(random > currentSpeed)
            {
                random = currentSpeed;
            }

            ghostSpeed[i] = validSpeed[random];
        }
        pacman_x = 7*BLOCK_SIZE;
        pacman_y = 11*BLOCK_SIZE;
        pacman_dx = 0;
        pacman_dy = 0;
        req_dx = 0;
        req_dy = 0;
        dying =false;
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLUE);
        g2d.fillRect(0,0,d.width,d.height);
        drawMaze(g2d);
        drawScore(g2d);
        if(inGame)
        {
            playGame(g2d);
        }else
        {
            showIntroScreen(g2d);
        }
        Toolkit.getDefaultToolkit().sync();
    }
    class TAdapter extends KeyAdapter{
        public void keyPressd(KeyEvent e)
        {
            int key = e.getKeyCode();
            if(inGame)
            {
                if(key == KeyEvent.VK_LEFT){
                    req_dx = -1;
                    req_dy = 0;
                }
                if(key == KeyEvent.VK_RIGHT){
                    req_dx = 1;
                    req_dy = 0;
                }
                if(key == KeyEvent.VK_UP){
                    req_dx = 0;
                    req_dy = 1;
                }
                if(key == KeyEvent.VK_DOWN){
                    req_dx = 0;
                    req_dy = -1;
                }
                if(key == KeyEvent.VK_ESCAPE && timer.isRunning()){
                    inGame = false;

                }
            }
            else{
                if(key == KeyEvent.VK_SPACE)
                {
                    inGame = true;
                    initGame();
                }
            }
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
