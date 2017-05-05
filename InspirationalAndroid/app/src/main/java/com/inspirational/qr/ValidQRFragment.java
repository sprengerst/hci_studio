package com.inspirational.qr;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.Timer;
import java.util.TimerTask;

import github.nisrulz.qreader.QRDataListener;
import github.nisrulz.qreader.QREader;


public class ValidQRFragment extends Fragment {

    private QREader mQRReaderView;
    private SurfaceView mHolder;
    private FrameLayout backgroundView;
    private long lastRecognisedTimeStamp = -1;

    private static final String LOG_TAG = "MCFR";
    private Timer mResetToRedTimer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_valid_qr, container, false);
        mHolder = (SurfaceView) rootView.findViewById(R.id.qrdecoderview);

        mResetToRedTimer = new Timer();
        mResetToRedTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d(LOG_TAG," TIMER TRIGGER ");
                if(lastRecognisedTimeStamp + 150 < System.currentTimeMillis()) {
                    Log.d(LOG_TAG," No active QR Code detected, background set to red ");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            backgroundView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.valid_qr_not_found));
                        }
                    });
                }
            }
        }, 0, 100);

        backgroundView = (FrameLayout) rootView.findViewById(R.id.valid_qr_color_frame);
        // Game mechanic goes here
        mQRReaderView = new QREader.Builder(getActivity(), mHolder, new QRDataListener() {
            @Override
            public void onDetected(final String data) {
                lastRecognisedTimeStamp = System.currentTimeMillis();
                Log.d(LOG_TAG, "Active QR Code detected: " + data+" Timestamp: "+lastRecognisedTimeStamp);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        backgroundView.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.valid_qr_found));
                    }
                });
            }
        }).facing(QREader.BACK_CAM)
                .enableAutofocus(true)
                .height(mHolder.getHeight())
                .width(mHolder.getWidth())
                .build();

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        mQRReaderView.initAndStart(mHolder);
    }

    @Override
    public void onPause() {
        super.onPause();
        mQRReaderView.releaseAndCleanup();
    }


    public void stopTimer() {
        if(mResetToRedTimer != null){
            mResetToRedTimer.cancel();
            mResetToRedTimer.purge();
        }
    }
}
