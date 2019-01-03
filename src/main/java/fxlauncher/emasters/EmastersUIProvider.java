package fxlauncher.emasters;

import fxlauncher.FXManifest;
import fxlauncher.UIProvider;
import fxlauncher.emasters.components.PatchNotesBox;
import fxlauncher.emasters.components.PlayBox;
import fxlauncher.emasters.components.PlayableTooltipBox;
import fxlauncher.emasters.location.I18N;
import fxlauncher.emasters.location.TAG;
import fxlauncher.emasters.utils.DownloadRate;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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

    private final DownloadRate rate = new DownloadRate();
    private final PatchNotesBox notesBox = new PatchNotesBox();
    private final PlayBox playBox = new PlayBox(WIDTH - (PADDING_LR * 2));
    private final PlayableTooltipBox estimateTooltip = new PlayableTooltipBox();

    private Pane root;
    private Button launchButton;

    public Parent createLoader() {
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
        root = new Pane();
        root.getStylesheets().add(EmastersUIProvider.class.getResource(STYLESHEET).toExternalForm());
        root.getStyleClass().add(CSS_BG);
        root.setPrefSize(WIDTH, HEIGHT);
    }

    public Parent createUpdater(FXManifest manifest) {
        playBox.changeToDownloading();
        launchButton.setVisible(false);
        notesBox.updateNotes(manifest.whatsNewPage);
        return root;
    }

    @Override
    public void updateProgress(double progress, String currentArchive, double currentMB, double totalMB) {
        if (progress >= 100) {
            launchButton.setDisable(false);
        }

        playBox.updateProgress(currentMB, totalMB, currentArchive, progress);
        estimateTooltip.setTime(null, playBox.getBar(), progress);

        boolean needUpdate = rate.calculateDownloadSpeed(currentMB, totalMB, currentArchive);
        if (needUpdate) {
            playBox.updateSpeedRate(rate.mbPerSecond);
            estimateTooltip.setTime(rate.secondsToComplete, playBox.getBar(), progress);
        }
    }
}
