import java.util.ArrayList;
import java.util.Scanner;
import java.io.Console;

public class StudentMenu {
	private static Scanner sc = new Scanner(System.in);
	public static Console cnsl = System.console();
	/**
	 * Prints appropriate information when student inputs function
	 * @param student student who logged in
	 * @param students all existing student records
	 * @param courses  all existing course records
	 */
	public static void studentMenu(Student student, ArrayList<Student> students, ArrayList<Course> courses) {	
		boolean terminate = false;	
		while (!terminate) {
			printMenuoptions();
			System.out.print("Please select a function and input the corresponding number: ");
			int choice = sc.nextInt();
			System.out.println();
			switch (choice) {
			case 1:
				//Add course
				sc.nextLine();
				System.out.println("======ADD NEW COURSE======");
				System.out.print("Enter Course Code: ");
				String courseCode = sc.nextLine();
				if (!StudentMgr.addCourse(courses, courseCode, student)){
					System.out.println("Course does not exist! Please try again.");
				}
				break;
			case 2:
				//Drop course
				sc.nextLine();
				System.out.println("======DROP COURSE======");
				if (!StudentMgr.dropCourse(student)){
					System.out.println("No course dropped...");
				}
				break;
			case 3:
				//check/print course registered
				sc.nextLine();
				System.out.println("======PRINT COURSE REGISTERED======");
				StudentMgr.checkCoursesRegistered(student);
				break;
			case 4:
				//check vacancies available
				sc.nextLine();
				System.out.println("======CHECK VACANCIES AVAILABLE======");
				System.out.print("Enter the course code: ");
				courseCode = sc.nextLine();
				System.out.print("Enter the index number: ");
				int indexNumber = sc.nextInt();
				if (!StudentMgr.getIndexVacancy(courses, courseCode, indexNumber)){
					System.out.println("Course does not have the index inputed! Please try again.");
				}
				break;
			case 5:
				//change index number
				sc.nextLine();
				System.out.println("======CHANGE INDEX NUMBER OF COURSE======");
				System.out.print("Enter Course Code: ");
				courseCode = sc.nextLine();
				System.out.print("Enter Current Index Number: ");
				int curindexNo = sc.nextInt();
				System.out.print("Enter Index Number you would like to change to: ");
				int newindexNo = sc.nextInt();
				if (!StudentMgr.changeIndex(courses, courseCode, student, curindexNo, newindexNo)) {
					System.out.println("Failed to change index number. Please try again!");
				}
				break;
			case 6:
				//swap index number with another student
				sc.nextLine();
				System.out.print("======SWAP INDEX NUMBER WITH ANOTHER STUDENT======");
				System.out.print("Please input your course code: ");
				courseCode = sc.nextLine();
				System.out.print("Please input your current index: ");
				int currentStudentIndex = sc.nextInt();
				System.out.print("Please input your partner's index: ");
				int newStudentIndex = sc.nextInt();
				System.out.print("Please input your partner's username: ");
				String pUsername = sc.next();
				char[] pw = cnsl.readPassword("Please input your partner's password: ");
				String pPassword = new String(pw);
				Student pStudent = StudentMgr.validateStudent(students, pUsername, pPassword);
				if (pStudent != null){
					if (!StudentMgr.swapIndexWithStudent(courses, courseCode, student, pStudent, currentStudentIndex, newStudentIndex)){
						System.out.println("Swap is unsuccessful!");
					}
				} else{
					System.out.println("Invalid login attempt (username or password)!! Please try again.");
				}
				break;
			case 7:
				//print timetable
				sc.nextLine();
				System.out.println("======STUDENT TIMETABLE======");
				StudentMgr.printStudentTimeTable(student);
				break;
			case 8:
				//view all courses available
				sc.nextLine();
				System.out.println("======COURSES======");
				AdminMgr.printAllCourses(courses);
				break;
			case 9:
				//view all indexes for a course
				sc.nextLine();
				System.out.println("======INDEXES OF A COURSE======");
				System.out.print("Enter the course code: ");
				courseCode = sc.next();
				StudentMgr.printAllIndexesForCourse(courses, courseCode);
				break;
			case 10:
				//view lesson timings for index
				sc.nextLine();
				System.out.println("======VIEW LESSON TIMINGS FOR INDEX======");
				System.out.print("Enter the course code: ");
				courseCode = sc.next();
				System.out.print("Enter the index number: ");
				int index = sc.nextInt();
				if (!StudentMgr.printLessonsForIndex(courses, courseCode, index)){
					System.out.println("Invalid course/index number.. Please try again!");
				}
				break;
			case 11:
				System.out.println("Logging out from " + student.getUsername() + "... Have a nice day!");
				terminate = true; 
				break;
			default:
				System.out.print("Please select a valid function!\n");
			}
		}
	}
	/**
	 * Show menu options for student
	 */
	public static void printMenuoptions() {
		System.out.println("\n======MENU OPTIONS======");
		System.out.println("(1) Add Course\r\n" 
							+ "(2) Drop Course\r\n"
							+ "(3) Check/Print Courses Registered\r\n"
							+ "(4) Check Vacancies Available\r\n"
							+ "(5) Change Index Number of Course\r\n"
							+ "(6) Swap Index Number with Another Student\r\n"
							+ "(7) Print Timetable\r\n"
							+ "(8) View all Courses Available\r\n"
							+ "(9) View all Indexes for a Course (Vacancy/Wait List)\r\n"
							+ "(10) View Lesson Timings for Index\r\n"
							+ "(11) Log out");
	}
		

}
