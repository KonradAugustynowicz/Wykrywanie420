import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;

public class ThresholdInputDialog  extends JPanel {
    NumberFormat format = NumberFormat.getInstance();
    NumberFormatter formatter = new NumberFormatter(format);

    JFormattedTextField highR;
    JFormattedTextField highG;
    JFormattedTextField highB;
    JFormattedTextField lowR;
    JFormattedTextField lowG;
    JFormattedTextField lowB;


    public ThresholdInputDialog() {
        setLayout(new GridBagLayout());
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(255);
        formatter.setAllowsInvalid(false);

        highR = new JFormattedTextField(formatter);
        highR.setColumns(3);
        highR.setValue(20);

        highG = new JFormattedTextField(formatter);
        highG.setColumns(3);
        highG.setValue(20);

        highB = new JFormattedTextField(formatter);
        highB.setColumns(3);
        highB.setValue(20);

        lowR = new JFormattedTextField(formatter);
        lowR.setColumns(3);
        lowR.setValue(20);

        lowG = new JFormattedTextField(formatter);
        lowG.setColumns(3);
        lowG.setValue(20);

        lowB = new JFormattedTextField(formatter);
        lowB.setColumns(3);
        lowB.setValue(20);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(new JLabel("Górny Czerwony:"),gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        this.add(highR,gbc);
        this.add(Box.createVerticalStrut(15)); // a spacer

        gbc.gridx = 0;
        gbc.gridy = 1;
        this.add(new JLabel("Dolny Czerwony:"),gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        this.add(lowR,gbc);
        this.add(Box.createHorizontalStrut(15)); // a spacer

        gbc.gridx = 2;
        gbc.gridy = 0;
        this.add(new JLabel("Górny Zielony:"),gbc);
        gbc.gridx = 3;
        gbc.gridy = 0;
        this.add(highG,gbc);
        this.add(Box.createHorizontalStrut(15)); // a spacer

        gbc.gridx = 2;
        gbc.gridy = 1;
        this.add(new JLabel("Dolny Zielony:"),gbc);
        gbc.gridx = 3;
        gbc.gridy = 1;
        this.add(lowG,gbc);
        this.add(Box.createHorizontalStrut(15)); // a spacer

        gbc.gridx = 4;
        gbc.gridy = 0;
        this.add(new JLabel("Górny Niebieski:"),gbc);
        gbc.gridx = 5;
        gbc.gridy = 0;
        this.add(highB,gbc);

        gbc.gridx = 4;
        gbc.gridy = 1;
        this.add(new JLabel("Dolny Niebieski:"),gbc);
        gbc.gridx = 5;
        gbc.gridy = 1;
        this.add(lowB,gbc);
    }
}