package se.sugarest.jane.notes.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import se.sugarest.jane.notes.FetchNotesTask;
import se.sugarest.jane.notes.NoteAdapter;
import se.sugarest.jane.notes.R;

public class MainActivity extends AppCompatActivity implements NoteAdapter.NoteAdapterOnClickHandler {

    private RecyclerView mRecyclerView;
    private TextView mEmptyTextView;
    private NoteAdapter mNoteAdapter;
    private int mNotesSize;

    public NoteAdapter getmNoteAdapter() {
        return mNoteAdapter;
    }

    public TextView getmEmptyTextView() {
        return mEmptyTextView;
    }

    public void setmNotesSize(int mNotesSize) {
        this.mNotesSize = mNotesSize;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup FAB to open DetailActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                // Pass current_note_size to detailActivity.
                // When adding a new note, its id will be current_note_size + 1
                intent.putExtra("current_note_size", String.valueOf(mNotesSize));
                startActivity(intent);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mEmptyTextView = (TextView) findViewById(R.id.tv_empty_view);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        if (mNoteAdapter == null) {
            mNoteAdapter = new NoteAdapter(this);
        }

        mRecyclerView.setAdapter(mNoteAdapter);

        new FetchNotesTask(this).execute();
    }

    @Override
    public void onClick(String noteId) {

    }
}
