/*
 * Copyright (C) 2011 nagamatu@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gmail.nagamatu.sample.voicewidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.speech.RecognizerIntent;
import android.widget.RemoteViews;

import java.util.List;

public class VoiceWidget extends AppWidgetProvider
{
    private static SharedPreferences sPrefs;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        
        if (sPrefs == null) {
            sPrefs = context.getSharedPreferences(VoiceActivity.PREFS_NAME, VoiceActivity.MODE_PRIVATE);
        }
        
        String result = sPrefs.getString(VoiceActivity.PREFS_VOICE_RECOGNITION_RESULT, null);
        if (result == null) {
            result = "";
        }

        final int N = appWidgetIds.length;

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, VoiceActivity.class);
            intent.setData(Uri.parse(String.valueOf(appWidgetId)));
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // Get the layout for the App Widget and attach an on-click listener to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.voice_widget);
            views.setOnClickPendingIntent(R.id.VoiceButton, pendingIntent);
            views.setTextViewText(R.id.message, result);

            // Check to see if a recognition activity is present
            final PackageManager pm = context.getPackageManager();
            final List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
            if (activities.size() != 0) {
                views.setOnClickPendingIntent(R.id.VoiceButton, pendingIntent);
            }
            
            // Tell the AppWidgetManager to perform an update on the current App Widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}