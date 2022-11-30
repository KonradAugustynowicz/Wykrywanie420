import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Frame extends JFrame {
    public Frame() throws HeadlessException, IOException {
        setLayout(new GridLayout(1,2,0,0));
        setSize(new Dimension(2000, 1000));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        PicturePanel pp =new PicturePanel();//kekw
        add(pp);
        add(new OriginalPicture());
//        MenuBar menuBar = new MenuBar(pp,this);
//        this.setJMenuBar(menuBar);
    }
}