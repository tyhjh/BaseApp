package com.yorhp.tyhjlibrary.util.camera;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.yorhp.tyhjlibrary.R;
import com.yorhp.tyhjlibrary.util.file.FileUtil;
import com.yorhp.tyhjlibrary.util.file.GetImagePath;

import java.io.File;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static android.content.Intent.ACTION_GET_CONTENT;
import static com.yorhp.tyhjlibrary.view.permisson.PermissonUtil.*;

/**
 * Created by Tyhj on 2017/8/29.
 */

public class CameraUtil {

    public static String date;
    public static String path;


    //选择图片
    public static void choosePicture(final Context context) {
        path = context.getExternalCacheDir().getPath();
        Button camoral, images;
        final AlertDialog.Builder di;
        di = new AlertDialog.Builder(context);
        di.setCancelable(true);
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.dialog_headchoose, null);
        di.setView(layout);
        final Dialog dialog = di.show();
        camoral = (Button) layout.findViewById(R.id.camoral);
        images = (Button) layout.findViewById(R.id.images);


        // 相机
        camoral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate();
                dialog.cancel();
                File file = new File(path, date);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//7.0及以上
                    Uri uriForFile = FileProvider.getUriForFile(context, "com.yorhp.fileProvider", file);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uriForFile);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                } else {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                }
                ((Activity)context).startActivityForResult(intent, TAKE_PHOTO);
            }
        });
        // 相册
        images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    return;
                }

                getDate();
                dialog.cancel();
                Intent intent = new Intent(ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                intent.putExtra("crop", true);
                intent.putExtra("scale", true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//如果大于等于7.0使用FileProvider
                    Uri uriForFile = FileProvider.getUriForFile(context, "com.yorhp.fileProvider", new File(path,date));
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uriForFile);
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } else {

                }
                ((Activity)context).startActivityForResult(intent, PICK_PHOTO);
            }
        });
    }


    //随机获取文件名字
    public static void getDate() {
        date = System.currentTimeMillis() + ".JPEG";
    }

    //剪裁图片
    public static void cropPhoto(Uri imageUri, Context context) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            File file = new File(CameraUtil.path, CameraUtil.date);
            Uri outPutUri = FileProvider.getUriForFile(context, "com.yorhp.fileProvider", file);
            intent.setDataAndType(FileProvider.getUriForFile(context, "com.yorhp.fileProvider", file), "image/*");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutUri);
            intent.putExtra("crop", true);
            intent.putExtra("noFaceDetection", false);//去除默认的人脸识别，否则和剪裁匡重叠
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            intent.setDataAndType(imageUri, "image/*");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        }
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        ((Activity)context).startActivityForResult(intent, CROP_PHOTO);
    }

    //获取返回值
    public static void getPhoto(int requestCode, int resultCode, Context context, Intent data, CamerabakListener getFile){
        switch (requestCode) {
            //这是从相机返回的数据
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    File file = new File(CameraUtil.path, CameraUtil.date);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Uri inputUri = FileProvider.getUriForFile(context, "com.yorhp.fileProvider", file);//通过FileProvider创建一个content类型的Uri
                        cropPhoto(inputUri,context);
                    } else {
                        cropPhoto(Uri.fromFile(file),context);
                    }
                }
                break;
            //这是从相册返回的数据
            case PICK_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//如果大于等于7.0使用FileProvider
                        String path_pre=GetImagePath.getPath(context, data.getData());
                        File newFile = new File(CameraUtil.path, CameraUtil.date);
                        Toast.makeText(context, context.getString(R.string.msg_loading), Toast.LENGTH_SHORT).show();
                        try {
                            FileUtil.copyFile(new File(path_pre), newFile);
                            Uri dataUri = FileProvider.getUriForFile(context, "com.yorhp.fileProvider", newFile);
                            CameraUtil.cropPhoto(dataUri,context);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        String path_pre = GetImagePath.getPath(context, data.getData());
                        File newFile = new File(CameraUtil.path, CameraUtil.date);
                        Toast.makeText(context, context.getString(R.string.msg_loading), Toast.LENGTH_SHORT).show();
                        try {
                            FileUtil.copyFile(new File(path_pre), newFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        cropPhoto(Uri.fromFile(newFile),context);
                    }

                }

                break;
            //剪裁图片返回数据,就是原来的文件
            case CROP_PHOTO:
                if (resultCode == RESULT_OK) {
                    String fileName = CameraUtil.path + "/" + CameraUtil.date;
                    File newFile = new File(CameraUtil.path, CameraUtil.date);
                    FileUtil.ImgCompress(fileName, newFile, 500, 500, 300);
                    //获取到的就是new File或fileName
                    getFile.getFile(newFile);
                }

                break;
            default:
                break;
        }
    }

    public interface CamerabakListener{
        void getFile(File file);
    }
    
}
