package com.example.bakingapp;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.rule.ActivityTestRule;
import android.support.test.espresso.contrib.RecyclerViewActions;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.bakingapp", appContext.getPackageName());
    }

    @Test
    public void viewMainActivity_ContainsRecyclerViewWithRecipes() {
        // Check that the recipes list is displayed
        onView(withId(R.id.recyclerview_recipes)).check(matches(isDisplayed()));
    }

    @Test
    public void clickMainActivityRecyclerViewItem_OpensRecipeActivity() {
        // Click the first Recipe in the main activity
        onView(withId(R.id.recyclerview_recipes))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        // Check that the ingredients list is displayed
        onView(withId(R.id.recyclerview_ingredients)).check(matches(isDisplayed()));
        // Check that the image is displayed
        onView(withId(R.id.iv_recipe)).check(matches(isDisplayed()));
    }

    @Test
    public void clickMainActivityRecyclerViewItem_HasIntentWithKey() {
        //Check if the key is present
        Intents.init();
        // Click the first Recipe in the main activity
        onView(withId(R.id.recyclerview_recipes))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        // Check that the RecipeActivity that is opened has the correct Recipe extra passed to it
        intended(hasExtraWithKey("Recipe"));
    }

}
