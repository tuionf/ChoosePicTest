# ChoosePicTest
第一行代码的拍照和选择图库照片，有改动

# 演示 
![yanshi](http://upload-images.jianshu.io/upload_images/19590-b4bf655107076782.gif)

#  笔记 
点击拍照

	1. 创建file存储图片


File outImage = new File(Environment.getExternalStorageDirectory(),"tempImg.png");

	1. 根据之前的路径，创建一个空文件


outImage.createNewFile();


	1. 将file文件转换成Uri—— 形式：file://


imageUri = Uri.fromFile(outImage);


	1. 启动相机


//启动相机程序
Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
startActivityForResult(intent,TAKE_PHOTO);


或者

	1. 打开相册


Intent intent = new Intent("android.intent.action.GET_CONTENT");
intent.setType("image/*");
intent.putExtra("crop",true);
intent.putExtra("scale",true);
intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
startActivityForResult(intent,SELECT_PHOTO);



选择图库相片
1,2,3步骤同上

	1. 设置intent


Intent intent = new Intent("android.intent.action.GET_CONTENT");
intent.setType("image/*");
intent.putExtra("crop",true);
intent.putExtra("scale",true);
intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
startActivityForResult(intent,SELECT_PHOTO);





onActivityResult处理——注意两处获取bitmap的方式是不一样的
拍照和裁剪 

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


选择并显示

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

