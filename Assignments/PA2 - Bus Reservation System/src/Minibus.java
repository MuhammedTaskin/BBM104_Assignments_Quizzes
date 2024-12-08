import java.util.Arrays;

public class Minibus extends Bus{

    private String[] seats;


    /**
     *
     * @param id id of the voyage
     * @param departure departure of the voyage
     * @param destination destination of the voyage
     * @param rows row amount of minibus
     * @param price price of a seat
     * @param outputFile output file to be written
     * @throws IllegalArgumentException prevents from creating a class if it is not meant to be created
     */
    public Minibus(String id, String departure, String destination, String rows, String price, String outputFile) throws IllegalArgumentException {

        super(id, departure, destination, price, outputFile);

        checkPositiveInt(rows,"number of seat rows of a voyage");
        this.seats = new String[ Integer.parseInt(rows) * 2 ];
        Arrays.fill(seats, "*");

        String line = String.format ("Voyage %d was initialized as a minibus (2) voyage from %s to %s" +
                        " with %.2f TL priced %d regular seats. Note that minibus tickets are not refundable.",
                getId() ,getDeparture(),getDestination(),getPrice(), seats.length);

        FileOutput.writeToFile(outputFile,line,true,true);

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
     * @return
     */

    @Override
    public int refundTicket(int seat) {
        FileOutput.writeToFile(outputFile,"ERROR: Minibus tickets are not refundable!",true,true);
        return -1;
    }

    /**
     * Prints out the called voyage
     */
    @Override
    public void printVoyage() {
        FileOutput.writeToFile(outputFile, "Voyage " + getId(),true,true);
        FileOutput.writeToFile(outputFile, getDeparture() + "-" + getDestination(),true,true);
        for (int i = 0; i < seats.length; i++) {
            if (i % 2 == 0) {
                FileOutput.writeToFile(outputFile, (seats[i] + " " + seats[i+1]),true,true);
            }
        }
        FileOutput.writeToFile(outputFile, String.format("Revenue: %.2f", getRevenue()),true,true);


    }
    /**
     * Cancels the wanted voyage
     */
    @Override
    public void cancelVoyage() {
        revenue = 0;
    }

    @Override
    public String[] getSeats() {
        return seats;
    }
}
