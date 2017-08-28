/*
 * Copyright 2017 Google Inc.
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

package gq.youn.hellogoodbye.feature;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.instantapps.InstantApps;
import com.google.firebase.appindexing.Action;
import com.google.firebase.appindexing.FirebaseAppIndex;
import com.google.firebase.appindexing.FirebaseUserActions;
import com.google.firebase.appindexing.Indexable;
import com.google.firebase.appindexing.builders.Actions;

/**
 * This Activity displays a simple hello world text and a button to open the GoodbyeActivity.
 */
public class HelloActivity extends AppCompatActivity {

    private static final String TAG = HelloActivity.class.getSimpleName();
    private static final int SHOW_INSTALL_PROMPT = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HelloActivity.this, GoodbyeActivity.class));
            }
        });
        if (InstantApps.isInstantApp(this)) {
            if (isAppInstalled(this, BuildConfig.APPLICATION_ID)) {
                findViewById(R.id.install_button).setVisibility(View.INVISIBLE);
            }
            findViewById(R.id.install_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InstantApps.showInstallPrompt(HelloActivity.this, SHOW_INSTALL_PROMPT, TAG);
                }
            });
        } else {
            findViewById(R.id.install_button).setVisibility(View.INVISIBLE);
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private Action getIndexApiAction() {
        // ATTENTION: This was auto-generated to handle app links.
        return Actions.newView(getIndexName(), getIntent().getData().toString());
    }

    private String getIndexName() {
        String indexPath = getIntent().getData().getPath();
        if (indexPath.equals("/hello")) {
            return "Hello";
        } else {
            return "Home";
        }
    }

    private String getIndexUrl() {
        Uri appLinkData = getIntent().getData();
        if (appLinkData.getPath() == null) {
            return appLinkData.toString();
        } else if (appLinkData.getPath().equals("/hello")) {
            return appLinkData.toString();
        } else {
            return appLinkData.toString();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getIntent().getData() == null) {
            return;
        }

        if (!InstantApps.isInstantApp(this)) {
            return;
        }

        Log.d(TAG, "App Indexing API: getIndexName() :" + getIndexName() + " ,getIndexUrl() : " + getIndexUrl() + " ,getIndexApiAction() :" + getIndexApiAction());
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Task<Void> task = FirebaseAppIndex.getInstance().update(new Indexable.Builder().setName(getIndexName()).setUrl(getIndexUrl()).build());
        // scheduled
        task.addOnSuccessListener(HelloActivity.this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "App Indexing API: Successfully added " + TAG + " to index");
            }
        });

        task.addOnFailureListener(HelloActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e(TAG, "App Indexing API: Failed to add " + TAG + " to index. " + exception.getMessage());
            }
        });
        Task<Void> actionTask = FirebaseUserActions.getInstance().start(getIndexApiAction());
        actionTask.addOnSuccessListener(HelloActivity.this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "App Indexing API: Successfully started view action on " + TAG);
            }
        });

        actionTask.addOnFailureListener(HelloActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e(TAG, "App Indexing API: Failed to start view action on " + TAG + ". "
                        + exception.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == SHOW_INSTALL_PROMPT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.

                // Do something with the contact here (bigger example below)
                Toast.makeText(HelloActivity.this, "App Install Successed", Toast.LENGTH_SHORT);
            } else {
                Toast.makeText(HelloActivity.this, "App Install Failed", Toast.LENGTH_SHORT);
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (getIntent().getData() == null) {
            return;
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Task<Void> actionTask = FirebaseUserActions.getInstance().end(getIndexApiAction());
        actionTask.addOnSuccessListener(HelloActivity.this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "App Indexing API: Successfully ended view action on " + TAG);
            }
        });

        actionTask.addOnFailureListener(HelloActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e(TAG, "App Indexing API: Failed to end view action on " + TAG + ". "
                        + exception.getMessage());
            }
        });
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        }
        catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
