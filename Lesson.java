import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;

public abstract class Lesson implements Serializable{
    private static final long serialVersionUID = 1l;
    /**
     * DayOfWeek of Lesson
     */
    protected DayOfWeek dayOfWeek;
    /**
     * timeSlot of Lesson
     */
    protected ArrayList<LocalTime> timeSlot;
    /**
     * venue of Lesson
     */
    protected Venue venue;
    /**
     * Creates a lesson with DayofWeek and timeslot
     * @param dayOfWeek dayOfWeek of lesson
     * @param timeSlot timeSlot of lesson
     */
    public Lesson(DayOfWeek dayOfWeek, ArrayList<LocalTime> timeSlot){
        this.dayOfWeek = dayOfWeek;
        this.timeSlot = timeSlot;
    }
    /**
     * Gets the DayofWeek of lesson
     */
	//Getters
    public DayOfWeek getDayOfWeek(){
        return this.dayOfWeek;
    }
    /**
     * Gets Timeslot of lesson
     */
    public ArrayList<LocalTime> getTimeSlot(){
        return this.timeSlot;
    }
    /**
     * Gets venue of lesson
     * @return venue of lesson
     */
    public Venue getVenue(){
        return this.venue;
    }
    /**
     * Adds a lesson to a venue
     */
    protected boolean setVenue(Venue venue){
        if (venue.addLesson(this)){
            this.venue = venue;
            return true;
        }else{
            return false;
        }
    }
    /**
     * Converts Lesson into a String
     * @param withVenue whether to print out venue of lesson  
     * @return Name of venue in String
     */
    public abstract String toString(boolean withVenue);
}
