package se.sugarest.jane.notes;

/**
 * Created by jane on 17-10-18.
 */

/**
 * Represents a Note.
 * It contains the note id and the note title.
 */
public class Note {

    private String mNoteId;
    private String mNoteTitle;
    private String mNoteDescription;

    public Note (String noteId, String noteTitle, String noteDescription) {
        mNoteId = noteId;
        mNoteTitle = noteTitle;
        mNoteDescription = noteDescription;
    }

    public String getNoteId(){
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
                "mNoteId='" + mNoteId + '\'' +
                ", mNoteTitle='" + mNoteTitle + '\'' +
                ", mNoteDescription='" + mNoteDescription + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Note note = (Note) o;

        if (!mNoteId.equals(note.mNoteId)) return false;
        if (!mNoteTitle.equals(note.mNoteTitle)) return false;
        return mNoteDescription.equals(note.mNoteDescription);

    }

    @Override
    public int hashCode() {
        int result = mNoteId.hashCode();
        result = 31 * result + mNoteTitle.hashCode();
        result = 31 * result + mNoteDescription.hashCode();
        return result;
    }
}
