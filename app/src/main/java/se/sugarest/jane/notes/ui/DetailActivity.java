package se.sugarest.jane.notes.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import se.sugarest.jane.notes.apiOperationTasks.DeleteNotesTask;
import se.sugarest.jane.notes.apiOperationTasks.PutNotesTask;
import se.sugarest.jane.notes.data.Note;
import se.sugarest.jane.notes.apiOperationTasks.PostNotesTask;
import se.sugarest.jane.notes.R;

/**
 * Created by jane on 17-10-18.
 */

public class DetailActivity extends AppCompatActivity {

    private int mAddAndEditRecordNumber;

    private static final int ADD_A_NOTE = 100;
    private static final int EDIT_A_NOTE = 200;

    private TextView mIdTextView;

    private EditText mTitleEditText;

    private EditText mDescriptionEditText;

    private int mNotePositionId;

    private int mNoteSaveId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mIdTextView = (TextView) findViewById(R.id.tv_note_id);
        mTitleEditText = (EditText) findViewById(R.id.et_note_title);
        mDescriptionEditText = (EditText) findViewById(R.id.et_note_description);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("current_note_size")) {
                setTitle(getString(R.string.set_detail_activity_title_add_a_note));
                mAddAndEditRecordNumber = ADD_A_NOTE;
                String currentNotesSize = getIntent().getExtras().getString("current_note_size");
                mNotePositionId = Integer.parseInt(currentNotesSize) + 1;
                mIdTextView.setText(String.valueOf(mNotePositionId));
            } else if (intentThatStartedThisActivity.hasExtra("note_object")
                    && intentThatStartedThisActivity.hasExtra("note_position")) {
                setTitle(getString(R.string.set_detail_activity_title_edit_a_note));
                mAddAndEditRecordNumber = EDIT_A_NOTE;
                mNotePositionId = getIntent().getExtras().getInt("note_position") + 1;
                mIdTextView.setText(String.valueOf(mNotePositionId));
                Note currentNote = (Note) getIntent().getExtras().getSerializable("note_object");
                mNoteSaveId = currentNote.getNoteId();
                mTitleEditText.setText(currentNote.getNoteTitle());
                mDescriptionEditText.setText(currentNote.getNoteDescription());
            }
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem deleteAction = menu.findItem(R.id.action_delete);
        if (mAddAndEditRecordNumber == ADD_A_NOTE) {
            deleteAction.setVisible(false);
        } else {
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
                    createAndPostNote();
                } else {
                    editAndPutCurrentNote();
                }
                return true;
            case R.id.action_delete:
                deleteCurrentNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteCurrentNote() {
        new DeleteNotesTask(this).execute(mNoteSaveId);
    }

    private void editAndPutCurrentNote() {
        String noteTitleString = mTitleEditText.getText().toString();
        String noteDescriptionString = mDescriptionEditText.getText().toString();

        // Sanity check, title and description cannot be empty.
        if (noteTitleString.isEmpty() || noteDescriptionString.isEmpty()) {
            Toast.makeText(this, getString(R.string.toast_update_note_title_description_cannot_be_empty), Toast.LENGTH_SHORT).show();
        } else {
            Note newNote = new Note(mNoteSaveId, noteTitleString, noteDescriptionString);
            new PutNotesTask(this).execute(newNote);
        }
    }

    private void createAndPostNote() {

        String noteTitleString = mTitleEditText.getText().toString();
        String noteDescriptionString = mDescriptionEditText.getText().toString();

        // Sanity check, title and description cannot be empty.
        if (noteTitleString.isEmpty() || noteDescriptionString.isEmpty()) {
            Toast.makeText(this, getString(R.string.toast_create_note_title_description_cannot_be_empty), Toast.LENGTH_SHORT).show();
        } else {
            Note newNote = new Note(mNotePositionId, noteTitleString, noteDescriptionString);
            new PostNotesTask(this).execute(newNote);
        }
    }
}
