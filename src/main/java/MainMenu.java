import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import javax.swing.JOptionPane;

public class MainMenu implements Serializable {

    public JPanel panel1;
    private JTextField name;
    private JTextField gpa;
    private JComboBox<Integer> age;
    private JButton addStudentButton;
    private JTextField notes;
    private JComboBox<String> gender;
    private JTextArea textArea1;
    private JTextField gradeAdd;
    private JButton submitButton;
    private JComboBox<String> studentSelect;
    private JComboBox<String> deleteStudent;
    private JButton submitButton1;
    private JComboBox<String> viewStudent;
    private JButton submitButton2;
    private JButton randomFillButton;
    private JButton saveButton;
    private JButton rankingsButton;
    private JButton button2;
    private JComboBox<String> dailyOrMajor;

    public ArrayList<Student> studentList = new ArrayList<>();

    private final String pathway = ".//data//data.txt";

    private final String formatter = "\t%-20.20s\t %-8.8s\t %-6.6s\t %-2.5s\t %-2.5s\n";

    MainMenu() {

        File tempFile = new File(pathway);
        boolean exists = tempFile.exists();

        if (exists) {
            int dialogResult = JOptionPane.showConfirmDialog(panel1,
                    "Would you like to load previously saved data?",
                    "Data Detected",
                    JOptionPane.YES_NO_OPTION);
            if (dialogResult == 0) {
                try {
                    load();
                    refresh();
                } catch (Exception ignored) {
                }
            } else if (dialogResult == 1) {
                textArea1.append(String.format(formatter, "Full Name", "Average", "Gender", "Age", "Notes") + "\n");
                studentList.add(new Student());
                refresh();
            }
        } else {
            textArea1.append(String.format(formatter, "Full Name", "Average", "Gender", "Age", "Notes") + "\n");
            studentList.add(new Student());
            refresh();
        }

        neverChange();
        comboSetter();

        addStudentButton.addActionListener(e -> {
            try {
                studentList.add(new Student(name.getText(), Double.parseDouble(gpa.getText()), Objects.requireNonNull(gender.getSelectedItem()).toString(), Integer.parseInt(Objects.requireNonNull(age.getSelectedItem()).toString()), notes.getText()));
                addButton(studentList.size() - 1);
            } catch (Exception err) {
                JOptionPane.showMessageDialog(panel1, "Please Input Values Before Registering");
                throw err;
            }
        });
        submitButton.addActionListener(e -> {
            for (Student student : studentList) {
                if (student.getName().equals(studentSelect.getSelectedItem())) {
                    int decider;
                    if (Objects.requireNonNull(dailyOrMajor.getSelectedItem()).toString().equals("Daily")) {
                        decider = 0;
                    } else {
                        decider = 1;
                    }
                    student.addGrade(Double.parseDouble(gradeAdd.getText()), decider);
                    JOptionPane.showMessageDialog(panel1, "Added Grade.");
                }
            }
            refresh();
        });
        submitButton1.addActionListener(e -> {
            for (int i = 0; i < studentList.size(); i++) {
                if (studentList.get(i).getName().equals(deleteStudent.getSelectedItem())) {
                    studentList.remove(i);
                    break;
                }
            }
            studentSelect.removeAllItems();
            deleteStudent.removeAllItems();
            viewStudent.removeAllItems();
            comboSetter();
            refresh();
        });
        submitButton2.addActionListener(e -> {
            int index = 0;
            for (int i = 0; i < studentList.size(); i++) {
                if (studentList.get(i).getName().equals(viewStudent.getSelectedItem())) {
                    index = i;
                }
            }
            JOptionPane.showMessageDialog(panel1, studentList.get(index).detailedView());
        });
        randomFillButton.addActionListener(e -> {
            for (int i = 0; i < 12; i++) {
                randomFill(i);
            }
            studentSelect.removeAllItems();
            deleteStudent.removeAllItems();
            viewStudent.removeAllItems();
            comboSetter();
            refresh();
        });
        saveButton.addActionListener(e -> {
            try {
                Arrays.stream(Objects.requireNonNull(new File(".//data//list//").listFiles())).forEach(File::delete);
                PrintWriter pw = new PrintWriter(new FileOutputStream(pathway));
                for (Student temp : studentList) {
                    pw.println(temp.name);
                    DumperOptions options = new DumperOptions();
                    options.setIndent(2);
                    options.setPrettyFlow(true);
                    options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
                    Yaml yaml = new Yaml(options);
                    PrintWriter writer = new PrintWriter(new File(".//data//list//" + temp.getName() + ".yml"));
                    yaml.dump(temp, writer);
                }
                pw.close();
                JOptionPane.showMessageDialog(panel1, "Saved Students");
            } catch (IOException error) {
                error.printStackTrace();
            }
        });
        rankingsButton.addActionListener(e -> {
            BinaryTree bt = new BinaryTree();
            for (Student student : studentList) {
                bt.add(student);
            }
            bt.traverseInOrder(bt.root);
            JOptionPane.showMessageDialog(panel1, bt.beautify());
            bt.resetStack();
        });
    }

    public void load() throws IOException {
        FileInputStream fstream = new FileInputStream(".//data//data.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        String strLine;
        while ((strLine = br.readLine()) != null) {
            InputStream inputStream = new FileInputStream(new File(".//data//list//" + strLine + ".yml"));
            Yaml yaml = new Yaml(new Constructor(Student.class));
            Student data = yaml.load(inputStream);
            studentList.add(data);
            System.out.println(data.getName() + " object made.");
            populate(studentList.indexOf(data));
        }
        fstream.close();
    }

    public void populate(int i) {
        Student def = studentList.get(i);
        textArea1.append(String.format(formatter, def.name, def.average, def.gender, def.age, def.notes));
    }

    public void addButton(int i) {
        Student def = studentList.get(i);
        textArea1.append(String.format(formatter, def.name, def.average, def.gender, def.age, def.notes));
        studentSelect.addItem(studentList.get(i).getName());
        deleteStudent.addItem(studentList.get(i).getName());
        viewStudent.addItem(studentList.get(i).getName());
    }

    public void neverChange() {
        gender.addItem("Male");
        gender.addItem("Female");
        gender.addItem("Other");

        dailyOrMajor.addItem("Daily");
        dailyOrMajor.addItem("Major");

        for (int i = 11; i < 22; i++) {
            age.addItem(i);
        }
    }

    public void comboSetter() {
        for (Student student : studentList) {
            studentSelect.addItem(student.getName());
            deleteStudent.addItem(student.getName());
            viewStudent.addItem(student.getName());
        }
    }

    public void refresh() {
        textArea1.setText(null);
        textArea1.append(String.format(formatter, "Full Name", "Average", "Gender", "Age", "Notes") + "\n");
        for (Student def : studentList) {
            textArea1.append(String.format(formatter, def.name, def.getAverage(), def.gender, def.age, def.notes));
        }
    }

    public void randomFill(int i) {
        String[] namess = {"Jack Mani", "Jill Berryman", "Risheel Fahul", "James Mahabition", "Jennifer Yao", "Tanya Ishali", "William Zhang", "Anika Maruthi", "Hermes Filter", "Alex Wegner", "James Madison", "Hannah Wells"};
        Double[] averagee = {90.9, 50.0, 30.6, 20.0, 59.3, 85.5, 53.3, 88.2, 55.2, 75.4, 27.4, 69.3};
        String[] genderr = {"Male", "Female", "Male", "Male", "Female", "Female", "Male", "Male", "Female", "Male", "Male", "Female"};
        Integer[] agee = {17, 17, 17, 16, 16, 18, 16, 17, 16, 16, 16, 17};
        String[] notess = {"Cheated", "Creative Personality", "Needs new PC", "Lagging Behind", "Quiet Kid", "Need to do Homework", "Cheated", "Retake Test", "Talkative", "Contact Parents", "Listens Attentively", "Hardworking Character"};
        studentList.add(new Student(namess[i], averagee[i], genderr[i], agee[i], notess[i]));
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 4, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel2.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        textArea1 = new JTextArea();
        textArea1.setEditable(false);
        scrollPane1.setViewportView(textArea1);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        randomFillButton = new JButton();
        randomFillButton.setText("Random Fill");
        panel3.add(randomFillButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        saveButton = new JButton();
        saveButton.setText("Save");
        panel3.add(saveButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rankingsButton = new JButton();
        rankingsButton.setText("Rankings");
        panel3.add(rankingsButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        button2 = new JButton();
        button2.setText("Button");
        panel3.add(button2, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel4, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(6, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel5, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Name");
        panel5.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        name = new JTextField();
        panel5.add(name, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Average");
        panel5.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        gpa = new JTextField();
        panel5.add(gpa, new GridConstraints(1, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Gender");
        panel5.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Age");
        panel5.add(label4, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        age = new JComboBox();
        panel5.add(age, new GridConstraints(3, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Notes");
        panel5.add(label5, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        notes = new JTextField();
        panel5.add(notes, new GridConstraints(4, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
        gender = new JComboBox();
        panel5.add(gender, new GridConstraints(2, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
        addStudentButton = new JButton();
        addStudentButton.setText("Submit");
        panel5.add(addStudentButton, new GridConstraints(5, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Register Student");
        panel4.add(label6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel6, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Add Grade");
        panel6.add(label7, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel6.add(panel7, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("Student");
        panel7.add(label8, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        studentSelect = new JComboBox();
        panel7.add(studentSelect, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("Grade");
        panel7.add(label9, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        gradeAdd = new JTextField();
        panel7.add(gradeAdd, new GridConstraints(1, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        dailyOrMajor = new JComboBox();
        panel7.add(dailyOrMajor, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        submitButton = new JButton();
        submitButton.setText("Submit");
        panel6.add(submitButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel8, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label10 = new JLabel();
        label10.setText("Delete Student");
        panel8.add(label10, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel8.add(panel9, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label11 = new JLabel();
        label11.setText("Student");
        panel9.add(label11, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        deleteStudent = new JComboBox();
        panel9.add(deleteStudent, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        submitButton1 = new JButton();
        submitButton1.setText("Submit");
        panel4.add(submitButton1, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel10 = new JPanel();
        panel10.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel10, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label12 = new JLabel();
        label12.setText("View Student");
        panel10.add(label12, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel11 = new JPanel();
        panel11.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel10.add(panel11, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label13 = new JLabel();
        label13.setText("Student");
        panel11.add(label13, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        viewStudent = new JComboBox();
        panel11.add(viewStudent, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        submitButton2 = new JButton();
        submitButton2.setText("Submit");
        panel1.add(submitButton2, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }

}
