package com.pcmiguel.anime;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DownloadApk extends Activity {


    private static ProgressDialog bar;
    private static String TAG = "DownloadApk";
    private static Context context ;
    private static Activity activity ;
    private static String downloadUrl ;


    public DownloadApk(Context context){
        this.context = context ;
        this.activity = (Activity)context;
    }


    public   void startDownloadingApk(String url){
        downloadUrl = url ;
        if(downloadUrl!=null){
            new DownloadNewVersion().execute();
        }

    }




    private static  class DownloadNewVersion extends AsyncTask<String,Integer,Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(bar==null){
                bar = new ProgressDialog(context);
                bar.setCancelable(false);
                bar.setMessage("Downloading...");
                bar.setIndeterminate(true);
                bar.setCanceledOnTouchOutside(false);
                bar.show();
            }


        }

        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);


            bar.setIndeterminate(false);
            bar.setMax(100);
            bar.setProgress(progress[0]);
            String msg = "";
            if(progress[0]>99){

                msg="Finishing... ";

            }else {

                msg="Downloading... "+progress[0]+"%";
            }
            bar.setMessage(msg);

        }
        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if(bar.isShowing() && bar!=null){
                bar.dismiss();
                bar=null;
            }


            if(result){

                Toast.makeText(context,"Update Done",
                        Toast.LENGTH_SHORT).show();

            }

        }

        @Override
        protected Boolean doInBackground(String... arg0) {
            int count;
            Boolean flag = false;
            try {
                URL url = new URL(downloadUrl);
                URLConnection c = url.openConnection();
                c.connect();

                int lenghtOfFile = c.getContentLength();

                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                String PATH = Environment
                        .getExternalStorageDirectory().toString()
                        + "/Download/";

                File file = new File(PATH);
                file.mkdirs();
                File outputFile = new File(file,"app-debug.apk");

                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++)
                {
                    System.out.println("FileName:" + files[i].getName());
                }

                final PackageManager pm = context.getPackageManager();
                PackageInfo info = pm.getPackageArchiveInfo(PATH+"/app-debug.apk", 0);

                System.out.println("V1: " + BuildConfig.VERSION_NAME);
                System.out.println("V2: " + info.versionName);

                /*if (BuildConfig.VERSION_NAME.equals(info.versionName)) {
                    Toast.makeText(context,"It already has the latest update",
                            Toast.LENGTH_SHORT).show();
                }*/
                if(outputFile.exists()){
                    outputFile.delete();
                }

                OutputStream output = new FileOutputStream(outputFile);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress((int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

                OpenNewVersion(PATH);
                flag = true;

            } catch (MalformedURLException e) {
                Log.e(TAG, "Update Error: " + e.getMessage());
                flag = false;
            }catch (IOException ex){
                ex.printStackTrace();
            }
            return flag;

        }

    }

    private static void OpenNewVersion(String location) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(getUriFromFile(location),
                "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(intent);

    }

    private static Uri getUriFromFile(String location) {

        if(Build.VERSION.SDK_INT<24){
            return   Uri.fromFile(new File(location + "app-debug.apk"));
        }
        else{
            return FileProvider.getUriForFile(context,
                    context.getApplicationContext().getPackageName() + ".provider",
                    new File(location + "app-debug.apk"));
        }
    }
}
