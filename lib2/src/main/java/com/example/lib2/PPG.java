package com.example.lib2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class PPG extends JPanel implements KeyListener, ActionListener {
    private int SCREEN_WIDTH;
    private int SCREEN_HEIGHT;

    private int InitialPad_Width = 20;
    private int InitialPad_Height = 100;
    final int PLAYER_NUM = 2;
    final int PAD_OFFSET = 12;
    int playerSpeedY = 20;
    int[] playerPosX = new int[PLAYER_NUM];
    int[] playerPosY = new int[PLAYER_NUM];

    private Timer ballTimer;
    private int Ball_PosX;
    private int Ball_PosY;
    int ballSpeedX = 1, ballSpeedY = 1;
    final int DELAY_MS = 10;
    int BALL_RADIUS = 20;

    int[] playerScore = new int[PLAYER_NUM];

    public PPG() {
        SCREEN_WIDTH = 400;
        SCREEN_HEIGHT = 400;
        setBallPos();
        setWindows();
    }

    public PPG(int width, int height) {
        SCREEN_WIDTH = width;
        SCREEN_HEIGHT = height;
        setBallPos();
        setWindows();
    }

    @Override
    public void show() {
        super.show();
    }

    public void setBallPos() {
        Ball_PosX = SCREEN_WIDTH / 2;
        Ball_PosY = SCREEN_HEIGHT / 2;
    }

    public void setWindows() {
        JFrame frame = new JFrame();
        frame.setTitle("遊戲基礎-乒乓球");
        frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        //this.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        this.setBounds(0, 0, 400, 400);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.addKeyListener(this);
        frame.add(this);

        initBallTimer();
        initPlayerPos();
    }

    private void initBallTimer() {
        ballTimer = new Timer(DELAY_MS, this);
        ballTimer.setInitialDelay(1);
        ballTimer.start();
    }

    private void initPlayerPos() {
        for (int i = 0; i < PLAYER_NUM; i++) {
            playerPosY[i] = Ball_PosY;
        }

        playerPosX[0] = InitialPad_Width - PAD_OFFSET;
        playerPosX[1] = SCREEN_WIDTH - InitialPad_Width - PAD_OFFSET;
    }

    public void update(Graphics g) {
        this.paint(g);
    }

    public void paint(Graphics g) {
        super.paint(g);         //父類別Window的paint

        drawPlayerPad(g);
        drawBall(g);
        drawScore(g);
    }

    private void drawPlayerPad(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(playerPosX[0], playerPosY[0], InitialPad_Width, InitialPad_Height);
        g.setColor(Color.BLUE);
        g.fillRect(playerPosX[1], playerPosY[1], InitialPad_Width, InitialPad_Height);
    }

    private void drawBall(Graphics g) {
        g.setColor(Color.CYAN);
        g.fillOval(Ball_PosX, Ball_PosY, BALL_RADIUS, BALL_RADIUS);
    }

    private void drawScore(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawString("P1:" + playerScore[0], playerPosX[0], 50);
        g.drawString("P2:" + playerScore[1], playerPosX[1] - 50, 50);
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {//按下鍵盤與釋放鍵盤之間的動作

    }

    @Override
    public void keyPressed(KeyEvent e) {//按下鍵盤的動作
        int key = e.getKeyCode();

        // Move the player on the left
        if (key == KeyEvent.VK_UP)
            playerPosY[1] -= playerSpeedY;

        if (key == KeyEvent.VK_DOWN)
            playerPosY[1] += playerSpeedY;

        // Move the player on the right
        if (key == KeyEvent.VK_W)
            playerPosY[0] -= playerSpeedY;

        if (key == KeyEvent.VK_S)
            playerPosY[0] += playerSpeedY;

        checkPadPosRange();
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {//釋放鍵盤的動作

    }

    // 檢查板子是否超出遊戲畫面的範圍
    private void checkPadPosRange() {
        for (int i = 0; i < PLAYER_NUM; i++) {
            if (playerPosY[i] < 0) playerPosY[i] = 0;
            if (playerPosY[i] > SCREEN_HEIGHT - InitialPad_Height)
                playerPosY[i] = SCREEN_HEIGHT - InitialPad_Height;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Ball_PosX += ballSpeedX;
        Ball_PosY += ballSpeedY;

        // 球是否碰到遊戲畫面邊界
        if (Ball_PosX >= SCREEN_WIDTH - BALL_RADIUS-5 || Ball_PosX <= 0) {
            ballSpeedX = -ballSpeedX;

            if (Ball_PosX <= 0) {
                playerScore[1]++;
            } else {
                playerScore[0]++;
            }
        }

        if (Ball_PosY >= SCREEN_HEIGHT - BALL_RADIUS-35 || Ball_PosY <= BALL_RADIUS-20)
            ballSpeedY = -ballSpeedY;

        // 球是否碰到左邊板子
        if (Ball_PosX <= playerPosX[0] + InitialPad_Width && Ball_PosX >= playerPosX[0] &&
            Ball_PosY <= playerPosY[0] + InitialPad_Height && Ball_PosY >= playerPosY[0])
            ballSpeedX = -ballSpeedX;

        // 球是否碰到右邊板子
        if (Ball_PosX <= playerPosX[1] - BALL_RADIUS + InitialPad_Width && Ball_PosX >= playerPosX[1] - BALL_RADIUS &&
            Ball_PosY <= playerPosY[1] + InitialPad_Height && Ball_PosY >= playerPosY[1])
            ballSpeedX = -ballSpeedX;

        this.repaint();//https://yunlinsong.blogspot.com/2016/10/java.html
    }
}
