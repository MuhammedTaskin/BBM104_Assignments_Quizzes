import java.util.*;

public class Navigator {

    public static void main(String[] args) {

        Locale.setDefault(Locale.US);

        String inputFile = args[0];
        String outputFile = args[1];

        //Reading input and parsing
        String[] oldInfo = FileInput.readFile(inputFile,true,true);
        assert oldInfo != null;
        String[] reach = oldInfo[0].split("\t");
        String startPoint = reach[0], endPoint = reach[1];

        String[] roadInfo = new String[oldInfo.length -1];
        System.arraycopy(oldInfo,1, roadInfo, 0, roadInfo.length);

        List<Road> roads = new ArrayList<>();

        for (String line : roadInfo) {
            String[] parts = line.split("\t");
            roads.add(new Road(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3])));
        }

        compare(roads);

        // Finding the fastest route, barely connected map, and analysis
        DataContent data = analyzeEverything(startPoint, endPoint, roads);

        // Writing out everything
        FileOutput.writeToFile(outputFile,"",false,false);
        writeOutput(outputFile, data, startPoint,endPoint);
    }

    /**
     * Analyzes the map to find the fastest route and the barely connected map.
     * Determines every single results which will be written out
     *
     * @param startPoint the starting point of the map.
     * @param endPoint the ending point of the map.
     * @param roads The list of all roads.
     * @return DataContent Contains the analysis of every result.
     */
    public static DataContent analyzeEverything(String startPoint, String endPoint, List<Road> roads) {
        // Analyze the map and record it to the DataContent file
        DataContent data = new DataContent();
        data.setFastestRoute(findFastestRoute(startPoint, endPoint, roads));
        data.setBarelyConnectedMap(findBarelyConnectedMap(roads));
        data.setFastestRouteOnBCP(findFastestRoute(startPoint, endPoint, data.getBarelyConnectedMap()));

        int originalMaterial = calculateTotalLength(roads);
        int bCMaterial = calculateTotalLength(data.getBarelyConnectedMap());
        int originalFastestRouteDistance = calculateTotalLength(data.getFastestRoute());
        int fastestRouteDistanceOnBCP = calculateTotalLength(data.getFastestRouteOnBCP());

        data.materialRatio = (double) bCMaterial / originalMaterial;
        data.routeRatio = (double) fastestRouteDistanceOnBCP / originalFastestRouteDistance;

        return data;
    }

    /**
     * Finds the fastest route between start and end points.
     *
     * @param startPoint the starting point of the map.
     * @param endPoint the ending point of the map.
     * @param roads The list of all roads.
     * @return The list of roads for the fastest route.
     */
    public static List<Road> findFastestRoute(String startPoint, String endPoint, List<Road> roads) {
        Map<String, Integer> distances = new HashMap<>();
        Map<String, Road> previousRoads = new HashMap<>();
        Set<String> visited = new HashSet<>();
        List<String> points = new ArrayList<>();

        for (Road road : roads) {
            if (!distances.containsKey(road.getPointA())) { points.add(road.getPointA()); }
            if (!distances.containsKey(road.getPointB())) { points.add(road.getPointB()); }
            distances.put(road.getPointA(), Integer.MAX_VALUE);
            distances.put(road.getPointB(), Integer.MAX_VALUE);
        }
        distances.put(startPoint, 0);

        while (!points.isEmpty()) {
            //Finding the closest point
            String closestPoint = null;
            int minDistance = Integer.MAX_VALUE;

            for (String point : points) {
                int distance = distances.get(point);
                if (distance < minDistance) {
                    minDistance = distance;
                    closestPoint = point;
                }
            }

            assert closestPoint != null;
            if (closestPoint.equals(endPoint)) break;

            points.remove(closestPoint);
            visited.add(closestPoint);

            compare(roads);

            //Finding the fastest route
            for (Road road : roads) {
                if (road.getPointA().equals(closestPoint) && !visited.contains(road.getPointB())) {
                    //If there is a road with the shortest distance update the distances and add it to previous roads
                    int newDistance = distances.get(closestPoint) + road.getLength();
                    if (newDistance < distances.get(road.getPointB())) {
                        distances.put(road.getPointB(), newDistance);
                        previousRoads.put(road.getPointB(), road);
                    }
                } else if (road.getPointB().equals(closestPoint) && !visited.contains(road.getPointA())) {
                    int newDistance = distances.get(closestPoint) + road.getLength();
                    if (newDistance < distances.get(road.getPointA())) {
                        distances.put(road.getPointA(), newDistance);
                        previousRoads.put(road.getPointA(), road);
                    }
                }
            }
        }

        //Create the route by iterating it from the endpoint
        List<Road> route = new ArrayList<>();
        String step = endPoint;
        while (previousRoads.containsKey(step)) {
            Road road = previousRoads.get(step);
            route.add(road);
            step = road.getPointA().equals(step) ? road.getPointB() : road.getPointA();
        }
        Collections.reverse(route);
        return route;
    }

    /**
     * Finds the barely connected map
     * Uses the minimum spanning tree approach.
     * (I utilized codes from geeksforgeeks.com by observing while creating an MST algorithm -It is not a copy, just fyi)
     *
     * @param roads The list of roads.
     * @return The list of roads for the barely connected map.
     */

    public static List<Road> findBarelyConnectedMap(List<Road> roads) {
        List<Road> sortedRoads = new ArrayList<>(roads);
        Map<String, String> parent = new HashMap<>();
        sortedRoads.sort(Comparator.comparing(Road::getPointA));
        compare(sortedRoads);

        //Initially, each road is the parent of itself
        for (Road road : roads) {
            parent.put(road.getPointA(), road.getPointA());
            parent.put(road.getPointB(), road.getPointB());
        }

        //Then connect the route like a tree form and, so, obstruct creating a loop
        List<Road> minimumSpanningTreeList = new ArrayList<>();
        for (Road road : sortedRoads) {
            String rootA = findRoot(road.getPointA(), parent);
            String rootB = findRoot(road.getPointB(), parent);
            if (!rootA.equals(rootB)) {
                minimumSpanningTreeList.add(road);
                parent.put(rootA, rootB);
                compare(minimumSpanningTreeList);
            }

        }
        return minimumSpanningTreeList;
    }

    /**
     * Finds the root of a point
     * Union-find structure.
     *
     * @param point The point to find the root of.
     * @param parent The union-find structure.
     * @return The root of the point.
     */
    public static String findRoot(String point, Map<String, String> parent) {
        while (!point.equals(parent.get(point))) {
            point = parent.get(point);
        }
        return point;
    }

    /**
     * Calculates the total length of a list of roads.
     *
     * @param roads The list of roads.
     * @return The total material length.
     */
    public static int calculateTotalLength(List<Road> roads) {
        int total = 0;
        for (Road road : roads) {
            total += road.getLength();
        }
        return total;
    }


    public static void compare(List<Road> roads){
        Comparator<Road> comparator = (r1, r2) -> {
            int lengthCompare = Integer.compare(r1.getLength(),r2.getLength());
            if (lengthCompare != 0) {
                return lengthCompare;
            } else {
                return Integer.compare(r1.getId(), r2.getId());
            }
        };

        roads.sort(comparator);
    }


    /**
     * Writes the expected results to the output file.
     *
     * @param outputFile The output file path.
     * @param data The data content containing every result.
     * @param startPoint The starting point of the road.
     * @param endPoint The ending point of the road.
     */
    public static void writeOutput(String outputFile, DataContent data, String startPoint, String endPoint) {

        FileOutput.writeToFile(outputFile, "Fastest Route from " + startPoint + " to " +
                endPoint + " (" + calculateTotalLength(data.getFastestRoute()) + " KM):",true,true);
        for (Road road : data.getFastestRoute()) {
            FileOutput.writeToFile(outputFile,road.getPointA() + "\t" + road.getPointB() + "\t" + road.getLength() + "\t" + road.getId(),true,true);
        }

        FileOutput.writeToFile(outputFile,"Roads of Barely Connected Map is:",true,true);
        for (Road road : data.getBarelyConnectedMap()) {
            FileOutput.writeToFile(outputFile,road.getPointA() + "\t" + road.getPointB() + "\t" + road.getLength() + "\t" + road.getId(),true,true);
        }

        FileOutput.writeToFile(outputFile,"Fastest Route from " + startPoint + " to " + endPoint + " on Barely Connected Map" +
                " (" + calculateTotalLength(data.getFastestRouteOnBCP()) +" KM):",true,true);

        for (Road road : data.getFastestRouteOnBCP()) {
            FileOutput.writeToFile(outputFile,road.getPointA() + "\t" + road.getPointB() + "\t" + road.getLength() + "\t" + road.getId(),true,true);
        }
        FileOutput.writeToFile(outputFile,"Analysis:",true,true);
        FileOutput.writeToFile(outputFile, String.format("Ratio of Construction Material Usage Between Barely Connected and Original Map: %.2f", data.materialRatio),true,true);
        FileOutput.writeToFile(outputFile,String.format("Ratio of Fastest Route Between Barely Connected and Original Map: %.2f", data.routeRatio),true,false);
    }
}
