import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;

public class Lab extends Lesson {
    private static final long serialVersionUID = 1L;
    private Index index;
    private boolean isOdd;

    public Lab(Index index, DayOfWeek dayOfWeek, ArrayList<LocalTime> timeSlot, Venue venue, boolean isOdd){
        super(dayOfWeek, timeSlot);
        this.isOdd = isOdd;
        this.index = index;
        this.setVenue(venue);
    }

    public Index getIndex(){
        return this.index;
    }

    public boolean isOdd(){
        return this.isOdd;
    }

    public String toString(boolean withVenue){
        if (withVenue){
            return this.index.getCourse().getCourseCode() + " LAB " + this.index.getIndexNumber() + " " + this.getVenue().getLocation();
        } else{
            return this.index.getCourse().getCourseCode() + " LAB " + this.index.getIndexNumber();
        }
    }

}
