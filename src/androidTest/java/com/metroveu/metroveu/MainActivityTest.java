package com.metroveu.metroveu;

import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.metroveu.metroveu.activities.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.PositionAssertions.isBelow;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

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
     * Ometo algunes linies a la comprovació ja que si apareixen les altres aquestes també
     */
    @Test public void allLinesAreShown() {
        onView(withId(R.id.show_lines_button))
                .perform(click());
        onView(withText("L1")).check(ViewAssertions.matches(isDisplayed()));
        onView(withText("L2")).check(ViewAssertions.matches(isDisplayed()));
        onView(withText("L9")).check(ViewAssertions.matches(isDisplayed()));
        onView(withText("L10")).check(ViewAssertions.matches(isDisplayed()));
        onView(withText("L11")).check(ViewAssertions.matches(isDisplayed()));
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

    /**
     * Test per comprovar que prement una linia de la llista ens apareix el fragment de parades
     */
    @Test public void onLineClickGoToStops() {
        onView(withId(R.id.show_lines_button))
                .perform(click());
        onView(withText("L3"))
                .perform(click());
        onView(withId(R.id.paradesListView))
                .check(ViewAssertions.matches(isDisplayed()));
    }

    /**
     * Test per comprovar que prement una linia de la llista ens apareixin les seves parades
     * Ometo algunes parades a la comprovació ja que si apareixen les altres aquestes també
     */
    @Test public void onLineClickShowStops() {
        onView(withId(R.id.show_lines_button))
                .perform(click());
        onView(withText("L3"))
                .perform(click());
        onView(withText("Zona Universitària"))
                .check(ViewAssertions.matches(isDisplayed()));

        onData(anything()).inAdapterView(withId(R.id.paradesListView))
                .atPosition(0)
                //.perform(click());
                .check(ViewAssertions.matches(isDisplayed()));

        //TODO: Es pot ampliar el test

    }

    /**
     * Test per comprovar que prement enrere a la llista de parades tornem a la llista de línies
     */
    @Test public void fromStopsReturnToLines() {
        onView(withId(R.id.show_lines_button))
                .perform(click());
        onView(withText("L2"))
                .perform(click());
        onView(withId(R.id.paradesListView))
                .perform(pressBack());
        onView(withId(R.id.liniesListView))
                .check(ViewAssertions.matches(isDisplayed()));
    }

    /**
     * Test per comprovar que prement una parada de la llista ens apareix el fragment dels detalls de la parada
     */
    @Test public void onStopClickGoToDetails() {
        onView(withId(R.id.show_lines_button))
                .perform(click());
        onView(withText("L4"))
                .perform(click());
        onView(withText("Maragall"))
                .perform(click());
        onView(withId(R.id.nomParada))
                .check(ViewAssertions.matches(isDisplayed()));
    }

    /**
     * Test per comprovar que prement una parada de la llista ens apareixen els detalls amb el seu nom
     */
    @Test public void onStopClickShowDetailsName() {
        onView(withId(R.id.show_lines_button))
                .perform(click());
        onView(withText("L4"))
                .perform(click());
        onView(withText("Maragall"))
                .perform(click());
        onView(withText("Maragall"))
                .check(ViewAssertions.matches(isDisplayed()));
    }

    /**
     * Test per comprovar que prement enrere als detalls d'una parada tornem a la llista de parades
     */
    @Test public void fromDetailsReturnToStops() {
        onView(withId(R.id.show_lines_button))
                .perform(click());
        onView(withText("L4"))
                .perform(click());
        onView(withText("Maragall"))
                .perform(click());
        onView(withId(R.id.nomParada))
                .perform(pressBack());
        onView(withId(R.id.paradesListView))
                .check(ViewAssertions.matches(isDisplayed()));
    }
    /**
     * Test per comprovar que lliscant amb el dit a l'esquerra es canvia de parada a l'anterior
     */
    @Test public void fromStopChangeStopToLeft() {
        onView(withId(R.id.show_lines_button))
                .perform(click());
        onView(withText("L5"))
                .perform(click());
        onView(withText("Collblanc"))
                .perform(click());
        onView(withId(R.id.nomParada))
                .perform(swipeLeft());
        onView(withText("Badal"))
                .check(ViewAssertions.matches(isDisplayed()));
    }

    /**
     * Test per comprovar que lliscant amb el dit a la dreta es canvia de parada a la següent
     */
    @Test public void fromStopChangeStopToRight() {
        onView(withId(R.id.show_lines_button))
                .perform(click());
        onView(withText("L5"))
                .perform(click());
        onView(withText("Collblanc"))
                .perform(click());
        onView(withId(R.id.nomParada))
                .perform(swipeRight());
        onView(withText("Pubilla Cases"))
                .check(ViewAssertions.matches(isDisplayed()));
    }

    /**
     * Test per comprovar que lliscant amb el dit a la parada següent es mostri la informació d'aquesta
     */
    @Test public void fromStopChangeStopAndDetails() {
        onView(withId(R.id.show_lines_button))
                .perform(click());
        onView(withText("L5"))
                .perform(click());
        onView(withText("Collblanc"))
                .perform(click());
        onView(withId(R.id.nomParada))
                .perform(swipeRight());
        onView(withText("Pubilla Cases"))
                .perform(click());
        onView(withId(R.id.nomParada))
                .check(ViewAssertions.matches(isDisplayed()));
    }

    /**
     * Test per comprovar que des d'una parada lliscant amb el dit a l'esquerra es canvia de parada a l'anterior i si tornem a lliscar surt l'anterior d'aquesta
     */
    @Test public void fromStopChangeStopToLeft2() {
        onView(withId(R.id.show_lines_button))
                .perform(click());
        onView(withText("L5"))
                .perform(click());
        onView(withText("Collblanc"))
                .perform(click());
        onView(withId(R.id.nomParada))
                .perform(swipeLeft());
        onView(withText("Badal"))
                .perform(click());
        onView(withId(R.id.nomParada))
                .perform(swipeLeft());
        onView(withText("Plaça de Sants"))
                .check(ViewAssertions.matches(isDisplayed()));
    }

    /**
     * Test per comprovar que des d'una parada lliscant amb el dit a la dreta es canvia de parada a seguent i si tornem a lliscar surt la següent d'aquesta
     */
    @Test public void fromStopChangeStopToRight2() {
        onView(withId(R.id.show_lines_button))
                .perform(click());
        onView(withText("L5"))
                .perform(click());
        onView(withText("Collblanc"))
                .perform(click());
        onView(withId(R.id.nomParada))
                .perform(swipeRight());
        onView(withText("Pubilla Cases"))
                .perform(click());
        onView(withId(R.id.nomParada))
                .perform(swipeRight());
        onView(withText("Can Vidalet"))
                .check(ViewAssertions.matches(isDisplayed()));
    }

    /**
     * Test per comprovar que des d'una parada lliscant amb el dit a l'esquerra es canvia de parada
     * a l'anterior i si llavors llisquem a la dreta tornem a la parada inicial
     */
    @Test public void fromStopChangeStopToLeftAndRight() {
        onView(withId(R.id.show_lines_button))
                .perform(click());
        onView(withText("L5"))
                .perform(click());
        onView(withText("Collblanc"))
                .perform(click());
        onView(withId(R.id.nomParada))
                .perform(swipeLeft());
        onView(withText("Badal"))
                .perform(click());
        onView(withId(R.id.nomParada))
                .perform(swipeRight());
        onView(withText("Collblanc"))
                .check(ViewAssertions.matches(isDisplayed()));
    }

    /**
     * Test per comprovar que des d'una parada lliscant amb el dit a la dreta es canvia de parada a
     * seguent i si llavors llisquem a l'esquerra tornem a la parada inicial
     */
    @Test public void fromStopChangeStopToRightAndLeft() {
        onView(withId(R.id.show_lines_button))
                .perform(click());
        onView(withText("L5"))
                .perform(click());
        onView(withText("Collblanc"))
                .perform(click());
        onView(withId(R.id.nomParada))
                .perform(swipeRight());
        onView(withText("Pubilla Cases"))
                .perform(click());
        onView(withId(R.id.nomParada))
                .perform(swipeLeft());
        onView(withText("Collblanc"))
                .check(ViewAssertions.matches(isDisplayed()));
    }

    /**
     * Test per comprovar que les linies estan ordenades
     */
    @Test public void orderedLines() {
        onView(withId(R.id.show_lines_button))
                .perform(click());
        onView(withText("L2")).check(isBelow(withText("L1")));
        onView(withText("L3")).check(isBelow(withText("L2")));
        onView(withText("L4")).check(isBelow(withText("L3")));
        onView(withText("L5")).check(isBelow(withText("L4")));
        onView(withText("L9")).check(isBelow(withText("L5")));
        onView(withText("L10")).check(isBelow(withText("L9")));
        onView(withText("L11")).check(isBelow(withText("L10")));
        onView(withText("TRAMVIA BLAU")).check(isBelow(withText("L11")));
        onView(withText("FUNICULAR DE MONTJUÏC")).check(isBelow(withText("TRAMVIA BLAU")));
    }

    /**
     * Test per comprovar que les parades estan ordenades
     */
    @Test public void ordereStations() {
        onView(withId(R.id.show_lines_button))
                .perform(click());
        onView(withText("L3"))
                .perform(click());
        onView(withText("Palau Reial")).check(isBelow(withText("Zona Universitària")));
        onView(withText("Maria Cristina")).check(isBelow(withText("Palau Reial")));
        onView(withText("Les Corts")).check(isBelow(withText("Maria Cristina")));
        onView(withText("Plaça del Centre")).check(isBelow(withText("Les Corts")));
    }

    /**
     * Test per comprovar que surt la accessibilitat correcta que tenen les parades
     */
    @Test public void checkAdaptadaNoAdaptada() {
        onView(withId(R.id.show_lines_button))
                .perform(click());
        onView(withText("L4"))
                .perform(click());
        onView(withText("Verdaguer"))
                .perform(click());
        onView(withText("No adaptada"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(pressBack());
        onView(withText("Joanic"))
                .perform(click());
        onView(withText("Adaptada"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(pressBack());
        onView(withText("Maragall"))
                .perform(click());
        onView(withText("No adaptada"))
                .check(ViewAssertions.matches(isDisplayed()));
    }

    /**
     * Test per comprovar que surt la accessibilitat correcta que tenen les parades quan naveguem per una línia
     */
    @Test public void checkSwipeAdaptadaNoAdaptada() {
        onView(withId(R.id.show_lines_button))
                .perform(click());
        onView(withText("L4"))
                .perform(click());
        onView(withText("Verdaguer"))
                .perform(click());
        onView(withText("No adaptada"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(swipeLeft());
        onView(withText("Girona"))
                .check(ViewAssertions.matches(isDisplayed()));
        onView(withText("Adaptada"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(swipeLeft());
        onView(withText("Passeig de Gràcia"))
                .check(ViewAssertions.matches(isDisplayed()));
        onView(withText("Adaptada"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(swipeLeft());
        onView(withText("Urquinaona"))
                .check(ViewAssertions.matches(isDisplayed()));
        onView(withText("No adaptada"))
                .check(ViewAssertions.matches(isDisplayed()));
    }

    /**
     * Test per comprovar que surten les connexions de les parades
     */
    @Test public void checkStationConnections() {
        onView(withId(R.id.show_lines_button))
                .perform(click());
        onView(withText("L3"))
                .perform(click());
        onView(withText("Sants Estació"))
                .perform(click());
        onView(withText("L5"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(pressBack());
        onView(withText("Espanya"))
                .perform(click());
        onView(withText("L1"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(pressBack());
        /*onView(withText("Trinitat Nova"))
                .perform(click());
        onView(withText("L1\nL11\n"))
                .check(ViewAssertions.matches(isDisplayed()));*/
    }

    @Test public void checkNavigateStationConnections() {
        onView(withId(R.id.show_lines_button))
                .perform(click());
        onView(withText("L3"))
                .perform(click());
        onView(withText("Sants Estació"))
                .perform(click());
        onView(withText("L5"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(click());
        onView(withText("Sants Estació"))
                .perform(click());
        onView(withText("L3"))
                .check(ViewAssertions.matches(isDisplayed()));
    }

    @Test public void checkBackStationConnections() {
        onView(withId(R.id.show_lines_button))
                .perform(click());
        onView(withText("L3"))
                .perform(click());
        onView(withText("Sants Estació"))
                .perform(click());
        onView(withText("L5"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(click());
        onView(withText("Sants Estació"))
                .perform(click());
        onView(withText("L3"))
                .check(ViewAssertions.matches(isDisplayed()));
        onView(withText("Sants Estació"))
                .perform(swipeRight());
        onView(withText("Plaça de Sants"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(pressBack());
        onView(withText("L3"))
                .check(ViewAssertions.matches(isDisplayed()));
    }

    @Test public void checkMoreThanOneConnections() {
        onView(withId(R.id.show_lines_button))
                .perform(click());
        onView(withText("L3"))
                .perform(click());
        onView(withText("Sants Estació"))
                .perform(click());
        onView(withText("L5"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(click());
        onView(withText("Sants Estació"))
                .perform(click());
        onView(withText("L3"))
                .check(ViewAssertions.matches(isDisplayed()));
        onView(withText("Sants Estació"))
                .perform(swipeRight());
        onView(withText("Plaça de Sants"))
                .check(ViewAssertions.matches(isDisplayed()));
        onView(withText("L1"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(click());
        onView(withText("Plaça de Sants"))
                .check(ViewAssertions.matches(isDisplayed()));
        onView(withText("L5"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(pressBack());
        onView(withText("L3"))
                .check(ViewAssertions.matches(isDisplayed()));
    }

    @Test public void checkBackToYourStationAfterConnection() {
        onView(withId(R.id.show_lines_button))
                .perform(click());
        onView(withText("L5"))
                .perform(click());
        onView(withText("Collblanc"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(click());
        onView(withText("Collblanc"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(swipeLeft());
        onView(withText("Badal"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(swipeLeft());
        onView(withText("Plaça de Sants"))
                .check(ViewAssertions.matches(isDisplayed()));
        onView(withContentDescription("L1"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(click());
        onView(withText("Plaça de Sants"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(swipeLeft());
        onView(withText("Hostafrancs"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(swipeLeft());
        onView(withText("Espanya"))
                .check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.L3))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(click());
        onView(withText("Espanya"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(swipeRight());
        onView(withText("Tarragona"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(swipeRight());
        onView(withText("Sants Estació"))
                .check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.L5))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(click());
        onView(withText("Sants Estació"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(pressBack());
        onView(withText("L5"))
                .check(ViewAssertions.matches(isDisplayed()));
    }

    @Test public void checkFinalDeLinia() {
        onView(withId(R.id.show_lines_button))
                .perform(click());
        onView(withText("L4"))
                .perform(click());
        onView(withText("Trinitat Nova"))
                .perform(click());
        onView(withText("Final de línia"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(pressBack());
        onView(withId(R.id.paradesListView))
                .perform(swipeUp())
                .perform(swipeUp());
        onView(withText("La Pau"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(click());
        onView(withText("Final de línia"))
                .check(ViewAssertions.matches(isDisplayed()));
    }

    @Test public void checkFinalDeLiniaNavegant() {
        onView(withId(R.id.show_lines_button))
                .perform(click());
        onView(withText("L9"))
                .perform(click());
        onView(withText("Onze de Setembre"))
                .perform(click());
        onView(withText("Onze de Setembre"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(swipeRight());
        onView(withText("Final de línia"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(swipeRight());
        onView(withText("Final de línia"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(swipeLeft());
        onView(withText("Onze de Setembre"))
                .perform(swipeLeft());
        onView(withText("Bon Pastor"))
                .perform(swipeLeft());
        onView(withText("Can Peixauet"))
                .perform(swipeLeft());
        onView(withText("Fondo"))
                .perform(swipeLeft());
        onView(withText("Església Major"))
                .perform(swipeLeft());
        onView(withText("Singuerlín"))
                .perform(swipeLeft());
        onView(withText("Final de línia"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(swipeLeft());
        onView(withText("Final de línia"))
                .check(ViewAssertions.matches(isDisplayed()));
    }
}

