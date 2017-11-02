package se.sugarest.jane.notes;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import se.sugarest.jane.notes.data.Note;

/**
 * Created by jane on 17-11-2.
 */

public interface NotesClient {
    @GET
    Call<List<Note>> notes();
}
