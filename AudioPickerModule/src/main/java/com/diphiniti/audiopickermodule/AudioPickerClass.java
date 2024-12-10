package com.diphiniti.audiopickermodule;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.app.Activity;

public class AudioPickerClass{
    private static final AudioPickerClass instance = new AudioPickerClass();
    public static AudioPickerClass getInstance(){ return instance;}
    public static final String LOGTAG = "Diphiniti";
    private AudioPickerClass(){
        Log.i(LOGTAG, "plugin created");
    }

    public Intent GetIntent(Activity activity){
        //return new Intent("com.diphiniti.audiopickermodule.AudioPickerActivity");
        //return new Intent(activity.getApplicationContext(), cls);
        return new Intent(activity.getApplicationContext(), AudioPickerActivity.class);
    }

    public void StartAudioPicker(Activity activity, String outputFolder){
        Intent intent = new Intent(activity.getApplicationContext(), AudioPickerActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString("output", outputFolder);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    public void LogError(String err){
        Log.i(LOGTAG, err);
    }
    public void openFile(Activity activity) {
        //Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        //intent.addCategory(Intent.CATEGORY_OPENABLE);
        //intent.setType("application/pdf");

        //Intent intent = new Intent("com.diphiniti.audiopickermodule.AudioPickerActivity.pickAudio");

        PackageManager pm = activity.getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage("com.diphiniti.audiopickermodule.AudioPickerActivity");

        //Intent intent = new Intent(activity.getApplicationContext(), AudioPickerActivity.class);

        ////Optionally, specify a URI for the file that should appear in the
        ////system file picker when it loads.
        //intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);

        Log.i(LOGTAG, "starting Pdf Activity. AudioRequest: " + AudioPickerActivity.PICK_AUDIO_REQUEST);
        //startActivityForResult(activity, intent, PICK_PDF_FILE, ActivityOptions.makeBasic().toBundle());
        //activity.startActivityForResult(intent, PICK_PDF_FILE, ActivityOptions.makeBasic().toBundle());
        activity.startActivity(intent);
    }
}
