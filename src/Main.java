import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.io.*;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.SynchronousQueue;

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
 *  This Program implements Lin-Kernighan Helsgaun  *
 *  algorithm for the Traveling Salesman Problem    *
 ****************************************************/


class Main {


    // percentage of preservation when applieing the reduce operator
    private static final int PRESERVE = 100;

    private static final int SHAKE = 50;
    // Amount of cities in a perfect configuration instance (non reduced
    private static final int TOTAL_CITIES = 12;

    // Distance between cities in a perfect configuration instance
    private static final int XYdistance = 12;

    private static DoublyLinkedListImpl<City> CITIES;
    private static double[][] costMatrix;


    public static void main(String[] args) {


    // %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
        // INITIALISATION OF THE GRID

        // make a grid with coordinates from the Cities.txt file
        //        CITIES = TSPFunctions.makeCityList();

        // Make a perfect grid
        CITIES = TSPFunctions.makePerfectCityList(TOTAL_CITIES, XYdistance);

        costMatrix = TSPFunctions.calculateCostMatrix(CITIES);
        TSPFunctions.printCostMatrix(costMatrix);

    // %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
        // MUTATING THE GRID

        // Reduction of the amount of cities
        //CITIES = TSPFunctions.reduce(CITIES, PRESERVE);

        CITIES = TSPFunctions.shake(CITIES, costMatrix, XYdistance, SHAKE);




        // SOLVER
        //Use the original Lin-Kernighan algorithm
        //DoublyLinkedListImpl<City> lkOptimalTour = Lin_Kernighan.solve_Lin_Kernighan(costMatrix);

        // PrintGrid
        TSPFunctions.printGrid(CITIES);
    }
}

