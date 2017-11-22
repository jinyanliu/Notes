package se.sugarest.jane.notes.mainActivityTests;

import android.app.Activity;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import se.sugarest.jane.notes.R;
import se.sugarest.jane.notes.ui.DetailActivity;
import se.sugarest.jane.notes.ui.MainActivity;

import static android.app.Instrumentation.ActivityResult;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.IdlingRegistry.getInstance;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static se.sugarest.jane.notes.util.Constant.INTENT_NOTE_ID_TITLE;
import static se.sugarest.jane.notes.util.Constant.INTENT_NOTE_POSITION_TITLE;
import static se.sugarest.jane.notes.util.ToolbarTitleMatcher.matchToolbarTitle;

/**
 * Created by jane on 17-11-22.
 */
@RunWith(AndroidJUnit4.class)
public class RecyclerViewIntentVerificationMainActivityTest {

    private IdlingResource mIdlingResource;

    @Rule
    public IntentsTestRule<MainActivity> mIntentsTestRule = new IntentsTestRule<>(MainActivity.class);

    @Before
    public void stubAllExternalIntents() {
        intending(not(isInternal())).respondWith(new ActivityResult(Activity.RESULT_OK, null));
    }

    // Registers any resource that needs to be synchronized with Espresso before the test is run.
    @Before
    public void registerIdlingResource() {
        mIdlingResource = mIntentsTestRule.getActivity().getIdlingResource();
        getInstance().register(mIdlingResource);
    }

    @Test
    public void clickRecyclerViewItem_OpensDetailActivity() {
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        intended(allOf(hasExtraWithKey(INTENT_NOTE_ID_TITLE),
                hasExtra(INTENT_NOTE_POSITION_TITLE, 0),
                hasComponent(DetailActivity.class.getName())));

        // Check the activity title shows on the toolbar of DetailActivity is "Edit a note"
        CharSequence title = InstrumentationRegistry.getTargetContext().getString(R.string.set_detail_activity_title_edit_a_note);
        matchToolbarTitle(title);
    }

    // Remember to unregister resources when not needed to avoid malfunction.
    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            getInstance().unregister(mIdlingResource);
        }
    }
}
