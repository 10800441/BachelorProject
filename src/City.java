/**
 * Created by Hans on 21-4-2017.
 */
class City {
    final int id;
    final int x;
    final int y;
    final int degree;


    public City(int id, int x, int y, int degree) {
        this. id = id;
        this.x = x;
        this.y = y;
        this.degree  = degree;
    }
    public String toString(){
        return "City " + id + ", placed on x = " + x + ", y = " + y + " has degree: " + degree ;

    }

}
