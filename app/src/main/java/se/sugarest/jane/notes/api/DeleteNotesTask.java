package se.sugarest.jane.notes.api;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;

import se.sugarest.jane.notes.ui.DetailActivity;
import se.sugarest.jane.notes.ui.MainActivity;
import se.sugarest.jane.notes.util.NetworkUtils;

import static se.sugarest.jane.notes.util.Constant.NOTES_BASE_URL;

/**
 * Created by jane on 17-10-18.
 */

public class DeleteNotesTask extends AsyncTask<Integer, Void, Integer> {

    private final static String TAG = DeleteNotesTask.class.getSimpleName();

    DetailActivity detailActivity;

    public DeleteNotesTask(DetailActivity detailActivity) {
        this.detailActivity = detailActivity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Integer... params) {

        // URL to call
        int noteId = params[0];
        String noteIdToAdd = "/" + String.valueOf(noteId);
        URL noteRequestUrl = NetworkUtils.buildUrl(NOTES_BASE_URL + noteIdToAdd);

        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) noteRequestUrl.openConnection();
            httpURLConnection.setRequestMethod("DELETE");

            Log.i("STATUS", String.valueOf(httpURLConnection.getResponseCode()));
            Log.i("MSG", httpURLConnection.getResponseMessage());

            httpURLConnection.disconnect();
        } catch (Exception e) {
            Log.e(TAG, "An exception occurred: " + e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        Intent intent = new Intent(this.detailActivity, MainActivity.class);
        this.detailActivity.startActivity(intent);
    }
}
