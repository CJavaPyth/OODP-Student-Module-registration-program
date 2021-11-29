import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;

public class Lecture extends Lesson {
    private static final long serialVersionUID = 1L;
    /**
     * The course that this lecture belongs to
     */
    private Course course;
    /**
     * Creates a Lecture
     * @param course course that this lecture belongs to
     * @param dayOfWeek dayOfWeek of lecture(inherit from Lesson class)
     * @param timeSlot  timeSlot of lecture(inherit from Lesson class)
     * @param venue venue of lecture
     */
    public Lecture(Course course, DayOfWeek dayOfWeek, ArrayList<LocalTime> timeSlot, Venue venue){
        super(dayOfWeek, timeSlot);
        this.course = course;
        this.setVenue(venue);
    }

    //Getter
    /**
     * returns course that this lecture belongs to
     * @return Course that this lecture belongs to
     */
    public Course getCourse(){
        return this.course;
    }
    /**
     * Converts Lecture to string for printing to console
     */
    public String toString(boolean withVenue){
        if (withVenue){
            return this.getCourse().getCourseCode() + " LEC " + this.getVenue().getLocation();
        } else{
            return this.getCourse().getCourseCode() + " LEC";
        }
    }

}
