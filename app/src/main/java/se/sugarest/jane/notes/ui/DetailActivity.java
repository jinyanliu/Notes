package se.sugarest.jane.notes.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

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
                finish();
                return true;
        }
        return true;
    }

    private void createAndPostNote() {

        String noteTitleString = mTitleEditText.getText().toString();
        String noteDescriptionString = mDescriptionEditText.getText().toString();
    }

}
