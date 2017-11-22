package se.sugarest.jane.notes;

import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import se.sugarest.jane.notes.data.NoteAdapter;
import se.sugarest.jane.notes.data.type.Note;

/**
 * Created by jane on 17-11-4.
 */
@RunWith(AndroidJUnit4.class)
public class NoteAdapterTest {

    private NoteAdapter noteAdapter;
    private List<Note> notesList = new ArrayList<>();

    @Before
    public void setUp() {
        noteAdapter = new NoteAdapter(null);
    }

    @Test
    public void getItemCountTest_oneNote() throws Exception {
        int oneNoteId = 0;
        String oneNoteTitle = "Jogging in park.";
        String oneNoteDescription = "At least 10 miles is the goal";
        Note oneNote = new Note(oneNoteTitle, oneNoteDescription);
        oneNote.setId(oneNoteId);

        notesList.add(oneNote);
        noteAdapter.setNotesData(notesList);

        Assert.assertEquals(1, noteAdapter.getItemCount());
    }

    @Test
    public void getItemCountTest_twoNotes() throws Exception {
        int firstNoteId = 0;
        String firstNoteTitle = "Jogging in park.";
        String firstNoteDescription = "At least 10 miles is the goal";
        Note firstNote = new Note(firstNoteTitle, firstNoteDescription);
        firstNote.setId(firstNoteId);

        int secondNoteId = 1;
        String secondNoteTitle = "Buy cheese and bread for breakfast.";
        String secondNoteDescription = "Needed for breakfast must be done by 8 am tomorrow!";
        Note secondNote = new Note(secondNoteTitle, secondNoteDescription);
        secondNote.setId(secondNoteId);

        notesList.add(firstNote);
        notesList.add(secondNote);
        noteAdapter.setNotesData(notesList);

        Assert.assertEquals(2, noteAdapter.getItemCount());
    }
}
