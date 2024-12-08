public class MapAnalyzer {

    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println("ERROR: This program works exactly with two command line arguments, the first one is the path" +
                    " to the input file whereas the second one is the path to the output file. Sample usage can be as follows:" +
                    " \"java8 MapAnalyzer input.txt output.txt\". Program is going to terminate!");
            return;
        }
        Navigator.main(args);
    }
}
