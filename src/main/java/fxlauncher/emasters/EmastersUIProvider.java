package fxlauncher.emasters;

import fxlauncher.FXManifest;
import fxlauncher.UIProvider;
import fxlauncher.emasters.location.I18N;
import fxlauncher.emasters.location.TAG;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.text.DecimalFormat;
import java.util.Locale;

public class EmastersUIProvider implements UIProvider {

    private final double WIDTH = 700;
    private final double HEIGHT = 540;
    private final double PADDING = 10;

    private StackPane root;
    private Text textNotes;
    private ProgressBar progressBar;
    private Label estimateLabel;
    private Label fileLabel;
    private Label downloadLabel;
    private Label sizeLabel;
    private Button launchButton;
    private long lastPacketTime = 0;
    private double lastPacketSize = 0;
    private DecimalFormat df = new DecimalFormat("#.##");

    public Parent createLoader() {
        root = new StackPane();
        root.getStylesheets().add("./css/update.css");
        root.getStyleClass().add("background");
        root.setPrefSize(WIDTH, HEIGHT);
        root.setPadding(new Insets(PADDING));

        final VBox main = new VBox();
        main.setAlignment(Pos.TOP_LEFT);
        main.setSpacing(20);

        final HBox logoBox = new HBox();
        logoBox.setPrefHeight(50);
        logoBox.getStyleClass().add("logo-box");

        final VBox notesBox = createPathNotes();
        final VBox playBox = createPlay();

        main.getChildren().addAll(logoBox, notesBox, playBox);
        root.getChildren().add(main);
        return root;
    }

    @Override
    public void updateProgress(double progress, String currentArchive, double currentMB, double totalMB) {
        if (progress >= 100) {
            launchButton.setDisable(false);
        }
        sizeLabel.setText(df.format(currentMB) + " MB | " + df.format(totalMB) + " MB");
        fileLabel.setText(currentArchive);
        progressBar.setProgress(progress);
        calculateDownloadSpeed(currentMB, totalMB);
    }

    private void calculateDownloadSpeed(double currentMB, double totalMB) {
        if (lastPacketTime == 0) {
            lastPacketTime = System.currentTimeMillis();
            lastPacketSize = currentMB;
            return;
        }

        long nowMillis = System.currentTimeMillis();
        long timeDiff = nowMillis - lastPacketTime;

        if (timeDiff < 1000) {
            return;
        }

        double sizeDiff = currentMB - lastPacketSize;

        if (sizeDiff == 0) {
            return;
        }

        double mbPerSecond = (sizeDiff / timeDiff) * 1000;

        double secondsToComplete = (totalMB - currentMB)/mbPerSecond;

        String timeRemain = formatTime(secondsToComplete);
        estimateLabel.setText(timeRemain);

        lastPacketTime = nowMillis;
        lastPacketSize = currentMB;
    }

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

    public void init(Stage stage) {

    }

    private VBox createPathNotes() {
        final VBox notesBox = new VBox();
        notesBox.setPadding(new Insets(0, 40, 0, 40));
        VBox.setVgrow(notesBox, Priority.ALWAYS);

        final HBox patchNotesBox = new HBox();
        patchNotesBox.getStyleClass().add("patch-notes-box");
        final Label patchNotes = new Label(I18N.get(TAG.EMASTERS_PATCH_NOTES));
        patchNotes.getStyleClass().add("label-patch");
        patchNotesBox.getChildren().add(patchNotes);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefViewportWidth(WIDTH - 100);
        scrollPane.setPrefViewportHeight(250);
        textNotes = new Text("-");
        textNotes.setFill(Color.WHITE);
        textNotes.setWrappingWidth(WIDTH - 140);
        scrollPane.setContent(textNotes);
        notesBox.getChildren().addAll(patchNotesBox, scrollPane);

        return notesBox;
    }

    private VBox createPlay() {
        final VBox playBox = new VBox();
        playBox.setPrefHeight(120);
        playBox.setPadding(new Insets(0, 40, 0, 40));
        playBox.setSpacing(10);

        launchButton = new Button(I18N.get(TAG.LAUNCH));
        launchButton.setDisable(true);
        launchButton.setVisible(false);

        final HBox downloadBox = new HBox();
        downloadBox.setSpacing(5);

        downloadLabel = new Label(I18N.get(TAG.UPDATING));
        downloadLabel.getStyleClass().add("label-bold");

        final HBox emptyBox = new HBox();
        HBox.setHgrow(emptyBox, Priority.ALWAYS);

        sizeLabel = new Label("");
        sizeLabel.getStyleClass().add("label-bold");

        downloadBox.getChildren().addAll(downloadLabel, emptyBox, sizeLabel);

        progressBar = new ProgressBar();
        progressBar.setPrefWidth(WIDTH - 100);

        final HBox estimateBox = new HBox();
        estimateBox.setSpacing(5);

        fileLabel = new Label("");
        fileLabel.getStyleClass().add("label-bold");

        final HBox emptyBox2 = new HBox();
        HBox.setHgrow(emptyBox2, Priority.ALWAYS);

        estimateLabel = new Label("");
        estimateLabel.getStyleClass().add("label-bold");

        estimateBox.getChildren().addAll(fileLabel, emptyBox2, estimateLabel);

        playBox.getChildren().addAll(launchButton, downloadBox, progressBar, estimateBox);

        return playBox;
    }

    public Parent createUpdater(FXManifest manifest) {
        downloadLabel.setText(I18N.get(TAG.DOWNLOADING));
        launchButton.setVisible(true);
        Locale locale = I18N.getLocale();
        if (locale.equals(Locale.forLanguageTag("pt-BR"))) {
            textNotes.setText(manifest.patchNotesBR);
        } else {
            textNotes.setText(manifest.patchNotesEN);
        }

        return root;
    }
}
