import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalTime;

public class AdminMenu {
	private static Scanner sc = new Scanner(System.in);
	public static void adminMenu(ArrayList<Student> students, ArrayList<Course> courses, ArrayList<Venue> venues) {
		boolean terminate = false;
		while (!terminate) {
			printMenuoptions();
			System.out.print("Please select a function and input the corresponding number: ");
			int choice = sc.nextInt();
			System.out.println();
			switch (choice) {
			case 1:
				//edit student period;
				sc.nextLine();
				System.out.println("======EDIT STUDENT ACCESS PERIOD======");
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				System.out.print("Enter the start date in the format (dd/mm/yyyy hh:mm) - 24 hour clock: ");
				String strStartDate = sc.nextLine();
				System.out.print("Enter the end date in the format (dd/mm/yyyy hh:mm) - 24 hour clock: ");
				String strEndDate = sc.nextLine();
				try{
					Date startDate = null;
					Date endDate = null;
					if (strStartDate != null && strStartDate.trim().length()>0){
						startDate = dateFormat.parse(strStartDate);
					} else{
						throw new ParseException("Date or format is not valid. Please try again!", 0);
					}
					if (strEndDate != null && strEndDate.trim().length()>0){
						endDate = dateFormat.parse(strEndDate);
					} else{
						throw new ParseException("Date or format is not valid. Please try again!", 0);
					}
					if (startDate != null && endDate != null){
						if (!AdminMgr.editAccessPeriod(startDate, endDate)){
							System.out.println("Start date cannot be later than end date. Please try again!");
						}
					}
				} catch (ParseException e){
					System.out.println("Please enter the date in the format specified!");
				}
				break;
			case 2:
				//add a student;
				sc.nextLine();
				System.out.println("======ADD A STUDENT======");
				System.out.print("Enter the student's name: ");
				String name = sc.nextLine();
				System.out.print("Enter the student's nationality: ");
				String nationality = sc.nextLine();
				System.out.print("Enter the student's gender (1 for Male/0 for Female): ");
				int gender = sc.nextInt();
				while (gender != 1 && gender != 0){
					System.out.print("Enter the student's gender (1 for Male/0 for Female): ");
					gender = sc.nextInt();
				}
				System.out.print("Enter the student's matriculation ID: ");
				String matriculationID = sc.next();
				for (int i = 1; i<=Course.SCHOOL.values().length; i++){
					System.out.println("(" + i + ") " + Course.SCHOOL.values()[i-1].name());
				}
				System.out.print("Select the student's school: ");
				int schoolInt = sc.nextInt();
				while (schoolInt < 1 || schoolInt > Course.SCHOOL.values().length){
					System.out.print("Select a valid school: ");
					schoolInt = sc.nextInt();
				}
				Course.SCHOOL school = Course.SCHOOL.values()[schoolInt-1];
				System.out.print("Enter the student's username: ");
				String username = sc.next();
				if (AdminMgr.registerStudent(students, name, nationality, gender, matriculationID, school, username)){
					AdminMgr.printAllStudents(students);
					break;
				} else{
					System.out.println("Student's matriculation ID or username already exists! Please try again.");
				}
				break;
			case 3:
				//add course;
				sc.nextLine();
				System.out.println("======ADD A COURSE======");
				System.out.print("Enter the course code: ");
				String courseCode = sc.nextLine();
				System.out.print("Enter the course name: ");
				String courseName = sc.nextLine();
				for (int i = 1; i<=Course.SCHOOL.values().length; i++){
					System.out.println("(" + i + ") " + Course.SCHOOL.values()[i-1].name());
				}
				System.out.print("Select the course's school: ");
				int schInt = sc.nextInt();
				while (schInt < 1 || schInt > Course.SCHOOL.values().length){
					System.out.print("Select a valid school: ");
					schInt = sc.nextInt();
				}
				Course.SCHOOL sch = Course.SCHOOL.values()[schInt-1];
				for (int i = 1; i<=Course.CourseType.values().length; i++){
					System.out.println("(" + i + ")" + Course.CourseType.values()[i-1].toString());
				}
				System.out.print("Select the course type: ");
				int courseTypeInt = sc.nextInt();
				while (courseTypeInt < 1 || courseTypeInt > Course.CourseType.values().length){
					System.out.print("Select a valid course type: ");
					courseTypeInt = sc.nextInt();
				}
				Course.CourseType courseType = Course.CourseType.values()[courseTypeInt-1];
				System.out.print("Enter the number of AUs for this course: ");
				int AU = sc.nextInt();
				while (AU<=0){
					System.out.print("AU must be greater than 0.. Enter number of AUs: ");
					AU = sc.nextInt();
				}
				System.out.println("Select time and location for the lecture: ");
				System.out.print("Enter the day of the lecture (1 for Monday to 5 for Friday): ");
				int lectDay = sc.nextInt();
				while (lectDay<1 || lectDay>5){
					System.out.print("Enter a valid day: ");
					lectDay = sc.nextInt();
				}
            	DayOfWeek lessonDay = DayOfWeek.of(lectDay);
				System.out.print("Enter the duration (number of hours) of the lecture: ");
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
				if (AdminMgr.addCourse(courses, courseCode, courseName, sch, courseType, AU, lessonDay, lessonTimeSlots, lessonVenue)){
					AdminMgr.printAllCourses(courses);
				} else{
					System.out.println("Failed to add course.. Please try again!");
				}
				break;
			case 4:
				//add new index;
				sc.nextLine();
				System.out.println("======ADD NEW INDEX======");
				System.out.print("Enter the course code to add new index to: ");
				String crsCode = sc.nextLine();
				System.out.print("Enter the new index number: ");
				int newIndexNum = sc.nextInt();
				System.out.print("Enter the vacancy (>0): ");
				int vacancy = sc.nextInt();
				while (vacancy<=0){
					System.out.print("Enter a vacancy > 0: ");
					vacancy = sc.nextInt();
				}
				if (!AdminMgr.addIndex(courses, venues, crsCode, newIndexNum, vacancy)){
					System.out.println("Failed to add index... Please try again!");
				}
				break;
			case 5:
				//update course
				sc.nextLine();
				System.out.println("======UPDATE COURSE CODE/SCHOOL======");
				System.out.print("Enter the course code: ");
				courseCode = sc.nextLine();
				System.out.print("Enter the new course code (re-enter same course code to leave it unchanged): ");
				String newCourseCode = sc.nextLine();
				for (int i = 1; i<=Course.SCHOOL.values().length; i++){
					System.out.println("(" + i + ") " + Course.SCHOOL.values()[i-1].name());
				}
				System.out.print("Select the school (select same school to leave school unchanged): ");
				schoolInt = sc.nextInt();
				while (schoolInt < 1 || schoolInt > Course.SCHOOL.values().length){
					System.out.print("Select a valid school: ");
					schoolInt = sc.nextInt();
				}
				school = Course.SCHOOL.values()[schoolInt-1];
				if (!AdminMgr.updateCourse(courses, courseCode, newCourseCode, school)){
					System.out.println("Failed to update course.. Please try again!");
				}
				break;
			case 6:
				//Check available slot for index number
				sc.nextLine();
				System.out.println("======CHECK AVAILABLE SLOT FOR AN INDEX NUMBER======");
				System.out.print("Enter the course code: ");
				courseCode = sc.nextLine();
				System.out.print("Enter the index number: ");
				int indexNumber = sc.nextInt();
				if (!AdminMgr.getIndexVacancy(courses, courseCode, indexNumber)){
					System.out.println("Course/Index inputed does not exist! Please try again.");
				}
				break;
			case 7:
				//Print student list by index number
				sc.nextLine();
				System.out.println("======PRINT STUDENT LIST BY INDEX NUMBER======");
				System.out.print("Enter the course code: ");
				courseCode = sc.nextLine();
				System.out.print("Enter the index number: ");
				indexNumber = sc.nextInt();
				if (!AdminMgr.printStudentListByIndex(courses, courseCode, indexNumber)){
					System.out.println("Course does not have the index inputed! Please try again.");
				}
				break;
			case 8:
				//Print student list by course
				sc.nextLine();
				System.out.println("======PRINT STUDENT LIST BY COURSE======");
				System.out.print("Enter the course code: ");
				courseCode = sc.nextLine();
				if (!AdminMgr.printStudentListByCourse(courses, courseCode)){
					System.out.println("Course does not exist! Please try again.");
				}
				break;
			case 9:
				//print venue timetable
				sc.nextLine();
				System.out.println("======PRINT VENUE======");
				for (int i = 0; i<venues.size(); i++){
					System.out.println((i+1) + ") " + venues.get(i).getLocation());
				}
				System.out.print("Please select a venue number: ");
				venueInt = sc.nextInt();
				while (venueInt<1 || venueInt>venues.size()){
					System.out.print("Enter a valid venue: ");
					venueInt = sc.nextInt();
				}
				AdminMgr.printVenueTimetable(venues.get(venueInt-1));
				break;
			case 10:
				//view all courses available
				sc.nextLine();
				System.out.println("======COURSES======");
				AdminMgr.printAllCourses(courses);
				break;
			case 11:
				//view all indexes of course
				sc.nextLine();
				System.out.println("======INDEXES OF A COURSE======");
				System.out.print("Enter the course code: ");
				courseCode = sc.next();
				StudentMgr.printAllIndexesForCourse(courses, courseCode);
				break;
			case 12:
				System.out.println("Logging out from ADMIN... Have a nice day!");
				terminate = true; 
				break;
			default:
				System.out.print("Please select a valid function!\n");
				break;
			}
		}
	}
	
	public static void printMenuoptions() {
		System.out.println("\n======MENU OPTIONS======");
		System.out.println("(1) Edit student access period\r\n" 
							+ "(2) Add a student\r\n"
							+ "(3) Add a course\r\n"
							+ "(4) Add an index to a course\r\n"
							+ "(5) Update a course code or school\r\n"
							+ "(6) Check available slot for an index number\r\n"
							+ "(7) Print student list by index number\r\n"
							+ "(8) Print student list by course\r\n"
							+ "(9) Print venue timetable\r\n"
							+ "(10) View all Courses Available\r\n"
							+ "(11) View all Indexes for a Course (Vacancy/Wait List)\r\n"
							+ "(12) Log out");
	}
	

}
