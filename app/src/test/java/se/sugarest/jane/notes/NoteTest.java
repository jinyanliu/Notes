package se.sugarest.jane.notes;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import se.sugarest.jane.notes.data.Note;

/**
 * Created by jane on 17-10-18.
 */

public class NoteTest {

    private int id = 18;

    private String title = "Buy cheese and bread for breakfast.";

    private String description = "Needed for breakfast must be done by 8 am tomorrow!";

    private Note note;

    @Before
    public void setUp() {
        this.note = new Note(id, title, description);
    }

    @Test
    public void getIdTest() throws Exception {
        Assert.assertEquals(id, note.getId());
    }

    @Test
    public void getTitleTest() throws Exception {
        Assert.assertEquals(title, note.getTitle());
    }

    @Test
    public void getDescriptionTest() throws Exception {
        Assert.assertEquals(description, note.getDescription());
    }
}
