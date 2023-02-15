import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageOperation {
    static File file = null;

    static boolean isEncrypted(File file) {
        try {
            BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
            image = ImageIO.read(file);
            if (image == null)
                return true;
        } catch (Exception e) {
            return true;
        }
        return false;
    }

    static void operation(int key, File file, boolean doEncrypt) {
        try {
            // encryption & decryption logic
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[fis.available()];
            fis.read(data);

            int i = 0;
            for (byte b : data) {
                data[i] = (byte) (b ^ key);
                i++;
            }

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data);
            fos.close();
            fis.close();

            String message;
            if (doEncrypt)
                message = "file Encrypted successfully.";
            else
                message = "file Decrypted successfully.";
            JOptionPane.showMessageDialog(null, message);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        // creating a frame
        JFrame f = new JFrame();
        f.setTitle("Image Encryption/Decryption");
        f.setSize(300, 300);
        f.setLocationRelativeTo(null);

        // choose file button
        JButton cf = new JButton();
        cf.setText("choose file");

        // Encryption button
        JButton enc = new JButton();
        enc.setText("Encrypt");

        // Decryption button
        JButton dec = new JButton();
        dec.setText("Decrypt");

        JLabel label = new JLabel("Key:");
        // creating text field
        JTextField tf = new JTextField(10);

        // button listeners
        cf.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.addChoosableFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png"));
            fc.setAcceptAllFileFilterUsed(false);
            fc.showOpenDialog(null);
            file = fc.getSelectedFile();
        });

        enc.addActionListener(e -> {
            try {
                String val = tf.getText();
                int key = Integer.parseInt(val);
                if (file == null) {
                    JOptionPane.showMessageDialog(null, "please select a file!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    if (isEncrypted(file)) {
                        JOptionPane.showMessageDialog(null, "File is already Encrypted!");
                    } else {
                        operation(key, file, true);
                    }
                }

            } catch (NumberFormatException no_key) {
                JOptionPane.showMessageDialog(null, "key cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dec.addActionListener(e -> {
            try {
                String val = tf.getText();
                int key = Integer.parseInt(val);
                if (file == null) {
                    JOptionPane.showMessageDialog(null, "please select a file!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    if (isEncrypted(file)) {
                        operation(key, file, false);
                    } else {
                        JOptionPane.showMessageDialog(null, "File is already Decrypted!");
                    }
                }

            } catch (NumberFormatException no_key) {
                JOptionPane.showMessageDialog(null, "key cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        });

        f.setLayout(new FlowLayout());
        f.add(label);
        f.add(tf);
        f.add(cf);
        f.add(enc);
        f.add(dec);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}