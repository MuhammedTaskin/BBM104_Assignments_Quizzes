public class Purchase {
    /**
     * This class manages all the purchase transactions
     */

    private String outputFile;
    private final Product[] inventory;
    private final String[] contentPurchase;

    /**
     * Class constructor
     * @param outputFile        the file which output is going to be written
     * @param inventory         the machine which products are loaded into
     * @param contentPurchase   the array which has purchase transactions
     */
    public Purchase(String outputFile, Product[] inventory, String[] contentPurchase) {
        this.outputFile = outputFile;
        this.inventory = inventory; 
        this.contentPurchase = contentPurchase;
    }


    /**
     * This method manages the purchase process
     * According to transaction criterion, it calls proper methods to check and complete transactions
     * @return -1 if invalid money is inserted, else 0
     */
    public int processPurchase() {
        for (String line : contentPurchase) {
            String[] purchaseInfo = line.split("\t");

            FileOutput.writeToFile(outputFile,"INPUT: " + line,true,true);

            String[] strMoneyInserted = purchaseInfo[1].split(" ");
            short money = 0;
            for (String s : strMoneyInserted) {
                short moneyInserted = Short.parseShort(s);
                switch (s) {
                    case "1":
                    case "5":
                    case "10":
                    case "20":
                    case "50":
                    case "100":
                    case "200":
                        money += moneyInserted;
                        break;
                    default:
                        String line1 = ("INFO: You inserted an unknown money.");  //Invalid money info message
                        FileOutput.writeToFile(outputFile,line1,true,true);
                        break;
                }
            }

            String choice = purchaseInfo[2];
            short value = Short.parseShort(purchaseInfo[3]);

            switch(choice) {
                case "NUMBER":
                    numberChoicePurchase(money, value);
                    break;
                case "PROTEIN":
                    proteinChoicePurchase(money, value);
                    break;
                case "CARB":
                    carbChoicePurchase(money,value);
                    break;
                case "FAT":
                    fatChoicePurchase(money,value);
                    break;
                case "CALORIE":
                    calorieChoicePurchase(money,value);
                    break;
            }

        }
        return 0;
    }

    /**
     * Performs transaction if all criteria are met in the entered slot number.
     * According to criteria, it writes out info or purchase and return messages
     *
     * @param money total money which is inserted
     * @param value the slot number that has entered
     * @return -1 if criteria aren't met, 0 if transaction is successful
     */
    public int numberChoicePurchase(int money, int value) {

        if (value>23) {
            // In case there is no such a slot number
            String line1 = ("INFO: Number cannot be accepted. Please try again with another number." + "\n");
            String line2 = ("RETURN: Returning your change: "+ money +" TL");
            FileOutput.writeToFile(outputFile,line1 + line2,true,true);
            return -1;
        }

        Product product = inventory[value];

        if (product == null || product.getAmountInSlot() == 0) {
            // In case the slot is empty
            String line1 = ("INFO: This slot is empty, your money will be returned." + "\n");
            String line2 = ("RETURN: Returning your change: "+ money +" TL");
            FileOutput.writeToFile(outputFile,line1 + line2,true,true);
            return -1;
        }
        if (money < product.getPrice()) {
            // In case the money is insufficient
            String line1 = ("INFO: Insufficient money, try again with more money." + "\n");
            String line2 = ("RETURN: Returning your change: " + money + " TL");
            FileOutput.writeToFile(outputFile,line1 + line2,true,true);
            return -1;
        }

        money -= product.getPrice();
        product.decreaseProductAmountInSlot();

        String line1 = ("PURCHASE: You have bought one " + product.getName() + "\n");
        String line2 = ("RETURN: Returning your change: " + money + " TL");
        FileOutput.writeToFile(outputFile,line1 + line2,true,true);
        return 0;

    }

    /**
     * Performs transaction if all criteria are met according to entered protein value.
     * According to criteria, it writes out info or purchase and return messages
     *
     * @param money total money which is inserted
     * @param value the protein value that is required ([-5, +5] range is ok)
     * @return -1 if criteria aren't met, 0 if transaction is successful
     */
    public int proteinChoicePurchase(int money, int value) {

        boolean productFound = false;

        for (Product product: inventory) {

            if (product != null && (value >= product.getProtein() - 5) &&
                    (value <= product.getProtein() + 5) && (product.getAmountInSlot() > 0)){
                productFound = true;

                if (money >= product.getPrice()) {

                    money -= product.getPrice();
                    product.decreaseProductAmountInSlot();

                    String line1 = ("PURCHASE: You have bought one " + product.getName() + "\n");
                    String line2 = ("RETURN: Returning your change: " + money + " TL");
                    FileOutput.writeToFile(outputFile,line1 + line2,true,true);

                    return 0;
                }
            }
        }

        if (productFound) {
            // In case there is a product however the money is insufficient
            String line1 = ("INFO: Insufficient money, try again with more money." + "\n");
            String line2 = ("RETURN: Returning your change: " + money + " TL");
            FileOutput.writeToFile(outputFile,line1 + line2,true,true);

            return -1;

        } else {
            // In case there is no such a product
            String line1 = ("INFO: Product not found, your money will be returned." + "\n");
            String line2 = ("RETURN: Returning your change: " + money + " TL");
            FileOutput.writeToFile(outputFile,line1 + line2,true,true);

            return -1;
        }

    }


    /**
     * Performs transaction if all criteria are met according to entered carbohydrate value.
     * According to criteria, it writes out info or purchase and return messages
     *
     * @param money total money which is inserted
     * @param value the carbohydrate value that is required ([-5, +5] range is ok)
     * @return -1 if criteria aren't met, 0 if transaction is successful
     */
    public int carbChoicePurchase(int money, int value) {

        boolean productFound = false;

        for (Product product: inventory) {

            if (product != null && (value >= product.getCarb() - 5) &&
                    (value <= product.getCarb() + 5) && (product.getAmountInSlot() > 0)){
                productFound = true;

                if (money >= product.getPrice()) {

                    money -= product.getPrice();
                    product.decreaseProductAmountInSlot();

                    String line1 = ("PURCHASE: You have bought one " + product.getName() + "\n");
                    String line2 = ("RETURN: Returning your change: " + money + " TL");
                    FileOutput.writeToFile(outputFile,line1 + line2,true,true);

                    return 0;
                }
            }
        }

        if (productFound) {
            // In case there is a product however the money is insufficient
            String line1 = ("INFO: Insufficient money, try again with more money." + "\n");
            String line2 = ("RETURN: Returning your change: " + money + " TL");
            FileOutput.writeToFile(outputFile,line1 + line2,true,true);

            return -1;

        } else {
            //In case there is no such a product
            String line1 = ("INFO: Product not found, your money will be returned." + "\n");
            String line2 = ("RETURN: Returning your change: " + money + " TL");
            FileOutput.writeToFile(outputFile,line1 + line2,true,true);

            return -1;
        }

    }

    /**
     * Performs transaction if all criteria are met according to entered fat value.
     * According to criteria, it writes out info or purchase and return messages
     *
     * @param money total money which is inserted
     * @param value the fat value that is required ([-5, +5] range is ok)
     * @return -1 if criteria aren't met, 0 if transaction is successful
     */
    public int fatChoicePurchase(int money, int value) {

        boolean productFound = false;

        for (Product product: inventory) {

            if (product != null && (value >= product.getFat() - 5) &&
                    (value <= product.getFat() + 5) && (product.getAmountInSlot() > 0)){
                productFound = true;

                if (money >= product.getPrice()) {

                    money -= product.getPrice();
                    product.decreaseProductAmountInSlot();

                    String line1 = ("PURCHASE: You have bought one " + product.getName() + "\n");
                    String line2 = ("RETURN: Returning your change: " + money + " TL");
                    FileOutput.writeToFile(outputFile,line1 + line2,true,true);

                    return 0;
                }
            }
        }

        if (productFound) {
            // In case there is a product however the money is insufficient
            String line1 = ("INFO: Insufficient money, try again with more money." + "\n");
            String line2 = ("RETURN: Returning your change: " + money + " TL");
            FileOutput.writeToFile(outputFile,line1 + line2,true,true);

            return -1;

        } else {
            // In case there is no such a product
            String line1 = ("INFO: Product not found, your money will be returned." + "\n");
            String line2 = ("RETURN: Returning your change: " + money + " TL");
            FileOutput.writeToFile(outputFile,line1 + line2,true,true);

            return -1;
        }

    }


    /**
     * Performs transaction if all criteria are met according to entered calorie value.
     * According to criteria, it writes out info or purchase and return messages
     *
     * @param money total money which is inserted
     * @param value the calorie value that is required ([-5, +5] range is ok)
     * @return -1 if criteria aren't met, 0 if transaction is successful
     */
    public int calorieChoicePurchase(int money, int value) {

        boolean productFound = false;

        for (Product product: inventory) {

            if (product != null && (value >= product.getCalorie() - 5) &&
                    (value <= product.getCalorie() + 5) && (product.getAmountInSlot() > 0)){
                productFound = true;

                if (money >= product.getPrice()) {

                    money -= product.getPrice();
                    product.decreaseProductAmountInSlot();

                    String line1 = ("PURCHASE: You have bought one " + product.getName() + "\n");
                    String line2 = ("RETURN: Returning your change: " + money + " TL");
                    FileOutput.writeToFile(outputFile,line1 + line2,true,true);

                    return 0;
                }
            }
        }

        if (productFound) {
            // In case there is a product however the money is insufficient
            String line1 = ("INFO: Insufficient money, try again with more money." + "\n");
            String line2 = ("RETURN: Returning your change: " + money + " TL");
            FileOutput.writeToFile(outputFile,line1 + line2,true,true);

            return -1;

        } else {
            // In case there is no such a product
            String line1 = ("INFO: Product not found, your money will be returned." + "\n");
            String line2 = ("RETURN: Returning your change: " + money + " TL");
            FileOutput.writeToFile(outputFile,line1 + line2,true,true);

            return -1;
        }

    }

}