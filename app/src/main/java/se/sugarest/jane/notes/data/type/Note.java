package se.sugarest.jane.notes.data.type;

/**
 * It is the outermost JSON response from NoteApi.
 * <p>
 * Created by jane on 17-10-18.
 */
public class Note {
    private int id;
    private String title;
    private String description;

    public Note(String noteTitle, String noteDescription) {
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
}
