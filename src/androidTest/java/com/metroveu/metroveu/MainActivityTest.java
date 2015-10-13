package com.metroveu.metroveu;

import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.metroveu.metroveu.activities.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by joanvila on 13/10/15.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public final ActivityTestRule<MainActivity> main = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void shouldBeAbleToLaunchMainScreen() {
        onView(withText("Parada"))
                .check(ViewAssertions.matches(isDisplayed()));
    }
}
