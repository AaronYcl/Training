package ncu.aaron.savefiles;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String LOG_TAG = "Training Log Tag";
    private static final int REQUEST_WRITE_ON_EXTERNAL_STORAGE = 0x01;
    private EditText mEtContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_main);
        mEtContent = (EditText) findViewById(R.id.et_text);
        findViewById(R.id.btn_save_internal_file).setOnClickListener(this);
        findViewById(R.id.btn_save_internal_cache_file).setOnClickListener(this);
        findViewById(R.id.btn_save_external_private_file).setOnClickListener(this);
        findViewById(R.id.btn_save_external_public_file).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String content = mEtContent.getText().toString().trim();
        switch (view.getId()) {
            case R.id.btn_save_internal_file:
                saveFilesOnInternalStorage(content);
                break;
            case R.id.btn_save_internal_cache_file:
                saveCacheFileOnInternalStorage(content);
                break;
            case R.id.btn_save_external_private_file:
                if (isExternalStorageWritable()) {
                    if (PermissionUtil.isGranted(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        savePrivateFileOnExternalStorage(content);
                    } else {
                        PermissionUtil.requestPermissions(this, REQUEST_WRITE_ON_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    }
                }
                break;
            case R.id.btn_save_external_public_file:
                if (isExternalStorageWritable()) {
                    if (PermissionUtil.isGranted(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        savePublicFileOnExternalStorage(content);
                    } else {
                        PermissionUtil.requestPermissions(this, REQUEST_WRITE_ON_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    }
                }
                break;
        }
    }

    private void saveFilesOnInternalStorage(String text) {
        String fileName = "internalFile";
        File file = new File(getFilesDir(), fileName);

        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(fileName, MODE_PRIVATE);
            outputStream.write(text.getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "saveFilesOnInternalStorage failed.");
        }
    }

    private void saveCacheFileOnInternalStorage(String text) {
        String fileName = "internalCacheFile";
        FileOutputStream outputStream;
        try {
            File file = File.createTempFile(fileName, null, getCacheDir());
            outputStream = new FileOutputStream(file);
            outputStream.write(text.getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "saveCacheFileOnInternalStorage failed.");
        }
    }

    private void savePublicFileOnExternalStorage(String text) {
        String dirName = "PublicFiles";
        File root = Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsoluteFile() + "/" + dirName);
//        File dir = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES), dirName);
        if (dir.exists() || dir.mkdirs()) {
            Toast.makeText(this, dir.getAbsolutePath(), Toast.LENGTH_LONG).show();
            File file = new File(dir, "public_file.txt");
            FileOutputStream outputStream;
            try {
                outputStream = new FileOutputStream(file);
                outputStream.write(text.getBytes());
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(LOG_TAG, "savePublicFileOnExternalStorage failed.");
            }
        } else {
            Log.e(LOG_TAG, "Directory not created.");
            Toast.makeText(this, "Directory not created.", Toast.LENGTH_LONG).show();
        }
    }


    private void savePrivateFileOnExternalStorage(String text) {
        File dir = new File(getExternalFilesDir(null), "PrivateFiles");
        if (!dir.mkdir()) {
            Log.e(LOG_TAG, "Directory not created.");
        }
        File file = new File(dir, "private_file.txt");
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(text.getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "savePrivateFileOnExternalStorage failed.");
        }
    }

    /* Checks if external storage is available for read and write */
    private boolean isExternalStorageWritable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /* Checks if external storage is available to at least read */
    private boolean isExternalStorageReadable() {
        return Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState());
    }

}
