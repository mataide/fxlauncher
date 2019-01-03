package fxlauncher.emasters.components;

import fxlauncher.emasters.location.I18N;
import fxlauncher.emasters.location.TAG;
import fxlauncher.emasters.utils.PatchNotesParser;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * VBox representing the patch notes.
 **/
public class PatchNotesBox extends VBox {

    private static final String CSS_LABEL_PATCH = "label-patch";

    private Text textNotes;

    /**
     * Default Constructor
     **/
    public PatchNotesBox() {
        VBox.setVgrow(this, Priority.ALWAYS);

        final HBox titleBox = new HBox();
        titleBox.getChildren().addAll(createTitleBox(true), createTitleBox(false));

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefViewportWidth(317);
        scrollPane.setPrefViewportHeight(168);

        textNotes = new Text("-");
        textNotes.setFont(Font.font(10));
        textNotes.setFill(Color.WHITE);
        textNotes.setWrappingWidth(295);

        scrollPane.setContent(textNotes);

        this.getChildren().addAll(titleBox, scrollPane);
    }

    /**
     * Creates a title box.
     *
     * @param visible should the title be visible or not
     * @return the new VBox
     **/
    private VBox createTitleBox(boolean visible) {
        final VBox titleBox = new VBox();
        titleBox.setSpacing(5);
        titleBox.setAlignment(Pos.BASELINE_CENTER);
        HBox.setHgrow(titleBox, Priority.ALWAYS);

        final Label title = new Label(I18N.get(TAG.EMASTERS_PATCH_NOTES));
        title.getStyleClass().add(CSS_LABEL_PATCH);

        final Separator separator = new Separator();
        separator.setOrientation(Orientation.HORIZONTAL);
        titleBox.getChildren().addAll(title, separator);
        titleBox.setVisible(visible);

        return titleBox;
    }


    /**
     * Updates the patch notes.
     *
     * @param whatsNewPage the entire patch notes manifest
     **/
    public void updateNotes(String whatsNewPage) {
        textNotes.setText(PatchNotesParser.parse(whatsNewPage));
    }
}
