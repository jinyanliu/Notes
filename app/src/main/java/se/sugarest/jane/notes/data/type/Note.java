package se.sugarest.jane.notes.data.type;

/**
 * Created by jane on 17-10-18.
 */
public class Note {

    // To use Retrofit to parse JSON response, fields name here must be the same as the title in JSON.
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
