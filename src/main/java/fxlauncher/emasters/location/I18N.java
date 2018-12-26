package fxlauncher.emasters.location;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

/**
 * I18N utility class..
 */
public final class I18N {

    /**
     * the current selected Locale.
     */
    private static final ObjectProperty<Locale> LOCALE;

    static {
        LOCALE = new SimpleObjectProperty<>(getDefaultLocale());
        LOCALE.addListener((observable, oldValue, newValue) -> Locale.setDefault(newValue));
    }

    /**
     * get the supported Locales.
     *
     * @return List of Locale objects.
     */
    public static List<Locale> getSupportedLocales() {
        List<Locale> localeList = new ArrayList<>();

        for (Locale entry : Locale.getAvailableLocales()) {
            try {
                ResourceBundle.getBundle("bundles.langBundle", entry);
                localeList.add(entry);
            } catch (MissingResourceException ex) {
                // ...
            }
        }

        return localeList;
    }

    /**
     * get the default locale. This is the systems default if contained in the
     * supported locales, english otherwise.
     *
     * @return
     */
    public static Locale getDefaultLocale() {
        Locale sysDefault = Locale.getDefault();
        return getSupportedLocales().contains(sysDefault) ? sysDefault : Locale.ENGLISH;
    }

    public static Locale getLocale() {
        return LOCALE.get();
    }

    public static void setLocale(Locale locale) {
        localeProperty().set(locale);
        Locale.setDefault(locale);
    }

    public static ObjectProperty<Locale> localeProperty() {
        return LOCALE;
    }

    /**
     * gets the string with the given key from the resource bundle for the
     * current locale and uses it as first argument to MessageFormat.format,
     * passing in the optional args and returning the result.
     *
     * @param key  message key
     * @param args optional arguments for the message
     * @return localized formatted string
     */
    private static String get(final String key, final Object... args) {
        ResourceBundle bundle = ResourceBundle.getBundle("bundles.langBundle", getLocale());
        return MessageFormat.format(bundle.getString(key), args);
    }

    /**
     * gets the string with the given key from the resource bundle for the
     * current locale and uses it as first argument to MessageFormat.format,
     * passing in the optional args and returning the result.
     *
     * @param key  message key
     * @param args optional arguments for the message
     * @return localized formatted string
     */
    public static String get(final TAG key, final Object... args) {
        ResourceBundle bundle = ResourceBundle.getBundle("bundles.langBundle", getLocale());
        return MessageFormat.format(bundle.getString(key.name().toLowerCase()), args);
    }

    /**
     * creates a String binding to a localized String for the given enum
     * tag
     *
     * @param key  key
     * @param args optional arguments
     * @return String binding
     */
    public static StringBinding getString(final TAG key, Object... args) {
        return Bindings.createStringBinding(() -> get(key.name().toLowerCase(), args), LOCALE);
    }

    /**
     * creates a String binding to a localized String for the given enum
     * tag
     *
     * @param key  key
     * @param args optional arguments
     * @return String binding
     */
    public static StringBinding getStringUpperCase(final TAG key, Object... args) {
        return Bindings.createStringBinding(() -> get(key.name().toLowerCase(), args).toUpperCase(), LOCALE);
    }

    /**
     * creates a String Binding to a localized String that is computed by
     * calling the given func
     *
     * @param func function called on every change
     * @return StringBinding
     */
    public static StringBinding getString(Callable<String> func) {
        return Bindings.createStringBinding(func, LOCALE);
    }

    /**
     * creates a bound Label whose value is computed on language change.
     *
     * @param func the function to compute the value
     * @return Label
     */
    public static Label labelForValue(Callable<String> func) {
        Label label = new Label();
        label.textProperty().bind(getString(func));
        return label;
    }

    /**
     * creates a bound Button for the given resourcebundle key
     *
     * @param key  ResourceBundle key
     * @param args optional arguments for the message
     * @return Button
     */
    public static Button buttonForKey(final TAG key, final Object... args) {
        Button button = new Button();
        button.textProperty().bind(getString(key, args));
        return button;
    }
}

