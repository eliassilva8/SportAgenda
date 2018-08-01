package com.eliassilva.sportagenda;

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
