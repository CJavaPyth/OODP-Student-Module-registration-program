import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;

public class Venue implements Serializable{
    private static final long serialVersionUID = 1l;
    /** 
     * The name of this venue 
     */ 
    private final String location;
    /** 
     * The timetable of this venue
    */
    private TimeTable timeTable;
    /**
     * Creates a new Venue with the given location name
     * @param location This Venue's location
     */
    public Venue(String location){
        this.location = location;
        this.timeTable = new TimeTable();
    }
    /**
     * Gets the location of this venue
     * @return this Venue's location
     */

    public String getLocation(){
        return this.location;
    }
    /**
     * Adds a lesson for this venue
     * @param lesson this lesson's timeSlot and DayOfWeek
     * @return whether lesson can be added to this venue 
     */
    public boolean addLesson(Lesson lesson){
        DayOfWeek dayOfWeek = lesson.getDayOfWeek();
        ArrayList<LocalTime> timeSlot = lesson.getTimeSlot();
        if (lesson instanceof Lab){
            Lab lab = (Lab) lesson;
            if (checkIfAvailable(dayOfWeek, timeSlot, lab.isOdd())){
                this.timeTable.addLessonToTimeTable(lab);
                return true;
            }
        } else{
            if (checkIfAvailable(dayOfWeek, timeSlot)){
                this.timeTable.addLessonToTimeTable(lesson);
                return true;
            }
        }
        return false;
    }
    /**
     * Checks if this Venue is available for tutorial lessons
     * @param dayOfWeek DayOfWeek of the tutorial to be added to this venue
     * @param timeSlots Timeslot of the tutorial to be added to this venue
     * @return whether the tutorial can be added to this venue
     */
    public boolean checkIfAvailable(DayOfWeek dayOfWeek, ArrayList<LocalTime> timeSlots){
        return this.timeTable.checkIfAvailable(dayOfWeek, timeSlots);
    }
    /**
     * Checks if this Venue is available for Lab lessons
     * @param dayOfWeek DayOfWeek of the lab to be added to this venue
     * @param timeSlots Timeslot of the lab to be added to this venue
     * @param isOdd whether the lab is on Odd or Even week
     * @return whether the lab can be added to this venue
     */
    public boolean checkIfAvailable(DayOfWeek dayOfWeek, ArrayList<LocalTime> timeSlots, boolean isOdd){
        return this.timeTable.checkIfAvailable(dayOfWeek, timeSlots, isOdd);
    }
    /**
     * Prints the timetable of this venue
     */
    public void printVenue(){
        System.out.println("Timetable for Venue " + this.location);
        this.timeTable.printTimeTable(false);
    }
}
