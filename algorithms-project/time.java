import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.*;

public class time {
	public static void main(String[] args) throws FileNotFoundException, ParseException {
		
			Scanner input = new Scanner(System.in);
			
			SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
			timeFormat.setLenient(false);	// strictly (hh.mm.ss) format only
			
			ArrayList<String> validTrips = validateTrips();
			System.out.println("Trips validated");
			ArrayList<String> matchingTrips = new ArrayList<String>();
			
			boolean quit = false;
			while(!quit) {
				System.out.print("Enter time in hh:mm:ss format: ");
				String inputTime = input.next();
				timeFormat.parse(inputTime);	// add error handling

				for (int i = 0; i < validTrips.size(); i++) {
					String trip = validTrips.get(i);
					String[] split = trip.split(",");
						
					Date tripTime = timeFormat.parse(split[1]);
					Date inputTime1 = timeFormat.parse(inputTime);
					if (tripTime.getTime() == inputTime1.getTime()) {
						matchingTrips.add(trip);
					}
				}

				ArrayList<String> sortedTrips = sort(matchingTrips);
				
				if (sortedTrips.size() != 0) {
					System.out.println(sortedTrips.size() + " matches found:");
					for(int i = 0; i < sortedTrips.size(); i++) {
						System.out.println(sortedTrips.get(i));
					}
					quit = true;
				}
				else {
					System.out.println("No matches, please try another time:");
					input.nextLine();
				}
			}
			input.close();
	}

	public static ArrayList<String> sort(ArrayList<String> matchingTrips) {
		//https://www.javatpoint.com/how-to-sort-arraylist-in-java
		System.out.println("Sorting by tripID");
		Collections.sort(matchingTrips);	// uses iterative mergesort, more stable - test for time
		return matchingTrips;
	}

	public static ArrayList<String> validateTrips() throws ParseException, FileNotFoundException {
		ArrayList<String> validTrips = new ArrayList<String>();
		FileReader f = new FileReader("stop_times.txt");
		Scanner scanner = new Scanner(f);
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		String maxTime = "23:59:59";
		Date maximum = timeFormat.parse(maxTime);
			
		scanner.nextLine(); // skip first line
		String nextLine = "";
		String[] split;
		Date time;
		
		while(scanner.hasNextLine()) {
			nextLine = scanner.nextLine();
			split = nextLine.split(",");
	        time = timeFormat.parse(split[1]);
	        if (time.getTime()<=maximum.getTime()) {
	        	validTrips.add(nextLine);
	        }
		}
		scanner.close();
		return validTrips;
	}
	
	
}
