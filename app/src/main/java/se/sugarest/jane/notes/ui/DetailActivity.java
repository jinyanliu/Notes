package se.sugarest.jane.notes.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import se.sugarest.jane.notes.R;
import se.sugarest.jane.notes.api.DeleteNotesTask;
import se.sugarest.jane.notes.api.PostNotesTask;
import se.sugarest.jane.notes.api.PutNotesTask;
import se.sugarest.jane.notes.data.type.Note;

import static se.sugarest.jane.notes.util.Constant.ADD_A_NOTE;
import static se.sugarest.jane.notes.util.Constant.EDIT_A_NOTE;

/**
 * Created by jane on 17-10-18.
 */

/**
 * This activity is used for adding a new note, or editing and deleting an existing note.
 */
public class DetailActivity extends AppCompatActivity {

    private int mAddAndEditRecordNumber;
    private TextView mIdTextView;
    private EditText mTitleEditText;
    private EditText mDescriptionEditText;
    private int mNotePositionId;
    private int mNoteSaveId;
    private Toast mToast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mIdTextView = (TextView) findViewById(R.id.tv_note_id);
        mTitleEditText = (EditText) findViewById(R.id.et_note_title);
        mDescriptionEditText = (EditText) findViewById(R.id.et_note_description);

        // Figure out it is an AddNoteDetailActivity or it is an EditNoteDetailActivity
        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("current_note_size")) {
                // It is an AddNoteDetailActivity
                setTitle(getString(R.string.set_detail_activity_title_add_a_note));
                mAddAndEditRecordNumber = ADD_A_NOTE;
                String currentNotesSize = getIntent().getExtras().getString("current_note_size");
                mNotePositionId = Integer.parseInt(currentNotesSize) + 1;
                mIdTextView.setText(String.valueOf(mNotePositionId));
            } else if (intentThatStartedThisActivity.hasExtra("note_object")
                    && intentThatStartedThisActivity.hasExtra("note_position")) {
                // It is an EditAndDeleteNoteDetailActivity
                setTitle(getString(R.string.set_detail_activity_title_edit_a_note));
                mAddAndEditRecordNumber = EDIT_A_NOTE;
                mNotePositionId = getIntent().getExtras().getInt("note_position") + 1;
                mIdTextView.setText(String.valueOf(mNotePositionId));
                Note currentNote = (Note) getIntent().getExtras().getSerializable("note_object");
                mNoteSaveId = currentNote.getId();
                mTitleEditText.setText(currentNote.getTitle());
                mDescriptionEditText.setText(currentNote.getDescription());
            }
        }
    }

    // When this activity is in the AddNoteMode, it makes no sense to have the delete menu item
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
                    // It is AddNoteMode
                    createAndPostNewNote();
                } else {
                    // It is EditNoteMode
                    editAndPutCurrentNote();
                }
                return true;
            case R.id.action_delete:
                // It is EditNoteMode, because in AddNoteMode, this delete item is invisible.
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
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(this, getString(R.string.toast_update_note_title_description_cannot_be_empty), Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.BOTTOM, 0, 0);
            mToast.show();
        } else {
            Note newNote = new Note(mNoteSaveId, noteTitleString, noteDescriptionString);
            new PutNotesTask(this).execute(newNote);
        }
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
            Note newNote = new Note(mNotePositionId, noteTitleString, noteDescriptionString);
            new PostNotesTask(this).execute(newNote);
        }
    }
}
