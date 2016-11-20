package ncu.aaron.savefiles;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "Aaron_Yu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_main);
    }

    private void saveFilesOnInternalStorage(String file_content) {
        String fileName = "internalFile";
        File file = new File(getFilesDir(), fileName);

        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(fileName, MODE_PRIVATE);
            outputStream.write(file_content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveCacheFileOnInternalStorage() {
        String fileName = "internalCacheFile";
        try {
            File file = File.createTempFile(fileName, null, getCacheDir());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getPublicFileOnExternalStorage(String fileName) {
        File file = new File(Environment.getExternalStoragePublicDirectory(null), fileName);
        if (!file.mkdirs()) {
            Log.e(LOG_TAG, "Directory not created.");
        }

        return file;
    }


    private void savePrivateFileOnExternalStorage(String fileName) {
        File file = new File(getExternalFilesDir(null), fileName);
        if (!file.mkdir()){
            Log.e(LOG_TAG, "Directory not created.");
        }
    }

    private boolean isExternalStorageWritable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    private boolean isExternalStorageReadable() {
        return Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState());
    }

    private boolean isExternalStorageUsable() {
        return isExternalStorageReadable() || isExternalStorageWritable();
    }
}
