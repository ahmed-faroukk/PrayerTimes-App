package com.example.alamiya_task.presentation.countdownWidget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.alamiya_task.R
import java.util.concurrent.TimeUnit

/**
 * Implementation of App Widget functionality.
 */
class countdownWidget : AppWidgetProvider() {
    companion object {
        private const val ACTION_UPDATE_COUNTDOWN = "action_update_countdown"
        private const val COUNTDOWN_DURATION_MS = 60000L // 1 minute in milliseconds
    }
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray,
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if (ACTION_UPDATE_COUNTDOWN == intent?.action) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds =
                appWidgetManager.getAppWidgetIds(context?.let {
                    ComponentName(
                        it,
                        countdownWidget::class.java
                    )
                })
            for (appWidgetId in appWidgetIds) {
                context?.let { updateAppWidget(it, appWidgetManager, appWidgetId) }
            }
        }
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int,
) {
    // Calculate the time remaining for the countdown
    val COUNTDOWN_DURATION_MS = 60000L // 1 minute in milliseconds
    val currentTimeMillis = System.currentTimeMillis()
    val endTimeMillis = currentTimeMillis + COUNTDOWN_DURATION_MS
    val timeRemaining = endTimeMillis - currentTimeMillis

    // Convert time remaining to HH:mm:ss format
    val hours = TimeUnit.MILLISECONDS.toHours(timeRemaining)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(timeRemaining) % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(timeRemaining) % 60


    val widgetText = context.getString(R.string.appwidget_text)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.countdown_widget)
    views.setTextViewText(
        R.id.countDown,
        String.format("%02d:%02d:%02d", hours, minutes, seconds)
    )
    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}