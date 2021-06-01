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
        g.drawImage(bola.obterImagem(), bola.posX, bola.posY, null);
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
        testeColisoes();
    }

    public void render() {
        repaint();
    }

    public void testeColisoes() {
        if(bola.posX + bola.raio * 2 >= Principal.LARGURA_TELA || bola.posX <= 0) {
            bola.velX *= -1;
        }

        if(bola.posY + bola.raio * 2 >= Principal.ALTURA_TELA || bola.posY <= 0) {
            bola.velY *= -1;
        }
    }
}
