import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Manages animations and sound effects for the game
 */
public class Animations {

    protected Timeline animation;

    /**
     * Plays a sound at the start of the game
     */
    protected void openingSound() {

        String soundPath = "src/assets/extras/MT/sound_effect_1.mp3";
        playSound(soundPath);
    }


    /**
     * Plays a random soil sound when drilling a soil
     */
    protected void soilSound() {
        Random random = new Random();
        int number = random.nextInt(3) + 1;
        String path = "src/assets/extras/MT/mining"+ number + ".mp3";
        playSound(path);
    }


    /**
     * Plays a sound when drilling a mineral
     */
    protected void mineralSound() {

        String path = "src/assets/extras/MT/mining_mineral.mp3";
        playSound(path);
    }


    /**
     * Plays the sound whose path is specified
     * @param path Path of the sound file
     */
    private void playSound(String path) {

        Media sound = new Media(new File(path).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }


    /**
     * Manages the animation when the game is over
     * @param root the pane where the video will be displayed
     * @param successful Indicates whether the player won the game or not
     * @return MediaPlayer the end game video
     */
    protected MediaPlayer gameOverAnimation(Pane root, boolean successful) {

        root.getChildren().clear();

        String videoPath = successful ? "src/assets/extras/MT/winner_cat.mp4" : "src/assets/extras/MT/loser_cat.mp4";

        File videoFile = new File(videoPath);

        // URI'yi doğru bir şekilde oluştur
        URI uri = videoFile.toURI();
        String mediaUrl = uri.toString();

        // Upload the video file
        Media media = new Media(mediaUrl);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        System.out.println(mediaUrl);

        // Display the video
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setFitWidth(800);
        mediaView.setFitHeight(800);
        mediaPlayer.getCycleDuration();

        root.getChildren().add(mediaView);
        mediaPlayer.play();

        return mediaPlayer;
    }


    /**
     * Creates an animation based on the drill direction
     *
     * @param drill represents the drill ImageView.
     * @param direction the direction of the movement.
     * @return Timeline drill animation.
     */
    protected Timeline drillAnimation(ImageView drill, String direction) {
        List<String> animPaths = new ArrayList<>();
        animation = new Timeline();
        switch (direction) {
            case "up":
                for (int i = 3; i < 8; i++) {
                    animPaths.add("file:src/assets/drill/drill_2" + i + ".png");
                }
                break;

            case "down":
                for (int i = 0; i < 5; i++) {
                    animPaths.add("file:src/assets/drill/drill_4" + i + ".png");
                }
                break;

            case "left":
                for (int i = 2; i < 9; i++) {
                    animPaths.add("file:src/assets/drill/drill_0" + i + ".png");
                }
                break;

            case "right":
                for (int i = 5; i < 10; i++) {
                    animPaths.add("file:src/assets/drill/drill_5" + i + ".png");
                }
                animPaths.add("file:src/assets/drill/drill_60.png");
                break;
        }

        double frameDuration = 0.4 / animPaths.size(); // Determines the duration of each frame

        for (int i = 0; i < animPaths.size(); i++) {
            int finalI = i;
            animation.getKeyFrames().add(
                    new KeyFrame(Duration.seconds(i * frameDuration), e -> drill.setImage(new Image(animPaths.get(finalI))))
            );
        }
        animation.setCycleCount(1);
        return animation;
    }
}
