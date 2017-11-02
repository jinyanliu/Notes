package se.sugarest.jane.notes.data;

/**
 * Created by jane on 17-10-18.
 */

import java.io.Serializable;

public class Note implements Serializable {

    // To use Retrofit to parse JSON response, fields name here must be the same as the title in JSON.
    private int id;
    private String title;
    private String description;

    public Note(int noteId, String noteTitle, String noteDescription) {
        id = noteId;
        title = noteTitle;
        description = noteDescription;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Note note = (Note) o;

        if (id != note.id) return false;
        if (!title.equals(note.title)) return false;
        return description.equals(note.description);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + title.hashCode();
        result = 31 * result + description.hashCode();
        return result;
    }
}
