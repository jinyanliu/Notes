package se.sugarest.jane.notes.util;

/**
 * Created by jane on 17-11-2.
 */
public class Constant {
    public static final String NOTES_BASE_URL = "https://timesheet-1172.appspot.com/";
    public static final String NOTES_API_END_POINT = "/cb7b02a7/notes";
    public static final String NOTES_API_SLASH = "/";

    // The token to indicate DetailActivity to display ADD_A_NOTE mode or EDIT_A_NOTE mode.
    public static final int ADD_A_NOTE = 100;
    public static final int EDIT_A_NOTE = 200;

    // The intent extra title passing between MainActivity and DetailActivity.
    public static final String INTENT_CURRENT_NOTE_SIZE_TITLE = "current_note_size";
    public static final String INTENT_NOTE_ID_TITLE = "note_id";
    public static final String INTENT_NOTE_POSITION_TITLE = "note_position";
}
