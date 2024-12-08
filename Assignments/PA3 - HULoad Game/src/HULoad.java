import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class HULoad extends Application {

    private final Animations animations = new Animations();   // Contains drill animations and other effects
    private final UndergroundAssets assets = new UndergroundAssets(); // Contains all the underground assets
    private final Drill drill = new Drill();
    private Pane root;
    private Scene scene;
    private final ImageView[][] undergroundBlocks = new ImageView[16][14]; // Includes underground assets based on their coordinates
    private Timeline fuelReduction; // Provides continuous fuel reduction
    private Timeline gravity;
    private boolean gravityEnabled = true; // Gravity is enabled temporarily on each move
    private double fuel = 10_000.000;
    private int haul;
    private int money;


    /**
     * The game is created and played here as long as keyboard inputs are available
     *
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set. The primary stage will be embedded in
     * the browser if the application was launched as an applet.
     * Applications may create other stages, if needed, but they will not be
     * primary stages and will not be embedded in the browser.
     */

    @Override
    public void start(Stage primaryStage) {

        root = new Pane();
        Rectangle sky = new Rectangle(0, 0, 800, 105);
        sky.setFill(Color.DEEPSKYBLUE);

        Rectangle underground = new Rectangle(0, 105, 800, 695);
        underground.setFill(Color.SADDLEBROWN);

        root.getChildren().addAll(sky, underground);
        scene = new Scene(root,800,800);

        animations.openingSound();

        // Create the initial screen and make sure there are soils more than any other element
        do {
            createStartingScreen();
        }while (!checkObjectDistribution());


        // Display the text of drill cases
        Text fuelText = new Text("fuel: " + fuel);
        Text haulText = new Text("haul: " + haul);
        Text moneyText = new Text("money: " + money);
        ArrayList<Text> texts = new ArrayList<>(Arrays.asList(fuelText,haulText,moneyText));
        for (int i = 0; i < texts.size(); i++) {
            texts.get(i).setX(10);
            texts.get(i).setY(25 + 15*i);
            texts.get(i).setFont(Font.font("Verdana",15));
            root.getChildren().add(texts.get(i));
        }

        startConsumingFuel(fuelText);
        startGravity();


        // Control the movement with arrow keys
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:
                    moveDrill("up", 0, -50);
                    break;
                case DOWN:
                    moveDrill("down", 0, 50);
                    break;
                case LEFT:
                    moveDrill("left", -50, 0);
                    break;
                case RIGHT:
                    moveDrill("right", 50, 0);
                    break;
            }
            adjustText(haulText,moneyText);
        });


        primaryStage.setScene(scene);
        primaryStage.setTitle("HU-Load");
        primaryStage.setResizable(false);
        primaryStage.show();

    }

    /**
     * Create the initial screen using assets
     */
    private void createStartingScreen(){

        for (int x = 0; x < 800; x += 50) {

            Random random = new Random();
            int randomInd = random.nextInt(2);
            // Add grasses to the top of underground
            ImageView imageView = new ImageView(assets.getGrasses().get(randomInd));
            imageView.setX(x);
            imageView.setY(100);
            root.getChildren().add(imageView);
            undergroundBlocks[x / 50][0] = imageView;

            //Fill underground with soil, obstacles, lava and minerals
            for (int y = 150; y < 800; y += 50) {

                if (x == 0 || x == 750 || y == 750) {
                    randomInd = random.nextInt(3);
                    ImageView imageView2 = new ImageView(assets.getObstacles().get(randomInd));
                    imageView2.setX(x);
                    imageView2.setY(y);
                    root.getChildren().add(imageView2);
                    undergroundBlocks[x / 50][(y - 100) / 50] = imageView2;

                } else {
                    undergroundBlocks[x / 50][(y - 100) / 50] = addRandomObject(x,y);
                }
            }
        }

        drill.addDrill(root);
    }

    /**
     * Checks if the initial screen has the required minimum soil blocks
     * The minimum number is 112 for this case
     * @return boolean if there is less than 112 soils on the initial screen, this method returns false
     */
    private boolean checkObjectDistribution(){
        Image image;
        int soil = 16;  // Initial number of grass

        // Checks the asset type of all blocks underground between boulders and grass
        for (int i = 1; i < 15; i++) {
            for (int j = 0; j < 13; j++) {

                image = undergroundBlocks[i][j].getImage();
                if (assets.getSoils().contains(image)) {
                    soil++;
                }
            }
        }
        return soil >= 112;
    }


    /**
     * Adds a random underground object to a specified x-y coordinate
     * The random distribution is represented as
     * %83 soil %7 lava %4 goldium %3 platinum %2 einsteinium %1 emerald
     *
     * @param x the x coordinate of where the object will be placed
     * @param y the y coordinate of where the object will be placed
     * @return ImageView the ImageView of the object which will be added
     */
    private ImageView addRandomObject(int x, int y) {

        Random random = new Random();
        int randomInd;

        ImageView imageView;
        int percentage = random.nextInt(100) + 1;

        // Determine the asset type which will be created based on the percentage
        if (percentage <= 83) {
            randomInd = random.nextInt(assets.getSoils().size());
            imageView = new ImageView(assets.getSoils().get(randomInd));
        } else if (percentage <= 90) {
            randomInd = random.nextInt(assets.getLavas().size());
            imageView = new ImageView(assets.getLavas().get(randomInd));
        } else if (percentage <= 94) {
            imageView = new ImageView(assets.getGoldium());
        } else if (percentage <= 97) {
            imageView = new ImageView(assets.getPlatinum());
        } else if (percentage <= 99) {
            imageView = new ImageView(assets.getEinsteinium());
        } else {
            imageView = new ImageView(assets.getEmerald());
        }

        // Set the coordinate of the asset and add to the root
        imageView.setX(x);
        imageView.setY(y);
        root.getChildren().add(imageView);
        return imageView;
    }


    /**
     * Moves the drill based on the direction and current position coordinate
     * If it is possible to move, updates the position
     * Decreases fuel and plays an animation on each call
     *
     * @param direction the 4 possible direction to move (up, down, left, right)
     * @param x the current x coordinate of the drill
     * @param y the current y coordinate of the drill
     */
    private void moveDrill(String direction, int x, int y) {

        fuel-= 100;

        double newX = drill.getX() + x;
        double newY = drill.getY() + y;

        // If the drill movement is allowed, update the coordinate and destroy the block if possible
        if (canDrillMove(direction, newX, newY)){
            drill.setX(newX);
            drill.setY(newY);
            destroyObject((int) newX, (int) newY);
        }

        // Temporarily disable gravity for the sake of better gameplay
        disableGravityTemporarily();

        if (animations.animation != null) {
            animations.animation.stop();
        }

        animations.drillAnimation(drill.drill,direction).playFromStart();
    }


    /**
     * Checks if the drill move to new location is possible
     * The drill can't get out of screen or drill obstacles and lavas
     * The drill can't drill upward
     *
     * @param direction the 4 possible direction to move (up, down, left, right)
     * @param newX the new x coordinate of the drill
     * @param newY the new y coordinate of the drill
     * @return boolean indicates if the drill move is possible
     */
    private boolean canDrillMove(String direction, double newX, double newY) {

        // Check if the new coordinates are outside the screen
        if (newX < 0 || newX > 750 || newY < 0 || newY > 750) {
            return  false;
        }

        try {
            // If every move is valid (sky or drilled block), throw an exception to return true
            ImageView object = undergroundBlocks[(int) newX / 50][(int) (newY - 100) / 50]; // Exception occurs in the sky
            if (object == null) throw new ArrayIndexOutOfBoundsException();

            // Obstruct the drill movement to upward
            if (direction.equals("up") ||
                    assets.getObstacles().contains(object.getImage())) {
                return false;
            }

            // If the drill tries to drill lava, player loses the game
            if (assets.getLavas().contains(object.getImage())) {
                gameOver(false);
                return false;
            }


        } catch (ArrayIndexOutOfBoundsException ignored) {}

        return true;
    }


    /**
     * Destroys the object in the new drill position if it is possible
     * It is possible if the block is not in the sky or drilled ground
     *
     * @param x the x coordinate of the drill.
     * @param y the y coordinate of the drill.
     */
    private void destroyObject(int x, int y) {
        try {
            // If the object is in the sky or on the already drilled ground, there is nothing to destroy
            ImageView object = undergroundBlocks[x / 50][(y - 100) / 50]; // Exception occurs in the sky
            if (object == null) throw new ArrayIndexOutOfBoundsException();

            adjustHaulAndMoney(object.getImage());

            root.getChildren().remove(object);
            undergroundBlocks[x / 50][(y - 100) / 50] = null; // Null indicates drilled blocks in the array

        } catch (ArrayIndexOutOfBoundsException ignored){}

    }


    /**
     * Adjusts the drill's weight and money according to mineral type collected
     * Updates the haul and money case
     * Play sound effects based on the asset that is destroyed
     *
     * @param assetType The image of the asset which is destroyed
     */
    private void adjustHaulAndMoney(Image assetType) {

        //No haul or money update if tha asset is soil or grass
        if (assets.getSoils().contains(assetType) ||
                assets.getGrasses().contains(assetType)) {
            animations.soilSound();
            return;
        }

        // Determine the type of the mineral and update haul and money
        if (assets.getGoldium().equals(assetType)) {
            haul += 20;
            money += 250;

        } else if (assets.getPlatinum().equals(assetType)) {
            haul += 30;
            money += 750;

        } else if (assets.getEinsteinium().equals(assetType)) {
            haul += 40;
            money += 2000;

        } else if (assets.getEmerald().equals(assetType)) {
            haul += 60;
            money += 5000;
        }

        animations.mineralSound();
    }


    /**
     * Update the haul and money text on the screen
     *
     * @param haulText The text which displays current haul
     * @param moneyText The text which displays current money.
     */
    private void adjustText(Text haulText,Text moneyText){
        haulText.setText("haul: " + haul);
        moneyText.setText("money: " + money);
    }


    /**
     * Initialize the fuel consumption and proceed it till the end of the game
     * Currently display the fuel amount on the screen
     * If the fuel reach 0 end the game
     *
     * @param fuelText The text which displays current fuel.
     */
    private void startConsumingFuel(Text fuelText) {

        // Decrease the fuel amount each 0.075 seconds according to haul variable
        fuelReduction = new Timeline(new KeyFrame(Duration.seconds(0.075), e -> {
            fuel -= 1.907 + haul/100.0;

            fuelText.setText("fuel: " + String.format("%.3f", fuel));

            if (fuel <= 0) {
                gameOver(true); // If the fuel has run out, call gameOver method
            }
        })
        );

        fuelReduction.setCycleCount(Timeline.INDEFINITE);
        fuelReduction.play();
    }


    /**
     * Initializes the continuous gravity which pulls drill downward if there is no block under the drill
     */
    private void startGravity() {

        gravity = new Timeline(new KeyFrame(Duration.seconds(0.4), e -> {

            if (gravityEnabled && canMoveDown((int) drill.getX(), (int) drill.getY() + 50)) {

                drill.setY(drill.getY() + 50);
            }
        }));
        gravity.setCycleCount(Timeline.INDEFINITE);
        gravity.play();
    }


    /**
     * Determines if the drill can move down if there is no block under it
     *
     * @param x the x-coordinate of the block under the drill.
     * @param y the y-coordinate of the block under the drill.
     * @return true if the drill can move down; false otherwise.
     */
    private boolean canMoveDown(int x, int y) {

        try{
            // If the movement is possible (sky or drilled ground), throws an exception
            ImageView object = undergroundBlocks[x / 50][(y - 100) / 50]; // Exception occurs in the sky
            if (object == null) throw new ArrayIndexOutOfBoundsException();

        } catch (ArrayIndexOutOfBoundsException e) { return true; }

        return false;
    }


    /**
     * Temporarily disables the gravity for the sake of better gameplay
     */
    private void disableGravityTemporarily() {

        gravityEnabled = false;
        new Timeline(new KeyFrame(Duration.seconds(0.4), e -> gravityEnabled = true)).play();
    }


    /**
     * Handle the game over logic and display the game over screen based on the scenarios
     * If fuel has run out when collected money is more than 7500, player wins the game
     * In the other scenarios player loses the game
     * @param successful Indicates if the player wins or loses
     */

    private void gameOver(boolean successful) {
        // Stop all the timelines in the game
        fuelReduction.stop();
        gravity.stop();

        if (animations.animation != null) {
            animations.animation.stop();
        }

        scene.setOnKeyPressed(null); // Do not allow any movement attempt

        root.getChildren().clear();

        // Play the game over video before
        MediaPlayer endGameCat = animations.gameOverAnimation(root, successful);


        Text gameOverText = new Text("GAME OVER");
        gameOverText.setFont(Font.font("Verdana", 50));
        gameOverText.setX(250); // Coordinate close to the center of the screen
        gameOverText.setY(350);

        if (successful) {

            Rectangle greenBG = new Rectangle(0,0,800,800);
            greenBG.setFill(Color.GREEN);

            Text moneyText = new Text("Collected Money : " + money );
            moneyText.setFont(Font.font("Verdana", 20));
            moneyText.setX(275);
            moneyText.setY(400);

            greenBG.setVisible(false);
            gameOverText.setVisible(false);
            moneyText.setVisible(false);

            //After the Game Over video ends, display "Game Over" screen over it
            endGameCat.setOnEndOfMedia(() -> {
                greenBG.setVisible(true);
                gameOverText.setVisible(true);
                moneyText.setVisible(true);
            });
            root.getChildren().addAll(greenBG, gameOverText, moneyText);

        } else {

            Rectangle redBG = new Rectangle(0,0,800,800);
            redBG.setFill(Color.DARKRED);

            redBG.setVisible(false);
            gameOverText.setVisible(false);

            //After the Game Over video ends, display "Game Over" screen over it
            endGameCat.setOnEndOfMedia(() -> {
                redBG.setVisible(true);
                gameOverText.setVisible(true);
            });

            root.getChildren().addAll(redBG, gameOverText);
        }
    }

    /**
     * Necessary method to run JavaFX applications
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}