package se.sugarest.jane.notes.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import se.sugarest.jane.notes.R;
import se.sugarest.jane.notes.api.NotesClient;
import se.sugarest.jane.notes.data.NoteAdapter;
import se.sugarest.jane.notes.data.type.Note;

import static se.sugarest.jane.notes.util.Constant.NOTES_BASE_URL;

public class MainActivity extends AppCompatActivity implements NoteAdapter.NoteAdapterOnClickHandler {
    private final static String LOG_TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private TextView mEmptyTextView;
    private NoteAdapter mNoteAdapter;
    private int mNotesSize;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEmptyTextView = (TextView) findViewById(R.id.tv_empty_view);

        setUpFabButtonOnClick();
        setUpRecyclerViewWithAdapter();
        sendNetworkRequestGet();
    }

    @Override
    protected void onStart() {
        super.onStart();
        sendNetworkRequestGet();
    }

    private void sendNetworkRequestGet() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(NOTES_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.client(httpClient.build()).build();
        NotesClient client = retrofit.create(NotesClient.class);
        Call<List<Note>> call = client.getNotes();
        call.enqueue(new Callback<List<Note>>() {
            @Override
            public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
                showRecyclerView();
                Log.i(LOG_TAG, "GET response success: Complete url to request is: "
                        + response.raw().request().url().toString()
                        + "\nresponse.body().toString == " + response.body().toString());
                List<Note> notesList = response.body();
                if (notesList != null && !notesList.isEmpty()) {
                    // Sort notes order via id.
                    Collections.sort(notesList, new Comparator<Note>() {
                        @Override
                        public int compare(Note o1, Note o2) {
                            return o1.getId() - o2.getId();
                        }
                    });
                    mNoteAdapter.setNotesData(notesList);
                } else {
                    showEmptyView();
                }
                mNotesSize = notesList.size();
            }

            @Override
            public void onFailure(Call<List<Note>> call, Throwable t) {
                Log.i(LOG_TAG, "GET response failure.");
                if (mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(MainActivity.this, getString(R.string.toast_retrofit_delete_on_failure), Toast.LENGTH_SHORT);
                mToast.setGravity(Gravity.BOTTOM, 0, 0);
                mToast.show();
                mNotesSize = 0;
            }
        });
    }

    private void setUpRecyclerViewWithAdapter() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        if (mNoteAdapter == null) {
            mNoteAdapter = new NoteAdapter(this);
        }
        mRecyclerView.setAdapter(mNoteAdapter);
    }

    /**
     * Opens DetailActivity. Add a new note.
     */
    private void setUpFabButtonOnClick() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                // Pass current_note_size to detailActivity.
                // When adding a new note, its id shows up in detail activity will be
                // current_note_size + 1
                intent.putExtra("current_note_size", String.valueOf(mNotesSize));
                startActivity(intent);
            }
        });
    }

    /**
     * This method is overridden by the MainActivity class in order to handle RecyclerView item
     * clicks.
     * Edit an existing note.
     */
    @Override
    public void onClick(int notePosition) {
        Note currentClickedNote = mNoteAdapter.getmNoteObjects().get(notePosition);
        Intent intentToStartDetailActivity = new Intent(MainActivity.this, DetailActivity.class);
        intentToStartDetailActivity.putExtra("note_id", currentClickedNote.getId());
        intentToStartDetailActivity.putExtra("note_position", notePosition);
        startActivity(intentToStartDetailActivity);
    }

    private void showRecyclerView() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mEmptyTextView.setVisibility(View.INVISIBLE);
    }

    private void showEmptyView() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mEmptyTextView.setVisibility(View.VISIBLE);
    }
}
