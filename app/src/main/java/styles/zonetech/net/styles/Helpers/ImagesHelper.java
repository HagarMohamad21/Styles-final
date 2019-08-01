package styles.zonetech.net.styles.Helpers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import styles.zonetech.net.styles.R;
import styles.zonetech.net.styles.Utils.Common;

public class ImagesHelper {
    String currentPhotoPath;

    
    static final int REQUEST_IMAGE_CAPTURE  = 1;
    static final int REQUEST_IMAGE_GALLERY = 2;

    Context mContext;
Intent intent;

    public ImagesHelper(Context mContext) {
        this.mContext = mContext;
    }

    public Intent selectImage(String action) {
        if(action.equals(mContext.getString(R.string.camera))){
            intent=openCamera();
        }
        else if(action.equals(mContext.getString(R.string.gallery))){
            intent=openGallery();
        }
    return intent;}


    private Intent openCamera(){
        Common.ACTION_REQUEST=REQUEST_IMAGE_CAPTURE;
       Intent  takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
           return takePictureIntent;
        }

        else{
        return null;

        }
    }


    private Intent openGallery() {

        Common.ACTION_REQUEST=REQUEST_IMAGE_GALLERY;
        Intent takePictureIntent=new Intent(Intent.ACTION_PICK);
        takePictureIntent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        takePictureIntent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);

        if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();

            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(mContext, "Error creating File", Toast.LENGTH_SHORT).show();
          }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Common.profileImagePath=photoFile.getAbsolutePath();
                return takePictureIntent;
            }

            else return null;

        }

        else return null;
    }


    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /*  prefix     */
                ".jpg",  /*  suffix     */
                storageDir     /*  directory  */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}


//
////                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
//                }