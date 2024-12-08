public class Machine {
    /**
     * Inserts, if available, given products in the machine and writes the machine out.
     */
    private String outputFile;
    private Product[] inventory;
    private final String[] contentProduct;
    private boolean newProductNotAllowed;

    /**
     * Class constructor and initializer for machine
     *
     * @param outputFile        the file which output is going to be written
     * @param contentProduct    all the products and their information
     */
    public Machine(String outputFile, String[] contentProduct) {
        this.outputFile = outputFile;
        this.contentProduct = contentProduct;
        this.inventory = new Product[24];
    }


    /**
     * Puts products in proper slots
     * Splits the product line and creates new products
     * If the machine becomes full, it returns -1 and stops loading meal
     */
    public int fill() {

        int i = 0;
        for (String line : contentProduct) {

            String[] productInfo = line.split("\t");
            String mealName = productInfo[0];

            byte price = Byte.parseByte(productInfo[1]);

            String[] nutritionalValues = productInfo[2].split(" "); // Protein, carb and fat values are seperated
            float[] nVals = new float[nutritionalValues.length];
            for (byte j = 0; j < nutritionalValues.length; j++) {
                nVals[j] = Float.parseFloat(nutritionalValues[j]);
            }

            Product newProduct = new Product(mealName, price, nVals[0],nVals[1],nVals[2]);

            if (shouldAllocateToNewSlot(mealName)) {
                inventory[i] = newProduct;
                i++;
            }

            if (newProductNotAllowed) {

                String line1 = ("INFO: There is no available place to put " + mealName + "\n");
                String line2 = ("INFO: The machine is full!");
                FileOutput.writeToFile(outputFile, line1 + line2, true,true);
                return -1;
            }
        }
        return 0;
    }


    /**
     * Decides whether to open a new slot for meal
     * If there is already a slot for the meal, it increases the product amount in slot
     *
     * @param mealName name of the product to be compared to existing slots
     * @return  true if the meal should allocate to new slot, false if there is an existing slot for it or if the machine is full
     */
    public boolean shouldAllocateToNewSlot(String mealName) {

        boolean isMachineFull = true;
        boolean isMeaInMachine = false;

        for (Product product : inventory) {

            if (product != null && product.getName().equals(mealName) && product.getAmountInSlot() < 10) {

                product.increaseProductAmountInSlot();
                return false;
            }
            if (product != null && mealName.equals(product.getName())) { isMeaInMachine = true;}

            if (product == null) { isMachineFull = false;}
        }

        if(isMachineFull && !isMeaInMachine) {
            newProductNotAllowed = true; // If new meal is tried to be loaded into machine, it gives feedback to stop
            return false;

        } else if (isMachineFull && isMeaInMachine ) {
            String line = ("INFO: There is no available place to put " + mealName);
            FileOutput.writeToFile(outputFile, line, true,true);
            return false;
        }
        return true;
    }


    /**
     * Writes out the machine in a suitable form
     */
    public void writeOutMachine() {

        FileOutput.writeToFile(outputFile, "-----Gym Meal Machine-----", true, true);
        for (int i = 0; i < 24; i++) {

            if (inventory[i] != null && inventory[i].getAmountInSlot() > 0) {

                String productDisplay = (inventory[i].getName() + "(" + inventory[i].getCalorie() +
                        ", " + inventory[i].getAmountInSlot() +")___" );

                FileOutput.writeToFile(outputFile, productDisplay, true, false);

            } else {
                FileOutput.writeToFile(outputFile, "___(0, 0)___", true, false);

            }
            if (i % 4 == 3){ FileOutput.writeToFile(outputFile, "", true, true);}
        }
        FileOutput.writeToFile(outputFile, "----------", true,true);
    }

    // Getter for inventory
    public Product[] getInventory() {
        return inventory;
    }
}
