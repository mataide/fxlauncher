package fxlauncher.emasters.components;

import fxlauncher.emasters.location.I18N;
import fxlauncher.emasters.location.TAG;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Class that represents a tooltip box where the playable estimate time will be located.
 **/
public class PlayableTooltipBox extends VBox {

    private static final String CSS_LABEL_BOLD = "label-bold";
    private static final String CSS_LABEL_BOLD_AMBER = "label-bold-amber";
    private static final String CSS_REGION_TOOLTIP = "region-tooltip";
    private static final String CSS_REGION_ARROW = "region-arrow";

    private final Label timeLabel;
    private final Region region;
    private long lastTime = 0;
    private double lastTimeRemain = 0;

    public PlayableTooltipBox() {
        //by default is invisible
        this.setVisible(false);

        this.setMinHeight(VBox.USE_PREF_SIZE);
        this.setMinWidth(VBox.USE_PREF_SIZE);
        this.setMaxHeight(VBox.USE_PREF_SIZE);
        this.setMaxWidth(VBox.USE_PREF_SIZE);

        //creates the playable label
        final Label playableLabel = new Label(I18N.get(TAG.PLAYABLE_IN));
        playableLabel.getStyleClass().add(CSS_LABEL_BOLD);
        playableLabel.getStyleClass().add(CSS_LABEL_BOLD_AMBER);

        //creates the estimate time label
        this.timeLabel = new Label("...");
        this.timeLabel.getStyleClass().add(CSS_LABEL_BOLD);

        //creates the box to put the labels
        final HBox box = new HBox(playableLabel, timeLabel);
        box.getStyleClass().add(CSS_REGION_TOOLTIP);
        box.setSpacing(4);

        //creates the arrow region of the tooltip
        this.region = new Region();
        this.region.getStyleClass().add(CSS_REGION_ARROW);
        this.region.setPrefSize(10, 6);
        this.region.setTranslateX(20);

        this.region.setMinHeight(VBox.USE_PREF_SIZE);
        this.region.setMinWidth(VBox.USE_PREF_SIZE);
        this.region.setMaxHeight(VBox.USE_PREF_SIZE);
        this.region.setMaxWidth(VBox.USE_PREF_SIZE);

        this.getChildren().addAll(region, box);
    }

    /**
     * Updates the time in the playable tooltip box.
     *
     * @param timeRemain the number of seconds remaining
     * @param bar        the ProgressBar where the tooltip is bounded
     * @param progress   the progress number between 0 and 1
     **/
    public void setTime(Double timeRemain, ProgressBar bar, Double progress) {
        //only shows for the first time
        if (!this.isVisible()) {
            this.setVisible(true);
        }

        calculateTooltipLocation(bar, progress);
        calculateTimeRemain(timeRemain);
    }

    /**
     * Calculates the estimate time remaining.
     *
     * @param timeRemain the number of seconds remaining
     **/
    private void calculateTimeRemain(Double timeRemain) {
        long now = System.currentTimeMillis();
        //if time remain is null, do not show the estimate time
        if (timeRemain == null && isLastTimeValid(now)) {
            timeRemain = lastTimeRemain - 1;
        } else if (timeRemain == null) {
            return;
        }

        lastTime = now;
        lastTimeRemain = timeRemain;

        String time = formatTime(timeRemain);
        this.timeLabel.setText(time);
    }

    /**
     * Checks if is valid to decrease the time by one second
     *
     * @param now the current time millis now
     **/
    private boolean isLastTimeValid(long now) {
        return lastTime != 0
                && lastTimeRemain >= 1
                && now > lastTime + 1000;
    }

    /**
     * Calculate the tooltip location with the estimate remain time.
     *
     * @param bar      the ProgressBar where the tooltip is bounded
     * @param progress the progress number between 0 and 1
     **/
    private void calculateTooltipLocation(ProgressBar bar, double progress) {
        Bounds boundsInScene = bar.localToScene(bar.getBoundsInLocal());
        double anchorX = boundsInScene.getMinX() - 25;
        double anchorY = boundsInScene.getMinY() + 20;
        if (progress > 0.5) {
            if (this.getAlignment().equals(Pos.TOP_LEFT)) {
                this.setAlignment(Pos.TOP_RIGHT);
                region.setTranslateX(-20);
            }

            anchorX = boundsInScene.getMinX() - this.getWidth() + 25;
        }
        this.relocate(anchorX + (bar.getWidth() * progress), anchorY);
        this.requestLayout();
    }

    /**
     * Format time based on the seconds remaining, If the number is too big, change the unit to minute, hour or day.
     *
     * @param timeRemain the number of seconds to format.
     * @return the formatted time
     **/
    private String formatTime(Double timeRemain) {
        TAG unit = TAG.SECONDS;
        if (timeRemain > 60) {
            timeRemain = timeRemain / 60;
            unit = TAG.MINUTES;
            if (timeRemain > 60) {
                timeRemain = timeRemain / 60;
                unit = TAG.HOURS;
                if (timeRemain > 24) {
                    timeRemain = timeRemain / 24;
                    unit = TAG.DAYS;
                }
            }
        }

        return timeRemain.intValue() + " " + I18N.get(unit);
    }
}
