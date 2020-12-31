import java.util.ArrayList;
import java.time.LocalTime;
import java.time.DayOfWeek;

public class Tutorial extends Lesson{
    private static final long serialVersionUID = 1L;
    private Index index;

    public Tutorial(Index index, DayOfWeek dayOfWeek, ArrayList<LocalTime> timeSlot, Venue venue){
        super(dayOfWeek, timeSlot);
        this.index = index;
        this.setVenue(venue);
    }


    public Index getIndex(){
        return this.index;
    }

    public String toString(boolean withVenue){
        if (withVenue){
            return this.index.getCourse().getCourseCode() + " TUT " + this.index.getIndexNumber() + " " + this.getVenue().getLocation();
        } else{
            return this.index.getCourse().getCourseCode() + " TUT " + this.index.getIndexNumber();
        }
    }
}
