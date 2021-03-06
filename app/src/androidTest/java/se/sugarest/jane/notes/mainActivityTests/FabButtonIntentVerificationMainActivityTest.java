package se.sugarest.jane.notes.mainActivityTests;

import android.app.Activity;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import se.sugarest.jane.notes.R;
import se.sugarest.jane.notes.ui.DetailActivity;
import se.sugarest.jane.notes.ui.MainActivity;

import static android.app.Instrumentation.ActivityResult;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static se.sugarest.jane.notes.util.Constant.INTENT_CURRENT_NOTE_SIZE_TITLE;
import static se.sugarest.jane.notes.util.ToolbarTitleMatcher.matchToolbarTitle;

/**
 * Created by jane on 17-10-18.
 */
@RunWith(AndroidJUnit4.class)
public class FabButtonIntentVerificationMainActivityTest {

    @Rule
    public IntentsTestRule<MainActivity> mIntentsTestRule = new IntentsTestRule<>(MainActivity.class);

    private Context instrumentationCtx;

    @Before
    public void setUp() {
        instrumentationCtx = InstrumentationRegistry.getTargetContext();
    }

    @Before
    public void stubAllExternalIntents() {
        intending(not(isInternal())).respondWith(new ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void clickFabButton_OpensDetailActivity() {
        onView(withId(R.id.fab)).perform(click());
        intended(allOf(hasExtraWithKey(INTENT_CURRENT_NOTE_SIZE_TITLE),
                hasComponent(DetailActivity.class.getName())));

        // Check the activity title shows on the toolbar of DetailActivity is "Add a note"
        matchToolbarTitle(instrumentationCtx.getString(R.string.set_detail_activity_title_add_a_note));

        // Check action_save exists on menu item
        onView(withId(R.id.action_save)).check(matches(isDisplayed())).perform(click());

        onView(withText(instrumentationCtx.getString(R.string.toast_create_note_title_description_cannot_be_empty)))
                .inRoot(withDecorView(not(is(mIntentsTestRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }
}

