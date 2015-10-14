package com.metroveu.metroveu;

import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.metroveu.metroveu.activities.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by joanvila on 13/10/15.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public final ActivityTestRule<MainActivity> main = new ActivityTestRule<>(MainActivity.class);

//    @Test
//    /**
//     * Test test lol
//     */
//    public void shouldBeAbleToLaunchMainScreen() {
//        onView(withText("Parada"))
//                .check(ViewAssertions.matches(isDisplayed()));
//    }

    @Test
    /**
     * Test if there is a homepage with the text MetroVeu on it
     */
    public void onAppStartedHomePageAppears() {
        onView(withText(R.string.home))
                .check(ViewAssertions.matches(isDisplayed()));
    }

    @Test
    /**
     * Test if lines button shows all lines
     */
    public void onLinesButtonClickedLinesAppear() {
        onView(withId(R.id.show_lines_button))
                .perform(click());
        onView(withId(R.id.lines_title))
                .check(ViewAssertions.matches(isDisplayed()));

    }
}
