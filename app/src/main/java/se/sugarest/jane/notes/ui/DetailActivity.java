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

import se.sugarest.jane.notes.Note;
import se.sugarest.jane.notes.PostNotesTask;
import se.sugarest.jane.notes.R;

/**
 * Created by jane on 17-10-18.
 */

public class DetailActivity extends AppCompatActivity {

    private TextView mIdTextView;

    private EditText mTitleEditText;

    private EditText mDescriptionEditText;

    private int mNoteId;

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
                String currentNotesSize = getIntent().getExtras().getString("current_note_size");
                mNoteId = Integer.parseInt(currentNotesSize) + 1;
                mIdTextView.setText(String.valueOf(mNoteId));
            }
        }
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
                createAndPostNote();
                return true;
        }
        return true;
    }

    private void createAndPostNote() {

        String noteTitleString = mTitleEditText.getText().toString();
        String noteDescriptionString = mDescriptionEditText.getText().toString();

        // Sanity check, title and description cannot be empty.
        if (noteTitleString.isEmpty() || noteDescriptionString.isEmpty()) {
            Toast.makeText(this, getString(R.string.toast_create_note_title_description_cannot_be_empty), Toast.LENGTH_SHORT).show();
        } else {
            Note newNote = new Note(mNoteId, noteTitleString, noteDescriptionString);
            new PostNotesTask(this).execute(newNote);
        }
    }
}
