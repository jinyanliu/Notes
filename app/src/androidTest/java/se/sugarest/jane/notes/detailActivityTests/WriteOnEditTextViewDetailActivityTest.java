package se.sugarest.jane.notes.detailActivityTests;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import se.sugarest.jane.notes.R;
import se.sugarest.jane.notes.ui.DetailActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by jane on 17-11-22.
 */
@RunWith(AndroidJUnit4.class)
public class WriteOnEditTextViewDetailActivityTest {

    @Rule
    public ActivityTestRule<DetailActivity> mActivityTestRule = new ActivityTestRule<>(DetailActivity.class);

    @Test
    public void writeOnTitleEditTextViewTest() {
        onView(withId(R.id.et_note_title)).perform(typeText("Buy Milk"), closeSoftKeyboard());
        onView(withId(R.id.et_note_title)).check(matches(withText("Buy Milk")));
    }

    @Test
    public void detailActivityDescriptionEditTextTest() {
        onView(withId(R.id.et_note_description)).perform(typeText("Before 6pm"), closeSoftKeyboard());
        onView(withId(R.id.et_note_description)).check(matches(withText("Before 6pm")));
    }
}
