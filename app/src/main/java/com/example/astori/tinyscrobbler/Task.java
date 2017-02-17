package com.example.astori.tinyscrobbler;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by Astori on 12/02/2017.
 */

public class Task extends AsyncTask<String,Void, String> {
    public AsyncResponse delegate = null;
    ProgressDialog progressDialog;
    private Context mContext;

    public Task(Context context) {
        mContext = context;
        progressDialog = new ProgressDialog(mContext);
    }
    protected void onPreExecute() {

        super.onPreExecute();
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Wait while loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    @Override
    protected String doInBackground(String... url) {
        Document doc = null;
        String page = "";
        int statusCode;
        String temp;
        try {
            Connection.Response response = Jsoup.connect(url[0]).userAgent("Mozilla/5.0 (Windows NT 6.0) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.46 Safari/536.5").timeout(15000).ignoreHttpErrors(true).execute();
            statusCode = response.statusCode();
            if (statusCode == 200) {
                doc = Jsoup.connect(url[0]).maxBodySize(0).timeout(15000).get();
                page = doc.toString();
                page = page.substring(page.indexOf("that. -->") + 9);
            }
        } catch (Exception ex) {

            }
        if (doc == (null))
        {
            try {
                Connection.Response response = Jsoup.connect(url[1]).userAgent("Mozilla/5.0 (Windows NT 6.0) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.46 Safari/536.5").timeout(15000).ignoreHttpErrors(true).execute();
                statusCode = response.statusCode();
                if (statusCode == 200) {
                    doc = Jsoup.connect(url[1]).maxBodySize(0).timeout(15000).get();
                    page = doc.toString();
                    page = page.substring(page.indexOf("iComment-text\">") + 15);
                    }
                else{
                    return "Lyrics was not found!";
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
            page = page.substring(0, page.indexOf("</div>"));
            temp = page;
            temp = temp.replaceAll("(?i)<br[^>]*>", "br2n");
            temp = temp.replaceAll("]", "]shk");
            temp = temp.replaceAll("\\[", "shk[");
            String lyrics = Jsoup.parse(temp).text();
            lyrics = lyrics.replaceAll("br2n", "\n");
            lyrics = lyrics.replaceAll("]shk", "]\n");
            lyrics = lyrics.replaceAll("shk\\[", "\n [");
            lyrics = " \n \n" + url[2] + " - " + url[3] + " \n \n" + " " + lyrics;
            return lyrics;
    }

    protected void onPostExecute(String result)
    {
        progressDialog.dismiss();
        delegate.processFinish(result);
    }
}
