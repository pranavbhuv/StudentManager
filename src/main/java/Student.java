import java.util.ArrayList;

public class Student {

    public String name;
    public double average;
    public String gender;
    public int age;
    public String notes;
    public ArrayList<ArrayList<Double>> grades;

    public Student() {
        name = "Maruthi Suzuki";
        average = 100;
        gender = "Male";
        age = 17;
        notes = "Very Smart.";

        grades = new ArrayList<>();
        ArrayList<Double> daily = new ArrayList<>();
        ArrayList<Double> major = new ArrayList<>();
        grades.add(daily);
        grades.add(major);

        weirdFix();
    }

    public Student(String pname, double paverage, String pgender, int page, String pnotes) {
        name = pname;
        average = paverage;
        gender = pgender;
        age = page;
        notes = pnotes;

        grades = new ArrayList<>();
        ArrayList<Double> daily = new ArrayList<>();
        ArrayList<Double> major = new ArrayList<>();
        grades.add(daily);
        grades.add(major);
        weirdFix();
    }

    public String getName() { return name; }

    public void addGrade(double grade, int decider) {
        // 0 means daily, 1 means major
        if (decider == 0) {
            grades.get(0).add(grade);
        } else if (decider == 1) {
            grades.get(1).add(grade);
        }
    }

    public double getAverage() { return average; }

    public Double getCalculatedAverage() {
        double dailyavg = 0;
        double majoravg = 0;
        for(int i = 0; i < grades.get(0).size(); i++) {
            dailyavg = dailyavg + grades.get(0).get(i);
        }
        for(int i = 0; i < grades.get(1).size(); i++) {
            majoravg = majoravg + grades.get(1).get(i);
        }
        this.average = ((dailyavg/grades.get(0).size()) + (majoravg/grades.get(1).size())) / 2.0;
        return average;
    }
    
    public String detailedView() {
        return "Name: " + this.name + "\nAverage: " + this.average + "\nGender: " + this.gender + "\nAge: " + this.age + "\nNotes: " + this.notes + "\nDaily Grade: " + grades.get(0).toString() + "\nMajor Grade: " + grades.get(1).toString();
    }

    public void weirdFix() {
        grades.get(0).add(100.0);
        grades.get(1).add(100.0);
    }
}
