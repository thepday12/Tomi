/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.neu.tomi.gcm;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.neu.tomi.ultity.Global;
import com.neu.tomi.ultity.SqliteHelper;

import org.json.JSONException;
import org.json.JSONObject;


public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Log.d(TAG, "RESPONSE_SERVER_From: " + from);
        Log.d(TAG, "RESPONSE_SERVER_Message: " + message);

//        if (from.startsWith("/topics/")) {
//            // message received from some topic.
//        } else {
//            // normal downstream message.
//        }


        sendNotification(message);
        // [END_EXCLUDE]
    }
    // [END receive_message]il

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonObject != null) {
            try {
                if (jsonObject.getBoolean("state")) {
                    SqliteHelper sqliteHelper = SqliteHelper.getInstanceSQLiteHelper(getBaseContext());
                    String id = jsonObject.getString("id");
                    String title = jsonObject.getString("title");
                    String content = jsonObject.getString("content");
                    String link = jsonObject.getString("link");
                    int linkType = jsonObject.getInt("link_type");
                    sqliteHelper.insertMessage(id, title, content, link,linkType);
                    Global.showNotification(getBaseContext(), title, content, id);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
