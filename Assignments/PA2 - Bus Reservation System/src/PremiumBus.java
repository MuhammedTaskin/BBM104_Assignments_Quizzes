import java.util.Arrays;

public class PremiumBus extends Bus {

    private String[] seats;
    private int refundCut;
    private double premiumPrice;

    /**
     *
     * @param id id of the voyage
     * @param departure departure of the voyage
     * @param destination destination of the voyage
     * @param rows row amount of minibus
     * @param price price of a standard seat
     * @param refundCut refund cut percentage of refunded seat
     * @param premiumFee price increase percentage of premium seats
     * @param outputFile output file to be written
     * @throws IllegalArgumentException prevents from creating a class if it is not meant to be created
     */

    public PremiumBus(String  id, String departure, String destination, String rows, String price,
                      String refundCut, String premiumFee, String outputFile) throws IllegalArgumentException {

        super(id, departure, destination,price, outputFile);

        checkPositiveInt(rows,"number of seat rows of a voyage");
        this.seats = new String[Integer.parseInt(rows) * 3];
        Arrays.fill(seats, "*");

        checkPositiveInt(premiumFee, "premiumFee");
        premiumPrice = getPrice() * (100.0 + Double.parseDouble(premiumFee)) / 100.0;

        try {
            this.refundCut = Integer.parseInt(refundCut);
            if (this.refundCut < 0 || this.refundCut > 100) {
                throw new NumberFormatException();
            }
        }catch (NumberFormatException e) {
            String line =String.format("ERROR: %s is not an integer that is in range of [0, 100], refund cut must be an integer that is in range of [0, 100]!",refundCut);
            FileOutput.writeToFile(outputFile,line,true,true);
            throw new IllegalArgumentException();
        }

        String line = String.format("Voyage %d was initialized as a premium (1+2) voyage from %s to %s" +
                        " with %.2f TL priced %d regular seats and %.2f TL priced %d" +
                        " premium seats. Note that refunds will be %d%% less than the paid amount.",
                getId(), getDeparture(), getDestination(), getPrice(), seats.length * 2/3,
                getPremiumPrice(), seats.length/3, getRefundCut());
        FileOutput.writeToFile(outputFile,line,true,true);
    }

    /**
     * sells the wanted seat
     * @param seat seat in the voyage
     */
    @Override
    public void sellTicket(int seat) {

        if (seat % 3 == 1) {
            increaseRevenue(getPremiumPrice());
        }else {
            increaseRevenue(getPrice());
        }

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
        if (seat % 3 == 1) {
            decreaseRevenue(getPremiumPrice() * (100- refundCut) / 100);
        }else {
            decreaseRevenue(getPrice() * (100- refundCut) / 100 );
        }
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
            if (i % 3 == 0) {
                FileOutput.writeToFile(outputFile, (seats[i] + " | " + seats[i+1] + " " + seats[i+2]),true,true);
            }
        }
        FileOutput.writeToFile(outputFile, String.format("Revenue: %.2f", getRevenue()),true,true);

    }

    /**
     * Cancels the wanted voyage
     */
    public void cancelVoyage() {

        for (int i = 0; i < seats.length; i++) {
            if (seats[i].equals("X") && i % 3 == 0) {
                decreaseRevenue(getPremiumPrice());
            } else if (seats[i].equals("X")) {
                decreaseRevenue(getPrice());
            }
        }
    }

    public int getRefundCut() {return refundCut;}
    public double getPremiumPrice() {return premiumPrice;}

    @Override
    public String[] getSeats() {return seats;}

}
