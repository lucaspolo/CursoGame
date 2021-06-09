import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Game extends JPanel {
    private final Inimigo inimigo;
    private Jogador jogador;
    private Bolinha bolinha;
    private boolean kCima = false;
    private boolean kBaixo = false;
    private boolean kDireita = false;
    private boolean kEsquerda = false;
    private boolean kShifit = false;
    private char estado;

    private long tempoAtual;
    private long tempoAnterior;
    private double deltaTime;

    private double FPS_limit = 60;

    private BufferedImage bg;

    private BufferedImage splashLogo;

    public Game() {
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {

            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if(estado == 'E') {
                    switch (keyEvent.getKeyCode()) {
                        case KeyEvent.VK_UP -> kCima = true;
                        case KeyEvent.VK_DOWN -> kBaixo = true;
                        case KeyEvent.VK_LEFT -> kEsquerda = true;
                        case KeyEvent.VK_RIGHT -> kDireita = true;
                        case KeyEvent.VK_SHIFT -> kShifit = true;
                        case KeyEvent.VK_ESCAPE -> {
                            estado = 'P';
                            Recursos.getInstance().tocarSomMenu();
                        }
                    }
                } else if(estado == 'P') {
                    switch (keyEvent.getKeyCode()) {
                        case KeyEvent.VK_UP -> {
                            Recursos.getInstance().pauseOpt = 0;
                            Recursos.getInstance().tocarSomMenu();
                        }
                        case KeyEvent.VK_DOWN -> {
                            Recursos.getInstance().pauseOpt = 1;
                            Recursos.getInstance().tocarSomMenu();
                        }
                        case KeyEvent.VK_ENTER -> {
                            Recursos.getInstance().tocarSomMenu();
                            if(Recursos.getInstance().pauseOpt == 0) {
                                estado = 'E';
                                kCima = false;
                                kBaixo = false;
                                kEsquerda = false;
                                kDireita = false;
                            } else {
                                System.exit(0);
                            }
                        }
                        case KeyEvent.VK_ESCAPE -> {
                            System.out.println("Saindo da pausa");
                            estado = 'E';
                            kCima = false;
                            kBaixo = false;
                            kEsquerda = false;
                            kDireita = false;
                        }
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                if(estado == 'E') {
                    switch (keyEvent.getKeyCode()) {
                        case KeyEvent.VK_UP -> kCima = false;
                        case KeyEvent.VK_DOWN -> kBaixo = false;
                        case KeyEvent.VK_LEFT -> kEsquerda = false;
                        case KeyEvent.VK_RIGHT -> kDireita = false;
                        case KeyEvent.VK_SHIFT -> kShifit = false;
                    }
                }
            }
        });

        jogador = new Jogador();
        inimigo = new Inimigo();
        bolinha = new Bolinha();

        estado = 'S';
        agendarTransicao(3000, 'E');

        try {
            bg = ImageIO.read(Objects.requireNonNull(getClass().getResource("imgs/bg.png")));
            splashLogo = ImageIO.read(getClass().getResource("imgs/logo.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setFocusable(true);
        setLayout(null);

        new Thread(this::gameLoop).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
            RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BICUBIC
        );
        super.paintComponent(g2d);

        if(estado == 'S') {
            g2d.drawImage(splashLogo, 0, 0, null);
        } else if(estado == 'R') {
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, Principal.LARGURA_TELA, Principal.ALTURA_TELA);
            g2d.setFont(Recursos.getInstance().fontMenu);
            g2d.setColor(Color.WHITE);
            g2d.drawString(Recursos.getInstance().msgFim, 150, 200);
        } else {
            g2d.drawImage(bg, 0, 0, Principal.LARGURA_TELA, Principal.ALTURA_TELA, null);
            g2d.setColor(Color.GRAY);
            g2d.fillRect(Principal.LIMITE_DIREITO, 0, 5, Principal.ALTURA_TELA);
            g2d.fillRect(Principal.LIMITE_ESQUERDO, 0, 5, Principal.ALTURA_TELA);
            g2d.drawImage(jogador.obterImagem(), jogador.af, null);
            g2d.drawImage(inimigo.img, inimigo.af, null);
            g2d.drawImage(bolinha.img, bolinha.af, null);

            if(estado == 'E') {
                g2d.setFont(Recursos.getInstance().fontPontuacao);
                g2d.setColor(Color.WHITE);
                g2d.drawString(Recursos.getInstance().pontosInimigo + "pts", 120, 40);
                g2d.drawString(Recursos.getInstance().pontosJogador + "pts", 460, 40);
            } else {
                g2d.setColor(new Color( 0, 0, 0, 128));
                g2d.fillRect(0, 0, Principal.LARGURA_TELA, Principal.ALTURA_TELA);
                g2d.setFont(Recursos.getInstance().fontMenu);
                g2d.setColor(Color.WHITE);
                g2d.drawString("JOGO PAUSADO", 150, 80);
                g2d.drawString("Continuar", 220, 200);
                g2d.drawString("Sair", 220, 270);

                if (Recursos.getInstance().pauseOpt == 0) {
                    g2d.fillRect(180, 170, 30, 30);
                } else {
                    g2d.fillRect(180, 240, 30, 30);
                }
            }
        }
    }

    public void gameLoop() {
        tempoAnterior = System.nanoTime();
        double tempoMinimo = (1e9) / FPS_limit;
        do {
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
        } while (true);
    }

    public void handlerEvents() {
        if(estado == 'E') {
            jogador.handlerEvents(kCima, kBaixo, kEsquerda, kDireita);
            inimigo.handlerEvents(bolinha);
        }
    }

    public void update() {
        if(estado == 'E') {
            jogador.update(deltaTime);
            inimigo.update(deltaTime);
            bolinha.update(deltaTime);
            testeColisoes(deltaTime);
            testeFimJogo();
        } else if(estado == 'G') {
            estado = 'R';
            reiniciar();
            agendarTransicao(2000, 'E');
        }
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

        if(bolinha.posX + (bolinha.raio * 2) >= Principal.LARGURA_TELA) {
            bolinha.velX *= -1;
            bolinha.posX = Principal.LARGURA_TELA / 2 - bolinha.raio + 90;
            Recursos.getInstance().pontosInimigo++;
        }

        if(bolinha.posX <= 0) {
            bolinha.velX *= -1;
            bolinha.posX = Principal.LARGURA_TELA / 2.0 - bolinha.raio - 90;
            Recursos.getInstance().pontosJogador++;
        }

        if(bolinha.posY + (bolinha.raio * 2) >= Principal.ALTURA_TELA) {
            bolinha.velY *= -1;
            bolinha.posY = Principal.ALTURA_TELA - (bolinha.raio * 2);
            Recursos.getInstance().tocarSomBolinha();
        }

        if(bolinha.posY <= 0) {
            bolinha.velY *= -1;
            bolinha.posY = 0;
            Recursos.getInstance().tocarSomBolinha();
        }

        var ladoHorizontal = jogador.getCentroX() - bolinha.getCentroX();
        var ladoVertical = jogador.getCentroY() - bolinha.getCentroY();
        var hipotenusa = Math.sqrt(Math.pow(ladoHorizontal, 2) + Math.pow(ladoVertical, 2));

        if(hipotenusa <= jogador.raio + bolinha.raio) {
            jogador.desmoverX(deltaTime);
            jogador.desmoverY(deltaTime);

            var cosseno = ladoHorizontal / hipotenusa;
            var seno = ladoVertical / hipotenusa;
            bolinha.velX = (-bolinha.velocidadeBase) * cosseno;
            bolinha.velY = (-bolinha.velocidadeBase) * seno;
            Recursos.getInstance().tocarSomBolinha();
        }

        ladoHorizontal = inimigo.getCentroX() - bolinha.getCentroX();
        ladoVertical = inimigo.getCentroY() - bolinha.getCentroY();
        hipotenusa = Math.sqrt(Math.pow(ladoHorizontal, 2) + Math.pow(ladoVertical, 2));

        if(hipotenusa <= inimigo.raio + bolinha.raio) {
            inimigo.desmoverX(deltaTime);
            inimigo.desmoverY(deltaTime);

            var cosseno = ladoHorizontal / hipotenusa;
            var seno = ladoVertical / hipotenusa;
            bolinha.velX = (-bolinha.velocidadeBase) * cosseno;
            bolinha.velY = (-bolinha.velocidadeBase) * seno;
            Recursos.getInstance().tocarSomBolinha();
        }
    }

    public void agendarTransicao(int tempo, char novoEstado) {
        var thread = new Thread(() -> {
            try {
                Thread.sleep(tempo);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            estado = novoEstado;
        });
        thread.start();
    }

    public void testeFimJogo() {
        if(Recursos.getInstance().pontosJogador == Recursos.getInstance().maxPontos) {
            Recursos.getInstance().msgFim = "VOCÊ VENCEU!";
            estado = 'G';
        } else if(Recursos.getInstance().pontosInimigo == Recursos.getInstance().maxPontos) {
            Recursos.getInstance().msgFim = "VOCÊ PERDEU!";
            estado = 'G';
        }
    }

    public void reiniciar() {
        inimigo.posX = (Principal.LARGURA_TELA * (1.0 / 8.0) - inimigo.raio);
        inimigo.posY = (Principal.ALTURA_TELA / 2.0) - inimigo.raio;
        inimigo.velY = inimigo.velocidadeBase;
        Recursos.getInstance().pontosInimigo = 0;

        jogador.posX = (Principal.LARGURA_TELA * (7.0 / 8.0) - jogador.raio);
        jogador.posY = (Principal.ALTURA_TELA / 2.0) - jogador.raio;
        Recursos.getInstance().pontosJogador = 0;

        bolinha.velX = bolinha.velocidadeBase / 2;
        bolinha.velY = bolinha.velocidadeBase / 2;
        bolinha.posX = (Principal.LARGURA_TELA / 2.0) - bolinha.raio;
        bolinha.posX = (Principal.ALTURA_TELA / 2.0) - bolinha.raio;

        kCima = false;
        kBaixo = false;
        kEsquerda = false;
        kDireita = false;
    }
}
