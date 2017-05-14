import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

/****************************************************
 **                Bachelor project                **
 **                                                **
 ** Mentor:     Daan van den Berg                  **
 ** Name:       Martijn Heijstek                   **
 ** Stud.num:   10800441                           **
 ** Date:       21-04-2017                         **
 ****************************************************
 *  This Program implements Lin-Kernighan
 *  algorithm for the Traveling Salesman Problem    *
 *  Source:                                         *
 *  http://akira.ruc.dk/~keld/research/LKH/LKH-1.3/DOC/LKH_REPORT.pdf
 *  (Page 12)                                       *
 ****************************************************/


public class Lin_Kernighan {

    public static Tour solve_Lin_Kernighan(ArrayList<City> emptyGrid) {
        System.out.println("\n-------------------------------------------------------------" +
                "\nSimplified Lin-Kernighan impementation\n");

        System.out.println("\n -> STEP 1: Random tour \n");
        Tour tour = TSPFunctions.generateRandomTour(emptyGrid);

//        System.out.println("mst\n " + TSPFunctions.MST(emptyGrid, emptyGrid.size()));

        Tour lkOptimalTour = null;
        double gain = 1.0;
        while(gain >= 1.0) {


            // STEP 2
            int i = 1;
            System.out.println("\n -> STEP 2 choose t1");
            Random rd = new Random();

            // Find an index in range
            int index = rd.nextInt(tour.amountOfEdges() - 1) + 1;
            City t1 = tour.head.elementAt(index);

            System.out.println("t1 = " + t1);


            //Step 3
            System.out.println("\n -> STEP 3, choose x1");

           ArrayList<City> t2Stack = new ArrayList<>();
            if (rd.nextBoolean()){
                t2Stack.add(tour.next(t1));
                t2Stack.add(tour.prev(t1));
            } else {
                t2Stack.add(tour.prev(t1));
                t2Stack.add(tour.next(t1));
            }
            boolean returnTo2 = false;
            for(City t2: t2Stack) {

                if(!returnTo2) {
                    Edge x1 = new Edge(t1, t2, Main.costMatrix[t1.id][t2.id]);

                    System.out.print("\nx1 = " + x1.from.id + " - " + x1.to.id);
                    ArrayList<Edge> candidateY1Edges = TSPFunctions.createCityEdgeSet(t2, emptyGrid);

                    Stack<Edge> y1Edges = new Stack<>();

                    // stack all possible y1 candidates
                    for (Edge candidateY1 : candidateY1Edges) {

                    //STEP 4
                        // Find a y1 that is smaller than x1 and not yet in tour
                        if ((candidateY1.length < x1.length) && !tour.edgeInTour(candidateY1)) {
                            y1Edges.push(candidateY1);
                        }
                    }

                    // loop trough all y1 candidates
                    boolean returnToStep3 = false;
                    for (Edge y1: y1Edges) {

                        if(!returnTo2 && !returnToStep3) {

                            System.out.println("\n -> STEP 4, choose y1");
                            System.out.println("\n y1 = " + y1.from.id + " - " + y1.to.id);

                            // Loop
                            System.out.println("\n -> STEP 5, enter loop");

                            i++;
                            // contains all previously chosen xi and yi's
                            ArrayList<Edge> xList = new ArrayList<>();
                            ArrayList<Edge> yList = new ArrayList<>();

                            xList.add(x1);
                            yList.add(y1);

                            // Loop over t3's
                            City t2i_1 = y1.to;
                            while (t2i_1 != null && !returnTo2 && !returnToStep3) {
                                // Determine t2i-1
                                t2i_1 = yList.get(yList.size() - 1).to;
                                System.out.println("t2i-1 = city " + t2i_1.id);

                               // Step 6 choose the next xi and yi, if not possible returns null
                                xiyiTour chosenEdges = chooseXiYi(tour, emptyGrid, t2i_1, x1.from, xList, yList);

                                if (chosenEdges == null) {
                                    System.out.println("- > STEP 12 no new xi is found ");
                                    returnTo2 = true;
                                }

                                // If the distance is is shorter the tour is improved
                                else if (chosenEdges.tour.length < tour.length) {
                                    gain = tour.length - chosenEdges.tour.length;
                                    System.out.println("\nFound improvement, Gain = " + gain + ", back to step 2..");
                                    tour = chosenEdges.tour;
                                    returnToStep3 = true;
                                    break;
                                } else {

                                // xi is set
                                xList.add(chosenEdges.xi);

                                // Determine the new t2i;
                                Stack<Edge> yiEdges = new Stack<>();
                                City t2i = xList.get(xList.size() - 1).to;

                                // stack all possible yi candidates
                                ArrayList<Edge> candidateYiEdges = TSPFunctions.createCityEdgeSet(t2i, emptyGrid);
                                for (Edge candidateYi : candidateYiEdges) {
                                    if (!tour.edgeInTour(candidateYi) && candidateYi != chosenEdges.yi && !yList.contains(candidateYi) && !yList.contains(candidateYi.Inverse())) {
                                        yiEdges.push(candidateYi);
                                    }
                                }

                                while (!yiEdges.isEmpty() && !returnToStep3) {
                                    Edge yi = yiEdges.pop();
                                    System.out.println("yi = " + yi);
                                    yList.add(yi);
                                    // Determine the new t2i_1
                                    t2i_1 = yi.to;
                                }
                            }
                        }
                    }
                }
                if (returnTo2) System.out.println("returning to step 2....");
                System.out.println("trying alternative for x1");
                }
            }
        }
        return tour;
    }



    private static xiyiTour chooseXiYi(Tour tour, ArrayList<City>emptyGrid, City t2i_1, City t1, ArrayList<Edge> xList, ArrayList<Edge> yList){
        // STEP 6
        Edge xi = null;
        Edge yi = null;
        System.out.println("\n -> STEP 6");

        // Search trough all edges of t3
        ArrayList<Edge> candidateXiEdges = TSPFunctions.createCityEdgeSet(t2i_1, emptyGrid);
        Tour noSubTour = null;
        for (Edge candidateXi : candidateXiEdges) {

            //TODO x2 y2 are wrong
            // Take an egde that is in the current tour but not already in xiList
            if ((tour.next(t2i_1) == candidateXi.to || tour.prev(t2i_1) == candidateXi.to) &&
                    (!xList.contains(candidateXi) && !xList.contains(candidateXi.Inverse())) && candidateXi.to != t1) {

                // Check if the current solution is valid by closing the tour and checking for subtours


                Edge yiCandidate = new Edge(candidateXi.to, t1, Main.costMatrix[t1.id][candidateXi.to.id]);

                System.out.println("candidateXi = " + candidateXi.from + " - " + candidateXi.to);
                System.out.println("yiCandidate = " + yiCandidate.from + " - " + yiCandidate.to);


                // Sub tour elimination; there should be exactly one possible xi
                noSubTour = makeLambdaSwap(xList, yList, candidateXi, yiCandidate, tour);
                // if there is a valid tour keep xi
                if (TSPFunctions.isValidTour(noSubTour, emptyGrid)) {
                    xi = candidateXi;
                    System.out.println("Valid tour found: xi = " + xi);

                    return new xiyiTour(xi, yiCandidate, noSubTour);
                }
            }
        }
        return null;
    }

    public static double calculateGain(ArrayList<Edge> xiList, ArrayList<Edge> yiList) {
        double xiLength = 0.0;
        double yiLength = 0.0;
        for (Edge xEdge : xiList) xiLength += xEdge.length;
        for (Edge yEdge : yiList) yiLength += yEdge.length;

        return xiLength - yiLength;
    }

    // NOT WORKING PROPERLY
//    // 2 opt operator to change the order of cities in the tour
    public static Tour makeSwap(Edge x1, Edge y1, Edge xi, Edge yi, Tour oldTour) {
        System.out.println("Swap procedure");
        System.out.println("x1 " + x1.from.id + " - " + x1.to.id);
        System.out.println("y1 " + y1.from.id + " - " + y1.to.id);
        System.out.println("xi " + xi.from.id + " - " + xi.to.id);
        System.out.println("yi " + yi.from.id + " - " + yi.to.id);


        DoublyLinkedListImpl<City> head = new DoublyLinkedListImpl<>();
        DoublyLinkedListImpl<City> tail = new DoublyLinkedListImpl<>();

        for (int k = 1; k <= oldTour.head.size(); k++) {
            City from = oldTour.head.elementAt(k);
            City to = oldTour.tail.elementAt(k);

            if ((from == x1.from && to == x1.to) || (to == x1.from && from == x1.to) ||
                    (from == xi.from && to == xi.to) || (to == xi.from && from == xi.to)) {


            } else {
                head.addLast(oldTour.head.elementAt(k));
                tail.addLast(oldTour.tail.elementAt(k));
            }
        }

        head.addFirst(y1.from);
        tail.addFirst(y1.to);
        head.addFirst(yi.from);
        tail.addFirst(yi.to);


        Double length = TSPFunctions.calculateEdgeLength(head, tail);
        return new Tour(length, head, tail);
    }

    public static class xiyiTour {
        public Edge xi;
        public Edge yi;
        public Tour tour;
        public xiyiTour(Edge xi, Edge yi, Tour tour) {
            this.xi = xi;
            this.yi = yi;
            this.tour= tour;
        }
    }

    // Lambda opt operator that will replace all edges in xList (xi candidate included) wit yi (yi candidate included)
    public static Tour makeLambdaSwap(ArrayList<Edge> xList, ArrayList<Edge> yList,Edge candidateXi, Edge yiCandidate,  Tour oldTour) {
        System.out.println("Swap procedure for lambda opt");
        if(candidateXi != null) {
            xList.add(candidateXi);
            yList.add(yiCandidate);
        }
        for (int i = 0; i < xList.size(); i++) {
            System.out.println("x" + (i + 1) + " = " + xList.get(i).from.id + " - " + xList.get(i).to.id);


            System.out.println("y" + (i + 1) + " = " + yList.get(i).from.id + " - " + yList.get(i).to.id);

        }

        DoublyLinkedListImpl<City> head = new DoublyLinkedListImpl<>();
        DoublyLinkedListImpl<City> tail = new DoublyLinkedListImpl<>();

        for (int k = 1; k <= oldTour.head.size(); k++) {
            City from = oldTour.head.elementAt(k);
            City to = oldTour.tail.elementAt(k);

            boolean isXi = false;
            for(Edge xi: xList) {
                if ((xi.from == from && xi.to == to) || (xi.to == from && xi.from == to)) {
                    isXi = true;
                }
            }

            if (!isXi) {
                head.addLast(from);
                tail.addLast(to);
            }

        }
        for (int i = 0; i < xList.size(); i++) {
            head.addFirst(yList.get(i).from);
            tail.addFirst(yList.get(i).to);
        }
        if(candidateXi != null) {
            xList.remove(candidateXi);
            yList.remove(yiCandidate);
        }
        Double length = TSPFunctions.calculateEdgeLength(head, tail);
        return new Tour(length, head, tail);
    }
}