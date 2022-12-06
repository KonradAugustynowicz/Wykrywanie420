import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;

public class MenuBar extends JMenuBar implements ActionListener {
    PicturePanel pp;
    Frame frame;
    OriginalPicture op;
    JMenuItem open;

    JMenuItem reset;
    public MenuBar(PicturePanel pp, OriginalPicture op, Frame frame){
        this.pp = pp;
        this.frame = frame;
        this.op = op;
        init();
    }

    private void init(){
        open = new JMenuItem("Otwórz");
        reset = new JMenuItem("Reset");
        open.addActionListener(this);
        reset.addActionListener(this);
        this.add(open);
        this.add(reset);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == open){
            final JFileChooser fc = new JFileChooser();
            int returnVal = fc.showOpenDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();

                String mimetype = null;
                try {
                    mimetype = Files.probeContentType(file.toPath());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                //mimetype should be something like "image/png"

                if (mimetype != null && mimetype.split("/")[0].equals("image")) {
                    System.out.println("it is an image");
                    try {
                        op.setImgPath(file.toURI().toURL());
                        pp.setImgPath(file.toURI().toURL());
                        pp.reset();
                        op.reset();
                    } catch (MalformedURLException ex) {
                        throw new RuntimeException(ex);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                else{
                    JOptionPane.showMessageDialog (null, "You must select a image!!!!", "No supported extension", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog (null, "You must select a image!!!!", "No supported extension", JOptionPane.ERROR_MESSAGE);
            }
        }
        if(e.getSource() == reset){
            try {
                pp.reset();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
