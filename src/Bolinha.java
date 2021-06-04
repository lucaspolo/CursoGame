import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Bolinha {
    public BufferedImage sprite;
    public BufferedImage img;

    public AffineTransform af;
    public double posX;
    public double posY;
    public double raio;
    public double velocidadeBase;
    public double velX;
    public double velY;

    public Bolinha() {
        try {
            sprite = ImageIO.read(getClass().getResource("imgs/sprite_person_bola.png"));
            img = Recursos.getInstance().cortarImagem(400, 100, 430, 130, sprite);
        } catch (IOException e) {
            e.printStackTrace();
        }

        af = new AffineTransform();
        raio = 15;
        posX = (Principal.LARGURA_TELA/ 2 - raio);
        posY = (Principal.ALTURA_TELA / 2 - raio);
        velocidadeBase = 5;
        velX = velocidadeBase / 2.0;
        velY = velocidadeBase / 2.0;
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
}
