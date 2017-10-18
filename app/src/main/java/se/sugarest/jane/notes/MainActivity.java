package se.sugarest.jane.notes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements NoteAdapter.NoteAdapterOnClickHandler {

    private RecyclerView mRecyclerView;
    private TextView mEmptyTextView;
    private NoteAdapter mNoteAdapter;

    public NoteAdapter getmNoteAdapter() {
        return mNoteAdapter;
    }

    public TextView getmEmptyTextView() {
        return mEmptyTextView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
