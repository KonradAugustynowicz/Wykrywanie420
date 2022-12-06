import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

public class OriginalPicture extends JPanel {
    BufferedImage image;
    int r = 0;
    int g = 0;
    int b = 0;
    int[][][] pixels;

    public void setImgPath(URL imgPath) {
        this.imgPath = imgPath;
    }

    static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }


    private BufferedImage medianaFilterImage(BufferedImage image) {
        int filteredPixels[][][] = new int[image.getHeight()][image.getWidth()][3];
        for (int i = 0; i < image.getHeight() - 1; i++) {
            for (int j = 0; j < image.getWidth() - 1; j++) {
                filteredPixels[i][j] = medianate(i, j);

                Color c = this.validateColor(filteredPixels[i][j][0], filteredPixels[i][j][1], filteredPixels[i][j][2]);
                image.setRGB(j, i, c.getRGB());
            }
        }
        return image;
    }

    private int[] medianate(int x, int y) {
        LinkedList<Integer> medianaList = new LinkedList<>();
        HashMap<Integer, int[]> map = new HashMap<>();
        int elementsCount = 0;
        int[] average = new int[3];
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                try {
                    medianaList.add((pixels[x + i][y + j][0] + pixels[x + i][y + j][1] + pixels[x + i][y + j][2]) / 3);

                    average[0] += pixels[x + i][y + j][0];
                    average[1] += pixels[x + i][y + j][1];
                    average[2] += pixels[x + i][y + j][2];
                    map.put((pixels[x + i][y + j][0] + pixels[x + i][y + j][1] + pixels[x + i][y + j][2]) / 3,
                            new int[]{pixels[x + i][y + j][0], pixels[x + i][y + j][1], pixels[x + i][y + j][2]});
                    elementsCount++;
                } catch (IndexOutOfBoundsException e) {
                    continue;
                }
            }
        }
        Collections.sort(medianaList);
        if (elementsCount%2 == 1){
            return map.get(medianaList.get(elementsCount / 2 +1));
        }else{
            try {
                return map.get(medianaList.get(elementsCount / 2 ));
            }catch (IndexOutOfBoundsException e) {
                return map.get(medianaList.get(0));
            }
        }
    }


    URL imgPath = getClass().getResource("kampus-PB-analiza-terenow-zielonych.png");

    public OriginalPicture() throws IOException {
        setSize(1000, 1000);
        image = ImageIO.read((imgPath));
        JLabel label = new JLabel("", new ImageIcon(image), 0);
        pixels = new int[image.getHeight()][image.getWidth()][3];
    }

    public void reset() throws IOException {
        image = ImageIO.read(imgPath);
        paintComponent(getGraphics());
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


    public Color validateColor(float red, float green, float blue) {
        if (red >= 255) {
            red = 255;
        } else if (red < 0) {
            red = 0;
        }
        if (green >= 255) {
            green = 255;
        } else if (green < 0) {
            green = 0;
        }
        if (blue >= 255) {
            blue = 255;
        } else if (blue < 0) {
            blue = 0;
        }
        return new Color((int) red, (int) green, (int) blue);
    }
}
