package se.sugarest.jane.notes;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import se.sugarest.jane.notes.ui.MainActivity;
import se.sugarest.jane.notes.util.DrawableMatcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.notNullValue;
import static se.sugarest.jane.notes.HasContentMainActivityTest.EspressoTestsMatchers.withDrawable;

/**
 * Created by jane on 17-11-22.
 */
@RunWith(AndroidJUnit4.class)
public class HasContentMainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainScreenHasContent_FabButton(){
        onView(withId(R.id.fab)).check(matches(isDisplayed())).check(matches(notNullValue()))
                .check(matches(withDrawable(R.drawable.ic_add)));
    }

    public static class EspressoTestsMatchers {

        public static Matcher<View> withDrawable(final int resourceId) {
            return new DrawableMatcher(resourceId);
        }

        public static Matcher<View> noDrawable() {
            return new DrawableMatcher(-1);
        }
    }
}
