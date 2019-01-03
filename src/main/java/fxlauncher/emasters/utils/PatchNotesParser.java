package fxlauncher.emasters.utils;

import fxlauncher.emasters.location.I18N;

import java.util.Locale;

/**
 * Parsers for the app patch notes.
 **/
public class PatchNotesParser {

    /**
     * Parse the manifest patch notes to the user location.
     *
     * @param patchNotes the entire patch notes with all locations
     * @return the notes parsed
     **/
    public static String parse(String patchNotes) {
        String patchNotesBR = parseNotes(patchNotes, "<br>");
        String patchNotesEN = parseNotes(patchNotes, "<en>");

        Locale locale = I18N.getLocale();
        if (locale.equals(Locale.forLanguageTag("pt-BR"))) {
            return patchNotesBR;
        }
        return patchNotesEN;
    }

    /**
     * Parse notes based on a specific language tag;
     *
     * @param original the original patch notes
     * @param tag      the language tag
     * @return the parsed notes
     **/
    private static String parseNotes(String original, String tag) {
        int startIndex = original.indexOf(tag) + tag.length();
        int endIndex = original.lastIndexOf(tag);
        return original.substring(startIndex, endIndex);
    }
}
