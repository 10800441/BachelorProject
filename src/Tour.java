import java.util.ArrayList;

public class Tour {
    double length;
    DoublyLinkedListImpl<City> head;
    DoublyLinkedListImpl<City> tail;

    public Tour(double length, DoublyLinkedListImpl<City> head, DoublyLinkedListImpl<City> tail) {
        this.length = length;
        this.head = head;
        this.tail = tail;
    }

    public int amountOfEdges(){
        return head.size();
    }
    public Edge edgeAt(int index){
        City h = head.elementAt(index);
        City t = tail.elementAt(index);
        double l = Main.costMatrix[h.id][t.id];
        return new Edge(h, t, l);
    }


    public Boolean edgeInTour(Edge edge){
        for(int l = 1; l <= head.size(); l ++){
            City h = head.elementAt(l);
            City t = tail.elementAt(l);
            if((edge.from == h && edge.to == t)||(edge.to == h && edge.from == t)) return true;
        }

        return false;
    }
    public City next(City current) {

        for (int i = 1; i <= head.size(); i ++){
            if (current == head.elementAt(i)){
                return tail.elementAt(i);
            }
        }
        return null;
    }

    public City prev(City current) {
        for (int i = 1; i <= tail.size(); i ++){
            if (current == tail.elementAt(i)){
                return head.elementAt(i);
            }
        }
        return null;
    }

        // Create all edges in a given tour
    public static ArrayList<Edge> createTourEdgeSet(Tour tour) {
        DoublyLinkedListImpl<City> from = tour.head;
        DoublyLinkedListImpl<City> to = tour.tail;
        ArrayList<Edge> tourEdgeSet = new ArrayList<>();
        for (int l = 1; l <= from.size(); l++) {
            City fromCity = from.elementAt(l);
            City toCity = to.elementAt(l);
            Edge inTourEdge = new Edge(fromCity, toCity, Main.costMatrix[fromCity.id][toCity.id]);
            tourEdgeSet.add(inTourEdge);
            tourEdgeSet.add(inTourEdge.Inverse());
        }
        //System.out.println("in tour " + tourEdgeSet);
        return tourEdgeSet;
    }

    public Edge nextEdge(Edge current){
        City nextFrom = head.next(current.from);
        City nextTo = tail.next(current.to);
        int id;
        if(head.elementAt(head.size()) == current.from) {
            nextFrom = head.elementAt(1);
            nextTo = tail.elementAt(1);
        }
        return new Edge(nextFrom,nextTo, Main.costMatrix[nextFrom.id][nextTo.id]);


    }
    public Edge prevEdge(Edge current){
        City nextFrom = head.prev(current.from);
        City nextTo = tail.prev(current.to);
        if(head.elementAt(1) == current.from) {
            nextFrom = head.elementAt(head.size());
            nextTo = tail.elementAt(tail.size());
        }

        return new Edge(nextFrom,nextTo, Main.costMatrix[nextFrom.id][nextTo.id]);


    }
    public String toString(){
        return "\nTour has length: "+ length + " with " + amountOfEdges() + " Edges\n";
    }

}


