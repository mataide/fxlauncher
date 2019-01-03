package fxlauncher.emasters.components;

import fxlauncher.emasters.location.I18N;
import fxlauncher.emasters.location.TAG;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.text.DecimalFormat;

public class PlayBox extends VBox {

    private static final String CSS_LABEL_DOWNLOAD = "label-download";
    private static final DecimalFormat DF = new DecimalFormat("#.##");

    private final ProgressBar progressBar;
    private final Label downloadLabel;
    private final Label currentSizeLabel;
    private final Label sizeOfLabel;
    private final Label totalSizeLabel;
    private final Label speedLabel;

    //
    public PlayBox(double width) {
        downloadLabel = LabelFactory.boldLabel(TAG.UPDATING, CSS_LABEL_DOWNLOAD);
        currentSizeLabel = LabelFactory.boldLabel();
        sizeOfLabel = LabelFactory.amberLabel();
        totalSizeLabel = LabelFactory.boldLabel();
        speedLabel = LabelFactory.amberLabel();

        progressBar = new ProgressBar();
        progressBar.setPrefWidth(width);

        final HBox emptyBox = new HBox();
        HBox.setHgrow(emptyBox, Priority.ALWAYS);

        final HBox estimateBox = new HBox();
        estimateBox.getChildren().addAll(emptyBox, currentSizeLabel, sizeOfLabel, totalSizeLabel, speedLabel);

        final StackPane progressPane = new StackPane();
        progressPane.setAlignment(Pos.CENTER_LEFT);
        progressPane.getChildren().addAll(progressBar, downloadLabel);

        this.getChildren().addAll(progressPane, estimateBox);
    }

    public void changeToDownloading() {
        downloadLabel.setText(I18N.get(TAG.DOWNLOADING, ""));
        sizeOfLabel.setText(I18N.get(TAG.OF));
    }

    public void updateProgress(double currentMB, double totalMB, String currentArchive, double progress) {
        currentSizeLabel.setText(DF.format(currentMB) + " MB ");
        totalSizeLabel.setText(" " + DF.format(totalMB) + " MB");

        downloadLabel.setText(I18N.get(TAG.DOWNLOADING, currentArchive));
        progressBar.setProgress(progress);
    }

    public void updateSpeedRate(double mbPerSecond) {
        speedLabel.setText(" (" + DF.format(mbPerSecond) + "MB/s)");
    }

    public ProgressBar getBar() {
        return progressBar;
    }
}
