import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Scanner;

// reference: https://algs4.cs.princeton.edu/44sp/DijkstraSP.java.html
public class DijkstraSP {
    private static double[] distTo;          
    private static DirectedEdge[] edgeTo;    
    private static IndexMinPQ<Double> priorityQueue;    				
	static EdgeWeightedDigraph graph;

    /**
     * Computes a shortest-paths tree from the source vertex {@code s} to every other
     * vertex in the edge-weighted digraph {@code G}.
     *
     * @param  G the edge-weighted digraph
     * @param  s the source vertex
     * @throws IllegalArgumentException if an edge weight is negative
     * @throws IllegalArgumentException unless {@code 0 <= s < V}
     */
    public DijkstraSP(EdgeWeightedDigraph G, int s) {
        for (DirectedEdge e : G.edges()) {
            if (e.weight() < 0)
                throw new IllegalArgumentException("edge " + e + " has negative weight");
        }

        distTo = new double[G.V()];
        edgeTo = new DirectedEdge[G.V()];

        validateVertex(s);

        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[s] = 0.0;

        // relax vertices in order of distance from s
        priorityQueue = new IndexMinPQ<Double>(G.V());
        priorityQueue.insert(s, distTo[s]);
        while (!priorityQueue.isEmpty()) {
            int v = priorityQueue.delMin();
            for (DirectedEdge e : G.adj(v))
                relax(e);
        }

        // check optimality conditions
        assert check(G, s);
    }

    // relax edge e and update priorityQueue if changed
    private static void relax(DirectedEdge e) {
        int v = e.from(), w = e.to();
        if (distTo[w] > distTo[v] + e.weight()) {
            distTo[w] = distTo[v] + e.weight();
            edgeTo[w] = e;
            if (priorityQueue.contains(w)) priorityQueue.decreaseKey(w, distTo[w]);
            else                priorityQueue.insert(w, distTo[w]);
        }
    }

    /**
     * Returns the length of a shortest path from the source vertex {@code s} to vertex {@code v}.
     * @param  v the destination vertex
     * @return the length of a shortest path from the source vertex {@code s} to vertex {@code v};
     *         {@code Double.POSITIVE_INFINITY} if no such path
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public static double distTo(int v) {
        validateVertex(v);
        return distTo[v];
    }

    /**
     * Returns true if there is a path from the source vertex {@code s} to vertex {@code v}.
     *
     * @param  v the destination vertex
     * @return {@code true} if there is a path from the source vertex
     *         {@code s} to vertex {@code v}; {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public static boolean hasPathTo(int v) {
        validateVertex(v);
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    /**
     * Returns a shortest path from the source vertex {@code s} to vertex {@code v}.
     *
     * @param  v the destination vertex
     * @return a shortest path from the source vertex {@code s} to vertex {@code v}
     *         as an iterable of edges, and {@code null} if no such path
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public static Iterable<DirectedEdge> pathTo(int v) {
        validateVertex(v);
        if (!hasPathTo(v)) return null;
        Bag<DirectedEdge> path = new Bag<DirectedEdge>(); // STACK
        for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
            path.add(e);
        }
        return path;
    }


    // check optimality conditions:
    // (i) for all edges e:            distTo[e.to()] <= distTo[e.from()] + e.weight()
    // (ii) for all edge e on the SPT: distTo[e.to()] == distTo[e.from()] + e.weight()
    private boolean check(EdgeWeightedDigraph G, int s) {

        // check that edge weights are non-negative
        for (DirectedEdge e : G.edges()) {
            if (e.weight() < 0) {
                System.err.println("negative edge weight detected");
                return false;
            }
        }

        // check that distTo[v] and edgeTo[v] are consistent
        if (distTo[s] != 0.0 || edgeTo[s] != null) {
            System.err.println("distTo[s] and edgeTo[s] inconsistent");
            return false;
        }
        for (int v = 0; v < G.V(); v++) {
            if (v == s) continue;
            if (edgeTo[v] == null && distTo[v] != Double.POSITIVE_INFINITY) {
                System.err.println("distTo[] and edgeTo[] inconsistent");
                return false;
            }
        }

        // check that all edges e = v->w satisfy distTo[w] <= distTo[v] + e.weight()
        for (int v = 0; v < G.V(); v++) {
            for (DirectedEdge e : G.adj(v)) {
                int w = e.to();
                if (distTo[v] + e.weight() < distTo[w]) {
                    System.err.println("edge " + e + " not relaxed");
                    return false;
                }
            }
        }

        // check that all edges e = v->w on SPT satisfy distTo[w] == distTo[v] + e.weight()
        for (int w = 0; w < G.V(); w++) {
            if (edgeTo[w] == null) continue;
            DirectedEdge e = edgeTo[w];
            int v = e.from();
            if (w != e.to()) return false;
            if (distTo[v] + e.weight() != distTo[w]) {
                System.err.println("edge " + e + " on shortest path not tight");
                return false;
            }
        }
        return true;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private static void validateVertex(int v) {
        int V = distTo.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }
    
    
    public static void readFiles() throws ParseException {
		try {
			// stops.txt;
			Scanner scanner = new Scanner(new FileReader("stops.txt")); 
			scanner.nextLine(); // skip first line

			int max = 0;
			while(scanner.hasNextLine()) { 
				String stops = scanner.nextLine();
				String[] line = stops.split(",");
				int stop_id = Integer.parseInt(line[0]);
				if(max <= stop_id) {
					max=stop_id +1;
				}
			}	
			graph = new EdgeWeightedDigraph(max);
			
			// transfers.txt;
			scanner = new Scanner(new FileReader("transfers.txt")); 
			scanner.nextLine(); // skip first line
			while(scanner.hasNextLine()) { 
				String transfers = scanner.nextLine();
				String[] line1 = transfers.split(",");

				int from_stop_id = Integer.parseInt(line1[0]);
				int to_stop_id = Integer.parseInt(line1[1]);
				int transfer_type = Integer.parseInt(line1[2]);
				double min_transfer_time = (line1.length == 3) ? -100 : Double.parseDouble(line1[3]);
				double cost = 0;
				
				if(transfer_type == 0) {
					cost = 2;
				}
				else if(transfer_type == 2)  {
					cost = min_transfer_time/100;
				}

				DirectedEdge edge = new DirectedEdge(from_stop_id, to_stop_id, cost);
				graph.addEdge(edge);
				
			}
			
			// stop_times.txt;
			scanner = new Scanner(new FileReader("stop_times.txt")); 
			scanner.nextLine(); // skip first line
			SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
			String maxTime = "23:59:59";
			Date maximum = timeFormat.parse(maxTime);
			
			ArrayList<Integer> list = new ArrayList<Integer>();
			while(scanner.hasNextLine()) { 
				String times = scanner.nextLine();
				String[] line2 = times.split(",");
				
				int trip_id = Integer.parseInt(line2[0]);
				String arrival_time = line2[1];
				Date arrival = timeFormat.parse(arrival_time);
				String departure_time = line2[2];
				Date departure = timeFormat.parse(departure_time);
				
				if(arrival.getTime()<maximum.getTime() && departure.getTime()<maximum.getTime()) {
					int stop_id = Integer.parseInt(line2[3]);
					list.add(trip_id);
					list.add(stop_id);
				}
				
			}
			scanner.close();
			
			for(int i=0; i<list.size()-3; i=i+2) {
				int tripID1 = list.get(i);
				int stopID1 = list.get(i+1);
				int tripID2 = list.get(i+2);
				int stopID2 = list.get(i+3);
				
				if(tripID1 == tripID2) {
					DirectedEdge newEdge = new DirectedEdge(stopID1, stopID2, 1);
					graph.addEdge(newEdge);
				}
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("Files not found.");
		}
	}

	public static void main(String[] args) {
		boolean quit = false;
		Scanner input = new Scanner(System.in);
		while(!quit) {
			System.out.println("=================================================");
			System.out.print("Please enter bus stop 1: ");
			int stop1 = 0;
			String inputString = input.next();
			boolean isInt1 = false;
			try {
				stop1 = Integer.parseInt(inputString);
				isInt1 = true;
			}catch (Exception e) {	
			}
			System.out.print("Please enter bus stop 2: ");
			int stop2 = 0;
			inputString = input.next();
			boolean isInt2 = false;
			boolean same = false;
			try {
				stop2 = Integer.parseInt(inputString);
				if(stop1 != stop2) isInt2 = true;
				else same = true;
			}catch (Exception e) {
			}
			
			if (isInt1 && isInt2) {
				shortestPath(graph, stop1);		
				Iterable<DirectedEdge> path_itr = pathTo(stop2);
				
				if (path_itr == null) {
					System.out.println("Invalid path, please try again.");
					quit = false;
				}
					
				else {
					double distance = distTo(stop2);	
					System.out.println("=================================================");
					System.out.println("Shortest route is as follows:");
					System.out.println("=================================================");
					for(DirectedEdge p: path_itr) {
						System.out.println("From "+ p.from() + " --> "+ p.to() + " weight: "+p.weight());
					}
					System.out.println("=================================================");			
					System.out.println("Shortest route from " + stop1 + " to "+ stop2 + " is " + distance);
					System.out.println("=================================================");
					
					System.out.print("Enter any key to continue, or 'exit' to return to the main menu: ");
					String checkExit = input.next();
					if (checkExit.equalsIgnoreCase("exit")) {
						quit = true;
					}
				}		
			}
			else {
				if(same) {
					System.out.println("Invalid input, please enter different stop numbers.");
				}
				else System.out.println("Invalid input, please try again.");
			}
		}
		System.out.println("=================================================");
	}

	private static void shortestPath(EdgeWeightedDigraph G, int s) {
		distTo = new double[G.V()];
		edgeTo = new DirectedEdge[G.V()];

		for(int v=0; v< G.V(); v++) {
			distTo[v] = Double.POSITIVE_INFINITY;
			distTo[s] = 0.0;
		}
		
		priorityQueue = new IndexMinPQ<Double>(G.V());
		priorityQueue.insert(s, distTo[s]);
		while (priorityQueue.size()!=0) {
			int v = priorityQueue.delMin();
			for (DirectedEdge e : G.adj(v))
				relax(e);
		}
	}
}
