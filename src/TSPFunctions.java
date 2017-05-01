import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static java.lang.Math.abs;
import static java.lang.Math.round;
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


    // Create all edges in a given tour
    public static ArrayList<Edge> createTourEdgeSet(DoublyLinkedListImpl<City> allCities, double[][] costMatrix) {
        ArrayList<Edge> tourEdgeSet = new ArrayList<>();
        for (int l = 1; l <= allCities.size(); l++) {
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
    public static ArrayList<Edge> createCityEdgeSet(City city, DoublyLinkedListImpl<City> allCities, double[][] costMatrix) {
        ArrayList<Edge> edgeSet = new ArrayList<>();
        for (int a = 1; a < allCities.size(); a++) {
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
    public static DoublyLinkedListImpl<City> generateRandomTour(DoublyLinkedListImpl<City> emptyGrid) {
        DoublyLinkedListImpl<City> randomTour = new DoublyLinkedListImpl<City>();

        // Make a list of unique random numbers
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < emptyGrid.size(); i++) indices.add(i);
        Collections.shuffle(indices);

        // Make a random tour
        for (int k = 0; k < emptyGrid.size(); k++) {
            City randomCity = emptyGrid.elementAt(indices.get(k) + 1);
            randomTour.addLast(randomCity);
        }

        return randomTour;

    }

    // Compute the total tour length of a given tour
    public static double calculateTourLenght(DoublyLinkedListImpl<City> cities, double[][] costMatrix) {
        double length = 0.0;
        System.out.println("\n");
        System.out.println("Total tour lenght:");
        System.out.print("for tour: ");

        //For every node in the tour
        for (int index = 1; index < (cities.size()); index++) {
            length += costMatrix[cities.elementAt(index).id][cities.elementAt(index + 1).id];
            System.out.print(cities.elementAt(index).id + " - ");
        }

        // addlength of the edge that closes the tour
        length += costMatrix[cities.elementAt(1).id][cities.elementAt(cities.size()).id];
        System.out.print(cities.elementAt(cities.size()).id + " ,has length: " + length);
        System.out.println("\n");
        return length;
    }

    // Fot a given list of cities, calculate the cost matrix
    public static double[][] calculateCostMatrix(DoublyLinkedListImpl<City> cities) {
        int citySize = cities.size();
        double[][] cm = new double[citySize][citySize];

        // Loop trough all rows
        for (int i = 0; i < citySize; i++) {
            City fromCity = cities.elementAt(i + 1);

            // Loop trough all columns
            for (int j = 0; j < citySize; j++) {
                City toCity = cities.elementAt(j + 1);

                if (i == j) {
                    // Negative distance to self
                    cm[j][i] = -1.0;
                } else {
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
    public static void printCostMatrix(double[][] costMatrix) {
        System.out.println("\nCost Matrix:");
        // Creation of rows
        for (int h = 0; h < costMatrix[0].length; h++) {
            if (h == 0) {
                System.out.print("           ");
                for (int l = 0; l < costMatrix[0].length; l++) {
                    System.out.print("City " + l + ":   ");
                }
            }
            System.out.println("");

            // Creation of columns
            for (int w = 0; w < costMatrix[0].length; w++) {
                if (w == 0) {
                    System.out.print("City " + h + ":  ");
                }
                if (costMatrix[h][w] == -1.0) {
                    System.out.print("     .    ");
                } else {
                    System.out.print("   " + String.format("%.2f", costMatrix[h][w]) + "  ");
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
        return cityDatabase;
    }

    // Cities are arranged in a chessboard like structure
    public static DoublyLinkedListImpl<City> makePerfectCityList(int totalCities, int XYdistance) {
        DoublyLinkedListImpl<City> cityDatabase = new DoublyLinkedListImpl<City>();
        int[] dimensions = calculate_grid_size(totalCities);
        int rows = dimensions[0];
        int columns = dimensions[1];
        int idCounter = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                if (idCounter < totalCities) idCounter++;
                cityDatabase.addFirst(new City(idCounter, r * XYdistance, c * XYdistance, 0));
            }
        }
        return cityDatabase;
    }

    // Operator that reduces
    public static DoublyLinkedListImpl<City> reduce(DoublyLinkedListImpl<City> cities, int preservation) {
        DoublyLinkedListImpl<City> newList = new DoublyLinkedListImpl<City>();

        // amount of cities to be removed
        int removeAmount = (int) cities.size() - (int) (cities.size() * 0.01 * preservation);
        Random rd = new Random();

        ArrayList<Integer> indexList = new ArrayList<>();
        int deleteIndex = rd.nextInt(cities.size());
        indexList.add(deleteIndex);
        for (int i = 1; i < removeAmount; i++) {

            while (indexList.contains(deleteIndex)) {
                deleteIndex = rd.nextInt(cities.size());
            }
            indexList.add(deleteIndex);
        }
        // Only add city indices
        for (int i = 1; i <= cities.size(); i++) {
            City city = cities.elementAt(i);
            if (!indexList.contains(city.id)) {
                newList.addFirst(city);
            }
        }
        return newList;
    }

    public static DoublyLinkedListImpl<City> shake(DoublyLinkedListImpl<City> cities, double[][] costMatrix, int XYdistance, int shake) {
        DoublyLinkedListImpl<City> newList = new DoublyLinkedListImpl<City>();
        double standardD = XYdistance * shake * 0.01;
        Random r = new Random();
        int minimalX = 0;
        int minimalY = 0;

        // determine the amount of offset  == length of the radius
        for (int i = 1; i <= cities.size(); i++) {
            City city = cities.elementAt(i);
            double offset = r.nextGaussian() * standardD + 0;

            // offset can be in any random direction
            int angle = r.nextInt(360);

            double xOffset = Math.sin(angle)/offset;
            double yOffset = Math.cos(angle)/offset;

            int xIntegerOffset = (int )round(xOffset);
            int yIntegerOffset = (int )round(yOffset);

            city = new City(city.id, city.x+xIntegerOffset, city.y+yIntegerOffset, city.degree);
            newList.addFirst(city);

            if (xIntegerOffset  < minimalX) minimalX = xIntegerOffset;
            if (yIntegerOffset < minimalY) minimalY = yIntegerOffset;
        }

        DoublyLinkedListImpl<City> shakeCities = new DoublyLinkedListImpl<>();

        // Scale to only positive integers
        for (int p = 1; p <= newList.size(); p++){
            City city = newList.elementAt(p);
            shakeCities.addFirst(new City(city.id,city.x+Math.abs(minimalX), city.y+Math.abs(minimalY), city.degree));
        }

        return shakeCities;
        }



public static void printGrid(DoublyLinkedListImpl<City> cities){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                DrawGraph.createAndShowGui(cities);
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


}
