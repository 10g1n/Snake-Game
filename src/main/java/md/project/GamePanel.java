package md.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

//TODO tomorrow ask chatgpt what all the classes and methods do

public class GamePanel extends JPanel implements ActionListener {

    public static final int SCREEN_WIDTH = 600;
    public static final int SCREEN_HEIGHT = 600;
    public static final int UNIT_SIZE = 25;
    public static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    public static final int DELAY = 75;

    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];

    int bodyPartsOfSnake = 6;

    int applesEaten;
    int applesX;
    int applesY;

    char startDirection = 'R';

    boolean running = false;

    Timer timer;
    Random random;


    public GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        createApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
            g.setColor(Color.red);
            g.fillOval(applesX, applesY, UNIT_SIZE, UNIT_SIZE );

            for (int i = 0; i < bodyPartsOfSnake; i++) {
                if (i == 0) {
                    g.setColor(Color.MAGENTA);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

            g.setColor(Color.WHITE);
            g.setFont(new Font("Ink Free", Font.BOLD, 35));
            FontMetrics fontMetrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - fontMetrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize()); // will center the text
        } else {
            gameOver(g);
        }
    }

    public void createApple() {
        applesX = random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
        applesY = random.nextInt(SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    }

    public void move() {
        for (int i = bodyPartsOfSnake; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (startDirection) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    public void checkApple() {
        if ((x[0] == applesX) && (y[0] == applesY)) {
            bodyPartsOfSnake++;
            applesEaten++;
            createApple();
        }
    }

    public void checkCollisions() {
        for (int i = bodyPartsOfSnake; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }

        if (x[0] < 0) {
            running = false;
        }

        if (x[0] > SCREEN_WIDTH) {
            running = false;
        }

        if (y[0] < 0) {
            running = false;
        }

        if (y[0] > SCREEN_WIDTH) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics fontMetrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - fontMetrics.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2); // will center the text

        g.setColor(Color.WHITE);
        g.setFont(new Font("Ink Free", Font.BOLD, 35));
        FontMetrics fontMetrics2 = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - fontMetrics2.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }


    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent keyEvent) {
            switch (keyEvent.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (startDirection != 'R') {
                        startDirection = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (startDirection != 'L') {
                        startDirection = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (startDirection != 'D') {
                        startDirection = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (startDirection != 'U') {
                        startDirection = 'D';
                    }
                    break;
            }
        }
    }
}
