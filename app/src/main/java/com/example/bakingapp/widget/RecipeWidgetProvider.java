package com.example.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.bakingapp.MainActivity;
import com.example.bakingapp.Preferences;
import com.example.bakingapp.R;
import com.example.bakingapp.model.Recipe;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    // Constant for logging
    private static final String TAG = RecipeWidgetProvider.class.getSimpleName();



    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Recipe recipe = Preferences.loadRecipe(context);

        if (recipe != null) {
            Log.d(TAG, "Widget recipe: " + recipe.getName());
            CharSequence widgetText = recipe.getName();
            // Construct the RemoteViews object
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);
            views.setTextViewText(R.id.recipe_widget_name_text, widgetText);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);
            views.setOnClickPendingIntent(R.id.recipe_widget_name_text, pendingIntent);

            Intent intent = new Intent(context, RecipeWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

            views.setRemoteAdapter(R.id.recipe_widget_listview, intent);

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.recipe_widget_listview);
        } else {
            Log.d(TAG, "No recipe found!");
            CharSequence widgetText = context.getString(R.string.no_recipe_widget);
            // Construct the RemoteViews object
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);
            views.setTextViewText(R.id.recipe_widget_name_text, widgetText);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);
            views.setOnClickPendingIntent(R.id.recipe_widget_name_text, pendingIntent);

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    public static void updateAppWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

