import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

public class PicturePanel extends JPanel implements MouseListener {
    BufferedImage image;
    int r = 0;
    int g = 0;
    int b = 0;
    int[][][] pixels;
    ThresholdInputDialog dialog;

    public PicturePanel() throws IOException {
        setSize(1000, 1000);
        image = ImageIO.read((getClass().getResource("kampus-PB-analiza-terenow-zielonych.png")));
        JLabel label = new JLabel("", new ImageIcon(image), 0);
        pixels = new int[image.getHeight()][image.getWidth()][3];
        addMouseListener(this);
        dialog = new ThresholdInputDialog();
    }

    public void displayImage(Graphics g) {
        g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
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

    public void binaryByInput(int r, int g, int b,
                              int lowR, int lowG, int lowB,
                              int highR, int highG, int highB) {
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int color = image.getRGB(i, j);
                this.b = color & 0xff;
                this.g = (color & 0xff00) >> 8;
                this.r = (color & 0xff0000) >> 16;
                if (this.r >= r - lowR && this.r <= r + highR && this.g >= g - lowG && this.g <= g + highG && this.b >= b - lowB && this.b <= b + highB) {
                    image.setRGB(i, j, Color.WHITE.getRGB());
                }else {
                    image.setRGB(i, j, Color.BLACK.getRGB());
                }
            }
        }
        Graphics graphics = getGraphics();
        graphics.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
    }

    public void dilatation() {
        loadPicture();
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int color = image.getRGB(i, j);
                this.b = color & 0xff;
                this.g = (color & 0xff00) >> 8;
                this.r = (color & 0xff0000) >> 16;
                image.setRGB(i, j, dilate(j, i).getRGB());
            }
        }
    }

    public Color dilate(int x, int y) {
        for (int i = -1; i < 1; i++) {
            for (int j = -1; j < 1; j++) {
                try {
                    if (pixels[x + i][y + j][0] == 0 &&
                            pixels[x + i][y + j][1] == 0 &&
                            pixels[x + i][y + j][2] == 0) {
                        return Color.BLACK;
                    }
                } catch (IndexOutOfBoundsException e) {
                    continue;
                }
            }
        }
        return Color.WHITE;
    }

    public void erosion() {
        loadPicture();
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int color = image.getRGB(i, j);
                this.b = color & 0xff;
                this.g = (color & 0xff00) >> 8;
                this.r = (color & 0xff0000) >> 16;
                image.setRGB(i, j, erode(j, i).getRGB());
            }
        }
    }

    public Color erode(int x, int y) {
        for (int i = -1; i < 1; i++) {
            for (int j = -1; j < 1; j++) {
                try {
                    if (pixels[x + i][y + j][0] == 255 &&
                            pixels[x + i][y + j][1] == 255 &&
                            pixels[x + i][y + j][2] == 255) {
                        return Color.WHITE;
                    }
                } catch (IndexOutOfBoundsException e) {
                    continue;
                }
            }
        }
        return Color.BLACK;
    }

    public void medianaFilter() {
        loadPicture();
        int filteredPixels[][][] = new int[image.getHeight()][image.getWidth()][3];
        for (int i = 0; i < image.getHeight() - 1; i++) {
            for (int j = 0; j < image.getWidth() - 1; j++) {
                filteredPixels[i][j] = medianate(i, j);

                Color c = this.validateColor(filteredPixels[i][j][0], filteredPixels[i][j][1], filteredPixels[i][j][2]);
                image.setRGB(j, i, c.getRGB());
            }
        }
        paintComponent(getGraphics());
    }

    public int[] medianate(int x, int y) {
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

    @Override
    public void mouseClicked(MouseEvent e) {
        JOptionPane.showConfirmDialog(null, dialog,
                "Podaj zakresy", JOptionPane.OK_CANCEL_OPTION);

        Point location = e.getPoint();
        location.x = (int) Math.round(location.x * ((double)image.getWidth()/1000));
        location.y = (int) Math.round(location.y * ((double)image.getHeight()/1000));
        System.out.println(location.toString());
        int color  = image.getRGB(location.x,location.y);
        int r = 0;
        int g = 0;
        int b = 0;
        b = color & 0xff;
        g = (color & 0xff00) >> 8;
        r = (color & 0xff0000) >> 16;
        System.out.println("Color: " + "Red: " + r + " Green: " + g + " Blue: " + b);

        binaryByInput(r,g,b,
                Integer.parseInt(dialog.lowR.getText()),
                Integer.parseInt(dialog.lowG.getText()),
                Integer.parseInt(dialog.lowB.getText()),
                Integer.parseInt(dialog.highR.getText()),
                Integer.parseInt(dialog.highG.getText()),
                Integer.parseInt(dialog.highB.getText())
        );

        medianaFilter();
        dilatation();
        medianaFilter();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

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