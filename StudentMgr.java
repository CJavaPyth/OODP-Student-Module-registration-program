import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class StudentMgr {
    public static Student student;
    public static Scanner sc = new Scanner(System.in);

    public static Student validateStudent(ArrayList<Student> students, String username, String password){
        String str;
        String studentUsername = null;
        String studentPassword = null;
        Student foundStudent = null;
        password = PasswordMD5.hashPassword(password);
        try{
            FileReader f = new FileReader("studentAccount.txt");
            BufferedReader b = new BufferedReader(f);
            while ((str = b.readLine()) != null){
                String[] values = str.split("\\|");
                studentUsername = values[0];
                if (username.equals(studentUsername)){
                    //check if password matches
                    studentPassword = values[1];
                    if (password.equals(studentPassword)){
                        for (Student student: students){
                            if (student.getUsername().equals(username)){
                                foundStudent = student;
                            }
                        }
                    }
                }
            }
            b.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        return foundStudent;
    }

    public static boolean addCourse(ArrayList<Course> courses, String courseCode, Student student){
		for (Course course: courses){
            if (course.getCourseCode().equals(courseCode)) {
            	student.addCourse(course);
            	return true;
            }
		}
		return false;
    }
    
	public static boolean dropCourse(Student student){
        System.out.println("Courses enrolled: ");
        int i = 0;
        int choice;
        ArrayList<Index> allIndexes = new ArrayList<>();
        for (Index index: student.getIndexesInWaiting()){
            i++;
            System.out.println(i + ") " + index.getIndexNumber() + " " + index.getCourse().getCourseCode() + " " + index.getCourse().getName() + " Onwait " + (index.getWaiting()-1));
            allIndexes.add(index);
        }
        for (Index index: student.getIndexes()){
            i++;
            System.out.println(i + ") " + index.getIndexNumber() + " " + index.getCourse().getCourseCode() + " " + index.getCourse().getName() + " Registered");
            allIndexes.add(index);
        }
        if (i == 0){
            System.out.println("You have not applied for any courses!");
            return false;
        }
        String output = (i == 1) ? "(1)" : "(1 to " + i + ")";
        System.out.print("Select a number " + output + " to drop or Enter 0 to exit: ");
        while(true){
            choice = sc.nextInt();
            if (choice>=0 && choice<=i){
                break;
            } else{
                System.out.print("Please select a valid number: ");
            }
        }
        if (choice == 0){
            return false;
        } else{
            System.out.println("Successfully dropped " + allIndexes.get(choice-1).getCourse().getCourseCode() + " - " + allIndexes.get(choice-1).getCourse().getName());
            return student.dropCourse(allIndexes.get(choice-1));
        }
        
    }

    public static void printStudentTimeTable(Student student){
        student.printStudentTimeTable();
    }

    public static void checkCoursesRegistered(Student student){
        System.out.println("Courses Registered for " + student.getName() + ": ");
        for (Index i: student.getIndexes()){
            System.out.println(i.getCourse().getCourseCode() + " " + i.getCourse().getName() + " Index: " + i.getIndexNumber());
        }
        System.out.println("\nCourses in Waiting List: ");
        for (Index i: student.getIndexesInWaiting()){
            System.out.println(i.getCourse().getCourseCode() + " " + i.getCourse().getName() + " Index: " + i.getIndexNumber());
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

    public static boolean changeIndex(ArrayList<Course> courses, String courseCode, Student student, int oldIndex, int newIndex){
        if (oldIndex == newIndex){
            System.out.println("Indexes are the same!");
            return false;
        }
		Index oIndex = null;
		Index nIndex = null;
		for (Course course: courses){
            if (course.getCourseCode().equals(courseCode)) {
				for (Index i: course.getIndexes()){
		            if (i.getIndexNumber() == oldIndex){
		            	oIndex = i;
		            }
		            if (i.getIndexNumber() == newIndex){
		            	nIndex = i;
		            } 	
				}
            }
		}
		if (oIndex != null && nIndex != null) {
			if (student.changeIndex(oIndex, nIndex)){
                System.out.println("Index successfully changed from " + oldIndex + " to " + newIndex + ".");
                return true;
            }
        } else{
            System.out.println("Indexes of course does not exist!");
        }
		return false; 
    }
    
    public static boolean swapIndexWithStudent(ArrayList<Course> courses, String courseCode, Student student, Student newStudent, int currentStudentIndex, int newStudentIndex){
        if (currentStudentIndex == newStudentIndex){
            System.out.println("Indexes are the same!");
            return false;
        }
        Index cSIndex = null;
		Index nSIndex = null;

        for (Course course: courses){
            if (course.getCourseCode().equals(courseCode)){
                for (Index i: course.getIndexes()){
                    if (i.getIndexNumber() == currentStudentIndex){
                        cSIndex = i;
                    }
                    if (i.getIndexNumber() == newStudentIndex){
                        nSIndex = i;
                    } 	
                }
            }
        }
        if (cSIndex != null && nSIndex != null){
            if (student.swapIndexWithStudent(newStudent, cSIndex, nSIndex, true)){
                System.out.println("Swap successful!");
                return true;
            }
        } else{
            System.out.println("Indexes of course does not exist!");
        }
        return false;
    }

    public static void printAllIndexesForCourse(ArrayList<Course> courses, String courseCode){
        for (Course course: courses){
            if (course.getCourseCode().equals(courseCode)){
                course.printIndexes();
                return;
            }
        }
        System.out.println("No such course found... Please try again!!!");
    }

    public static boolean printLessonsForIndex(ArrayList<Course> courses, String courseCode, int indexNumber){
        for (Course course: courses){
            if (course.getCourseCode().equals(courseCode)){
                for (Index i: course.getIndexes()){
                    if (i.getIndexNumber() == indexNumber){
                        i.printIndex();
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
