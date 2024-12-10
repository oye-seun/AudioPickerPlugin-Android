package com.diphiniti.audiopickermodule;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;

import com.unity3d.player.UnityPlayer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AudioPickerActivity extends Activity {

    //ActivityResultLauncher<Intent> resultLauncher;
    public static final int PICK_AUDIO_REQUEST = 1;
    private String rootFolderPath;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_AUDIO_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri audioUri = data.getData();

            // Handle the picked audio file URI
            Log.i(AudioPickerClass.LOGTAG, "Picked URI: " +  audioUri.toString());
            String folderPath = CopyAudio(audioUri, rootFolderPath);
            UnityPlayer.UnitySendMessage("Game Manager", "OnAudioSelectionFinished", folderPath);
        }
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(AudioPickerClass.LOGTAG, "AudioActivity created");
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        rootFolderPath = bundle.getString("output");

        pickAudio();
    }

    public void pickAudio()
    {
        //Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        //startActivityForResult(intent, PICK_AUDIO_REQUEST);

        Intent intent = new Intent();
        intent.setType("audio/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Audio "), PICK_AUDIO_REQUEST);
    }

    public String CopyAudio(Uri uri, String folder){
        ContentResolver resolver = getContentResolver();

        String filename = queryName(resolver, uri);
        int periodPos = findLastIndex(filename, '.');
        filename = filename.substring(0,periodPos);
        String folderPath = folder + "/" + filename;
        File dir = new File(folderPath);
        if(!dir.exists()) dir.mkdir();

        String filePath = folderPath + "/A.mp3";
        Log.i(AudioPickerClass.LOGTAG, "Output Filepath: " + folderPath);

        try {copy(uri, filePath);}
        catch(IOException e) {e.printStackTrace();}

        return folderPath;
    }

    public void copy(Uri uri, String dst) throws IOException {
        try (InputStream in = getContentResolver().openInputStream(uri)) {
            try (OutputStream out = new FileOutputStream(dst)) {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
        }
    }

    private String queryName(ContentResolver resolver, Uri uri) {
        Cursor returnCursor =
                resolver.query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }

    private static int findLastIndex(String str, Character x)
    {
        int index = -1;
        for (int i = 0; i < str.length(); i++)
            if (str.charAt(i) == x)
                index = i;
        return index;
    }

//    public void pickAudio(){
//        Log.i(AudioPickerClass.LOGTAG, "Pick Audio called");
//
////        resultLauncher =  registerForActivityResult(
////            new ActivityResultContracts.StartActivityForResult(),
////            new ActivityResultCallback<ActivityResult>(){
////                @Override
////                public void onActivityResult(ActivityResult res){
////                    Intent data = res.getData();
////                    Uri uri = data.getData();
////                    if(data != null){
////                        Log.i(AudioPickerClass.LOGTAG, "Picked URI: " +  uri.toString());
////                    }
////                }
////            }
////        );
//
////        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
////        intent.setType("application/pdf");
////        resultLauncher.launch(intent);
//
//        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("application/pdf");
//
//        startActivityForResult(intent, PICK_AUDIO_REQUEST, ActivityOptions.makeBasic().toBundle());
//    }
}
