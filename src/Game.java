import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Game extends JPanel {
    private final Inimigo inimigo;
    private Jogador jogador;
    private boolean kCima = false;
    private boolean kBaixo = false;
    private boolean kDireita = false;
    private boolean kEsquerda = false;
    private boolean kShifit = false;

    private long tempoAtual;
    private long tempoAnterior;
    private double deltaTime;

    private double FPS_limit = 60;

    private BufferedImage bg;

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
                    case KeyEvent.VK_SHIFT -> kShifit = value;
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

        jogador = new Jogador();
        inimigo = new Inimigo();

        try {
            bg = ImageIO.read(getClass().getResource("imgs/bg.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

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
        g2d.drawImage(bg, 0, 0, Principal.LARGURA_TELA, Principal.ALTURA_TELA, null);
        g2d.setColor(Color.GRAY);
        g2d.fillRect(Principal.LIMITE_DIREITO, 0, 5, Principal.ALTURA_TELA);
        g2d.fillRect(Principal.LIMITE_ESQUERDO, 0, 5, Principal.ALTURA_TELA);
        g2d.drawImage(jogador.obterImagem(), jogador.af, null);
        g2d.drawImage(inimigo.img, inimigo.af, null);
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
        jogador.handlerEvents(kCima, kBaixo, kEsquerda, kDireita);
    }

    public void update() {
        jogador.update(deltaTime);
        inimigo.update(deltaTime);
        testeColisoes(deltaTime);
    }

    public void render() {
        repaint();
    }

    public void testeColisoes(double deltaTime) {
        if(jogador.posX + jogador.raio * 2 >= Principal.LARGURA_TELA || jogador.posX <= 0) {
            jogador.desmoverX(deltaTime);
        }

        if(jogador.posY + jogador.raio * 2 >= Principal.ALTURA_TELA || jogador.posY <= 0) {
            jogador.desmoverY(deltaTime);
        }

        if(jogador.posX <= Principal.LIMITE_DIREITO) {
            jogador.desmoverX(deltaTime);
        }
    }
}
