import com.sun.tools.javac.Main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.ArrayList;

public class StudentManager {

    public static void main(String[] args) {
        JFrame frame = new JFrame("GradeBook");

        JPanel panel = new MainMenu().panel1;
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(1000,520);

        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
    }
}
