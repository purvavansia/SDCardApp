package com.example.purva.sdcardapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    /*Declaration od all the components*/
    private Button start, stop;
    private ProgressDialog progressDialog;
    private ListView lv;
    private ArrayList<File> FilesInFolder;
    MyAsyncTask myAsyncTask = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*initialization of the components*/
        start = findViewById(R.id.buttonStart);
        stop = findViewById(R.id.buttonStop);
        lv = findViewById(R.id.listView);
        FilesInFolder = new ArrayList<>();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Start Async task*/
               myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute();

            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Stop Async task*/
                progressDialog.cancel();
                myAsyncTask = new MyAsyncTask();
                myAsyncTask.cancel(true);

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        myAsyncTask.cancel(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        myAsyncTask.cancel(true);

    }



    public class NameSize{
        ArrayList<File> names;
        long[] result;
        long avg;
        String[] extension;

        public String[] getExtension() {
            return extension;
        }

        public long getAvg() {
            return avg;
        }

        public NameSize(ArrayList<File> names, long[] result, long avg, String[] ext) {
            this.names = names;
            this.result = result;
            this.avg = avg;
            this.extension = ext;
        }

        public ArrayList<File> getNames() {
            return names;
        }

        public long[] getResult() {
            return result;
        }
    }

    /*Get all the files from the SD*/
    public NameSize getFiles(String DirectoryPath) {

        final long[] result;

        ArrayList<File> MyFiles = new ArrayList<File>();

        long total = 0;

        String[] path;

        File f1 = new File(DirectoryPath);
        f1.mkdirs();
        final File[] files1 = f1.listFiles();

        result = new long[files1.length];
        path = new String[files1.length];
        String[] extension = new String[files1.length];
        if (files1.length == 0)
            return null;
        else {
            int j = 0;
            for (int i = 0; i < files1.length; i++) {


                if (files1[i].isFile()) {

                    MyFiles.add(files1[i]);
                    path[i] = files1[i].getPath();
                    extension[j] = files1[i].getName().substring(files1[i].getName().length() - 3);
                    result[j] = files1[i].length() / 1024;
                    total = total + files1[i].length();
                    j++;

                } else if (files1[i].isDirectory()) {
                        getFiles(files1[i].getPath());

                }
            }
            /*   for sorting the files according to the sizes   */
           /* Collections.sort(MyFiles, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    File file1 = o1;
                    File file2 = o2;
                    if (file1.length() > file2.length()) {
                        return -1;
                        //else return 1
                    } else if (file1.length() < file2.length()) {
                        return 1;
                    }

                    return 0;
                }
            });*/



            return new NameSize(MyFiles, result, total, extension);
        }
    }

    public class MyAsyncTask extends AsyncTask<Void,Void,Void> {


        long[] result;
        long average;
        String[] ext;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*before executing the scan displays progress dialog*/
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Task is Running...");
            progressDialog.setCancelable(true);

            progressDialog.setButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    progressDialog.dismiss();
                    myAsyncTask.cancel(true);
                }
            });
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... voids) {



            //Call getFiles method

            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
           // String path = "storage/emulated/0/Purva";
            NameSize nameSize = getFiles(path);
            FilesInFolder = nameSize.getNames();
            result = nameSize.getResult();
            average = nameSize.getAvg();
            ext = nameSize.getExtension();
            for (int i = 0; i<50; i++) {
                try {
                    Thread.sleep(100);
                    if (isCancelled())
                        break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... voids) {

            super.onProgressUpdate();


        }

        @Override
        protected void onPostExecute(Void voids) {
            super.onPostExecute(voids);

            /*sets all the files in a listview*/
            progressDialog.cancel();
            ListAdapter adapter = new MyAdapter(average,FilesInFolder,ext,result, MainActivity.this);
            lv.setAdapter(adapter);

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            myAsyncTask.cancel(true);
        }
    }

}
