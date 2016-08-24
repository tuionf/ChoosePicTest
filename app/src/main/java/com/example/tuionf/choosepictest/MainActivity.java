package com.example.tuionf.choosepictest;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ImageView mImageView;
    private Button takePhoto;
    private Button selectPhoto;
    private Uri imageUri;
    private static final String TAG = "MainActivity";

    public static final int TAKE_PHOTO = 1;
    public static final int CROP_PHOTO = 2;
    public static final int SELECT_PHOTO = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        takePhoto = (Button) findViewById(R.id.take_photo);
        mImageView = (ImageView) findViewById(R.id.imageview);
        selectPhoto = (Button) findViewById(R.id.select_photo);

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //创建file对象，用于存储拍照后的照片
                File outImage = new File(Environment.getExternalStorageDirectory(),"tempImg.png");

                try {
                    if (outImage.exists()){
                        outImage.delete();
                    }
                    //根据之前的路径，创建一个空文件，若成功返回true 否则false
                    outImage.createNewFile();
                    Log.d(TAG, "onClick: "+outImage.createNewFile());
                }catch (Exception e){
                    e.printStackTrace();
                }
                //Creates a Uri from a file. The URI has the form "file:// "
                imageUri = Uri.fromFile(outImage);

                //启动相机程序
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(intent,TAKE_PHOTO);

            }
        });

        selectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //创建file，存储图片
                File selectimg = new File(Environment.getExternalStorageDirectory(),"selectImg.png");
                if (selectimg.exists()){
                    selectimg.delete();
                }

                try {
                    selectimg.createNewFile();
                }catch (IOException e){
                    e.printStackTrace();
                }
                imageUri = Uri.fromFile(selectimg);
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");
                intent.putExtra("crop",true);
                intent.putExtra("scale",true);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(intent,SELECT_PHOTO);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case  TAKE_PHOTO:
                if (resultCode == RESULT_OK){
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(imageUri,"image/*");
                    intent.putExtra("scale",true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                    startActivityForResult(intent,CROP_PHOTO);
                }

               break;

            case  CROP_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        // 把InputStream转换成bitmap
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        //将裁减后的照片显示出来
                        mImageView.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                break;
            case SELECT_PHOTO:
                ContentResolver resolver = getContentResolver();
                //照片的原始资源地址
                Uri uri = data.getData();
                Log.d(TAG, "onActivityResult: "+uri);

                try {

                    Bitmap photo = MediaStore.Images.Media.getBitmap(resolver,uri);
                    if (photo != null){
                        mImageView.setImageBitmap(photo);
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            default:
                break;
        }
    }
}
