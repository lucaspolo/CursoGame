import javax.swing.*;
import java.awt.*;

public class Game extends JPanel {
    private Bola bola;

    public Game() {
        bola = new Bola();
        setFocusable(true);
        setLayout(null);

        new Thread(new Runnable() {
            @Override
            public void run() {
                gameLoop();
            }
        }).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.LIGHT_GRAY);
        g.setColor(Color.RED);
        g.fillOval(bola.posX, bola.posY, bola.raio * 2, bola.raio * 2);
    }

    public void gameLoop() {
        while(true) {
            handlerEvents();
            update();
            render();

            try {
                Thread.sleep(17);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void handlerEvents() {
    }

    public void update() {
        bola.posX += bola.velX;
        bola.posY += bola.velY;
        System.out.println(bola.posX);
    }

    public void render() {
        repaint();
    }
}
