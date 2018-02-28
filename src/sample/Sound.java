package sample;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;

public class Sound {
    Media sound;
    MediaPlayer player;

    Sound(String path, int duration) {
        sound = new Media(new File(path).toURI().toString());
        player = new MediaPlayer(sound);
        player.setStopTime(Duration.seconds(duration));
        player.play();
    }

    void stop() {
        player.stop();
    }
}
