package se.sugarest.jane.notes.data;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import se.sugarest.jane.notes.R;
import se.sugarest.jane.notes.data.type.Note;

/**
 * This class gets a list of note objects from inputs, retrieves the corresponding fields of each
 * note object, and then populates to related views.
 * <p>
 * That means the controller in the MVC pattern.
 * <p>
 * Created by jane on 17-10-18.
 */
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteAdapterViewHolder> {
    // An On-click handler to make it easy for MainActivity to interface with the RecyclerView
    private final NoteAdapterOnClickHandler mClickHandler;
    private ArrayList<Note> mNoteObjects = new ArrayList<>();

    public ArrayList<Note> getmNoteObjects() {
        return mNoteObjects;
    }

    /**
     * Creates a NoteAdapter.
     *
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public NoteAdapter(NoteAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If RecyclerView has more than one type of item (which this one don't)
     *                  this viewType can be used to provide a different layout.
     * @return A new NoteAdapterViewHolder that holds the View for each list item
     */
    @Override
    public NoteAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.list_item_note;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new NoteAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, update the contents of the ViewHolder to display the movie
     * posters for each particular position, using the "position" argument that is conveniently
     * passed in.
     *
     * @param holder   The ViewHolder which should be updated to represent the
     *                 contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(NoteAdapterViewHolder holder, int position) {
        holder.mNoteIdTextView.setVisibility(View.VISIBLE);
        holder.mNoteTitleTextView.setVisibility(View.VISIBLE);
        holder.mNoteIdTextView.setText(String.valueOf(position + 1));
        holder.mNoteTitleTextView.setText(mNoteObjects.get(position).getTitle());
    }

    /**
     * @return The number of items to display.
     */
    @Override
    public int getItemCount() {
        return mNoteObjects.size();
    }

    /**
     * This method is used to set the notes data on a NoteAdapter
     */
    public void setNotesData(List<Note> notesList) {
        mNoteObjects.clear();
        mNoteObjects.addAll(notesList);
        notifyDataSetChanged();
    }

    /**
     * The interface that receives onClick messages.
     */
    public interface NoteAdapterOnClickHandler {
        void onClick(int notePosition);

    }

    /**
     * Cache of the children views for a note
     */
    public class NoteAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mNoteIdTextView;
        public final TextView mNoteTitleTextView;

        public NoteAdapterViewHolder(View view) {
            super(view);
            mNoteIdTextView = (TextView) view.findViewById(R.id.tv_id);
            mNoteTitleTextView = (TextView) view.findViewById(R.id.tv_title);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPositon = getAdapterPosition();
            mClickHandler.onClick(adapterPositon);
        }
    }
}
