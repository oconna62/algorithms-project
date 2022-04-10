import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;

public class UI {
	public static Scanner input = new Scanner(System.in);
	
	public static boolean userInterface() throws FileNotFoundException, ParseException {
		
		boolean quit = false;

		System.out.print("Choose an option below or enter 0 to exit the program:\n"
				+ "- To find the shortest path between two bus stop enter 1,\n" 
				+ "- To find a stop name's information enter 2,\n"
				+ "- To find trips within a specific time of arrival sorted by trip ID enter 3.\n");

		int inputValue = 0;
		if (input.hasNextInt()) {
			inputValue = input.nextInt();
		}

		if (inputValue >= 0 && inputValue <= 3) {
			switch (inputValue) {
			case 0:
				quit = true;
				break;
				
			case 1:
				DijkstraSP.main(null);
				quit = false;
				break;

			case 2:
				TST.main(null);
				quit = false;
				break;

			case 3:
				time.main(null);
				quit = false;
				break;
			}
		} else {
			System.out.println("Please enter a valid number.");
			quit = false;
		}

		return quit;
	}

	public static void main(String[] args) throws IOException, ParseException {
		boolean quit = false;
		do {
			quit = userInterface();
		} while (quit != true);
		System.out.println("Program terminated");
	}
}
