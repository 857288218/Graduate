package com.example.rjq.myapplication.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by Fsh on 2016/12/28.
 */

public class FileStorage {
    private File cropIconDir;
    private File iconDir;

    public FileStorage() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File external = Environment.getExternalStorageDirectory();
            String rootDir = "/" + "demo";
            cropIconDir = new File(external, rootDir + "/crop");
            if (!cropIconDir.exists()) {
                cropIconDir.mkdirs();

            }
            iconDir = new File(external, rootDir + "/icon");
            if (!iconDir.exists()) {
                iconDir.mkdirs();

            }
        }
    }

    public File createCropFile() {
        String fileName = "";
        if (cropIconDir != null) {
            fileName = CommonUtils.GenerateUUID() + ".png";
        }
        return new File(cropIconDir, fileName);
    }

    public File createIconFile() {
        String fileName = "";
        if (iconDir != null) {
            fileName = CommonUtils.GenerateUUID() + ".png";
        }
        return new File(iconDir, fileName);
    }

    public static String getRealFilePath(final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    public static void saveDataToFile(File fileToWrite, String data, boolean appand) {
        FileOutputStream fOut = null;
        OutputStreamWriter myOutWriter = null;
        try {
            if (!fileToWrite.exists()) {
                File parentFile = fileToWrite.getParentFile();
                parentFile.mkdirs();
                fileToWrite.createNewFile();
            }
            fOut = new FileOutputStream(fileToWrite, appand);
            myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(data);
            myOutWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fOut != null) {
                try {
                    fOut.close();
                } catch (IOException e) {
                }
            }
            if (myOutWriter != null) {
                try {
                    myOutWriter.close();
                } catch (IOException e) {
                }
            }
        }
    }

}
