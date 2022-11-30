import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class OriginalPicture extends JPanel {
    BufferedImage image;
    int r = 0;
    int g = 0;
    int b = 0;
    int[][][] pixels;

    public OriginalPicture() throws IOException {
        setSize(1000, 1000);
        image = ImageIO.read((getClass().getResource("kampus-PB-analiza-terenow-zielonych.png")));
        JLabel label = new JLabel("", new ImageIcon(image), 0);
        pixels = new int[image.getHeight()][image.getWidth()][3];
    }

    public void displayImage(Graphics g) {
        g.drawImage(image, 0, 0, 1000, 1000, this);
    }

    @Override
    public void paintComponent(Graphics g) {
        this.displayImage(g);
    }

    public void loadPicture() {
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int color = image.getRGB(i, j);
                Color c = new Color(color);
                b = color & 0xff;
                g = (color & 0xff00) >> 8;
                r = (color & 0xff0000) >> 16;

                pixels[j][i][0] = r;
                pixels[j][i][1] = g;
                pixels[j][i][2] = b;
                image.setRGB(i, j, c.getRGB());
            }
        }
    }
}
