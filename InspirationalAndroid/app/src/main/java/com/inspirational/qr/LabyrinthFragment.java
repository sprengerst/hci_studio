package com.inspirational.qr;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import github.nisrulz.qreader.QRDataListener;
import github.nisrulz.qreader.QREader;


public class LabyrinthFragment extends Fragment {

    private TextView nextLetterIndicator;

    // hear you can specify the order of the qr codes to be scanned
    final String[] correctPath = {"A", "C"};//, "E", "I", "K", "P", "R", "T", "Z"};


    private QREader mQRReaderView;
    private SurfaceView mHolder;


    private static final String LOG_TAG = "LF";
    private boolean isPlaying = false;
    private Button mStartGameBtn;
    private Button mStopGameBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_labyrinth, container, false);
        mHolder = (SurfaceView) rootView.findViewById(R.id.qrdecoderview);
        nextLetterIndicator = (TextView) rootView.findViewById(R.id.next_letter_indicator);

        mStartGameBtn = (Button) rootView.findViewById(R.id.start_game_btn);
        mStopGameBtn = (Button) rootView.findViewById(R.id.stop_game_btn);


        mStartGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();

            }
        });

        mStopGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });

        // Game mechanic goes here
        mQRReaderView = new QREader.Builder(getActivity(), mHolder, new QRDataListener() {
            @Override
            public void onDetected(final String data) {

                if(isPlaying) {
                    Log.d(LOG_TAG, "Data read " + data);
                    if (data.matches("[A-Z]")) {
                        final int index = findIndexArray(correctPath, data);
                        if (index != -1) {
                            Log.d(LOG_TAG, "Found index at: " + index + " from " + correctPath.length);
                            if ((index + 1) < correctPath.length) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        nextLetterIndicator.setVisibility(View.VISIBLE);
                                        nextLetterIndicator.setText(correctPath[index + 1]);
                                    }
                                });
                            } else {
                                showWinDialog();
                                resetGame();
                            }
                        } else {
                            // BOMB
                            Log.d(LOG_TAG, "BOMB");
                            Vibrator vibe = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                            vibe.vibrate(100);
                            resetGame();
                        }
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

    private void startGame() {
        mStartGameBtn.setVisibility(View.GONE);
        mStopGameBtn.setVisibility(View.VISIBLE);
        isPlaying = true;
    }

    private void resetGame() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isPlaying = false;
                mStopGameBtn.setVisibility(View.GONE);
                mStartGameBtn.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showWinDialog() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

        // set title
        alertDialogBuilder.setTitle("You won the game");

        // set dialog message
        alertDialogBuilder
                .setMessage("Congratulations")
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

    public int findIndexArray(String[] strings, String desired) {

        for (int i = 0; i < strings.length; i++) {
            if (strings[i].equals(desired)) {
                return i;
            }
        }

        return -1;
    }
}
