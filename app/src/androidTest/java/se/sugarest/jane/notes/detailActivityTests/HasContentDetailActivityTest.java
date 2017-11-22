package se.sugarest.jane.notes.detailActivityTests;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import se.sugarest.jane.notes.R;
import se.sugarest.jane.notes.ui.DetailActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Created by jane on 17-10-18.
 */
@RunWith(AndroidJUnit4.class)
public class HasContentDetailActivityTest {

    @Rule
    public ActivityTestRule<DetailActivity> mActivityTestRule = new ActivityTestRule<>(DetailActivity.class);

    private Context instrumentationCtx;

    @Before
    public void setUp() {
        instrumentationCtx = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void detailScreenHasContent_idLabelTextView() {
        onView(withId(R.id.tv_note_id_label)).check(matches(isDisplayed())).check(matches(notNullValue()))
                .check(matches(withText(instrumentationCtx.getString(R.string.note_id_label))));
    }

    @Test
    public void detailScreenHasContent_idTextView() {
        onView(withId(R.id.tv_note_id)).check(matches(isDisplayed())).check(matches(notNullValue()));
    }

    @Test
    public void detailScreenHasContent_titleLabelTextView() {
        onView(withId(R.id.tv_note_title_label)).check(matches(isDisplayed())).check(matches(notNullValue()))
                .check(matches(withText(instrumentationCtx.getString(R.string.note_title_label))));
    }

    @Test
    public void detailScreenHasContent_titleEditTextView() {
        onView(withId(R.id.et_note_title)).check(matches(isDisplayed()));
    }

    @Test
    public void detailScreenHasContent_DescriptionLabelTextView() {
        onView(withId(R.id.tv_note_description_label)).check(matches(isDisplayed())).check(matches(notNullValue()))
                .check(matches(withText(instrumentationCtx.getString(R.string.note_description_label))));
    }

    @Test
    public void detailScreenHasContent_DescriptionEditTextView() {
        onView(withId(R.id.et_note_description)).check(matches(isDisplayed()));
    }
}

