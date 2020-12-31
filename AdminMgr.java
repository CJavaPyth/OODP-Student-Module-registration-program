import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalTime;

public class AdminMgr {
    public static Scanner sc = new Scanner(System.in);

    public static boolean editAccessPeriod(Date startDate, Date endDate){
        if (startDate.after(endDate)){
            return false; //end date cannot be before start date
        }
        try{
            FileOutputStream fo = new FileOutputStream("accessPeriod.ser");
            ObjectOutputStream out = new ObjectOutputStream(fo);
            ArrayList<Date> accessDates = new ArrayList<>();
            accessDates.add(startDate);
            accessDates.add(endDate);
            out.writeObject(accessDates);
            out.close();
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy, hh:mm a");
            System.out.println("Start date set to: " + dateFormat.format(startDate));
            System.out.println("End date set to: " + dateFormat.format(endDate));
        } catch (IOException e){
            e.printStackTrace();
        }
        return true;
    }

    public static boolean validateAdmin(String username, String password){
        String str;
        String adminUsername = null;
        String adminPassword = null;
        password = PasswordMD5.hashPassword(password);
        try{
            FileReader f = new FileReader("adminAccount.txt");
            BufferedReader b = new BufferedReader(f);
            str = b.readLine();
            String[] values = str.split("\\|");
            adminUsername = values[0];
            adminPassword = values[1];
            b.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        if (username.equals(adminUsername) && password.equals(adminPassword)){
            return true;
        } else{
            return false;
        }
    }

    
    public static boolean registerStudent(ArrayList<Student> students, String name, String nationality, int gender, String matriculationID, Course.SCHOOL school, String username){
        for (Student student: students){
            if (student.getMatriculationID().equals(matriculationID) || student.getUsername().equals(username)){
                return false; //cannot have 2 students with the same matriculationID or username
            }
        }
        //if a unique matriculation ID and username given, create the student
        String password = randomPasswordGenerator(8);
        Student newStudent = new Student(name, nationality, gender, matriculationID, school, username, password);
        addStudentAccount(username, password);
        students.add(newStudent);
        //Email user account details to student
        Email.passwordNotification(newStudent);
        System.out.println("Password is " + password);
        return true;
    }


    private static String randomPasswordGenerator(int length){
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i<length; i++){
            int randomNumber = (int) (characters.length() * Math.random());
            sb.append(characters.charAt(randomNumber));
        }
        return sb.toString();
    }

    private static void addStudentAccount(String username, String password){
        password = PasswordMD5.hashPassword(password);
        try{
            FileWriter fw = new FileWriter("studentAccount.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            pw.println(username + "|" + password);
            pw.flush();
            pw.close();
            bw.close();
            fw.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }


    public static void printAllStudents(ArrayList<Student> students){
        System.out.println("\nList of Students:");
        for (Student student: students){
            System.out.println(student.getName() + " (" + student.getGender() + ") " + student.getNationality() + " " + student.getMatriculationID() + " " + student.getSchool());
        }
    }

    public static boolean addCourse(ArrayList<Course> courses, String courseCode, String name, Course.SCHOOL school, Course.CourseType courseType, int AU, DayOfWeek lessonDay, ArrayList<LocalTime> lessonTimeSlots, Venue lessonVenue){
        for (Course course: courses){
            if (course.getCourseCode().equals(courseCode)){
                System.out.println("Course code already exists!");
                return false; //course codes must be unique
            }
        }
        if (lessonVenue.checkIfAvailable(lessonDay, lessonTimeSlots)){
            Course newCourse = new Course(courseCode, name, school, courseType, AU);
            newCourse.createLecture(lessonDay, lessonTimeSlots, lessonVenue);
            courses.add(newCourse);
            return true;
        } else{
            System.out.println("Venue selected is not available during selected time slots!");
            return false;
        }
    }

    public static void printAllCourses(ArrayList<Course> courses){
        System.out.println("\nList of all Courses:");
        for (Course course: courses){
            System.out.println(course.getCourseCode() + ": " + course.getName() + " " + course.getSchool() + " Number of AUs: " + course.getAUs());
        }
    }

    public static boolean updateCourse(ArrayList<Course> courses, String courseCode, String newCourseCode, Course.SCHOOL school){
        boolean found = false;
        boolean newAlreadyExists = false;
        for (Course course: courses){
            if (course.getCourseCode().equals(courseCode)){
                course.setSchool(school);
                found = true;
            }
            if (course.getCourseCode().equals(newCourseCode)){
                newAlreadyExists = true;
            }
        }
        if (!found){
            System.out.println("Course not found!");
            return false;
        }
        if (!courseCode.equals(newCourseCode)){
            if (newAlreadyExists){
                System.out.println("New course code already exists!");
            } else{
                for (Course course: courses){
                    if (course.getCourseCode().equals(courseCode)){
                        System.out.println("Course code changed from " + courseCode + " to " + newCourseCode);
                        course.setCourseCode(newCourseCode);
                    }
                }
            }
        }
        return true;
    }


    public static boolean addIndex(ArrayList<Course> courses, ArrayList<Venue> venues, String courseCode, int indexNumber, int vacancy){
        for (Course course: courses){
            if (course.getCourseCode().equals(courseCode)){
                for (Index i: course.getIndexes()){
                    if (i.getIndexNumber() == indexNumber){
                        System.out.println("Index Number already exists!");
                        return false; //index number already exists
                    }
                }
                Index newIndex = course.createIndex(indexNumber, vacancy);
                System.out.println("Index " + indexNumber + " created for course " + course.getCourseCode());
                if (course.getCourseType() == Course.CourseType.LECT){
                    return true;
                } else if (course.getCourseType() == Course.CourseType.LECT_TUT){
                    while(true){
                        if (createLessonForIndex(newIndex, venues, false)){
                            break;
                        }
                    }
                } else{
                    while (true){
                        if (createLessonForIndex(newIndex, venues, false)){
                            break;
                        }
                    }
                    while (true){
                        if (createLessonForIndex(newIndex, venues, true)){
                            break;
                        }
                    }
                }
                return true;
            }
        }
        System.out.println("Course not found!");
        return false;
    }

    private static boolean createLessonForIndex(Index index, ArrayList<Venue> venues, boolean isLab){
        boolean isOdd = true;
        if (isLab){
            System.out.println("Select time and location for Lab: ");
        } else{
            System.out.println("Select time and location for Tutorial: ");
        }
        String lessonType = isLab ? "lab" : "tutorial";
        System.out.print("Enter the day of the " + lessonType + " (1 for Monday to 5 for Friday): ");
		int lectDay = sc.nextInt();
		while (lectDay<1 || lectDay>5){
			System.out.print("Enter a valid day: ");
			lectDay = sc.nextInt();
		}
        DayOfWeek lessonDay = DayOfWeek.of(lectDay);
        System.out.print("Enter the duration (number of hours) of the " + lessonType + ": ");
		int lessonDuration = sc.nextInt();
		while (lessonDuration<=0){
			System.out.print("Lesson cannot be less than 1 hour. Enter a valid duration: ");
			lessonDuration = sc.nextInt();
        }
        System.out.println("Select the starting time slot ");
		for (int i = 1; i<=TimeTable.TIMESLOTS.length; i++){
			System.out.println("(" + i + ") " + TimeTable.TIMESLOTS[i-1].toString());
		}
		System.out.print("Select the starting time slot (1 - 12): ");
		int lessonStartTime = sc.nextInt() - 1;
		while (lessonStartTime<0 || lessonStartTime>11 || (lessonStartTime+lessonDuration)>12){
			System.out.print("Select a valid time slot: ");
			lessonStartTime = sc.nextInt() - 1;
		}
        ArrayList<LocalTime> lessonTimeSlots = new ArrayList<>();
        for (int i = 0; i < lessonDuration; i++){
           	lessonTimeSlots.add(TimeTable.TIMESLOTS[lessonStartTime + i]);
        }
        System.out.println("Select a venue (1-" + venues.size() + "): ");
        for (int i = 0; i<venues.size(); i++){
          	System.out.println((i+1) + ") " + venues.get(i).getLocation());
        }
        int venueInt = sc.nextInt();
        while (venueInt<1 || venueInt>venues.size()){
            System.out.print("Select a valid venue number: ");
          	venueInt = sc.nextInt();
        }
        Venue lessonVenue = venues.get(venueInt-1);
        if (isLab){
            System.out.print("Is the lab on odd week (0) or even week (1): ");
            int week = sc.nextInt();
            while (week!=0 && week!=1){
                System.out.print("Enter either 0 for odd week or 1 for even week: ");
                week = sc.nextInt();
            }
            if (week == 1){
                isOdd = false;
            }
        }
        if (isLab){
            if (index.createLesson(lessonDay, lessonTimeSlots, lessonVenue, isOdd)){
                System.out.println("Lab created successfully");
                return true;
            } else{
                System.out.println("The timing and location is not available. Please try again...\n");
                return false;
            }
        } else{
            if (index.createLesson(lessonDay, lessonTimeSlots, lessonVenue)){
                System.out.println("Tutorial created successfully");
                return true;
            } else{
                System.out.println("The timing and location is not available. Please try again...\n");
                return false;
            }
        }
    }

    public static boolean getIndexVacancy(ArrayList<Course> courses, String courseCode, int indexNumber){
        for (Course course: courses){
            if (course.getCourseCode().equals(courseCode)){
                for (Index i: course.getIndexes()){
                    if (i.getIndexNumber() == indexNumber){
                        System.out.println("Vacancy remaining: " + i.getVacancy() + "/" + i.getTotalSize());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean printStudentListByIndex(ArrayList<Course> courses, String courseCode, int indexNumber){
        for (Course course: courses){
            if (course.getCourseCode().equals(courseCode)){
                for (Index i: course.getIndexes()){
                    if (i.getIndexNumber() == indexNumber){
                        for (Student student: i.getStudentsEnrolled()){
                            System.out.println(student.getName() + " (" + student.getGender() + ") " + student.getNationality());
                        }
                        return true;
                    }
                }
            }
        }
        return false; //no such (course, index number) pair exists
    }

    public static boolean printStudentListByCourse(ArrayList<Course> courses, String courseCode){
        for (Course course: courses){
            if (course.getCourseCode().equals(courseCode)){
                for (Index i: course.getIndexes()){
                    for (Student student: i.getStudentsEnrolled()){
                        System.out.println(student.getName() + " (" + student.getGender() + ") " + student.getNationality());
                    }
                }
                return true;
            }
        }
        return false; //no such course exists
    }

    public static void printVenueTimetable(Venue venue){
        venue.printVenue();
    }
    
}