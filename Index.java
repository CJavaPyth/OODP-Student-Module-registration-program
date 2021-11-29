import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Index implements Serializable{
    private static final long serialVersionUID = 1l;
    private int indexNumber;
    private Course course;
    private int vacancy;
    private int totalSize;
    private ArrayList<Student> waitingList;
    private ArrayList<Student> students;
    private Lesson tutorial;
    private Lesson lab;

    public Index(int indexNumber, Course course, int vacancy){
        this.indexNumber = indexNumber;
        this.course = course;
        this.vacancy = vacancy;
        this.totalSize = vacancy;
        this.students = new ArrayList<>();
        this.waitingList = new ArrayList<>();
    }

    //Getters
    public int getIndexNumber(){
        return this.indexNumber;
    }

    public Course getCourse(){
        return this.course;
    }

    public int getVacancy(){
        return this.vacancy;
    }

    public int getWaiting(){
        return this.waitingList.size();
    }

    public int getTotalSize(){
        return this.totalSize;
    }

    public ArrayList<Student> getStudentsEnrolled(){
        return new ArrayList<Student>(this.students);
    }

    public Lesson getTutorial(){
        return this.tutorial;
    }

    public Lesson getLab(){
        return this.lab;
    }
    
    //can only add a student that is not already enrolled or in waiting list -- checked in student class
    public boolean addStudent(Student student){
        if (vacancy>0){
            this.students.add(student);
            vacancy--;
            return true;
        } else{
            this.waitingList.add(student);
            return false;
        }
    }

    //removeStudent removes a student enrolled in this index or from the waiting list
    public boolean removeStudent(Student student){
        Student removed = null;
        for (int i = 0; i<this.students.size(); i++){
            if (this.students.get(i).getMatriculationID().equals(student.getMatriculationID())){
                System.out.println("Successfully removed from index"); //////////////
                removed = this.students.remove(i);
                break;
            }
        }
        if (removed != null) {
            if ((this.waitingList.size() != 0) && (this.vacancy == 0)){
                Student newStudent = this.waitingList.remove(0);
                System.out.println("Student polled is " + newStudent.getName()); /////
                this.students.add(newStudent);
                newStudent.enrolledFromWaitingList(this);
            } else{
                this.vacancy++;
            }
            return true;
        } else{ //student to be removed is not enrolled - check if in waiting list
            for (int i = 0; i<this.waitingList.size(); i++){
                if (this.waitingList.get(i).getMatriculationID().equals(student.getMatriculationID())){
                    removed = this.waitingList.remove(i);
                    return true;
                }
            }
            return false; //student is neither enrolled nor in waiting list
        }
    }

    public void swapStudent(Student oldStudent, Student newStudent){
        this.students.remove(oldStudent);
        this.students.add(newStudent);
    }

    public boolean createLesson(DayOfWeek dayOfWeek, ArrayList<LocalTime> timeSlot, Venue venue){
        if (venue.checkIfAvailable(dayOfWeek, timeSlot)){
            Lesson tutorial = new Tutorial(this, dayOfWeek, timeSlot, venue);
            this.tutorial = tutorial;
            return true;
        }
        return false;
    }

    public boolean createLesson(DayOfWeek dayOfWeek, ArrayList<LocalTime> timeSlot, Venue venue, boolean isOdd){
        if (venue.checkIfAvailable(dayOfWeek, timeSlot, isOdd)){
            Lesson lab = new Lab(this, dayOfWeek, timeSlot, venue, isOdd);
            this.lab = lab;
            return true;
        }
        return false;
    }

    public void printIndex(){
        StringBuilder sb = new StringBuilder();
        sb.append("\nIndex " + this.indexNumber + " for Course " + this.course.getCourseCode() + "\n");
        sb.append("Vacancy remaining: " + this.getVacancy() + "/" + this.getTotalSize() + "\n");
        sb.append("Length of waiting list: " + this.getWaiting() + "\n");
        sb.append("Lecture: " + this.course.getLecture().getDayOfWeek().name() + " " 
                            + this.course.getLecture().getTimeSlot().get(0).toString() + " - "
                            + this.course.getLecture().getTimeSlot().get(0).plusHours(this.course.getLecture().getTimeSlot().size()) + " "
                            + this.course.getLecture().getVenue().getLocation() + "\n");
        if (this.tutorial != null){
            sb.append("Tutorial: " + this.tutorial.getDayOfWeek().name() + " " 
                    + this.tutorial.getTimeSlot().get(0).toString() + " - "
                    + this.tutorial.getTimeSlot().get(0).plusHours(this.tutorial.getTimeSlot().size()) + " "
                    + this.tutorial.getVenue().getLocation() + "\n");
        }
        if (this.lab != null){
            String week;
            if (((Lab)this.lab).isOdd()){
                week = "Odd";
            } else{
                week = "Even";
            }
            sb.append("Lab: " + this.lab.getDayOfWeek().name() + " " 
                    + this.lab.getTimeSlot().get(0).toString() + " - "
                    + this.lab.getTimeSlot().get(0).plusHours(this.lab.getTimeSlot().size()) + " "
                    + this.lab.getVenue().getLocation() + " " + week + " week\n");
        }
        System.out.println(sb.toString());
    }
        
}