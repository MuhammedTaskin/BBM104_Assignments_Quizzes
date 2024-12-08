import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Drill {

    ImageView drill; // The drill's visual representation


    /**
     * Adds drill to starting screen at starting position
     *
     * @param root the pane to add to the scene
     */
    public void addDrill(Pane root) {
        // Add the drill
        drill = new ImageView("file:src/assets/drill/drill_01.png");
        drill.setX(700);
        drill.setY(0);
        root.getChildren().add(drill);
    }

    // Getter and setters for Drill coordinates
    public double getX() {
        return drill.getX();
    }
    public void setX(double x) {
        drill.setX(x);
    }
    public double getY() {
        return drill.getY();
    }
    public void setY(double y) {
        drill.setY(y);
    }
}
