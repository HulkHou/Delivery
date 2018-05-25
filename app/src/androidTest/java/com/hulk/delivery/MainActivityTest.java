package com.hulk.delivery;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;


/**
 * Created by hulk-out on 2018/5/4.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    private String result;

    @Before
    public void initString() {
        result = "Clicked";
    }

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Test
    public void changeTextTest() {
        ViewInteraction view = Espresso.onView(withId(R.id.bottom_navigation_bar));
        view.check(matches(not(isDisplayed())));
    }

}
