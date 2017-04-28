import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

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
    private static int TOTAL_CITIES = 0;
    private static DoublyLinkedListImpl<City> CITIES;
    private static double[][] costMatrix;


    // Create all edges in a given tour
    public static ArrayList<Edge> createTourEdgeSet (DoublyLinkedListImpl<City> allCities) {
        ArrayList<Edge> tourEdgeSet = new ArrayList<>();
        for (int l = 1 ; l <= TOTAL_CITIES; l ++) {
            City fromCity = allCities.elementAt(l);
            City toCity = allCities.next(fromCity);
            Edge inTourEdge = new Edge(fromCity, toCity, costMatrix[fromCity.id][toCity.id]);
            tourEdgeSet.add(inTourEdge);
            tourEdgeSet.add(inTourEdge.Inverse());
        }
        //System.out.println("in tour " + tourEdgeSet);
        return tourEdgeSet;
    }

    // For one city this function creates all possible edges
    public static ArrayList<Edge> createCityEdgeSet (City city, DoublyLinkedListImpl<City> allCities) {
        ArrayList<Edge> edgeSet = new ArrayList<>();
        for (int a = 1; a < TOTAL_CITIES; a ++) {
            City toCity = allCities.elementAt(a);

            Edge edge;
            if (city != toCity) {
                edge = new Edge(city, toCity, costMatrix[city.id][toCity.id]);
                edgeSet.add(edge);
                edgeSet.add(edge.Inverse());
            }
        }
        //System.out.println(edgeSet);
        return edgeSet;
    }


    //Generate a random tour and put it in a DoublyLinkedList
    public static DoublyLinkedListImpl<City> generateRandomTour() {
        DoublyLinkedListImpl<City> randomTour = new DoublyLinkedListImpl<City>();

        // Make a list of unique random numbers
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i=0; i < TOTAL_CITIES; i++) indices.add(i);
        Collections.shuffle(indices);

        // Make a random tour
        for (int k=0; k < TOTAL_CITIES; k++) {
            City randomCity = CITIES.elementAt(indices.get(k)+1);
            randomTour.addLast(randomCity);
        }

        return randomTour;

    }

    // Compute the total tour length of a given tour
    public static double calculateTourLenght(DoublyLinkedListImpl<City> cities) {
        double length = 0.0;
        System.out.println("\n");
        System.out.println("Total tour lenght:");
        System.out.print("for tour: ");

        //For every node in the tour
        for (int index = 1; index < (TOTAL_CITIES); index++) {
            length += costMatrix[cities.elementAt(index).id][cities.elementAt(index + 1).id];
            System.out.print(cities.elementAt(index).id + " - ");
        }

        // addlength of the edge that closes the tour
        length += costMatrix[cities.elementAt(1).id][cities.elementAt(TOTAL_CITIES).id];
        System.out.print(cities.elementAt(TOTAL_CITIES).id + " ,has length: " + length);
        System.out.println("\n");
        return length;
    }

    // Fot a given list of cities, calculate the cost matrix
    public static double[][] calculateCostMatrix() {

        double[][] cm = new double[TOTAL_CITIES][TOTAL_CITIES];

        // Loop trough all rows
        for (int i = 0; i < TOTAL_CITIES; i++) {
            City fromCity = CITIES.elementAt(i+1);

            // Loop trough all columns
            for (int j = 0; j < TOTAL_CITIES; j++) {
                City toCity = CITIES.elementAt(j+1);

                if (i == j) {
                    // Negative distance to self
                    cm[j][i] = -1.0;
                }
                else {
                    //Calculate euclidean distance using pythagoras theorem
                    double delta_x = abs(fromCity.x - toCity.x);
                    double delta_y = abs(fromCity.y - toCity.y);
                    cm[j][i] = sqrt(Math.pow(delta_x,2) + Math.pow(delta_y,2));
                }
            }
        }

        costMatrix = cm;
        return cm;
    }

    // Visualise the cost matix
    public static void printCostMatrix() {
        System.out.println("\nCost Matrix:");

        // Creation of rows
        for (int h = 0; h < TOTAL_CITIES; h++) {
            if (h==0){
                System.out.print("           ");
                for (int l = 0; l < TOTAL_CITIES; l ++) {
                    System.out.print("City " + l + ":   ");
                }
            }
            System.out.println("");

            // Creation of columns
            for (int w = 0; w < TOTAL_CITIES; w++) {
                if (w==0) {
                    System.out.print("City " + h + ":  ");
                }
                if (costMatrix[h][w] == -1.0) {
                    System.out.print("     .    ");
                } else {
                    System.out.print("   " + String.format("%.2f",costMatrix[h][w]) + "   ");
                }
            }
        }
    }



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
                int city_degree = Integer.valueOf(cityString[3]);

                City city = new City(city_id, city_x, city_y, city_degree);
                cityDatabase.addLast(city);
            }
            rd.close();
        } catch (IOException ex) {
            System.err.println("Error: " + ex);
        }
        CITIES = cityDatabase;
        TOTAL_CITIES = CITIES.size();
        return cityDatabase;
    }


}
