import javafx.scene.image.Image;
import java.util.Arrays;
import java.util.List;

/**
 * Stores all the underground asset images used on the screen
 */
public class UndergroundAssets {

    // Stores different obstacle images
    private final List<Image> obstacles =Arrays.asList(new Image("file:src/assets/underground/obstacle_01.png"),
            new Image("file:src/assets/underground/obstacle_02.png"),
            new Image("file:src/assets/underground/obstacle_03.png"));

    // Stores different soil images
    private final List<Image> soils = Arrays.asList(new Image("file:src/assets/underground/soil_01.png"),
            new Image("file:src/assets/underground/soil_02.png"),
            new Image("file:src/assets/underground/soil_03.png"),
            new Image("file:src/assets/underground/soil_04.png"),
            new Image("file:src/assets/underground/soil_05.png"));

    // Stores different grass images
    private final List<Image> grasses = Arrays.asList(new Image("file:src/assets/underground/top_01.png"),
            new Image("file:src/assets/underground/top_02.png"));

    // Stores different lava images
    private final List<Image> lavas = Arrays.asList(new Image("file:src/assets/underground/lava_01.png"),
            new Image("file:src/assets/underground/lava_02.png"),
            new Image("file:src/assets/underground/lava_03.png"));


    // Stores single images for minerals
    private final Image goldium = new Image("file:src/assets/underground/valuable_goldium.png");
    private final Image platinum = new Image("file:src/assets/underground/valuable_platinum.png");
    private final Image einsteinium = new Image("file:src/assets/underground/valuable_einsteinium.png");
    private final Image emerald = new Image("file:src/assets/underground/valuable_emerald.png");


    // Getter methods for each field
    public Image getGoldium() {
        return goldium;
    }

    public Image getPlatinum() {
        return platinum;
    }

    public Image getEinsteinium() {
        return einsteinium;
    }

    public Image getEmerald() {
        return emerald;
    }

    public List<Image> getObstacles() {
        return obstacles;
    }

    public List<Image> getSoils() {
        return soils;
    }

    public List<Image> getGrasses() {
        return grasses;
    }

    public List<Image> getLavas() {
        return lavas;
    }

}