import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Inimigo {
    public BufferedImage img;
    public int posX;
    public int posY;
    public int raio;

    public Inimigo() {
        try {
            img = ImageIO.read(getClass().getResource("imgs/inimigo.gif"));
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
