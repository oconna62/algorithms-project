import java.util.Scanner;
import java.io.IOException;

public class UI {

    public static boolean userInterface() { 
    	boolean quit = false;
  	  	while(quit != true) {
  	  		System.out.print("Choose an option below or enter 4 to quit the program:\n"
                + "type 1 to find the shortest path between two bus stops \n"
                + "type 2 to find a stop's information  \n"
                + "type 3 to find all trips with a specific time of arrival sorted by trip ID\n");
        
  	  		Scanner input = new Scanner(System.in);
  	  		int inputValue= input.nextInt();
  	  		
  	  		
  	  		if (inputValue == 1){
  	  			// DijkstraSP
  	  		}
  	  		else if (inputValue == 2) {
  	  			// stopInformation
  	  		}
  	  		else if (inputValue == 3) {
  	  			// arrivalTime
  	  		} 
  	  		else {
  	  			return true;
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
