package se.sugarest.jane.notes.apiOperationTasks;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import se.sugarest.jane.notes.data.Note;
import se.sugarest.jane.notes.ui.DetailActivity;
import se.sugarest.jane.notes.ui.MainActivity;
import se.sugarest.jane.notes.utilities.NetworkUtils;

import static se.sugarest.jane.notes.Constant.NOTES_BASE_URL;

/**
 * Created by jane on 17-10-18.
 */

public class PutNotesTask extends AsyncTask<Note, Void, Note> {

    private final static String TAG = PutNotesTask.class.getSimpleName();

    DetailActivity detailActivity;

    public PutNotesTask(DetailActivity detailActivity) {
        this.detailActivity = detailActivity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Note doInBackground(Note... params) {

        // data to post
        Note noteToPut = params[0];

        // URL to call
        int noteId = noteToPut.getNoteId();
        String noteIdToAdd = "/" + String.valueOf(noteId);
        URL noteRequestUrl = NetworkUtils.buildUrl(NOTES_BASE_URL + noteIdToAdd);

        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) noteRequestUrl.openConnection();
            httpURLConnection.setRequestMethod("PUT");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("id", noteToPut.getNoteId());
            jsonParam.put("title", noteToPut.getNoteTitle());
            jsonParam.put("description", noteToPut.getNoteDescription());

            DataOutputStream os = new DataOutputStream(httpURLConnection.getOutputStream());
            os.writeBytes(jsonParam.toString());

            os.flush();
            os.close();

            Log.i("STATUS", String.valueOf(httpURLConnection.getResponseCode()));
            Log.i("MSG", httpURLConnection.getResponseMessage());

            httpURLConnection.disconnect();
        } catch (Exception e) {
            Log.e(TAG, "An exception occurred: " + e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Note note) {
        Intent intent = new Intent(this.detailActivity, MainActivity.class);
        this.detailActivity.startActivity(intent);
    }
}
