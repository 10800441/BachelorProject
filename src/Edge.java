/****************************************************
 **                Bachelor project                **
 ** City.java                                      **
 **                                                **
 ****************************************************
 */
class Edge {


    final City from;
    final City to;
    final double length;


    public Edge( City from, City to, double length) {

        this.from = from;
        this.to = to;
        this.length = length;
    }

    public Edge Inverse(){
        return new Edge(to, from, length);
    }


    public String toString(){
        return "\n Edge  goes from city "+ from.id + " to city " + to.id + " and has length: " + length + "\n";
    }

}
