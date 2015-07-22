package com.udacity.gradle.builditbigger;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class ServletPostAsyncTask extends AsyncTask<String, Void, String> {
    private Context context;
    ProgressDialog pDialog;
    AsyncTaskResponseListener listener;

    public ServletPostAsyncTask(Context context, AsyncTaskResponseListener listener) {
        this.listener = listener;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading Joke");
        pDialog.show();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://10.0.2.2:8080/hello"); // 10.0.2.2 is localhost's IP address in Android emulator
        try {
            // Execute HTTP Post Request
            HttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                String joke = EntityUtils.toString(response.getEntity());
                listener.onAsyncResponse(joke);
                return joke;
            }
            return "Error: " + response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase();

        } catch (ClientProtocolException e) {
            return e.getMessage();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        pDialog.hide();
        Toast.makeText(context, result, Toast.LENGTH_LONG).show();
    }

    public interface AsyncTaskResponseListener {
        void onAsyncResponse(String joke) throws IOException;
    }
}
