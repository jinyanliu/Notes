package se.sugarest.jane.notes;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import java.net.URL;
import java.util.Collections;
import java.util.List;

import se.sugarest.jane.notes.ui.MainActivity;
import se.sugarest.jane.notes.utilities.NetworkUtils;
import se.sugarest.jane.notes.utilities.NoteJsonUtils;

/**
 * Created by jane on 17-10-18.
 */

public class GetNotesTask extends AsyncTask<Void, Void, List<Note>> {

    private final static String TAG = GetNotesTask.class.getSimpleName();

    final static String NOTES_BASE_URL = "https://timesheet-1172.appspot.com/cb7b02a7/notes";

    MainActivity mainActivity;

    public GetNotesTask(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Note> doInBackground(Void... params) {

        URL noteRequestUrl = NetworkUtils.buildUrl(NOTES_BASE_URL);

        try {
            String jsonNoteResponse = NetworkUtils.getResponseFromHttpUrl(noteRequestUrl);
            List<Note> jsonNoteData = NoteJsonUtils.extractResultsFromJson(jsonNoteResponse);
            Log.i(TAG, "There are: " + jsonNoteData.size() + " notes.");
            return jsonNoteData;
        } catch (Exception e) {
            Log.e(TAG, "Wrong with Request URL: " + noteRequestUrl);
            return Collections.emptyList();
        }
    }

    @Override
    protected void onPostExecute(List<Note> notes) {
        if (notes != null && notes.size() > 0) {
            this.mainActivity.getmNoteAdapter().setNotesData(notes);
        } else {
            this.mainActivity.getmEmptyTextView().setVisibility(View.VISIBLE);
        }
        this.mainActivity.setmNotesSize(notes.size());
    }
}
