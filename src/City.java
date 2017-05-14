/****************************************************
 **                Bachelor project                **
 ** City.java                                      **
 **                                                **
 ****************************************************
 */
class City {
    final int id;
    final int x;
    final int y;


    public City(int id, int x, int y ) {
        this. id = id;
        this.x = x;
        this.y = y;
    }
    public String toString(){
        return "City " + id + ", placed on x = " + x + ", y = " + y  ;

    }

}
