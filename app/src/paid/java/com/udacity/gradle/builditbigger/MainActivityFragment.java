package com.udacity.gradle.builditbigger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import sample.com.displayjokeactivity.JokeActivity;

public class MainActivityFragment extends Fragment {

    Activity activity;

    public MainActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        activity = getActivity();
        Button tellJokeBtn = (Button) root.findViewById(R.id.tellJokeBtn);
        tellJokeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                downloadJokeAndDisplay();
            }
        });
        return root;
    }

    private void downloadJokeAndDisplay() {
        ServletPostAsyncTask.AsyncTaskResponseListener listener = new ServletPostAsyncTask.AsyncTaskResponseListener() {
            @Override
            public void onAsyncResponse(String joke) {
                Intent displayJokeIntent = new Intent(activity, JokeActivity.class);
                displayJokeIntent.putExtra("SAMPLE_JOKE", joke);
                activity.startActivity(displayJokeIntent);
            }
        };
        new ServletPostAsyncTask(getActivity(), listener).execute();
    }
}
