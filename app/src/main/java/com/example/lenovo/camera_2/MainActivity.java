package com.example.lenovo.camera_2;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;


import android.widget.Button;
import android.widget.ImageView;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.os.Environment;
import android.view.Menu;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;



public class MainActivity extends AppCompatActivity {
    private Button btn, btn1, btn2;
    private ImageView img;
    private static final String TAG = "OCVSample::Activity";

    public static final int TAKE_PHOTO = 1;
    
    public static final int CHOOSE_PHOTO = 2;
    

    private ImageView picture;

    private Uri imageUri;

    public String getInnerSDCardPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.take_photo:
                take_photo();
                break;
            case R.id.choose_from_photos:
                choose_from_photos();
                break;
            case R.id.save_photo:
                save_photo();
                break;
            default:

        }
        return true;
    }

    private void save_photo(){
        img.setDrawingCacheEnabled(true);
        Bitmap bitmap1=img.getDrawingCache();
     //   img.setDrawingCacheEnabled(false);

        try {
            FileOutputStream b = null;
            String path = getInnerSDCardPath();
            File file = new File(path + "/DCIM/Camera/1");
            file.mkdirs();// 创建文件夹
            String fileName = path + "/DCIM/Camera/1/14.jpg";
            b = new FileOutputStream(fileName);
            bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, b);
            Toast.makeText(this, "saved successfully.", Toast.LENGTH_SHORT ).show();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        img.setDrawingCacheEnabled(false);


    }

    public static Bitmap compressImage(Bitmap image, int size, int options) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        image.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
        /*
        while (baos.toByteArray().length / 1024 > size) {
            options -= 10;// 每次都减少10
            baos.reset();// 重置baos即清空baos
            // 这里压缩options%，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);

        }
        */
        // 把压缩后的数据baos存放到ByteArrayInputStream中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        // 把ByteArrayInputStream数据生成图片
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        return bitmap;
    }

    private void take_photo(){
        File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(MainActivity.this, "com.example.lenovo.camera_2.fileprovider", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }
    
    public void choose_from_photos(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
//        startActivityForResult(intent, TAKE_PHOTO);
    }

/*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    choose_from_photos();
                }else{
                    Toast.makeText(this, "you denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
                default:
        }
    }
    */




    private void initView() {
        btn = (Button) findViewById(R.id.btn);
        btn1 = (Button) findViewById(R.id.btn1);
        img = (ImageView) findViewById(R.id.img);
        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(
                R.drawable.ic)).getBitmap();
        picture = (ImageView) findViewById(R.id.picture);
   //     img.setImageBitmap(bitmap);

        //img.setImageBitmap(bitmap);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "OpenCV loaded successfully");
                img.setDrawingCacheEnabled(true);
                Bitmap bitmap=img.getDrawingCache();
                //img.setDrawingCacheEnabled(false);
              //  Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(
              //          R.drawable.ic)).getBitmap();
                bitmap = compressImage(bitmap, 10, 100);
     //           String path = "/storage/extSdCard/DCIM/Camera/13.jpg";
     //           Bitmap bmp = BitmapFactory.decodeFile(path);

         //      img.setImageBitmap(bmp);
                //     img.setImageBitmap(bitmap);
                int w = bitmap.getWidth();
                int h = bitmap.getHeight();
                int[] pix = new int[w * h];
                bitmap.getPixels(pix, 0, w, 0, 0, w, h);
             //   Log.i(TAG, "OpenCV loaded successfully")


                int[] resultPixes = OpenCVHelper.gray(pix, w, h);
                Bitmap result = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
                result.setPixels(resultPixes, 0, w, 0, 0, w, h);
                img.setImageBitmap(result);

           //     int[] resultPixes = OpenCVHelper.gray(pix, w, h);
                Log.i(TAG, "OpenCV loaded successfully");
    //            String path1 = "/storage/extSdCard/DCIM/Camera/13.jpg";
  //              bmp = BitmapFactory.decodeFile(path1);

//                img.setImageBitmap(bmp);

/*
                Bitmap result = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
                result.setPixels(resultPixes, 0, w, 0, 0, w, h);
                img.setImageBitmap(result);
                */
                img.setDrawingCacheEnabled(false);
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT >= 24) {
                    imageUri = FileProvider.getUriForFile(MainActivity.this, "com.example.lenovo.camera_2.fileprovider", outputImage);
                } else {
                    imageUri = Uri.fromFile(outputImage);
                }
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, TAKE_PHOTO);
            }
        });




    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    FileOutputStream b = null;
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));

                        String path = getInnerSDCardPath();
                        File file = new File(path + "/DCIM/Camera");
                        file.mkdirs();// 创建文件夹
                        String fileName = path + "/DCIM/Camera/13.jpg";
                        b = new FileOutputStream(fileName);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, b);
                        img.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    /*
                    finally{
                        try{

                            b.flush();
                            b.close();
                        }catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    */
                }
                break;
            case CHOOSE_PHOTO:
                if(resultCode == RESULT_OK){
                    handleImageOnKitKat(data);
                }
                break;
            default:
                break;
        }
    }



    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(this, uri)){
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.example.lenovo.camera_2".equals(uri.getAuthority())){
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            }else if("com.example.lenovo.camera_2".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }

        }else if("content".equalsIgnoreCase(uri.getScheme())){
            imagePath = getImagePath(uri, null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection){
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath){
        if(imagePath != null){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            img.setImageBitmap(bitmap);
        }else{
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT ).show();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
