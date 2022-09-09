package com.kkkkkn.kdraw;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.kkkkkn.signatureView.SignView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author kkkkkn
 */
public class MainActivity extends AppCompatActivity {
    private SignView signView;
    private AppCompatButton btnClear, btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signView = findViewById(R.id.sign_view);
        btnClear = findViewById(R.id.btn_cancel);
        btnOk = findViewById(R.id.btn_ok);

        btnClear.setOnClickListener(view -> signView.clear());

        btnOk.setOnClickListener(view -> {
            //signature.png
            File folder = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES),"images");
            if (!folder.mkdirs()) {
                Toast.makeText(getApplication(), "创建目录失败", Toast.LENGTH_SHORT).show();
            }
            Bitmap bitmap = signView.getSignatureBitmap();
            boolean flag = saveImg(bitmap, folder.getPath() + "/signature.png");
            if (flag) {
                String str = "保存签字图像" + folder.getPath() + "/signature.png" + "成功";
                Toast.makeText(getApplication(), str, Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getApplication(), "保存失败", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private boolean saveImg(Bitmap bitmap, String path) {
        if (bitmap == null) {
            return false;
        }
        File saveFile = new File(path);
        try {
            FileOutputStream saveImgOut = new FileOutputStream(saveFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, saveImgOut);
            saveImgOut.flush();
            saveImgOut.close();
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

}