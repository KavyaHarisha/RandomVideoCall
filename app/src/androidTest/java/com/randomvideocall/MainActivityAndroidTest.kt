package com.randomvideocall

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityAndroidTest {
    @Rule
    @JvmField
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Test
    fun getStaredIsDisplay() {
        onView(withId(R.id.btn_get_started)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_get_started)).check(matches(withText("Get started")));
        onView(withId(R.id.btn_get_started)).perform(click())
    }

    @Test
    fun adsViewIsDisplay() {
        onView(withId(R.id.ad_view)).check(matches(isDisplayed()))
    }
}
