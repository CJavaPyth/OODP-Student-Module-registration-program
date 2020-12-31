import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;


public class Testing {
    public static void main(String[] args){
		
		Venue LT1 = new Venue("LT1");
		Venue LT2 = new Venue("LT2");
		Venue LT3 = new Venue("LT3");
        Venue TR1 = new Venue("TR1");
		Venue TR2 = new Venue("TR2");
		Venue TR3 = new Venue("TR3");
		Venue SWLab = new Venue("SWLab");
		Venue HWLab = new Venue("HWLab");
		Venue PLab = new Venue("Project Lab");
		ArrayList<Venue> venues = new ArrayList<>();
		venues.add(LT1);
		venues.add(LT2);
		venues.add(LT3);
        venues.add(TR1);
		venues.add(TR2);
		venues.add(TR3);
		venues.add(SWLab);
		venues.add(HWLab);
		venues.add(PLab);
		
    	writeSerializedObject("venue.txt", venues);
        venues = readSerializedObject("venue.txt");
        venues.get(0).printVenue();

        
    }

    public static ArrayList<Venue> readSerializedObject(String filename) {
		ArrayList<Venue> pDetails = null;
		FileInputStream fis = null;
		ObjectInputStream in = null;
		try {
			fis = new FileInputStream(filename);
			in = new ObjectInputStream(fis);
			pDetails = (ArrayList<Venue>) in.readObject();
			in.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		return pDetails;
	}

	public static void writeSerializedObject(String filename, ArrayList<Venue> list) {
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try {
			fos = new FileOutputStream(filename);
			out = new ObjectOutputStream(fos);
			out.writeObject(list);
			out.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
