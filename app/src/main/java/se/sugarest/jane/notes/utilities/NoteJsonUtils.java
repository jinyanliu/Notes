package se.sugarest.jane.notes.utilities;

/**
 * Created by jane on 17-10-18.
 */

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import se.sugarest.jane.notes.data.Note;

/**
 * Utility functions to handle Notes JSON data.
 */
public class NoteJsonUtils {

    private final static String TAG = NoteJsonUtils.class.getSimpleName();

    /**
     * @return a list of {@link Note} objects that has been built up from parsing the given
     * JSON response.
     */
    public static List<Note> extractResultsFromJson(String notesJSON) {

        if (TextUtils.isEmpty(notesJSON)) {
            return null;
        }

        // Create an empty ArrayList that can start adding notes to
        List<Note> notes = new ArrayList<>();

        /**
         * Try to parse the JSON response string. If there's a problem with the way the JSON
         * is formatted, a JSONException object will be thrown.
         * Catch the exception so the app doesn't crash, and print the error message to the logs.
         */
        try {
            // Create a JSONArray from the JSON response string
            JSONArray baseJsonArray = new JSONArray(notesJSON);

            // For each note in the baseJsonArray, create a Note object
            for (int i = 0; i < baseJsonArray.length(); i++) {

                // Get a single note at position i within the list of notes
                JSONObject currentNote = baseJsonArray.getJSONObject(i);

                // Get the "id" key value String and store it in note_id variable.
                int note_id = currentNote.getInt("id");

                // Get the "title" key value String and store it in note_title variable.
                String note_title = currentNote.getString("title");

                // Get the "description" key value String and store it in note_description variable.
                String note_description = currentNote.getString("description");

                // Create a new Note object with the note_id, note_title, note_description from the
                // JSON response.
                Note note = new Note(note_id, note_title, note_description);

                // Add the new Note to the list of notes.
                notes.add(note);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Failed to pass Notes JSON: " + notesJSON);
        }
        return notes;
    }
}
