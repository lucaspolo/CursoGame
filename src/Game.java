import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;

public class Game extends JPanel {
    private final Inimigo inimigo;
    private Bola bola;
    private boolean kCima = false;
    private boolean kBaixo = false;
    private boolean kDireita = false;
    private boolean kEsquerda = false;

    private long tempoAtual;
    private long tempoAnterior;
    private double deltaTime;

    private double FPS_limit = 60;

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
        inimigo = new Inimigo();
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
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
            RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BICUBIC
        );
        super.paintComponent(g2d);
        var af1 = new AffineTransform();
        var af2 = new AffineTransform();
        af1.translate(bola.posX, bola.posY);
        af2.translate(inimigo.posX, inimigo.posY);
        setBackground(Color.LIGHT_GRAY);
        g2d.setColor(Color.RED);
        g2d.drawImage(bola.obterImagem(), af1, null);
        g2d.drawImage(inimigo.img, af2, null);
    }

    public void gameLoop() {
        tempoAnterior = System.nanoTime();
        double tempoMinimo = (1e9) / FPS_limit;
        while(true) {
            tempoAtual = System.nanoTime();
            deltaTime = (tempoAtual - tempoAnterior) * (6e-8);
            handlerEvents();
            update();
            render();

            tempoAnterior = tempoAtual;

            var tempoEspera = (int) ((tempoMinimo - deltaTime) * (1e-6));
            try {
                Thread.sleep(tempoEspera);
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
        bola.posX += (bola.velX * deltaTime);
        bola.posY += (bola.velY * deltaTime);
        testeColisoes(deltaTime);
    }

    public void render() {
        repaint();
    }

    public void testeColisoes(double deltaTime) {
        if(bola.posX + bola.raio * 2 >= Principal.LARGURA_TELA || bola.posX <= 0) {
            bola.posX -= (bola.velX * deltaTime);
        }

        if(bola.posY + bola.raio * 2 >= Principal.ALTURA_TELA || bola.posY <= 0) {
            bola.posY -= (bola.velY * deltaTime);
        }

        // Colisão com inimigo
        var catetoH = bola.getCentroX() - inimigo.getCentroX();
        var catetoV = bola.getCentroY() - inimigo.getCentroY();

        var hipotenusa = Math.sqrt(Math.pow(catetoH, 2) + Math.pow(catetoV, 2));

        if(hipotenusa <= bola.raio + inimigo.raio) {
            bola.posX -= (bola.velX * deltaTime);
            bola.posY -= (bola.velY * deltaTime);
        }
    }
}
