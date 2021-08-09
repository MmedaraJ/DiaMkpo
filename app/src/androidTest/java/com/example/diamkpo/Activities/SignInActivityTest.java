package com.example.diamkpo.Activities;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.diamkpo.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

public class SignInActivityTest {
    @Rule
    public ActivityScenarioRule<MainActivity> mainActivityActivityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() {
    }

    @Test
    public void testCustomerSignInTabOpens(){
        onView(withId(R.id.tabLayout)).perform(click());
    }

    @After
    public void tearDown() {
    }
}