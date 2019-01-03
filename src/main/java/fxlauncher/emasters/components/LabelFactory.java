package fxlauncher.emasters.components;

import fxlauncher.emasters.location.I18N;
import fxlauncher.emasters.location.TAG;
import javafx.scene.control.Label;

/**
 * Factory to create the most used labels in the update.
 **/
public class LabelFactory {

    private static final String CSS_LABEL_BOLD = "label-bold";
    private static final String CSS_LABEL_BOLD_AMBER = "label-bold-amber";

    /**
     * Creates a bold and amber label with no text.
     *
     * @return the new Label
     **/
    public static Label amberLabel() {
        return boldLabel(null, CSS_LABEL_BOLD_AMBER);
    }

    /**
     * Creates a bold and amber label with a TAG text.
     *
     * @param tag the text TAG
     * @return the new Label
     **/
    public static Label amberLabel(TAG tag) {
        return boldLabel(tag, CSS_LABEL_BOLD_AMBER);
    }

    /**
     * Creates a bold label with no text.
     *
     * @return the new Label
     **/
    public static Label boldLabel() {
        return boldLabel(null);
    }

    /**
     * Creates a bold label with a TAG text and an optional CSS class.
     *
     * @param tag the TAG text
     * @param css optional additional CSS class
     * @return the new Label
     **/
    public static Label boldLabel(TAG tag, String... css) {
        String text = "";
        if (tag != null) {
            text = I18N.get(tag);
        }
        Label label = new Label(text);
        label.getStyleClass().add(CSS_LABEL_BOLD);
        label.getStyleClass().addAll(css);
        return label;
    }
}
