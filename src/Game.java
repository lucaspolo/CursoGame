import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Game extends JPanel {
    private Bola bola;
    private boolean kCima = false;
    private boolean kBaixo = false;
    private boolean kDireita = false;
    private boolean kEsquerda = false;

    public Game() {
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {

            }

            private void changeKeys(KeyEvent keyEvent, boolean value) {
                switch (keyEvent.getKeyCode()) {
                    case KeyEvent.VK_UP -> kCima = value;
                    case KeyEvent.VK_DOWN -> kBaixo = value;
                    case KeyEvent.VK_LEFT -> kEsquerda = value;
                    case KeyEvent.VK_RIGHT -> kDireita = value;
                }
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                this.changeKeys(keyEvent, true);
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                this.changeKeys(keyEvent, false);
            }
        });

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
        bola.velX = 0;
        bola.velY = 0;

        if(kCima) bola.velY = -3;
        if (kBaixo) bola.velY = 3;
        if (kEsquerda) bola.velX = -3;
        if (kDireita) bola.velX = 3;
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
            bola.posX -= bola.velX;
        }

        if(bola.posY + bola.raio * 2 >= Principal.ALTURA_TELA || bola.posY <= 0) {
            bola.posY -= bola.velY;
        }
    }
}
