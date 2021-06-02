import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Inimigo {
    public BufferedImage sprite;
    public BufferedImage img;
    public int posX;
    public int posY;
    public int raio;

    public Inimigo() {
        try {
            sprite = ImageIO.read(Objects.requireNonNull(getClass().getResource("imgs/sprite.png")));
            img = Recursos.getInstance().cortarImagem(400, 100, 500, 200, sprite);
        } catch (IOException e) {
            e.printStackTrace();
        }

        raio = 50;
        posX = Principal.LARGURA_TELA / 2 - raio;
        posY = Principal.ALTURA_TELA / 2 - raio;
    }

    public int getCentroX() {
        return posX + raio;
    }

    public int getCentroY() {
        return posY + raio;
    }
}
