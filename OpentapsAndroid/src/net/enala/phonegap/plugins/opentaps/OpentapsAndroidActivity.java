package net.enala.phonegap.plugins.opentaps;

import org.apache.cordova.DroidGap;

import android.os.Bundle;

public class OpentapsAndroidActivity extends DroidGap {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.loadUrl("file:///android_asset/www/index.html");
    }
}