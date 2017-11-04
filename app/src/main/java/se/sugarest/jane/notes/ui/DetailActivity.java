package se.sugarest.jane.notes.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import se.sugarest.jane.notes.R;
import se.sugarest.jane.notes.api.NotesClient;
import se.sugarest.jane.notes.data.type.Note;

import static se.sugarest.jane.notes.util.Constant.ADD_A_NOTE;
import static se.sugarest.jane.notes.util.Constant.EDIT_A_NOTE;
import static se.sugarest.jane.notes.util.Constant.INTENT_CURRENT_NOTE_SIZE_TITLE;
import static se.sugarest.jane.notes.util.Constant.INTENT_NOTE_ID_TITLE;
import static se.sugarest.jane.notes.util.Constant.INTENT_NOTE_POSITION_TITLE;
import static se.sugarest.jane.notes.util.Constant.NOTES_BASE_URL;

/**
 * This class handles the requests towards adding a new note, or editing an existing note(e.g.,
 * update or deletion).
 * <p>
 * Created by jane on 17-10-18.
 */
public class DetailActivity extends AppCompatActivity {
    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    private int mAddAndEditRecordNumber;
    private TextView mIdTextView;
    private EditText mTitleEditText;
    private EditText mDescriptionEditText;
    private int mNotePositionId;
    private int mNoteSaveId;
    private Toast mToast;
    private Note mCurrentNote;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mIdTextView = (TextView) findViewById(R.id.tv_note_id);
        mTitleEditText = (EditText) findViewById(R.id.et_note_title);
        mDescriptionEditText = (EditText) findViewById(R.id.et_note_description);

        // By checking the content of the intent that started this activity, to figure out either
        // this is an AddNoteActivity or an EditNoteActivity
        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity == null) {
            Log.e(LOG_TAG, "Missing intent.");
            return;
        }
        if (!intentThatStartedThisActivity.hasExtra(INTENT_CURRENT_NOTE_SIZE_TITLE) &&
                !intentThatStartedThisActivity.hasExtra(INTENT_NOTE_ID_TITLE) &&
                !intentThatStartedThisActivity.hasExtra(INTENT_NOTE_POSITION_TITLE)) {
            Log.e(LOG_TAG, "Missing intent extra information.");
            return;
        }
        if (intentThatStartedThisActivity.hasExtra(INTENT_CURRENT_NOTE_SIZE_TITLE)) {
            // It is an AddNoteActivity
            setTitle(getString(R.string.set_detail_activity_title_add_a_note));
            mAddAndEditRecordNumber = ADD_A_NOTE;
            String currentNotesSize = getIntent().getExtras().getString(INTENT_CURRENT_NOTE_SIZE_TITLE);
            mNotePositionId = Integer.parseInt(currentNotesSize) + 1;
            mIdTextView.setText(String.valueOf(mNotePositionId));
        }
        if (intentThatStartedThisActivity.hasExtra(INTENT_NOTE_ID_TITLE)
                && intentThatStartedThisActivity.hasExtra(INTENT_NOTE_POSITION_TITLE)) {
            // It is an EditNoteActivity
            setTitle(getString(R.string.set_detail_activity_title_edit_a_note));
            mAddAndEditRecordNumber = EDIT_A_NOTE;
            mNotePositionId = getIntent().getExtras().getInt(INTENT_NOTE_POSITION_TITLE) + 1;
            mIdTextView.setText(String.valueOf(mNotePositionId));
            mNoteSaveId = getIntent().getExtras().getInt(INTENT_NOTE_ID_TITLE);
            sendNetworkRequestGetOneNote(mNoteSaveId);
        }
    }

    // Use External Library Retrofit to GET ONE note data, according to note id.
    // Reference: https://github.com/square/retrofit
    private void sendNetworkRequestGetOneNote(int id) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(NOTES_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        NotesClient client = retrofit.create(NotesClient.class);
        Call<Note> call = client.getOneNoteById(id);
        call.enqueue(new Callback<Note>() {
            @Override
            public void onResponse(Call<Note> call, Response<Note> response) {
                Log.i(LOG_TAG, "GET ONE response success: Complete url to request is: "
                        + response.raw().request().url().toString()
                        + "\nresponse.code() == " + response.code());
                mCurrentNote = response.body();
                mTitleEditText.setText(mCurrentNote.getTitle());
                mDescriptionEditText.setText(mCurrentNote.getDescription());
            }

            @Override
            public void onFailure(Call<Note> call, Throwable t) {
                Log.i(LOG_TAG, "GET ONE response failure.");
                if (mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(DetailActivity.this, getString(R.string.toast_retrofit_get_on_failure), Toast.LENGTH_SHORT);
                mToast.setGravity(Gravity.BOTTOM, 0, 0);
                mToast.show();
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem deleteAction = menu.findItem(R.id.action_delete);
        if (mAddAndEditRecordNumber == ADD_A_NOTE) {
            // Not showing Delete item in an AddNoteActivity
            deleteAction.setVisible(false);
        } else {
            // User can only delete a note in an EditNoteActivity
            deleteAction.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                if (mAddAndEditRecordNumber == ADD_A_NOTE) {
                    // Add a note in an AddNoteActivity
                    createAndPostNewNote();
                } else {
                    // Edit a note in an EditNoteActivity
                    editAndPutCurrentNote();
                }
                return true;
            case R.id.action_delete:
                // Delete a note in an EditNoteActivity
                deleteCurrentNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteCurrentNote() {
        sendNetworkRequestDelete(mNoteSaveId);
    }

    // Use External Library Retrofit to DELETE existing note data, according to note id.
    // Reference: https://github.com/square/retrofit
    private void sendNetworkRequestDelete(int id) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(NOTES_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        NotesClient client = retrofit.create(NotesClient.class);
        Call<Void> call = client.deleteNote(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.i(LOG_TAG, "DELETE response success: Complete url to request is: "
                        + response.raw().request().url().toString()
                        + "\nresponse.code() == " + response.code());
                finish();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.i(LOG_TAG, "DELETE response failure.");
                if (mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(DetailActivity.this, getString(R.string.toast_retrofit_delete_on_failure), Toast.LENGTH_SHORT);
                mToast.setGravity(Gravity.BOTTOM, 0, 0);
                mToast.show();
            }
        });
    }

    private void editAndPutCurrentNote() {
        String noteTitleString = mTitleEditText.getText().toString();
        String noteDescriptionString = mDescriptionEditText.getText().toString();
        // Sanity check, title and description cannot be empty.
        if (noteTitleString.isEmpty() || noteDescriptionString.isEmpty()) {
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(this, getString(R.string.toast_update_note_title_description_cannot_be_empty), Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.BOTTOM, 0, 0);
            mToast.show();
        } else {
            Note newNote = new Note(noteTitleString, noteDescriptionString);
            sendNetworkRequestPut(mNoteSaveId, newNote);
        }
    }

    // Use External Library Retrofit to PUT existing note data, according to note id.
    // Reference: https://github.com/square/retrofit
    private void sendNetworkRequestPut(int id, Note note) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(NOTES_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        NotesClient client = retrofit.create(NotesClient.class);
        Call<Note> call = client.updateNote(id, note);
        call.enqueue(new Callback<Note>() {
            @Override
            public void onResponse(Call<Note> call, Response<Note> response) {
                Log.i(LOG_TAG, "PUT response success: Complete url to request is: "
                        + response.raw().request().url().toString()
                        + "\nresponse.body().toString == " + response.body().toString());
                finish();
            }

            @Override
            public void onFailure(Call<Note> call, Throwable t) {
                Log.i(LOG_TAG, "PUT response failure.");
                if (mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(DetailActivity.this, getString(R.string.toast_retrofit_post_put_on_failure), Toast.LENGTH_SHORT);
                mToast.setGravity(Gravity.BOTTOM, 0, 0);
                mToast.show();
            }
        });
    }

    private void createAndPostNewNote() {
        String noteTitleString = mTitleEditText.getText().toString();
        String noteDescriptionString = mDescriptionEditText.getText().toString();
        // Sanity check, title and description cannot be empty.
        if (noteTitleString.isEmpty() || noteDescriptionString.isEmpty()) {
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(this, getString(R.string.toast_create_note_title_description_cannot_be_empty), Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.BOTTOM, 0, 0);
            mToast.show();
        } else {
            Note newNote = new Note(noteTitleString, noteDescriptionString);
            sendNetworkRequestPost(newNote);
        }
    }

    // Use External Library Retrofit to POST new note data.
    // Reference: https://github.com/square/retrofit
    private void sendNetworkRequestPost(Note note) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(NOTES_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        NotesClient client = retrofit.create(NotesClient.class);
        Call<Note> call = client.createNote(note);
        call.enqueue(new Callback<Note>() {
            @Override
            public void onResponse(Call<Note> call, Response<Note> response) {
                Log.i(LOG_TAG, "POST response success: Complete url to request is: "
                        + response.raw().request().url().toString()
                        + "\nresponse.body().toString == " + response.body().toString());
                finish();
            }

            @Override
            public void onFailure(Call<Note> call, Throwable t) {
                Log.i(LOG_TAG, "POST response failure.");
                if (mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(DetailActivity.this, getString(R.string.toast_retrofit_post_put_on_failure), Toast.LENGTH_SHORT);
                mToast.setGravity(Gravity.BOTTOM, 0, 0);
                mToast.show();
            }
        });
    }
}
