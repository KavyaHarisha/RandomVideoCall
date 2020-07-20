package com.randomvideocall

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
class VideoViewActivityAndroidTest {
    @Rule
    @JvmField
    var activityRule: ActivityTestRule<VideoViewActivity> = ActivityTestRule(VideoViewActivity::class.java)

    @Test
    fun randomCallIsDisplay() {
        onView(withId(R.id.btn_random_call)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_random_call)).check(matches(withText("Random call")))
    }

    @Test
    fun randomCallPerformClick(){
        onView(withId(R.id.btn_end_call)).check(matches(not(isDisplayed())))
        onView(withId(R.id.video_frame_container)).check(matches(not(isDisplayed())))
        onView(withId(R.id.btn_random_call)).perform(click())
        onView(withId(R.id.video_frame_container)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_end_call)).check(matches(isDisplayed()))
        onView(withId(R.id.local_video_view_container)).check(matches(isDisplayed()))
    }

    @Test
    fun endCallIsDisplay(){
        onView(withId(R.id.btn_end_call)).check(matches(not(isDisplayed())))
        onView(withId(R.id.btn_random_call)).perform(click())
        onView(withId(R.id.btn_end_call)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_end_call)).check(matches(withText("End call")))
    }
}
