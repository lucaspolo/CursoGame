import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Bola {
    public BufferedImage sprite;
    public BufferedImage parada;
    public BufferedImage baixo;
    public BufferedImage cima;
    public BufferedImage esquerda;
    public BufferedImage direita;
    public BufferedImage direita_baixo;
    public BufferedImage direita_cima;
    public BufferedImage esquerda_baixo;
    public BufferedImage esquerda_cima;
    public double posX;
    public double posY;
    public double raio;
    public double velX;
    public double velY;

    public Bola() {
        try {
            sprite = ImageIO.read(Objects.requireNonNull(getClass().getResource("imgs/sprite.png")));
            cima = Recursos.getInstance().cortarImagem(100, 0,200, 100, sprite);
            baixo = Recursos.getInstance().cortarImagem(0, 100,100, 200, sprite);
            esquerda = Recursos.getInstance().cortarImagem(200, 100,300, 200, sprite);
            direita = Recursos.getInstance().cortarImagem(300, 0,400, 100, sprite);
            parada = Recursos.getInstance().cortarImagem(300, 100,400, 200, sprite);
            esquerda_baixo = Recursos.getInstance().cortarImagem(100, 100,200, 200, sprite);
            esquerda_cima = Recursos.getInstance().cortarImagem(0, 0,100, 100, sprite);
            direita_baixo = Recursos.getInstance().cortarImagem(400, 0,500, 100, sprite);
            direita_cima = Recursos.getInstance().cortarImagem(200, 0,300, 100, sprite);
        } catch (IOException e) {
            e.printStackTrace();
        }
        posX = 100;
        posY = 100;
        raio = 50;
        velX = 0;
        velY = 0;
    }

    public BufferedImage obterImagem() {
        var image = this.parada;

        if (velY < 0) {
            image = this.cima;

            if (velX > 0) {
                image = this.direita_cima;
            }
            if (velX < 0) {
                image = this.esquerda_cima;
            }
        } else if (velY > 0) {
            image = this.baixo;

            if (velX > 0) {
                image = this.direita_baixo;
            }
            if (velX < 0) {
                image = this.esquerda_baixo;
            }
        } else if (velX < 0) {
            image = this.esquerda;
        } else if (velX > 0) {
            image = this.direita;
        }

        return image;
    }

    public double getCentroX() {
        return posX + raio;
    }

    public double getCentroY() {
        return posY + raio;
    }
}
