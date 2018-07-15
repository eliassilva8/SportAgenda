package com.eliassilva.sportagenda;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Created by Elias on 14/07/2018.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class EventRecyclerViewTest {
    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class, false, true);
    public ActivityTestRule<NewEditEvent> activityTestRule2 = new ActivityTestRule<>(NewEditEvent.class, false, true);

    @Test
    public void displayEventItem() {
        onView(allOf(withId(R.id.rv_sport_tv), isDisplayed()));
        onView(allOf(withId(R.id.rv_date_tv), isDisplayed()));
        onView(allOf(withId(R.id.rv_time_tv), isDisplayed()));
        onView(allOf(withId(R.id.rv_local_tv), isDisplayed()));
        onView(allOf(withId(R.id.rv_participants_tv), isDisplayed()));
    }
}
