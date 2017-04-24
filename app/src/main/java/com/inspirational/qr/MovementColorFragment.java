package com.inspirational.qr;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;

import github.nisrulz.qreader.QRDataListener;
import github.nisrulz.qreader.QREader;


public class MovementColorFragment extends Fragment {

    private View nextColorIndicator;

    // hear you can specify the order of the qr codes to be scanned
    int[] gameTry = new int[]{1, 2, 3, 4, 5, 6, 3, 2, 1};

    int currentPos = 0;

    private QREader mQRReaderView;
    private SurfaceView mHolder;
    private Chronometer chronoMeterGameTime;

    boolean isPlaying = false;

    private static final String LOG_TAG = "MCFR";
    private String mCurrentTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movement_color, container, false);
        mHolder = (SurfaceView) rootView.findViewById(R.id.qrdecoderview);
        nextColorIndicator = rootView.findViewById(R.id.next_color_indicator);
        final Button startGame = (Button) rootView.findViewById(R.id.start_game_btn);
        final Button stopGame = (Button) rootView.findViewById(R.id.stop_game_btn);

        chronoMeterGameTime = (Chronometer) rootView.findViewById(R.id.game_time);
        chronoMeterGameTime.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer cArg) {
                convertChronometerTimeToMinutesSeconds(cArg);
            }
        });


        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPos = 0;

                chronoMeterGameTime.setBase(SystemClock.elapsedRealtime());
                chronoMeterGameTime.start();

                startGame.setVisibility(View.GONE);
                stopGame.setVisibility(View.VISIBLE);

                int colorID = getActivity().getResources().getIdentifier("mov_col_" + gameTry[currentPos], "color", getActivity().getPackageName());
                nextColorIndicator.setBackgroundColor(ContextCompat.getColor(getActivity(), colorID));

                isPlaying = true;
            }
        });

        stopGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronoMeterGameTime.stop();
                stopGame.setVisibility(View.GONE);
                startGame.setVisibility(View.VISIBLE);
                nextColorIndicator.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.transparent));

                isPlaying = false;
            }
        });


        // Game mechanic goes here
        mQRReaderView = new QREader.Builder(getActivity(), mHolder, new QRDataListener() {
            @Override
            public void onDetected(final String data) {
                if (isPlaying) {
                    Log.d(LOG_TAG, "Data read " + data);
                    if (data.equals("id=" + gameTry[currentPos])) {
                        Log.d(LOG_TAG, "Found right QR Code " + data);

                        Vibrator vibe = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                        vibe.vibrate(100);

                        currentPos++;

                        if (currentPos >= gameTry.length) {
                            Log.d(LOG_TAG, "The game has been won");
                            chronoMeterGameTime.stop();

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    nextColorIndicator.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.transparent));
                                    stopGame.setVisibility(View.GONE);
                                    startGame.setVisibility(View.VISIBLE);
                                }
                            });

                            isPlaying = false;

                            showWinDialog();
                        } else {

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Set next color
                                    final int colorID = getActivity().getResources().getIdentifier("mov_col_" + gameTry[currentPos], "color", getActivity().getPackageName());
                                    nextColorIndicator.setBackgroundColor(ContextCompat.getColor(getActivity(), colorID));
                                }
                            });

                        }
                    } else {
                        Log.d(LOG_TAG, "Not the right qr code or double scanned");
                    }
                }
            }
        }).facing(QREader.BACK_CAM)
                .enableAutofocus(true)
                .height(mHolder.getHeight())
                .width(mHolder.getWidth())
                .build();

        return rootView;
    }

    private void showWinDialog() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

        // set title
        alertDialogBuilder.setTitle("You won the game");

        // set dialog message
        alertDialogBuilder
                .setMessage("Your time: "+mCurrentTime)
                .setCancelable(true);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

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

    private void convertChronometerTimeToMinutesSeconds(Chronometer cArg) {
        long time = SystemClock.elapsedRealtime() - cArg.getBase();
        int h = (int) (time / 3600000);
        int m = (int) (time - h * 3600000) / 60000;
        int s = (int) (time - h * 3600000 - m * 60000) / 1000;
        String mm = m < 10 ? "0" + m : m + "";
        String ss = s < 10 ? "0" + s : s + "";
        mCurrentTime = mm + ":" + ss;
        cArg.setText(mCurrentTime);
    }


}
