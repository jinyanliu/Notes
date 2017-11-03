package se.sugarest.jane.notes.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import se.sugarest.jane.notes.data.type.Note;

import static se.sugarest.jane.notes.util.Constant.NOTES_API_END_POINT;
import static se.sugarest.jane.notes.util.Constant.NOTES_API_SLASH;

/**
 * Created by jane on 17-11-2.
 */
public interface NotesClient {
    @GET(NOTES_API_END_POINT)
    Call<List<Note>> getNotes();

    @POST(NOTES_API_END_POINT)
    Call<Note> createNote(@Body Note note);

    @PUT(NOTES_API_END_POINT + NOTES_API_SLASH + "{id}")
    Call<Note> updateNote(
            @Path("id") int id,
            @Body Note Note);
}
