import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

// reference: https://algs4.cs.princeton.edu/44sp/DijkstraSP.java.html
public class DijkstraSP {
    private static double[] distTo;          
    private static DirectedEdge[] edgeTo;    
    private static IndexMinPQ<Double> priorityQueue;    
    static ArrayList<stops> stop = new ArrayList<>();		
	ArrayList<stops> s = new ArrayList<>();
	static ArrayList<trips> list = new ArrayList<>();		
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
        Stack<DirectedEdge> path = new Stack<DirectedEdge>(); // change to graph and see if still works
        for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
            path.push(e);
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
    
    
    public static void readFiles() {

		try {
			//readStops;
			Scanner scanner = new Scanner(new FileReader("stops.txt")); 
			
			while(scanner.hasNextLine()) { 
				String stops = scanner.nextLine();
				String[] line = stops.split(",");
				if (!line[1].equals("stop_code")) {
					int stop_id = Integer.parseInt(line[0]);
					int stop_code = (line[1].equals(" ")) ? -1 : Integer.parseInt(String.valueOf(line[1]));
					String stop_name = line[2];
					String stop_desc = line[3];
					double stop_lat = Double.parseDouble(line[4]);
					double stop_lon = Double.parseDouble(line[5]);
					String zone_id = line[6];
					String stop_url = line[7];
					int location_type = Integer.parseInt(line[8]);
					String parent_station = (line.length == 9) ? "" : line[9];
					stops s = new stops(stop_id, stop_code, stop_name, stop_desc, stop_lat,
							stop_lon,zone_id,stop_url,location_type, parent_station);
					stop.add(s);
				}
			}
			
			int max=0;
			for(stops s: stop) {
				if(max <= s.stop_id)
					max=s.stop_id +1;
			}
			graph = new EdgeWeightedDigraph(max);
			
			for(stops s : stop){
				DirectedEdge d = new DirectedEdge(s.stop_id,s.stop_id,0);
				graph.addEdge(d);
			}
			
			//readTransfers;
			scanner = new Scanner(new FileReader("transfers.txt")); 
			
			while(scanner.hasNextLine()) { 
				String transfers = scanner.nextLine();
				String[] line1 = transfers.split(",");
				if(!line1[1].equals("to_stop_id")) {
					int from_stop_id = Integer.parseInt(line1[0]);
					int to_stop_id = Integer.parseInt(line1[1]);
					int transfer_type = Integer.parseInt(line1[2]);
					double min_transfer_time = (line1.length == 3) ? -100 : Double.parseDouble(line1[3]);
					double cost = 0;
					if(transfer_type == 2)  {
						cost = min_transfer_time /100;
					}
					else if(transfer_type == 0) {
						cost = 2;
					}

					DirectedEdge edge = new DirectedEdge(from_stop_id, to_stop_id, cost);
					graph.addEdge(edge);
				}
			}
			
			//ReadStopTimes;
			scanner = new Scanner(new FileReader("stop_times.txt")); 
			
			while(scanner.hasNextLine()) { 
				String times = scanner.nextLine();
				String[] line2 = times.split(",");
				if(!line2[1].equals("arrival_time")) {
					int trip_id = Integer.parseInt(line2[0]);
					String arrival_time = line2[1];
					String departure_time = line2[2];
					if(time(arrival_time)<24 && time(departure_time)<24) {
						int stop_id = Integer.parseInt(line2[3]);
						int stop_sequence = Integer.parseInt(line2[4]);
						int stop_headsign = (line2[5].equals("")) ? -100 : Integer.parseInt(line2[5]);
						int pickup_type = Integer.parseInt(line2[6]);
						int drop_off_type = Integer.parseInt(line2[7]);
						double shape_dist_traveled = (line2.length == 8) ? -100 : Double.parseDouble(line2[8]);
						trips t = new trips(trip_id,arrival_time,departure_time,stop_id,stop_sequence,
								stop_headsign, pickup_type, drop_off_type, shape_dist_traveled);
						list.add(t);
					}
				}
			}
			scanner.close();
			int size = list.size();
			for(int i=0; i<size-1; i++) {
				trips trip1 = list.get(i);
				trips trip2 = list.get(i+1);

				if(trip1.trip_id == trip2.trip_id) {
					DirectedEdge newEdge = new DirectedEdge(trip1.stop_id, trip2.stop_id, 1);
					graph.addEdge(newEdge);
				}
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("Files not found.");
		}
	}

	public static int time(String time) {
		String[] parts = time.strip().split(":");
		int hour = Integer.parseInt(parts[0]);

		return hour;
	}

	public static void main(String[] args) {
		readFiles();
		/*
		System.out.println();
		String stop1String = "Please enter bus stop 1: ";
		int stop1 = verifyInput(stop1String);

		String stop2String = "Please enter bus stop 2: ";
		int stop2 = verifyInput(stop2String);
		*/
		Scanner input = new Scanner(System.in);
		System.out.println("Please enter bus stop 1: ");
		int stop1 = input.nextInt();
		System.out.println("Please enter bus stop 2: ");
		int stop2 = input.nextInt();
		
		ShortestPath(graph, stop1);
		double distance = distTo(stop2);
		pathTo(stop2);
		
		System.out.println("From - "+ stop1 +" to - "+ stop2 +"  weight - " + distance);
		Iterable<DirectedEdge> path_itr = pathTo(stop2);

		for(DirectedEdge p: path_itr) {
			System.out.println("From - "+ p.from() + " to - "+ p.to() + " weight - "+p.weight());
		}

		ArrayList<Integer> path = getEnrouteStops(stop2);

		for (int p : path) {
			//if(p==0) System.out.print(stop1);
			System.out.print(p + " -> ");
		}
	} 
		
	
	/*
	private static int verifyInput(String stopString) {
		Scanner input = new Scanner(System.in);
		int stopNumber = -1;
		boolean valid = false;
		while(!valid) {
			System.out.println(stopString);
			if(input.hasNextInt()) {
				stopNumber = input.nextInt();
				
				if(graph.findStop(stopNumber)) { // if in graph
					valid = true;
				}
				
				else {
					System.out.println("There is no stop with the id: \"" + stop + "\" on our system.");
				}
			}
			else {
				System.out.println("Invalid input. Please only enter integers.");
				input.nextLine();				
			}
		}
		input.close();
		return stopNumber;
	}
	*/

	public static ArrayList<Integer> getEnrouteStops(int v){
		ArrayList<Integer> res = new ArrayList<>();
		Iterable<DirectedEdge> path = pathTo(v);
		res.add(v);
		for(DirectedEdge e : path) {
			res.add(e.from());
		}
		Collections.reverse(res);
		return res;
	}
	
	private static void ShortestPath(EdgeWeightedDigraph G, int s) {
		distTo = new double[G.V()];
		edgeTo = new DirectedEdge[G.V()];

		for(int v=0; v< G.V(); v++) {
			distTo[v] = Double.POSITIVE_INFINITY;
			distTo[s] = 0.0;
		}
		// relax vertices in order of distance from s
		priorityQueue = new IndexMinPQ<Double>(G.V());
		priorityQueue.insert(s, distTo[s]);
		while (!priorityQueue.isEmpty()) {
			int v = priorityQueue.delMin();
			for (DirectedEdge e : G.adj(v))
				relax(e);
		}
	}
}
