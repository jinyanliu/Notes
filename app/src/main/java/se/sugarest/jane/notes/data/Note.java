package se.sugarest.jane.notes.data;

/**
 * Created by jane on 17-10-18.
 */

import java.io.Serializable;

/**
 * Represents a Note.
 * It contains the note id and the note title.
 */
public class Note implements Serializable {

    private int mNoteId;
    private String mNoteTitle;
    private String mNoteDescription;

    public Note (int noteId, String noteTitle, String noteDescription) {
        mNoteId = noteId;
        mNoteTitle = noteTitle;
        mNoteDescription = noteDescription;
    }

    public int getNoteId(){
        return mNoteId;
    }

    public String getNoteTitle(){
        return mNoteTitle;
    }

    public String getNoteDescription(){
        return mNoteDescription;
    }

    @Override
    public String toString() {
        return "Note{" +
                "mNoteId=" + mNoteId +
                ", mNoteTitle='" + mNoteTitle + '\'' +
                ", mNoteDescription='" + mNoteDescription + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Note note = (Note) o;

        if (mNoteId != note.mNoteId) return false;
        if (!mNoteTitle.equals(note.mNoteTitle)) return false;
        return mNoteDescription.equals(note.mNoteDescription);

    }

    @Override
    public int hashCode() {
        int result = mNoteId;
        result = 31 * result + mNoteTitle.hashCode();
        result = 31 * result + mNoteDescription.hashCode();
        return result;
    }
}
