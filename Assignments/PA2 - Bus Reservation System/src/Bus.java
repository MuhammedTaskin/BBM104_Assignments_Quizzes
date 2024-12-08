public abstract class Bus {

    private int id;
    private String departure;
    private String destination;
    private double price;
    protected double revenue;
    private String[] seats;
    protected String outputFile;


    /**
     *
     * @param id id of the voyage
     * @param departure departure of the voyage
     * @param destination destination of the voyage
     * @param price price of a seat
     * @param outputFile output file to be written
     * @throws IllegalArgumentException prevents from creating a class if it is not meant to be created
     */
    public Bus(String id, String departure, String destination, String price, String outputFile) throws IllegalArgumentException {
        this.outputFile = outputFile;
        checkPositiveInt(id, "ID of a voyage");
        checkPositiveDb(price, "price");
        this.id = Integer.parseInt(id);
        this.departure = departure;
        this.destination = destination;
        this.price = Double.parseDouble(price);
    }

    /**
     * sells the wanted seat
     * @param seat seat in the voyage
     */
    public abstract void sellTicket(int seat);

    /**
     * Refunds the wanted seat
     * @param seat seat in the voyage
     * @return -1 if minibus because it has to stop from the calling line, 0 else
     */
    public abstract int refundTicket(int seat);

    /**
     * Prints out the called voyage
     */
    public abstract void printVoyage();

    /**
     * Cancels the wanted voyage
     */
    public abstract void cancelVoyage();

    /**
     * Checks if it is appropriate parameter
     * @param d value of s
     * @param s any field that is to be written
     * @throws IllegalArgumentException to write out a spesific message
     */
    public void checkPositiveDb(String d, String s) throws IllegalArgumentException {
        try {
            if (Double.parseDouble(d) <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            FileOutput.writeToFile(outputFile, String.format("ERROR: %s is not a positive number, %s must be a positive number!",d,s),true,true);
            throw new IllegalArgumentException();
        }
    }

    /**
     * Checks if it is appropriate parameter
     * @param d value of s
     * @param s any field that is to be written
     * @throws IllegalArgumentException to write out a spesific message
     */

    public void checkPositiveInt(String d, String s) throws IllegalArgumentException {
        try {
            if (Integer.parseInt(d) <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            if (s.equals("premiumFee")) {
                if (Integer.parseInt(d) == 0){return;}
                FileOutput.writeToFile(outputFile, String.format("ERROR: %s is not a non-negative integer, premium fee must be a non-negative integer!",d),true,true);
            }else {
                FileOutput.writeToFile(outputFile, String.format("ERROR: %s is not a positive integer, %s must be a positive integer!", d, s),true,true);
            }
            throw new IllegalArgumentException();
        }
    }

    public int getId() {return id;}

    public String getDeparture() {return departure;}

    public String getDestination() {return destination;}

    public double getPrice() {return price;}

    public double getRevenue() {return revenue;}

    public String[] getSeats() {return seats;}

    public void increaseRevenue(double extraRevenue) {
        revenue += extraRevenue;
    }

    public void decreaseRevenue(double revenueLoss) {
        revenue -= revenueLoss;
    }
}
