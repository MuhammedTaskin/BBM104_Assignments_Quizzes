import java.util.ArrayList;
import java.util.List;

public class DataContent {
    private List<Road> fastestRoute;
    private List<Road> barelyConnectedMap;
    private List<Road> fastestRouteOnBCP;
    double materialRatio;
    double routeRatio;


    /**
     * The constructor to initialize the lists.
     */
    DataContent() {
        fastestRoute = new ArrayList<>();
        barelyConnectedMap = new ArrayList<>();
        fastestRouteOnBCP = new ArrayList<>();
    }

    //getters
    public List<Road> getFastestRoute() {
        return fastestRoute;
    }

    public List<Road> getBarelyConnectedMap() {
        return barelyConnectedMap;
    }

    public List<Road> getFastestRouteOnBCP() {
        return fastestRouteOnBCP;
    }

    public void setFastestRoute(List<Road> fastestRoute) {
        this.fastestRoute = fastestRoute;
    }

    public void setBarelyConnectedMap(List<Road> barelyConnectedMap) {
        this.barelyConnectedMap = barelyConnectedMap;
    }

    public void setFastestRouteOnBCP(List<Road> fastestRouteOnBCP) {
        this.fastestRouteOnBCP = fastestRouteOnBCP;
    }
}
