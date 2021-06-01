import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Bola {
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
            direita_baixo = ImageIO.read(getClass().getResource("imgs/direita_baixo.gif"));
            direita_cima = ImageIO.read(getClass().getResource("imgs/direita_cima.gif"));
            esquerda_baixo = ImageIO.read(getClass().getResource("imgs/esquerda_baixo.gif"));
            esquerda_cima = ImageIO.read(getClass().getResource("imgs/esquerda_cima.gif"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        posX = 200;
        posY = 200;
        raio = 50;
        velX = 1;
        velY = 1;
    }

    public BufferedImage obterImagem() {
        if (velX < 0) {
            if (velY < 0) {
                return esquerda_cima;
            } else {
                return esquerda_baixo;
            }
        } else {
            if (velY < 0) {
                return direita_cima;
            } else {
                return direita_baixo;
            }
        }
    }
}
