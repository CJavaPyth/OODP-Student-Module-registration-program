import java.time.LocalTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.TreeMap;
import java.io.Serializable;
import java.time.DayOfWeek;

public class TimeTable implements Serializable{
    private static final long serialVersionUID = 1l;
    /**
     * The different timeslots available(0830-1930 hrs)
     */
    public static final LocalTime EIGHT_THIRTY = LocalTime.of(8, 30);
    public static final LocalTime NINE_THIRTY = LocalTime.of(9, 30);
    public static final LocalTime TEN_THIRTY = LocalTime.of(10, 30);
    public static final LocalTime ELEVEN_THIRTY = LocalTime.of(11,30);
    public static final LocalTime TWELVE_THIRTY = LocalTime.of(12, 30);
    public static final LocalTime THIRTEEN_THIRTY = LocalTime.of(13, 30);
    public static final LocalTime FOURTEEN_THIRTY = LocalTime.of(14, 30);
    public static final LocalTime FIFTEEN_THIRTY = LocalTime.of(15, 30);
    public static final LocalTime SIXTEEN_THIRTY = LocalTime.of(16, 30);
    public static final LocalTime SEVENTEEN_THIRTY = LocalTime.of(17, 30);
    public static final LocalTime EIGHTEEN_THIRTY = LocalTime.of(18, 30);
    public static final LocalTime NINETEEN_THIRTY = LocalTime.of(19, 30);
    public static final LocalTime[] TIMESLOTS = {EIGHT_THIRTY, NINE_THIRTY, TEN_THIRTY, ELEVEN_THIRTY, TWELVE_THIRTY, THIRTEEN_THIRTY, FOURTEEN_THIRTY, FIFTEEN_THIRTY, SIXTEEN_THIRTY,
                                                SEVENTEEN_THIRTY, EIGHTEEN_THIRTY, NINETEEN_THIRTY};
    private EnumMap<DayOfWeek, TreeMap<LocalTime, Lesson[]>> timeTable; //Lesson array {odd week, even week}
    /**
    * Creates a timetable with all timeslots(0830-1930 hrs) from Monday-Friday
     */
    public TimeTable(){
        this.timeTable = new EnumMap<DayOfWeek, TreeMap<LocalTime, Lesson[]>>(DayOfWeek.class);
        this.timeTable.put(DayOfWeek.MONDAY, new TreeMap<>());
        this.timeTable.put(DayOfWeek.TUESDAY, new TreeMap<>());
        this.timeTable.put(DayOfWeek.WEDNESDAY, new TreeMap<>());
        this.timeTable.put(DayOfWeek.THURSDAY, new TreeMap<>());
        this.timeTable.put(DayOfWeek.FRIDAY, new TreeMap<>());
        for (LocalTime timeSlot: TIMESLOTS){
            this.timeTable.get(DayOfWeek.MONDAY).put(timeSlot, new Lesson[]{null, null});
            this.timeTable.get(DayOfWeek.TUESDAY).put(timeSlot, new Lesson[]{null, null});
            this.timeTable.get(DayOfWeek.WEDNESDAY).put(timeSlot, new Lesson[]{null, null});
            this.timeTable.get(DayOfWeek.THURSDAY).put(timeSlot, new Lesson[]{null, null});
            this.timeTable.get(DayOfWeek.FRIDAY).put(timeSlot, new Lesson[]{null, null});
        }
    }

    /**
     * Gets the timetable of Student or Venue
     * @return EnumMap 
     */
    public EnumMap<DayOfWeek, TreeMap<LocalTime, Lesson[]>> getTimeTable(){
        return this.timeTable;
    }
    /**
     * Checks if Lesson can be added to timetable of student or venue
     * @param lesson DayOfWeek and Timeslot of lesson to be added
     * @return whether the lesson can be added to timetable of student or venue
     */
    public boolean addLessonToTimeTable(Lesson lesson){
        DayOfWeek dayOfWeek = lesson.getDayOfWeek();
        ArrayList<LocalTime> timeSlot = lesson.getTimeSlot();
        if (lesson instanceof Lab){
            Lab lab = (Lab) lesson;
            int week = lab.isOdd() ? 0 : 1;
            for (LocalTime t: timeSlot){
                this.timeTable.get(dayOfWeek).get(t)[week] = lesson;
            }
            return true;
        } else{
            for (LocalTime t: timeSlot){
                this.timeTable.get(dayOfWeek).get(t)[0] = lesson;
                this.timeTable.get(dayOfWeek).get(t)[1] = lesson;
            }
            return true;
        }
    }
    /**
     * Checks if Lesson can be removed from timetable of student or venue
     * @param lesson DayOfWeek and Timeslot of lesson to be removed
     * @return whether the lesson can be removed from timetable of student or venue
     */
    public boolean removeLessonFromTimeTable(Lesson lesson){
        DayOfWeek dayOfWeek = lesson.getDayOfWeek();
        ArrayList<LocalTime> timeSlots = lesson.getTimeSlot();
        if (lesson instanceof Lab){
            Lab lab = (Lab) lesson;
            int week = lab.isOdd() ? 0 : 1;
            for (LocalTime t: timeSlots){
                timeTable.get(dayOfWeek).get(t)[week] = null;
            }
        } else{
            for (LocalTime t: timeSlots){
                timeTable.get(dayOfWeek).get(t)[0] = null;
                timeTable.get(dayOfWeek).get(t)[1] = null;
            }
        }
        return true;
    }
    /**
     * Checks if DayOfWeek and Timeslot of tutorial to be added clashes with another lesson in the timetable 
     * @param dayOfWeek DayOfWeek of tutorial to be added
     * @param timeSlots Timeslot of tutorial to be added
     * @return if clash return true, if no clash return false
     */
    public boolean checkIfAvailable(DayOfWeek dayOfWeek, ArrayList<LocalTime> timeSlots){
        boolean available = true;
        for (LocalTime t: timeSlots){
            if (this.timeTable.get(dayOfWeek).get(t)[0] != null || this.timeTable.get(dayOfWeek).get(t)[1] != null){
                available = false;
                break;
            }
        }
        return available;
    }
    /**
     * Checks if DayOfWeek and Timeslot of lab to be added clashes with another lesson in the timetable
     * @param dayOfWeek DayOfWeek of lab to be added
     * @param timeSlots Timeslot of lab to be added
     * @param isOdd whether lab is on Odd or Even week
     * @return if clash return true, if no clash return false
     */
    public boolean checkIfAvailable(DayOfWeek dayOfWeek, ArrayList<LocalTime> timeSlots, boolean isOdd){
        boolean available = true;
        int week = isOdd ? 0 : 1;
        for (LocalTime t: timeSlots){
            if (this.timeTable.get(dayOfWeek).get(t)[week] != null){
                available = false;
                break;
            }
        }
        return available;
    }

    /**
     * Prints Timetable of student/Venue
     * @param withVenue if printing student's timetable, withVenue will be true, Venue would show in the timetable
     *                  if printing venue's timetable, withVeneue will be false
     */
    public void printTimeTable(boolean withVenue){
        String oddLesson;
        String evenLesson;
        for (DayOfWeek day: timeTable.keySet()){
            System.out.println(day.toString());
            for (LocalTime t: timeTable.get(day).keySet()){
                if (timeTable.get(day).get(t)[0] != null){
                    oddLesson = timeTable.get(day).get(t)[0].toString(withVenue);
                } else{
                    oddLesson = "NIL";
                }
                if (timeTable.get(day).get(t)[1] != null){
                    evenLesson = timeTable.get(day).get(t)[1].toString(withVenue);
                } else{
                    evenLesson = "NIL";
                }
                System.out.println(t + ": Odd Week - " + String.format("%-30s", oddLesson) + " Even Week - " + evenLesson);
            }
            System.out.println();
        }
    }
}
