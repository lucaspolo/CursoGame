import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Inimigo {
    public BufferedImage sprite;
    public BufferedImage img;
    public AffineTransform af;
    public double posX;
    public double posY;
    public double raio;
    public double velocidadeBase;
    public double velX;
    public double velY;

    public long acumuladorTempoVertical;
    public long acumuladorTempoHorizontal;
    public long tempoAtual;
    public long tempoAnterior;

    public Inimigo() {
        try {
            sprite = ImageIO.read(Objects.requireNonNull(getClass().getResource("imgs/sprite_inimigo.png")));
            img = Recursos.getInstance().cortarImagem(300, 100, 400, 200, sprite);
        } catch (IOException e) {
            e.printStackTrace();
        }
        af = new AffineTransform();
        raio = 50;
        posX = (Principal.LARGURA_TELA * (1.0 / 8.0) - raio);
        posY = Principal.ALTURA_TELA / 2.0 - raio;
        velocidadeBase = 3;
        velX = 0;
        velY = 0;
        acumuladorTempoVertical = 0;
        tempoAnterior = System.currentTimeMillis();
    }

    public double getCentroX() {
        return posX + raio;
    }

    public double getCentroY() {
        return posY + raio;
    }

    public void mover(double deltaTime) {
        posX += (velX * deltaTime);
        posY += (velY * deltaTime);
        af.setToTranslation(posX, posY);
    }

    public void desmoverX(double deltaTime) {
        posX -= (velX * deltaTime);
        af.setToTranslation(posX, posY);
    }

    public void desmoverY(double deltaTime) {
        posY -= (velY * deltaTime);
        af.setToTranslation(posX, posY);
    }

    public void update(double deltaTime) {
        mover(deltaTime);
    }

    public void handlerEvents(Bolinha bolinha) {
        testeColisoes();

        tempoAtual = System.currentTimeMillis();
        acumuladorTempoVertical = acumuladorTempoVertical + (tempoAtual - tempoAnterior);
        acumuladorTempoHorizontal = acumuladorTempoHorizontal + (tempoAtual - tempoAnterior);

        if(acumuladorTempoVertical >= Recursos.getInstance().gerarAleatorio(80, 120)) {
            acumuladorTempoVertical = 0;
            movimentVertical(bolinha);
        }

        if(acumuladorTempoHorizontal >= Recursos.getInstance().gerarAleatorio(400, 600)) {
            acumuladorTempoHorizontal = 0;
            movimentoHorizontal(bolinha);
        }

        tempoAnterior = tempoAtual;
    }

    public void testeColisoes() {
        if(colideBaixo()) {
            velY = 0;
            posY = Principal.ALTURA_TELA - raio * 2;
        }

        if(colideCima()) {
            velY = 0;
            posY = 0;
        }

        if(colideEsquerda()) {
            velX = 0;
            posX = 0;
        }

        if(colideDireita()) {
            velX = 0;
            posX = Principal.LIMITE_ESQUERDO - raio * 2;
        }
    }

    public void movimentVertical(Bolinha bolinha) {
        velY = 0;
        double diferencaY = getCentroY() - bolinha.getCentroY();
        double limite = raio * (Recursos.getInstance().gerarAleatorio(4, 8) / 10.0);

        if(diferencaY < -limite) {
            velY = velocidadeBase;
        } else if(diferencaY > limite) {
            velY = -velocidadeBase;
        }

    }

    public void movimentoHorizontal(Bolinha bolinha) {
        velX = 0;
        var distanciaBolinhaX = Math.abs(getCentroX() - bolinha.getCentroX());
        var distanciaBolinhaY = Math.abs(getCentroY() - bolinha.getCentroY());
        var aceleracao = distanciaBolinhaY / 120.0;
        if(distanciaBolinhaX > 211) {
            velX = velocidadeBase * aceleracao;
        } else {
            velX = -velocidadeBase * aceleracao;
        }
    }

    private boolean colideCima() {
        return posY <= 0;
    }

    private boolean colideBaixo() {
        return posY + raio * 2 >= Principal.ALTURA_TELA;
    }

    private boolean colideDireita() {
        return posX + raio * 2 >= Principal.LIMITE_ESQUERDO;
    }

    private boolean colideEsquerda() {
        return posX <= 0;
    }

    private void mudarSprite() {

    }
}
