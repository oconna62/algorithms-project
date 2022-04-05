Algorithms final project:

1. Finding shortest paths between 2 bus stops (as input by the user), returning the list of stops
en route as well as the associated �cost�.
Stops are listed in stops.txt and connections (edges) between them come from stop_times.txt and
transfers.txt files. All lines in transfers.txt are edges (directed), while in stop_times.txt an edge
should be added only between 2 consecutive stops with the same trip_id.
eg first 3 entries in stop_times.txt are
9017927, 5:25:00, 5:25:00,646,1,,0,0,
9017927, 5:25:50, 5:25:50,378,2,,0,0,0.3300
9017927, 5:26:28, 5:26:28,379,3,,0,0,0.5780
This should add a directed edge from 646 to 378, and a directed edge from 378 to 379 (as they�re on
the same trip id 9017927).
Cost associated with edges should be as follows: 1 if it comes from stop_times.txt, 2 if it comes from
transfers.txt with transfer type 0 (which is immediate transfer possible), and for transfer type 2 the
cost is the minimum transfer time divided by 100.

2. Searching for a bus stop by full name or by the first few characters in the name, using a
ternary search tree (TST), returning the full stop information for each stop matching the
search criteria (which can be zero, one or more stops)
In order for this to provide meaningful search functionality please move keywords flagstop, wb, nb,
sb, eb from start of the names to the end of the names of the stops when reading the file into a TST
(eg �WB HASTINGS ST FS HOLDOM AVE� becomes �HASTINGS ST FS HOLDOM AVE WB�)

3. Searching for all trips with a given arrival time, returning full details of all trips matching the
criteria (zero, one or more), sorted by trip id
Arrival time should be provided by the user as hh:mm:ss. When reading in stop_times.txt file you
will need to remove all invalid times, e.g., there are times in the file that start at 27/28 hours, so are
clearly invalid. Maximum time allowed is 23:59:59.

4. Provide front interface enabling selection between the above features or an option to exit
the programme, and enabling required user input. It does not matter if this is command-line
or graphical UI, as long as functionality/error checking is provided.
You are required to provide error checking and show appropriate messages in the case of erroneous
inputs � eg bus stop doesn�t exist, wrong format for time for bus stop (eg letters instead of
numbers), no route possible etc. 
