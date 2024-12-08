public class BookingSystem {

    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println("ERROR: This program works exactly with two command line arguments, the first one is the path" +
                    " to the input file whereas the second one is the path to the output file. Sample usage can be as follows:" +
                    " \"java8 BookingSystem input.txt output.txt\". Program is going to terminate!");
            return;
        }

        try {
            FileOutput.writeToFile(args[1],"",false,false );
        } catch (Exception e) {
            System.out.println("ERROR: This program cannot write to the \"<OUTPUT_FILE_PATH>\", please check the " +
                    "permissions to write that directory. Program is going to terminate!");
            return;
        }

        String[] commands = FileInput.readFile(args[0],true,true);
        if (commands == null) {
            System.out.println("ERROR: This program cannot read from the \"<INPUT_FILE_PATH>\", either this program " +
                    "does not have read permission to read that file or file does not exist. Program is going to terminate!");
            return;
        }


        BusBookingManager manager = new BusBookingManager(commands, args[1]);

        String[] file = FileInput.readFile(args[1],true,true);
        FileOutput.writeToFile(args[1],"",false,false );


        for (int i = 0; i < file.length-1; i++) {
            FileOutput.writeToFile(args[1],file[i],true,true);
        }
        FileOutput.writeToFile(args[1],file[file.length-1],true,false);

    }

}
