import java.util.Arrays;

public class StandardBus extends Bus{

    private String[] seats;
    private int refundCut;

    /**
     *
     * @param id id of the voyage
     * @param departure departure of the voyage
     * @param destination destination of the voyage
     * @param rows row amount of minibus
     * @param price price of a standard seat
     * @param refundCut refund cut percentage of refunded seat
     * @param outputFile output file to be written
     * @throws IllegalArgumentException prevents from creating a class if it is not meant to be created
     */
    public StandardBus(String id, String departure, String destination, String rows , String price,
                       String refundCut, String outputFile) throws IllegalArgumentException {

        super(id, departure, destination, price, outputFile);

        checkPositiveInt(rows, "number of seat rows of a voyage");
        this.seats = new String[Integer.parseInt(rows) * 4];
        Arrays.fill(seats, "*");


        try {
            this.refundCut = Integer.parseInt(refundCut);
            if (this.refundCut < 0 || this.refundCut > 100) {
                throw new NumberFormatException();
            }
        }catch (NumberFormatException e) {
            String line = String.format("ERROR: %s is not an integer that is in range of [0, 100], refund cut must be an integer that is in range of [0, 100]!",refundCut);
            FileOutput.writeToFile(outputFile, line,true,true);
            throw new IllegalArgumentException();
        }


        String line = String.format("Voyage %d was initialized as a standard (2+2) voyage from %s to %s" +
                        " with %.2f TL priced %d regular seats. Note that refunds will be %d%% less than the paid amount.",
                getId(), getDeparture(), getDestination(), getPrice(), seats.length, getRefundCut());
        FileOutput.writeToFile(outputFile, line,true,true);

    }

    /**
     * sells the wanted seat
     * @param seat seat in the voyage
     */

    @Override
    public void sellTicket(int seat) {
        increaseRevenue(getPrice());
        seats[seat-1] = "X";
    }

    /**
     * Refunds the wanted seat
     * @param seat seat in the voyage
     * @return -1 if minibus because it has to stop from the calling line, 0 else
     */
    @Override
    public int refundTicket(int seat) {
        seats[seat-1] = "*";
        decreaseRevenue(getPrice() * (100- refundCut) / 100);
        return 0;
    }


    /**
     * Prints out the called voyage
     */
    @Override
    public void printVoyage() {
        FileOutput.writeToFile(outputFile, ("Voyage " + getId()),true,true);
        FileOutput.writeToFile(outputFile, (getDeparture() + "-" + getDestination()),true,true);
        for (int i = 0; i < seats.length; i++) {
            if (i % 4 == 0) {
                FileOutput.writeToFile(outputFile, seats[i] + " " + seats[i+1] +
                        " | " + seats[i+2] + " " + seats[i+3], true,true);
            }
        }
        FileOutput.writeToFile(outputFile, String.format("Revenue: %.2f", getRevenue()),true,true);

    }

    /**
     * Cancels the wanted voyage
     */
    @Override
    public void cancelVoyage() {
        for (String i : seats) {
            if (i.equals("X")) {
                decreaseRevenue(getPrice());
            }
        }
    }

    public int getRefundCut() {return refundCut;}

    @Override
    public String[] getSeats() {return seats;}

}
