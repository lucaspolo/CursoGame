import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Bola {
    public BufferedImage parada;
    public BufferedImage baixo;
    public BufferedImage cima;
    public BufferedImage esquerda;
    public BufferedImage direita;
    public BufferedImage direita_baixo;
    public BufferedImage direita_cima;
    public BufferedImage esquerda_baixo;
    public BufferedImage esquerda_cima;
    public int posX;
    public int posY;
    public int raio;
    public int velX;
    public int velY;

    public Bola() {
        try {
            parada = ImageIO.read(getClass().getResource("imgs/parada.gif"));
            baixo = ImageIO.read(getClass().getResource("imgs/baixo.gif"));
            cima = ImageIO.read(getClass().getResource("imgs/cima.gif"));
            esquerda = ImageIO.read(getClass().getResource("imgs/esquerda.gif"));
            direita = ImageIO.read(getClass().getResource("imgs/direita.gif"));
            direita_baixo = ImageIO.read(getClass().getResource("imgs/direita_baixo.gif"));
            direita_cima = ImageIO.read(getClass().getResource("imgs/direita_cima.gif"));
            esquerda_baixo = ImageIO.read(getClass().getResource("imgs/esquerda_baixo.gif"));
            esquerda_cima = ImageIO.read(getClass().getResource("imgs/esquerda_cima.gif"));
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
}
