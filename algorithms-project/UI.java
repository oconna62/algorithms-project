import java.util.Scanner;
import java.io.IOException;

public class UI {

    public static boolean userInterface() {  
    	Scanner input = new Scanner(System.in);
    	boolean quit = false;
    	
  	  	System.out.print("Choose an option below or enter 4 to end the program:\n"
        + "type 1 to find the shortest path between two bus stop\n"
        + "type 2 to find a stop's information\n"
        + "type 3 to find all trips with a specific time of arrival sorted by trip ID\n");
  	  		
  	  	int inputValue = 0;
  	  	if(input.hasNextInt()) {
  	  		inputValue = input.nextInt();
  	  	}
  	  	
  	  	if (inputValue >=1 && inputValue <= 4) {
  	  		switch (inputValue) {
  	  			case 1: 
  	  				quit = false;
  	  				break;

  	  			case 2:
  	  				quit = false;
  	  				break;
  	  			
  	  			case 3: 
  	  				quit = false;
  	  				break;
  	  			
  	  			case 4:
  	  				quit = true;
  	  				break;
  	  		}
  	  	}
  	  	else {
  	  		System.out.println("Please enter a valid number.");
			quit = false;
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
