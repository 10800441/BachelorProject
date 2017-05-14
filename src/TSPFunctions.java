import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static java.lang.Math.*;

/****************************************************
 **                Bachelor project                **
 **                                                **
 ** Mentor:     Daan van den Berg                  **
 ** Name:       Martijn Heijstek                   **
 ** Stud.num:   10800441                           **
 ** Date:       21-04-2017                         **
 ****************************************************
 *  Functions applicable to the TSP                 *
 ****************************************************/

public class TSPFunctions {




    // For one city this function creates all possible edges
    public static ArrayList<Edge> createCityEdgeSet(City city, ArrayList<City> allCities) {
        ArrayList<Edge> edgeSet = new ArrayList<>();
        for (int a = 0; a < allCities.size(); a++) {
            City toCity = allCities.get(a);

            Edge edge;
            if (city != toCity) {
                edge = new Edge(city, toCity, Main.costMatrix[city.id][toCity.id]);
                edgeSet.add(edge);
            }
        }
        //System.out.println(edgeSet);
        return edgeSet;
    }


    //Generate a random tour and put it in a DoublyLinkedList
    public static Tour generateRandomTour(ArrayList<City> emptyGrid) {

        DoublyLinkedListImpl<City> head =  new DoublyLinkedListImpl<City>();
        DoublyLinkedListImpl<City> tail =  new DoublyLinkedListImpl<City>();

        // Make a list of unique random numbers
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < emptyGrid.size(); i++) indices.add(i);
        Collections.shuffle(indices);

        // Make a random tour
        for (int k = 0; k < emptyGrid.size(); k++) {
            City randomCity = emptyGrid.get(indices.get(k));
            if (k < emptyGrid.size()-1) {
                head.addLast(randomCity);
                tail.addLast(randomCity);
            } else {
                head.addFirst(randomCity);
                tail.addLast(randomCity);
            }
        }

        double tourLength = calculateEdgeLength(head, tail);
        return new Tour(tourLength, head, tail);
    }

    // Compute the total tour length of a given tour
    public static double calculateEdgeLength(DoublyLinkedListImpl<City> head ,DoublyLinkedListImpl<City> tail) {
        double length = 0.0;
        System.out.println("\n");
        System.out.println("Total tour lenght:");
        System.out.print("for tour: ");


        // Sum all lengths of linepieces
        for (int index = 1; index <= head.size(); index++) {
            System.out.print( (head.elementAt(index).id) + " - " +  tail.elementAt(index).id + ", ");
            length += Main.costMatrix[head.elementAt(index).id][tail.elementAt(index).id];
        }

        // add the length of the edge that closes the tour

        System.out.println("has length: " + length);
        System.out.println("\n");
        return length;
    }

    // Fot a given list of cities, calculate the cost matrix
    public static double[][] calculateCostMatrix(ArrayList<City> cities) {
        int citySize = cities.size();
        double[][] cm = new double[citySize][citySize];

        // Loop trough all rows
        for (int i = 0; i < citySize; i++) {
            City fromCity = cities.get(i);

            // Loop trough all columns
            for (int j = 0; j < citySize; j++) {
                City toCity = cities.get(j);

                if (i == j) {
                    // Negative distance to self
                    cm[j][i] = -1;

                }
                // City not present
                if(toCity.x == -2|| fromCity.x == -2) {
                    cm[j][i] = -2;
                }


                else {
                    //Calculate euclidean distance using pythagoras theorem
                    double delta_x = abs(fromCity.x - toCity.x);
                    double delta_y = abs(fromCity.y - toCity.y);
                    cm[j][i] = sqrt(Math.pow(delta_x, 2) + Math.pow(delta_y, 2));
                }
            }
        }

        return cm;
    }

    // Visualise the cost matix
    public static void printCostMatrix(double[][] costMatrix, ArrayList<City> cities ) {
        System.out.println("\nCost Matrix:");
        // Creation of rows
        for (int h = 0; h < costMatrix[0].length; h++) {
            if (h == 0) {
                System.out.print("           ");
                for (int l = 0; l < cities.size(); l++) {
                    if (cities.get(l).id>9)System.out.print("City " + cities.get(l).id + ":  ");
                    else System.out.print("City " + cities.get(l).id + ":   ");
                }
            }
            System.out.println("");

            // Creation of columns
            for (int w = 0; w < costMatrix[0].length; w++) {
                if (w == 0) {
                    if(cities.get(h).id>9) System.out.print("City " +  cities.get(h).id + ": ");
                    else System.out.print("City " +  cities.get(h).id + ":  ");
                }
                if (costMatrix[h][w] == -1.00 ){
                    System.out.print("     .    ");
                } if ( cities.get(h).x == -2 || cities.get(w).x == -2) {
                    System.out.print("    np    ");
                }

                else {
                    System.out.print("   " + String.format("%.2f", costMatrix[h][w]) + "  ");
                }
            }
        }
        System.out.println("\n");
    }

    // DEPRICATED
    // retrieve cities from Cities.txt ans store them in an doublyLinkedList
    public static DoublyLinkedListImpl<City> makeCityList() {
        DoublyLinkedListImpl<City> cityDatabase = new DoublyLinkedListImpl<City>();
        try {
            BufferedReader rd = new BufferedReader(new FileReader("src/Cities.txt"));
            String line;
            while (true) {
                line = rd.readLine();
                if (line == null) break;
                String[] cityString = line.split(",");
                int city_id = Integer.valueOf(cityString[0]);
                int city_x = Integer.valueOf(cityString[1]);
                int city_y = Integer.valueOf(cityString[2]);

                City city = new City(city_id, city_x, city_y);
                cityDatabase.addLast(city);
            }
            rd.close();
        } catch (IOException ex) {
            System.err.println("Error: " + ex);
        }
        return cityDatabase;
    }

    // Cities are arranged in a chessboard like structure
    public static ArrayList<City> makePerfectCityList(int totalCities, int XYdistance) {
        ArrayList<City> cityDatabase = new ArrayList<>();
        int[] dimensions = calculate_grid_size(totalCities);
        int rows = dimensions[0];
        int columns = dimensions[1];
        int idCounter = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                cityDatabase.add(new City(idCounter, r * XYdistance, c * XYdistance));
                if (idCounter < totalCities) idCounter++;
            }
        }
        return cityDatabase;
    }

    // Operator that reduces
    public static ArrayList<City> reduce(ArrayList<City> cities, int preservation) {
        ArrayList<City> newList = new ArrayList<City>();

        // amount of cities to be removed
        int removeAmount = (int) cities.size() - (int) (cities.size() * 0.01 * preservation);
        Random rd = new Random();

        ArrayList<Integer> indexList = new ArrayList<>();
        int deleteIndex = -1;
        if (removeAmount > 0) {
            deleteIndex = rd.nextInt(cities.size());

            indexList.add(deleteIndex);
        }
        for (int i = 1; i < removeAmount; i++) {

            while (indexList.contains(deleteIndex)) {
                deleteIndex = rd.nextInt(cities.size());
            }
            indexList.add(deleteIndex);
        }
        // Only add city indices
        for (int i = 0; i < cities.size(); i++) {
            City city = cities.get(i);
            if (!indexList.contains(city.id)) {
                newList.add(city);
            }
            else newList.add(new City(city.id, -2,-2));
        }
        return newList;
    }

    public static ArrayList<City> shake(ArrayList<City> cities, int XYdistance, int shake) {

        ArrayList<City> newList = new ArrayList<City>();
        double standardD = XYdistance * shake * 0.01;
        Random r = new Random();
        int minimalX = 0;
        int minimalY = 0;

        // determine the amount of offset  == length of the radius
        for (int i = 0; i < cities.size(); i++) {
            City city = cities.get(i);
            double offset = r.nextGaussian() * standardD + 0;

            // offset can be in any random direction
            int angle = r.nextInt(360);

            double xOffset = Math.sin(angle)/offset;
            double yOffset = Math.cos(angle)/offset;

            int xIntegerOffset = (int )round(xOffset);
            int yIntegerOffset = (int )round(yOffset);

            city = new City(city.id, city.x+xIntegerOffset, city.y+yIntegerOffset);
            newList.add(city);

            if (xIntegerOffset  < minimalX) minimalX = xIntegerOffset;
            if (yIntegerOffset < minimalY) minimalY = yIntegerOffset;
        }

        ArrayList<City> shakeCities = new ArrayList<>();

        // Scale to only positive integers
        for (int p = 0; p < newList.size(); p++){
            City city = newList.get(p);
            shakeCities.add(new City(city.id,city.x+Math.abs(minimalX), city.y+Math.abs(minimalY)));
        }

        return shakeCities;
        }



public static void printGrid(Tour cities, ArrayList<City> emptyGrid){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                DrawGraph.createAndShowGui(cities, emptyGrid);
            }
        });
    }



    // Distributes an amount of cities evenly in rows and columns
    public static int[] calculate_grid_size(int totalCities) {
        double root_num = Math.sqrt(totalCities);
        int divider = (int) Math.ceil(root_num);
        while (divider <= totalCities) {

            if (totalCities % divider == 0) {
                int otherDivider = totalCities / divider;
                int[] data = new int[2];
                return new int[]{divider, otherDivider};
            }
            divider++;
        }
    return null;
    }



    public static double[][] calculateAnearnessMatrix(ArrayList<City> cities){
        int citySize = cities.size();
        double m1t = minimum1Tree(cities);

        double[][] cm = new double[citySize][citySize];

        // Loop trough all rows
        for (int i = 0; i < citySize; i++) {
            City fromCity = cities.get(i);

            // Loop trough all columns
            for (int j = 0; j < citySize; j++) {
                City toCity = cities.get(j);

                if (i == j) {
                    // Negative distance to self
                    cm[j][i] = -1.0;
                } else {
                    //Calculate Anearness
                    cm[j][i] = calculateAnearness(cities, m1t, new Edge(fromCity, toCity, Main.costMatrix[fromCity.id][toCity.id]));
                }
            }
        }

        return cm;
    }




    private static double calculateAnearness(ArrayList<City> allCities, double m1t, Edge e){

        ArrayList<Edge> with = create1Tree(allCities, e.to);

        double withLength = 0.0;
        for(Edge edge: with ) {
            withLength += edge.length;
        }

        return withLength - m1t;
    }



    // 1-Tree algorithm
    public static double minimum1Tree(ArrayList<City> allCities) {


        ArrayList<Edge> optimalTree = new ArrayList<>();
        double minlength = 1000000;

            for(City city: allCities) {
                ArrayList<Edge> oneTree = create1Tree(allCities, city);
                double length = 0;
                for(Edge edge : oneTree) {
                    length += edge.length;

                }
                if (minlength > length) {
                minlength = length;
                    optimalTree = oneTree;


                }
            }
System.out.print("Ideal tree:"  + optimalTree);

        return minlength;
    }

        public static ArrayList<Edge> create1Tree(ArrayList<City> allCities, City city1){

            ArrayList<City> subsetCities = new ArrayList<>();

            Comparator<Edge> comparator = new EdgeComparator();
            PriorityQueue<Edge> queue =
                    new PriorityQueue<Edge>(allCities.size(), comparator);

            for (City city: allCities){
                 if(city1 !=  city) {
                     queue.add(new Edge(city1, city, Main.costMatrix[city1.id][city.id]));

                     subsetCities.add(city);
                }
            }
            int allCitiesSize = allCities.size();
            ArrayList<Edge> mst = MST(subsetCities, allCitiesSize);

            mst.add(queue.poll());
            mst.add(queue.poll());

            return mst;

    }

    // KRUSKAL for minimum spannin tree
    public static ArrayList<Edge> MST(ArrayList<City> cityList, int allCitiesSize){
     ArrayList<Edge> currentMST = new ArrayList<>();
//        Graph graph = new Graph(cityList.size(), cityList.size()*cityList.size());
//double[][] cm = Main.costMatrix;
//for(int t=0;t<cityList.size(); t++){
//    for(int r=0;r<cityList.size(); r++) {
//        double dist = cm[t][r];
//        if (dist != -1)
//            graph.edge[0].src = t;
//            graph.edge[0].dest = r;
//            graph.edge[0].weight = (int)round(dist);
//        }
//        }
//
//
//
//


        Graph g1 = new Graph(allCitiesSize+1);
        for(City city: cityList){

            Edge minimalEdge = null;
            // Very high number st length will be shorter
            double minLength = 100000000;
            for (City city2: cityList) {
                Edge candidate = new Edge (city, city2, Main.costMatrix[city.id][city2.id]);
                if (minLength > candidate.length && candidate.length != -1) {
                    minLength = candidate.length;
                    minimalEdge  =candidate;
                }
            }

            g1.addEdge(minimalEdge.from.id, minimalEdge.to.id);

            if(!g1.isCyclic()){
                currentMST.add(minimalEdge);

            } else if (g1.hasEdge(minimalEdge.from.id, minimalEdge.to.id) ){

            g1.removeEdge(minimalEdge.from.id, minimalEdge.to.id);
            }
        }
        return currentMST;
    }

    public static boolean noCycle(ArrayList<Edge> subtour, Edge extra,int allCitiesSize){

        if (extra!=null) subtour.add(extra);
        Graph g1 = new Graph(allCitiesSize);
        for (Edge edge: subtour) {
            g1.addEdge((edge.from.id), (edge.to.id));

        }
         return g1.isCyclic();

    }




    public static boolean isValidTour(Tour tour, ArrayList<City> emptyGrid) {
        if (emptyGrid.size() != tour.head.size() || emptyGrid.size() != tour.tail.size()) return false;
        ArrayList<Integer> degreeList = new ArrayList<>();
        for (int i = 1; i <= emptyGrid.size(); i++) degreeList.add(0);

        //make sure degree of al verices is 2
        for (int g = 1; g <= emptyGrid.size(); g++) {

            degreeList.set(tour.head.elementAt(g).id, degreeList.get(tour.head.elementAt(g).id) + 1);
            degreeList.set(tour.tail.elementAt(g).id, degreeList.get(tour.tail.elementAt(g).id) + 1);
        }
        for (int degree : degreeList) {
            if (degree != 2) return false;
        }

        // make sure there are nu subtours: if one edge is deleted there shouldnt be any cycles

        Graph g3 = new Graph(emptyGrid.size());
        for (int l = 2; l <= tour.head.size(); l ++){
            g3.addEdge(tour.head.elementAt(l).id, tour.tail.elementAt(l).id);
        }
        return !(g3.isCyclic());

    }

}
