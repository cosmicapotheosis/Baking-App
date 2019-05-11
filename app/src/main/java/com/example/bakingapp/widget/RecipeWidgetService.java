package com.example.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViewsService;

import com.example.bakingapp.Preferences;
import com.example.bakingapp.model.Recipe;

public class RecipeWidgetService extends RemoteViewsService {

    public static void updateWidget(Context context, Recipe recipe) {
        Preferences.saveRecipe(context, recipe);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, RecipeWidgetProvider.class));
        RecipeWidgetProvider.updateAppWidgets(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return null;
    }
}
