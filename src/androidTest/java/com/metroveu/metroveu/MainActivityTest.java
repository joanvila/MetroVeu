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
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
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


    /**
     * Test per comprovar que la pàgina principal té el text MetroVeu quan s'enjega l'app
     */
    @Test public void onAppStartedHomePageAppears() {
        onView(withText(R.string.home))
                .check(ViewAssertions.matches(isDisplayed()));
    }

    /**
     * Test per comprovar que clicant el botó línies es mostra el fragment de línies
     */
    @Test public void onLinesButtonClickedLinesAppear() {
        onView(withId(R.id.show_lines_button))
                .perform(click());
        onView(withId(R.id.liniesListView))
                .check(ViewAssertions.matches(isDisplayed()));

    }

    /**
     * Test per comprovar si es mostren totes les línies al fragment de línies
     */
    @Test public void allLinesAreShown() {
        onView(withId(R.id.show_lines_button))
                .perform(click());
        onView(withText("L1")).check(ViewAssertions.matches(isDisplayed()));
        onView(withText("L2")).check(ViewAssertions.matches(isDisplayed()));
        onView(withText("L3")).check(ViewAssertions.matches(isDisplayed()));
        onView(withText("L4")).check(ViewAssertions.matches(isDisplayed()));
        onView(withText("L5")).check(ViewAssertions.matches(isDisplayed()));
        onView(withText("L9")).check(ViewAssertions.matches(isDisplayed()));
        onView(withText("L10")).check(ViewAssertions.matches(isDisplayed()));
        onView(withText("L11")).check(ViewAssertions.matches(isDisplayed()));
        onView(withText("L9|L10")).check(ViewAssertions.matches(isDisplayed()));
        onView(withText("TELEFÈRIC DE MONTJUÏC")).check(ViewAssertions.matches(isDisplayed()));
        onView(withText("TRAMVIA BLAU")).check(ViewAssertions.matches(isDisplayed()));
    }

    /**
     * Test per comprovar que prement enrere a la llista de línies tornem al menú principal
     */
    @Test public void fromLinesReturnToMain() {
        onView(withId(R.id.show_lines_button))
                .perform(click());
        onView(withId(R.id.liniesListView))
                .perform(pressBack());
        onView(withText(R.string.home))
                .check(ViewAssertions.matches(isDisplayed()));
    }
    
}
