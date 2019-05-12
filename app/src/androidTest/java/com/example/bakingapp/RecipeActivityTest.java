package com.example.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.bakingapp.model.Recipe;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.matcher.ViewMatchers.hasFocus;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.action.ViewActions.click;

@RunWith(AndroidJUnit4.class)
public class RecipeActivityTest {

    Recipe mRecipe = Preferences.loadRecipe(InstrumentationRegistry.getInstrumentation()
            .getTargetContext());

    @Rule
    public ActivityTestRule<RecipeActivity> mActivityTestRule =
            new ActivityTestRule<>(RecipeActivity.class);

    @Before
    public void loadIntentIntoActivity() {
        Context targetContext = InstrumentationRegistry.getInstrumentation()
                .getTargetContext();
        Intent result = new Intent(targetContext, RecipeActivity.class);
        result.putExtra("Recipe", mRecipe);

        mActivityTestRule.launchActivity(result);
    }

    @Test
    public void viewRecipeActivity_ContainsRecyclerViewWithIngredients() {
        // Check that the recipes list is displayed
        onView(withId(R.id.recyclerview_ingredients)).check(matches(isDisplayed()));
    }

    @Test
    public void viewRecipeActivity_ContainsImageView() {
        // Check that the recipes list is displayed
        onView(withId(R.id.iv_recipe)).check(matches(isDisplayed()));
    }

    @Test
    public void viewRecipeActivity_ContainsListWithSteps() {
        // Check that the recipes list is displayed
        onView(withId(R.id.step_list_container)).check(matches(isEnabled()));
    }
}



