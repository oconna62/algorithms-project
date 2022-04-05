import java.util.Scanner;
import java.io.IOException;

public class UI {

    public static boolean userInterface() {  
    	Scanner input = new Scanner(System.in);
    	boolean quit = false;
    	
  	  	while(quit != true) {
  	  		System.out.print("Choose an option below or enter 4 to end the program:\n"
                + "type 1 to find the shortest path between two bus stops \n"
                + "type 2 to find a stop's information  \n"
                + "type 3 to find all trips with a specific time of arrival sorted by trip ID  \n");
  	  		
  	  		if(input.hasNextInt()) {
  	  			int inputValue= input.nextInt();  // add a switch here instead
  	  			if (inputValue == 1){
  	  				quit = false;
  	  			}
  	  			else if (inputValue == 2) {
  	  				quit = false;
  	  			}
  	  			else if (inputValue == 3) {
  	  				quit = false;
  	  			} 
  	  			else if (inputValue == 4) {
  	  				return true;
  	  			}
  	  			else {
  	  				System.out.println("Please enter a valid number.");
  	  			}
  	  			input.nextLine();
  	  		}
  	  		else {
  	  			System.out.println("Please enter a valid number.");  
  	  			input.nextLine();
  	  		}
  	  	}	
  	  	return quit;
	 }	  
    
    public static void main(String[] args) throws IOException {
        boolean quit = false;
        do {
            quit = userInterface();
        } while (quit != true);
        System.out.println("Program terminated");
    }
}
