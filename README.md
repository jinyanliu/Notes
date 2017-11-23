# Notes
A small app to retrieve notes list, to create, update and delete note with Notes API. 

README

Tests

1. Test all UI widgets are displayed on the main screen and detail screen. 

2. Test static views, e.g., Fab Button on main screen and Note Id label TextView on detail screen, has the correct images or texts. 

3. Test all EditText views performs as expected. 

4. Created 2 customized Matcher classes. One is DrawableMatcher, which checks if an ImageView has a correct image. The other is ToolbarTitleMatcher class, which checks if a Toolbar has the correct title text.

5. Test the Fab Button on the right bottom of main screen, using Intent Verification Espresso Test. Performs clicking on the button, which opens detail screen. 

6. Test the RecyclerView to show all the notes on main screen, using Intent Verification Espresso Test, and also introducing IdlingResource to control Espresso while the app is doing long-running operations, e.g., retrieving data from REST API during testing.  Perform clicking on one of the note items, which opens detail screen. 

7. Test a Toast message is displayed when clicking save button without providing note's title or description. 

8. Test getItemCount() method in NoteAdapter class. 
