import java.io.Serializable;
import java.util.ArrayList;

public class Student implements Serializable {

    public String name;
    public double average;
    public String gender;
    public int age;
    public String notes;
    public ArrayList<Double> grades;

    public Student() {
        name = "James Harlem";
        average = 100;
        gender = "Male";
        age = 17;
        notes = "Very Smart.";
        grades = new ArrayList<>();

        grades.add(100.0);
    }

    public Student(String pname, double paverage, String pgender, int page, String pnotes) {
        name = pname;
        average = paverage;
        gender = pgender;
        age = page;
        notes = pnotes;

        grades = new ArrayList<>();
        grades.add(paverage);
    }

    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void addGrade(double grade) {
        grades.add(grade);
    }

    public Double getAverage() {
        double avg = 0;
        for (Double grade : grades) {
            avg = avg + grade;
        }
        this.average = avg / grades.size();
        return average;
    }
    
    public String detailedView() {
        return "Name: " + this.name + "\nAverage: " + this.average + "\nGender: " + this.gender + "\nAge: " + this.age + "\nNotes: " + this.notes + "\nGrades: " + this.grades.toString();
    }
}
