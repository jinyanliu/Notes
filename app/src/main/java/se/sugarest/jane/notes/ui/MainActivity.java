package se.sugarest.jane.notes.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.FloatingActionButton;
import android.support.test.espresso.IdlingResource;
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
import se.sugarest.jane.notes.idlingResource.SimpleIdlingResource;

import static se.sugarest.jane.notes.util.Constant.INTENT_CURRENT_NOTE_SIZE_TITLE;
import static se.sugarest.jane.notes.util.Constant.INTENT_NOTE_ID_TITLE;
import static se.sugarest.jane.notes.util.Constant.INTENT_NOTE_POSITION_TITLE;
import static se.sugarest.jane.notes.util.Constant.NOTES_BASE_URL;

/**
 * This is the main controller of the whole app.
 * It initiates the app and switches between activities, e.g., MainActivity itself and DetailActivity.
 */
public class MainActivity extends AppCompatActivity implements NoteAdapter.NoteAdapterOnClickHandler {
    private final static String LOG_TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private TextView mEmptyTextView;
    private NoteAdapter mNoteAdapter;
    private int mNotesSize;
    private Toast mToast;

    // The Idling Resource which will be null in production.
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEmptyTextView = (TextView) findViewById(R.id.tv_empty_view);

        setUpFabButtonOnClick();
        setUpRecyclerViewWithAdapter();
        sendNetworkRequestGet();

        // Get the IdlingResource instance
        getIdlingResource();
    }

    /**
     * Call finish() in DetailActivity after performing create/update/delete, override onStart()
     * method in MainActivity to GET the up-to-date notes list.
     */
    @Override
    protected void onStart() {
        super.onStart();
        sendNetworkRequestGet();
    }

    // Use External Library Retrofit to GET notes list.
    // Reference: https://github.com/square/retrofit
    private void sendNetworkRequestGet() {

        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(false);
        }

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

                    if (mIdlingResource != null) {
                        mIdlingResource.setIdleState(true);
                    }

                } else {
                    showEmptyView();
                }
                mNotesSize = notesList.size();
            }

            @Override
            public void onFailure(Call<List<Note>> call, Throwable t) {
                Log.e(LOG_TAG, "Failed to get notes list data back.", t);
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

    // To open DetailActivity, add a new note
    private void setUpFabButtonOnClick() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                // Pass current_note_size to detailActivity.
                // When adding a new note, its id shows up in detail activity will be
                // current_note_size + 1, because positionId starts from 1, not from 0.
                intent.putExtra(INTENT_CURRENT_NOTE_SIZE_TITLE, String.valueOf(mNotesSize));
                startActivity(intent);
            }
        });
    }

    /**
     * This method is to handle RecyclerView item clicks in MainActivity, retrieves related note
     * data in DetailActivity.
     * And use can edit an existing note in DetailActivity.
     */
    @Override
    public void onClick(int notePosition) {
        Note currentClickedNote = mNoteAdapter.getmNoteObjects().get(notePosition);
        Intent intentToStartDetailActivity = new Intent(MainActivity.this, DetailActivity.class);
        intentToStartDetailActivity.putExtra(INTENT_NOTE_ID_TITLE, currentClickedNote.getId());
        intentToStartDetailActivity.putExtra(INTENT_NOTE_POSITION_TITLE, notePosition);
        startActivity(intentToStartDetailActivity);
    }

    // Used while data is loaded
    private void showRecyclerView() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mEmptyTextView.setVisibility(View.INVISIBLE);
    }

    // Used while there is no data returned
    private void showEmptyView() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mEmptyTextView.setVisibility(View.VISIBLE);
    }
}
