import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class BusBookingManager {
    private String[] commands;
    private ArrayList<Bus> voyages = new ArrayList<>();
    private String outputFile;

    /**
     *
     * @param commands Represents all the lines in the input file
     * @param outputFile Represents the output file to be written in
     */
    public BusBookingManager(String[] commands, String outputFile) {
        this.commands = commands;
        this.outputFile = outputFile;
        manage();
    }

    /**
     * Understands each main commands in a line and directs code to appropriate methods
     */
    public void manage(){

        if (commands.length == 0) {
            String line =
                    "Z Report\n" +
                    "----------------\n" +
                    "No Voyages Available!\n" +
                    "----------------";

            FileOutput.writeToFile(outputFile,line,true,true);
        }

        for (String line: commands) {
            FileOutput.writeToFile(outputFile,"COMMAND: " + line,true,true);

            String[] c = line.split("\t");
            String command = c[0];

            try {

                switch (command) {
                    case "INIT_VOYAGE":
                        initVoyage(c);
                        break;
                    case "SELL_TICKET":
                        sellTicket(c);
                        break;
                    case "REFUND_TICKET":
                        refundTicket(c);
                        break;
                    case "PRINT_VOYAGE":
                        printVoyage(c);
                        break;
                    case "CANCEL_VOYAGE":
                        cancelVoyage(c);
                        break;
                    case "Z_REPORT":
                        zReport(c);
                        break;
                    default:
                        FileOutput.writeToFile(outputFile,"ERROR: There is no command namely " + c[0] + "!",true,true);
                }

            }catch (Exception e) {
                FileOutput.writeToFile(outputFile,String.format("ERROR: Erroneous usage of \"%s\" command!", c[0]),true,true);
            }

        }
        // If there is no Z-Report at the end, prints it out
        if ( !(commands[commands.length -1].startsWith("Z_REPORT")) ) {
            String[] c = {"c"};
            try { zReport(c);} catch(Exception ignored) {}
        }

    }

    /**
     * If appropriate, adds new voyage to the ArrayList voyages
     * @param c is a line in the input file
     * @throws Exception if there is an erroneous usage of a command, writes a specific exception message
     */
    public void initVoyage(String[] c) throws Exception {


        try {
            if ( ! ((c.length == 7 && c[1].equals("Minibus")) ||
                    (c.length == 8 && c[1].equals("Standard")) ||
                    (c.length == 9 && c[1].equals("Premium"))) ) {
                throw new ArrayIndexOutOfBoundsException();
            }
            for (Bus voyage: voyages) {
                if ( String.valueOf(voyage.getId()).equals(c[2])){
                    FileOutput.writeToFile(outputFile, String.format("ERROR: There is already a voyage with ID of %s!",voyage.getId()),true,true);
                    return;
                }
            }

            switch (c[1]) {
                case "Minibus":
                    voyages.add(new Minibus(c[2], c[3], c[4], c[5], c[6], outputFile));
                    break;
                case "Standard":
                    voyages.add(new StandardBus(c[2], c[3], c[4], c[5], c[6], c[7], outputFile));
                    break;
                case "Premium":
                    voyages.add(new PremiumBus(c[2], c[3], c[4], c[5], c[6], c[7], c[8], outputFile));
                    break;
            }

        } catch (ArrayIndexOutOfBoundsException a) {
            throw new Exception();
        } catch (IllegalArgumentException ignored) {}
    }

    /**
     * Buys seats from a proper voyage and sets the seats of the voyage
     * @param c is a line in the input file
     * @throws Exception if there is an erroneous usage of a command, writes a specific exception message
     */

    public void sellTicket(String[] c ) throws Exception {

        boolean idMatch = false;
        int seat = 0;

        try {
            if (c.length != 3 ) {
                throw new Exception();
            }

            for (Bus voyage: voyages) {
                if ( String.valueOf(voyage.getId()).equals(c[1]) ){
                    idMatch = true;
                    String[] seatsToBuy = c[2].split("_");
                    double oldRevenue = voyage.getRevenue();

                    Set<String> allSeatNumbers = new HashSet<>(); // To control if the customer tries to buy a seat twice

                    for (String sSeat: seatsToBuy) {

                        seat = Integer.parseInt(sSeat);
                        if (voyage.getSeats()[seat - 1].equals("X")) {
                            FileOutput.writeToFile(outputFile, "ERROR: One or more seats already sold!",true,true);
                            return;
                        }

                        if (!allSeatNumbers.add(sSeat)) {
                            FileOutput.writeToFile(outputFile, "ERROR: A seat cannot be bought more than once in a command",true,true);
                            return;
                        }
                    }

                    for (String sSeat: seatsToBuy) {
                        seat = Integer.parseInt(sSeat);

                        voyage.sellTicket(seat);
                    }

                    String line = String.format ("Seat %s of the Voyage %d from " +
                                    "%s to %s was successfully sold for %.2f TL.",
                            String.join("-", seatsToBuy), voyage.getId(), voyage.getDeparture(),
                            voyage.getDestination(), voyage.getRevenue()-oldRevenue );
                    FileOutput.writeToFile(outputFile,line,true,true);

                    break;
                }
            }
            if (!idMatch) {
                FileOutput.writeToFile(outputFile, String.format("ERROR: There is no voyage with ID of %s!",c[1]),true,true);
            }

        } catch (ArrayIndexOutOfBoundsException e) {
            if (seat < 0) {
                FileOutput.writeToFile(outputFile, String.format("ERROR: %d is not a positive integer, seat number must be a positive integer!",seat),true,true);
            } else {
                FileOutput.writeToFile(outputFile, String.format("ERROR: There is no such a seat!"),true,true);
            }
        }
    }

    /**
     * Refunds seats from a proper voyage and sets the seats of the voyage
     * @param c is a line in the input file
     * @throws Exception if there is an erroneous usage of a command, writes a specific exception message
     */

    public void refundTicket(String[] c) throws Exception{
        boolean idMatch = false;
        int seat = 0;

        if (c.length > 3) {
            throw new Exception();
        }
        try {
            if (c.length != 3 ) {
                throw new IllegalArgumentException();
            }
            for (Bus voyage : voyages) {
                if (String.valueOf(voyage.getId()).equals(c[1])) {
                    idMatch = true;
                    String[] seatsToRefund = c[2].split("_");

                    double oldRevenue = voyage.getRevenue();

                    Set<String> allSeatNumbers = new HashSet<>(); // To control if the customer tries to buy a seat twice

                    for (String sSeat: seatsToRefund) {
                        seat = Integer.parseInt(sSeat);

                        if (voyage.getSeats()[seat - 1].equals("*")) {
                            FileOutput.writeToFile(outputFile, "ERROR: One or more seats are already empty!",true,true);
                            return;
                        }

                        if (!allSeatNumbers.add(sSeat)) {
                            FileOutput.writeToFile(outputFile, "ERROR: A seat cannot be refunded more than once in a command",true ,true);
                            return;
                        }
                    }
                    boolean isMinibus = false;

                    for (String sSeat: seatsToRefund) {

                        seat = Integer.parseInt(sSeat);
                        if (voyage.refundTicket(seat) == -1) {
                            isMinibus = true;
                            break;
                        }

                    }
                    if (isMinibus) {break;}

                    String line = String.format("Seat %s of the Voyage %d from " +
                                    "%s to %s was successfully refunded for %.2f TL.",
                            String.join("-", seatsToRefund), voyage.getId(), voyage.getDeparture(),
                            voyage.getDestination(), oldRevenue - voyage.getRevenue() );
                    FileOutput.writeToFile(outputFile,line,true,true);

                    break;
                }
            }
            if (!idMatch) {
                FileOutput.writeToFile(outputFile, String.format("ERROR: There is no voyage with ID of %s!",c[1]),true,true);
            }

        } catch (ArrayIndexOutOfBoundsException e) {
            if (seat < 0) {
                FileOutput.writeToFile(outputFile, String.format("ERROR: %d is not a positive integer, seat number must be a positive integer!",seat),true,true);
            } else {
                FileOutput.writeToFile(outputFile, "ERROR: There is no such a seat!",true,true);
            }
        }
    }

    /**
     * Writes out the desired voyage, if appropriate
     * @param c is a line in the input file
     * @throws Exception if there is an erroneous usage of a command, writes a specific exception message
     */
    public void printVoyage(String[] c) throws Exception {
        boolean idMatch = false;
        int searchID = 0;

        if (c.length != 2 ) {
            throw new Exception();
        }

        try {

            searchID = Integer.parseInt(c[1]);
            if (searchID < 0) {
                throw new NumberFormatException();
            }

            for (Bus voyage : voyages) {

                if (searchID == voyage.getId()) {
                    idMatch = true;
                    voyage.printVoyage();
                }
            }

            if (!idMatch) {
                FileOutput.writeToFile(outputFile,String.format("ERROR: There is no voyage with ID of %s!",c[1]),true,true);
            }

        }catch (NumberFormatException e) {
            FileOutput.writeToFile(outputFile,String.format("ERROR: %s is not a positive integer, ID of a voyage must be a positive integer!", c[1]),true,true);
        }

    }

    /**
     * Cancels the desired voyage, if appropriate
     * @param c is a line in the input file
     * @throws Exception if there is an erroneous usage of a command, writes a specific exception message
     */

    public void cancelVoyage(String[] c) throws Exception{
        boolean idMatch = false;
        int searchID = 0;


        Bus toRemove = null;
        if (c.length != 2 ) {
            throw new Exception();
        }

        try {
            searchID = Integer.parseInt(c[1]);
            if (searchID < 0) {
                throw new NumberFormatException();
            }

            for (Bus voyage: voyages) {
                if (searchID == voyage.getId()) {
                    idMatch = true;
                    FileOutput.writeToFile(outputFile, String.format("Voyage %d was successfully cancelled!\n" + "Voyage details can be found below:", searchID),true,true);
                    voyage.cancelVoyage();
                    voyage.printVoyage();
                    toRemove = voyage;
                    break;
                }
            }
            voyages.remove(toRemove);

            if (!idMatch) {
                FileOutput.writeToFile(outputFile, String.format("ERROR: There is no voyage with ID of %s!",c[1]),true,true);
            }

        } catch (NumberFormatException e) {
            FileOutput.writeToFile(outputFile, String.format("ERROR: %s is not a positive integer, ID of a voyage must be a positive integer!", c[1]),true,true);
        }

    }

    /**
     * Writes out all of the voyages available, if appropriate
     * @param c is a line in the input file
     * @throws Exception if there is an erroneous usage of a command, writes a specific exception message
     */
    public void zReport(String[] c) throws Exception {

        if (c.length != 1) {
            throw new Exception();
        }

        if (voyages.isEmpty()) {
            String line =
                    "Z Report\n" +
                            "----------------\n" +
                            "No Voyages Available!\n" +
                            "----------------";
            FileOutput.writeToFile(outputFile,line,true,true);

        }else {

            voyages.sort(Comparator.comparingInt(Bus::getId));
            FileOutput.writeToFile(outputFile, "Z Report:",true,true);
            FileOutput.writeToFile(outputFile, "----------------",true,true);
            for (Bus voyage : voyages) {
                voyage.printVoyage();
                FileOutput.writeToFile(outputFile, "----------------",true,true);
            }
        }

    }

}
