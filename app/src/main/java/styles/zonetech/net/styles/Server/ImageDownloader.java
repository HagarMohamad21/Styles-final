package styles.zonetech.net.styles.Server;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import styles.zonetech.net.styles.Utils.Common;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public class ImageDownloader extends AsyncTaskLoader<Bitmap> {

    Bitmap result=null;
   String Url="";
    public ImageDownloader(@NonNull Context context,String Url) {
        super(context);
        this.Url=Url;
    }

    @Nullable
    @Override
    public Bitmap loadInBackground() {
        final OkHttpClient client = new OkHttpClient();

    Request request = new Request.Builder()
            .url(Url)
            .build();

    Response response = null;
    Bitmap mIcon11 = null;
    try {
        response = client.newCall(request).execute();
    } catch (IOException e) {
        e.printStackTrace();
    }
    if(response!=null){
        if (response.isSuccessful()) {
            try {
                mIcon11 = BitmapFactory.decodeStream(response.body().byteStream());
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

        }
    }


    return mIcon11;



    }
    @Override
    protected void onStartLoading() {

        if (result != null) {
            // Use cached data
            deliverResult(result);
        } else {
            // We have no data, so kick off loading it
            forceLoad();
        }
    }

    }

