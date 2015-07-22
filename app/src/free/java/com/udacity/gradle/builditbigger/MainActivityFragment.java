package com.udacity.gradle.builditbigger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.ServletPostAsyncTask;

import sample.com.displayjokeactivity.JokeActivity;

public class MainActivityFragment extends Fragment {

    InterstitialAd mInterstitialAd;
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
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    downloadJokeAndDisplay();
                }
            }
        });
        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                downloadJokeAndDisplay();
            }
        });
        requestNewInterstitial();
        return root;
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mInterstitialAd.loadAd(adRequest);
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
