package fxlauncher.emasters;

import fxlauncher.FXManifest;
import fxlauncher.UIProvider;
import fxlauncher.emasters.components.LabelFactory;
import fxlauncher.emasters.components.PatchNotesBox;
import fxlauncher.emasters.components.PlayableTooltipBox;
import fxlauncher.emasters.location.I18N;
import fxlauncher.emasters.location.TAG;
import fxlauncher.emasters.utils.DownloadRate;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.text.DecimalFormat;

public class EmastersUIProvider implements UIProvider {

    /**
     * FIXED SIZES
     **/
    private static final double WIDTH = 700;
    private static final double HEIGHT = 414;
    private static final double PADDING_LR = 35;
    private static final double PADDING_TB = 25;

    /**
     * CSS CONSTANTS
     **/
    private static final String STYLESHEET = "/css/update.css";
    private static final String CSS_BG = "background";
    private static final String CSS_RIGHT_IMAGE = "right-image";
    private static final String CSS_LOGO_BOX = "logo-box";
    private static final String CSS_LABEL_DOWNLOAD = "label-download";

    private final DownloadRate rate = new DownloadRate();
    private final PatchNotesBox notesBox = new PatchNotesBox();

    private Pane root;
    private ProgressBar progressBar;
    private Label downloadLabel;
    private Label currentSizeLabel;
    private Label sizeOfLabel;
    private Label totalSizeLabel;
    private Label speedLabel;
    private Button launchButton;
    private PlayableTooltipBox estimateTooltip;
    private DecimalFormat df = new DecimalFormat("#.##");

    public Parent createLoader() {
        root = new Pane();
        root.getStylesheets().add(EmastersUIProvider.class.getResource(STYLESHEET).toExternalForm());
        root.getStyleClass().add(CSS_BG);
        root.setPrefSize(WIDTH, HEIGHT);

        final VBox main = new VBox();
        main.setPrefWidth(WIDTH);
        main.setPrefHeight(HEIGHT);
        main.setPadding(new Insets(PADDING_TB, PADDING_LR, PADDING_TB, PADDING_LR));

        final HBox hbox = new HBox();
        VBox.setVgrow(hbox, Priority.ALWAYS);
        hbox.setSpacing(15);

        final VBox mainLeft = new VBox();
        mainLeft.setPrefWidth(320);
        final VBox mainRight = new VBox();
        mainRight.setPrefWidth(295);
        mainRight.getStyleClass().add(CSS_RIGHT_IMAGE);

        final HBox logoBox = new HBox();
        logoBox.setPrefHeight(37);
        logoBox.setPrefWidth(235);
        logoBox.getStyleClass().add(CSS_LOGO_BOX);

        final VBox playBox = createPlay();

        launchButton = new Button(I18N.get(TAG.LAUNCH).toUpperCase());
        launchButton.setDisable(true);
        launchButton.setVisible(false);

        mainLeft.getChildren().addAll(logoBox, emptyPane(), notesBox, launchButton, emptyPane());
        hbox.getChildren().addAll(mainLeft, mainRight);

        main.getChildren().addAll(hbox, playBox);

        root.getChildren().add(main);
        root.getChildren().add(estimateTooltip);
        return root;
    }

    private Pane emptyPane() {
        Pane empty = new Pane();
        VBox.setVgrow(empty, Priority.ALWAYS);
        HBox.setHgrow(empty, Priority.ALWAYS);

        return empty;
    }

    public void init(Stage stage) {

    }


    private VBox createPlay() {
        final VBox playBox = new VBox();

        downloadLabel = LabelFactory.boldLabel(TAG.UPDATING, CSS_LABEL_DOWNLOAD);
        currentSizeLabel = LabelFactory.boldLabel();
        sizeOfLabel = LabelFactory.amberLabel();
        totalSizeLabel = LabelFactory.boldLabel();
        speedLabel = LabelFactory.amberLabel();

        progressBar = new ProgressBar();
        progressBar.setPrefWidth(WIDTH - (PADDING_LR * 2));

        final HBox emptyBox = new HBox();
        HBox.setHgrow(emptyBox, Priority.ALWAYS);

        final HBox estimateBox = new HBox();
        estimateBox.getChildren().addAll(emptyBox, currentSizeLabel, sizeOfLabel, totalSizeLabel, speedLabel);

        final StackPane progressPane = new StackPane();
        progressPane.setAlignment(Pos.CENTER_LEFT);
        progressPane.getChildren().addAll(progressBar, downloadLabel);

        estimateTooltip = new PlayableTooltipBox();
        playBox.getChildren().addAll(progressPane, estimateBox);

        return playBox;
    }

    public Parent createUpdater(FXManifest manifest) {
        downloadLabel.setText(I18N.get(TAG.DOWNLOADING, ""));
        sizeOfLabel.setText(I18N.get(TAG.OF));
        launchButton.setVisible(false);
        notesBox.updateNotes(manifest.whatsNewPage);
        return root;
    }

    @Override
    public void updateProgress(double progress, String currentArchive, double currentMB, double totalMB) {
        if (progress >= 100) {
            launchButton.setDisable(false);
        }

        currentSizeLabel.setText(df.format(currentMB) + " MB ");
        totalSizeLabel.setText(" " + df.format(totalMB) + " MB");

        downloadLabel.setText(I18N.get(TAG.DOWNLOADING, currentArchive));
        progressBar.setProgress(progress);
        estimateTooltip.setTime(null, progressBar, progress);

        boolean needUpdate = rate.calculateDownloadSpeed(currentMB, totalMB, currentArchive);
        if (needUpdate) {
            speedLabel.setText(" (" + df.format(rate.mbPerSecond) + "MB/s)");
            estimateTooltip.setTime(rate.secondsToComplete, progressBar, progress);
        }
    }
}
