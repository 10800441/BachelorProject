import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Hans on 27-4-2017.
 */
public class Lin_Kernighan {

    private static double minimalTourLength;

    public static DoublyLinkedListImpl<City> solve_Lin_Kernighan(double[][] costMatrix) {
        System.out.println("\nRandom tour \n");
        DoublyLinkedListImpl<City> lkOptimalTour = TSPFunctions.generateRandomTour();
        minimalTourLength = TSPFunctions.calculateTourLenght(lkOptimalTour);

//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                DrawGraph.createAndShowGui(lkOptimalTour);
//            }
//        });

        // STEP 2
        int i = 1;
        Random rd = new Random();
        City t1 = lkOptimalTour.elementAt(1);
        City t2;

        if (rd.nextBoolean()) t2 = lkOptimalTour.next(t1);
        else t2 = lkOptimalTour.prev(t1);

        //Step 3
        Edge x1 = new Edge(t1, t2, costMatrix[t1.id][t2.id]);
        Edge y1;

        ArrayList<Edge> t2Edges = TSPFunctions.createCityEdgeSet(t2, lkOptimalTour);
        ArrayList<Edge> tourEdgeSet = TSPFunctions.createTourEdgeSet(lkOptimalTour);

        // Could result in backtracking
        for (Edge t2Edge : t2Edges) {

            //STEP 4
            // Find a y1 that is smaller than x1 and not yet in tour
            if (t2Edge.length < x1.length && !tourEdgeSet.contains(t2Edge)) {
                y1 = t2Edge;

                City t3 = null;
                if(t2Edge.to == t2) t3 = t2Edge.from;
                else if (t2Edge.from == t2) t3 = t2Edge.to;

                //STEP 5
                i++;

                //STEP 6
                //search trough all edges of t3
                ArrayList<Edge> t3Edges = TSPFunctions.createCityEdgeSet(t3, lkOptimalTour);
                for (Edge t3Edge : t3Edges) {

                    //take an egde that is in the current tour
                    if(tourEdgeSet.contains(t3Edge)){
                        City t4 = null;
                        if(t3Edge.to == t3) t4 = t3Edge.from;
                        else if (t3Edge.from == t3) t4 = t3Edge.to;

                        ArrayList<Edge> t4Edges = TSPFunctions.createCityEdgeSet(t4, lkOptimalTour);
                        for (Edge t4Edge : t4Edges){

                            // t4 must be connected to t1
                            if(t4Edge.to == t1){
                                Edge xi = t3Edge;
                                Edge yi = t4Edge;
                                lkOptimalTour = makeSwap(x1,xi,y1,yi,t1,t2,t3,t4, lkOptimalTour);

                                //        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                DrawGraph.createAndShowGui(lkOptimalTour);
//            }
//        });

                            }
                        }


                    }
                }




            }
        }



    return lkOptimalTour;
    }

    private static DoublyLinkedListImpl<City> makeSwap(Edge x1, Edge xi, Edge y1, Edge yi,
                                                       City t1, City t2, City t3, City t4,
                                                       DoublyLinkedListImpl<City> lkOptimalTour) {
        DoublyLinkedListImpl<City> newOptimal = new DoublyLinkedListImpl<City>();
        for (int a = 1; a <= lkOptimalTour.size(); a ++){
            newOptimal.
            if (lkOptimalTour.elementAt(a) == t1){
        }



        return newOptimal;
    }


    //Compute A-nearness matrix
    //calculateAlphaNearnessMatrix();
// Expansion to lkh
//    private static double[][] calculateAlphaNearnessMatrix(){
//        double[][] alphaNearnessMatrix = new double[TOTAL_CITIES][TOTAL_CITIES];
//
//        // Choose random node V1
//        Random rg = new Random();
//        int a = rg.nextInt(TOTAL_CITIES);
//        // Compute Minimum Spanning Tree without V1
//
//        // calculate 1-Tree
//
//        //calculate alpha nearness
//        return alphaNearnessMatrix;
//    }
}
