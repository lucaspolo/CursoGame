import javax.swing.*;
import java.awt.*;

public class Principal {
    public static final int LARGURA_TELA = 640;
    public static final int ALTURA_TELA = 480;
    public static final int LIMITE_DIREITO = 420;
    public static final int LIMITE_ESQUERDO = 210;

    public Principal() {
        JFrame janela = new JFrame("Jogo2D");
        Game game = new Game();
        game.setPreferredSize(new Dimension(LARGURA_TELA, ALTURA_TELA));
        janela.getContentPane().add(game);
        janela.setSize(LARGURA_TELA, ALTURA_TELA);
        janela.setResizable(false);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setLocation(100, 100);
        janela.setVisible(true);
        janela.pack();
    }

    public static void main(String[] args) {
        new Principal();
    }
}
