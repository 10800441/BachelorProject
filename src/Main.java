import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.io.*;
import java.util.Collections;
import java.util.Random;

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
    private static int TOTAL_CITIES = 0;
    private static DoublyLinkedListImpl<City> CITIES;
    private static double[][] costMatrix;


    public static void main(String[] args) {



        // INITIALISATION OF THE GRID
        // Load cities form Cities.txt into an ArrayList
        CITIES = TSPFunctions.makeCityList();
        TOTAL_CITIES = CITIES.size();
        costMatrix = TSPFunctions.calculateCostMatrix();
        TSPFunctions.printCostMatrix();


        // SOLVER
        //Use the original Lin-Kernighan algorithm
        DoublyLinkedListImpl<City> lkOptimalTour = Lin_Kernighan.solve_Lin_Kernighan(costMatrix);


    }
}