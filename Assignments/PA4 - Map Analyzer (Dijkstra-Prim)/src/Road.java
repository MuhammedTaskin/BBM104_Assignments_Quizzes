public class Road {
    private String pointA;
    private String pointB;
    private int length;
    private int id;

    /**
     * Constructs a Road with specified points, length, and id.
     *
     * @param pointA An endpoint of the road.
     * @param pointB Another endpoint of the road.
     * @param length The length of the road.
     * @param id The id for the road.
     */
    Road(String pointA, String pointB, int length, int id) {
        this.pointA = pointA;
        this.pointB = pointB;
        this.length = length;
        this.id = id;
    }

    //Getters
    public String getPointA() {
        return pointA;
    }

    public String getPointB() {
        return pointB;
    }

    public int getLength() {
        return length;
    }

    public int getId() {
        return id;
    }
}
