package se.sugarest.jane.notes.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import se.sugarest.jane.notes.R;

/**
 * Created by jane on 17-10-18.
 */

public class DetailActivity extends AppCompatActivity {

    private TextView mIdTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mIdTextView = (TextView) findViewById(R.id.tv_note_id);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("current_note_size")) {
                String currentNotesSize = getIntent().getExtras().getString("current_note_size");
                mIdTextView.setText(String.valueOf(Integer.parseInt(currentNotesSize) + 1));
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
    }

}
