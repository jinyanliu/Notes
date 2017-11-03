package se.sugarest.jane.notes.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import se.sugarest.jane.notes.data.type.Note;

import static se.sugarest.jane.notes.util.Constant.NOTES_API_END_POINT;

/**
 * Created by jane on 17-11-2.
 */

public interface NotesClient {
    @GET(NOTES_API_END_POINT)
    Call<List<Note>> notes();
}