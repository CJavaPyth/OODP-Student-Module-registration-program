import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;

public class Course implements Serializable{
    public static enum SCHOOL{SCSE, CEE, MAE, EEE, SCBE, MSE, NBS, ADM, SoH, SSS, WKWSCI, SBS, SPMS, ASE, LKCSoM, NIE};
    public static enum CourseType {
        LECT("Lecture only"), 
        LECT_TUT("Lecture and Tutorial"), 
        LECT_TUT_LAB("Lecture, Tutorial and Lab");
        private final String name;
        private CourseType(String s){
            name = s;
        }
        public String toString(){
            return this.name;
        }
    };
    private static final long serialVersionUID = 1l;
    private String courseCode;
    private String name;
    private SCHOOL school;
    private CourseType courseType;
    private int AU;
    private Lesson lecture;
    private ArrayList<Index> indexes;

    public Course(String courseCode, String name, SCHOOL school, CourseType courseType, int AU){
        this.courseCode = courseCode;
        this.name = name;
        this.school = school;
        this.courseType = courseType;
        this.AU = AU;
        this.indexes = new ArrayList<>();
    }

    public void createLecture(DayOfWeek lessonDay, ArrayList<LocalTime> timeSlots, Venue venue){
        Lesson lecture = new Lecture(this, lessonDay, timeSlots, venue);
        this.lecture = lecture;
    }

    //getters

    public String getCourseCode(){
        return this.courseCode;
    }

    public ArrayList<Index> getIndexes(){
        return new ArrayList<Index>(this.indexes);
    }

    public int getAUs(){
        return this.AU;
    }

    public Lesson getLecture(){
        return this.lecture;
    }

    public String getSchool(){
        return this.school.name();
    }

    public String getName(){
        return this.name;
    }

    public CourseType getCourseType(){
        return this.courseType;
    }

    //setters
    public void setCourseCode(String courseCode){
        this.courseCode = courseCode;
    }

    public void setSchool(SCHOOL school){
        this.school = school;
    }

    public void printIndexes(){
        System.out.println("Index: Index Number/Vacancy/Length of Waitlist");
        for (Index i: this.indexes){
            System.out.println(i.getIndexNumber() + "/" + i.getVacancy() + "/" + i.getWaiting());
        }
    }

    public Index createIndex(int indexNumber, int vacancy){
        Index newIndex = new Index(indexNumber, this, vacancy);
        this.indexes.add(newIndex);
        return newIndex;
    }
   
}
