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
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.PositionAssertions.isBelow;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.core.StringStartsWith.startsWith;

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
        onView(withText("Joanic"))
                .perform(click());
        onView(withText("Adaptada"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(swipeLeft());
        onView(withText("Verdaguer"))
                .check(ViewAssertions.matches(isDisplayed()));
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

    @Test public void checkCreateRoute() {
        onView(withId(R.id.show_lines_button))
                .perform(click());
        onView(withText("L5"))
                .perform(click());
        onView(withText("Collblanc"))
                .perform(click());
        onView(withText("Començar ruta"))
                .perform(click());
        onView(withText("Collblanc"))
                .perform(swipeLeft());
        onView(withText("Badal"))
                .perform(swipeLeft());
        onView(withText("Plaça de Sants"))
                .perform(swipeLeft());
        onView(withText("Sants Estació"))
                .perform(swipeLeft());
        onView(withText("Entença"))
                .check(ViewAssertions.matches(isDisplayed()));
        onView(withText("Finalitzar ruta"))
                .perform(click());
        onView(withText("Començar ruta"))
                .check(ViewAssertions.matches(isDisplayed()));
    }

    @Test public void checkRouteCreated() {
        onView(withId(R.id.show_routes_button))
                .perform(click());
        onView(withText("De L5-Collblanc a L5-Entença"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(click());
        onView(withText("L5-Collblanc"))
                .check(ViewAssertions.matches(isDisplayed()));
        onView(withText("L5-Badal"))
                .check(ViewAssertions.matches(isDisplayed()));
        onView(withText("L5-Plaça de Sants"))
                .check(ViewAssertions.matches(isDisplayed()));
        onView(withText("L5-Sants Estació"))
                .check(ViewAssertions.matches(isDisplayed()));
        onView(withText("L5-Entença"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(pressBack());
        onView(withText("De L5-Collblanc a L5-Entença"))
                .check(ViewAssertions.matches(isDisplayed()));
    }

    /*@Test public void checkEliminarRuta() {
        onView(withId(R.id.show_routes_button))
                .perform(click());
        onView(withText("De L5-Collblanc a L5-Entença"))
                .check(ViewAssertions.matches(isDisplayed()));
        onData(hasToString(startsWith("De L5-Collblanc a L5-Entença")))
                .inAdapterView(withId(R.id.routesListView)).atPosition(0)
                .perform(click());
        onView(withId(R.id.deleteRouteClickable))
                .perform(click());
        onView(withText("ACCEPTAR"))
                .perform(click());
        onView(withText("De L5-Collblanc a L5-Entença"))
                .check(doesNotExist());
    }*/

    @Test public void checkCreateRouteDeletingLastStation() {
        onView(withId(R.id.show_lines_button))
                .perform(click());
        onView(withText("L3"))
                .perform(click());
        onView(withText("Palau Reial"))
                .perform(click());
        onView(withText("Començar ruta"))
                .perform(click());
        onView(withText("Palau Reial"))
                .perform(swipeLeft());
        onView(withText("Maria Cristina"))
                .perform(swipeLeft());
        onView(withText("Les Corts"))
                .perform(swipeLeft());
        onView(withText("Plaça del Centre"))
                .check(ViewAssertions.matches(isDisplayed()));
        onView(withText("Eliminar última parada afegida"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(click());
        onView(withText("ACCEPTAR"))
                .perform(click());
        onView(withText("Finalitzar ruta"))
                .perform(click());
        onView(withText("Les Corts"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(pressBack());
        onView(withText("L3"))
                .perform(pressBack())
                .perform(pressBack());
        onView(withId(R.id.show_routes_button))
                .perform(click());
        onView(withText("De L3-Palau Reial a L3-Les Corts"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(click());
        onView(withText("L3-Palau Reial"))
                .check(ViewAssertions.matches(isDisplayed()));
        onView(withText("L3-Maria Cristina"))
                .check(ViewAssertions.matches(isDisplayed()));
        onView(withText("L3-Les Corts"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(pressBack());
        onView(withText("De L3-Palau Reial a L3-Les Corts"))
                .check(ViewAssertions.matches(isDisplayed()));
    }

    @Test public void checkCreateRouteDeletingAllStations() {
        onView(withId(R.id.show_lines_button))
                .perform(click());
        onView(withText("L3"))
                .perform(click());
        onView(withText("Palau Reial"))
                .perform(click());
        onView(withText("Començar ruta"))
                .perform(click());
        onView(withText("Palau Reial"))
                .perform(swipeLeft());
        onView(withText("Maria Cristina"))
                .perform(swipeLeft());
        onView(withText("Les Corts"))
                .perform(swipeLeft());
        onView(withText("Plaça del Centre"))
                .check(ViewAssertions.matches(isDisplayed()));
        onView(withText("Eliminar última parada afegida"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(click());
        onView(withText("ACCEPTAR"))
                .perform(click());
        onView(withText("Eliminar última parada afegida"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(click());
        onView(withText("ACCEPTAR"))
                .perform(click());
        onView(withText("Eliminar última parada afegida"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(click());
        onView(withText("ACCEPTAR"))
                .perform(click());
        onView(withText("Eliminar última parada afegida"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(click());
        onView(withText("ACCEPTAR"))
                .perform(click());
        onView(withText("Començar ruta"))
                .check(ViewAssertions.matches((isDisplayed())));
    }

    @Test public void checkRatesBitlletSenzill() {
        onView(withId(R.id.show_rate_button))
                .perform(click());
        onView(withText("BitlletsSenzills"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(click());
        onView(withText("BitlletSenzill"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(click());
        onView(withText("2.15€"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(pressBack());
    }

    @Test public void checkRatesAbmesonamentsPerDies() {
        onView(withId(R.id.show_rate_button))
                .perform(click());
        onView(withText("AbonamentsPerDies"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(click());
        onView(withText("5Dies"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(click());
        onView(withText("32€"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(pressBack());
        onView(withText("4Dies"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(click());
        onView(withText("26.50€"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(pressBack());
        onView(withText("3Dies"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(click());
        onView(withText("20.50€"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(pressBack());
        onView(withText("2Dies"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(click());
        onView(withText("14€"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(pressBack());
    }
}

