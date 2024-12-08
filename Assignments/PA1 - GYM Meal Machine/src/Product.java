public class Product {
    /**
     * Product represents a meal that will be loaded into machine
     */

    private final String name;
    private byte productAmountInSlot = 1;
    private final byte price;
    private final float protein;
    private final float carb;
    private final float fat;
    private final short calorie;


    /**
     * Constructs and initializes a new Product
     * @param name      name of the meal
     * @param price     price of the meal
     * @param protein   protein value of the meal
     * @param carb      carbohydrate value of the meal
     * @param fat       fat value of the meal
     */
    public Product(String name, byte price, float protein, float carb, float fat) {

        this.name = name;
        this.price = price;
        this.protein = protein;
        this.carb = carb;
        this.fat = fat;
        this.calorie = (short) Math.round(4 * protein + 4 * carb  + 9 * fat);     // Calculates the calorie amount
    }

    //Getters and setters
    public String getName() {return name;}

    public byte getPrice() {return price;}

    public float getProtein() {return protein;}

    public float getCarb() {return carb;}

    public float getFat() {return fat;}

    public byte getAmountInSlot() {return productAmountInSlot;}

    public short getCalorie() {return calorie;}

    public void increaseProductAmountInSlot() {this.productAmountInSlot ++;}

    public void decreaseProductAmountInSlot() {this.productAmountInSlot --;}
}
