import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.*;

public class time {
	public static ArrayList<String> validTrips = new ArrayList<String>();
	public static String line1; // trip_id,arrival_time,departure_time,stop_id,stop_sequence,stop_headsign,pickup_type,drop_off_type,shape_dist_traveled

	public static void main(String[] args) throws FileNotFoundException, ParseException {
		Scanner input = new Scanner(System.in);
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		timeFormat.setLenient(false); // strictly (hh:mm:ss) format only

		boolean quit = false;
		while (!quit) {
			ArrayList<String> matchingTrips = new ArrayList<String>();
			System.out.println("=================================================");
			System.out.print("Enter time in hh:mm:ss format: ");
			String inputTime = input.next();
			try {
				timeFormat.parse(inputTime);
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
					System.out.println("=================================================");
					System.out.println(sortedTrips.size() + " matches found:");
					System.out.println("=================================================");
					System.out.println(line1.toUpperCase());
					for (int i = 0; i < sortedTrips.size(); i++) {
						System.out.println(sortedTrips.get(i));
					}
				} else {
					System.out.println("=================================================");
					System.out.println("No matches, please try another time.");
					input.nextLine();
				}
				System.out.println("=================================================");
				System.out.print("Enter any key to continue, or 'exit' to return to the main menu: ");
				String checkExit = input.next();
				if (checkExit.equalsIgnoreCase("exit")) {
					quit = true;
				}
			} catch (ParseException e) {
				System.out.println("Invalid input, please try again.");
			}
		}
		System.out.println("=================================================");
	}

	public static ArrayList<String> sort(ArrayList<String> matchingTrips) {
		// https://www.javatpoint.com/how-to-sort-arraylist-in-java
		System.out.println("Sorting by tripID...");
		Collections.sort(matchingTrips); // uses iterative mergesort, more stable
		return matchingTrips;
	}

	public static void validateTrips() throws ParseException, FileNotFoundException {
		FileReader f = new FileReader("stop_times.txt");
		Scanner scanner = new Scanner(f);
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		String maxTime = "23:59:59";
		Date maximum = timeFormat.parse(maxTime);

		line1 = scanner.nextLine(); // skip first line
		String nextLine = "";
		String[] split;
		Date time;

		while (scanner.hasNextLine()) {
			nextLine = scanner.nextLine();
			split = nextLine.split(",");
			time = timeFormat.parse(split[1]);
			if (time.getTime() <= maximum.getTime()) {
				validTrips.add(nextLine);
			}
		}
		scanner.close();
	}
}
