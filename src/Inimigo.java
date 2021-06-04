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

    public Inimigo() {
        try {
            sprite = ImageIO.read(Objects.requireNonNull(getClass().getResource("imgs/sprite.png")));
            img = Recursos.getInstance().cortarImagem(400, 100, 500, 200, sprite);
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
