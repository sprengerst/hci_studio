package com.inspirational.qr;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import github.nisrulz.qreader.QRDataListener;
import github.nisrulz.qreader.QREader;


public class MovementColorFragment extends Fragment  {

    private View nextColorIndicator;

    int [] game1 = new int[]{1,2,3};
    int [] game2 = new int[]{3,2,1};

    int currentPos = 0;
    private QREader mQRReaderView;
    private SurfaceView mHolder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movement_color, container, false);

        mHolder = (SurfaceView) rootView.findViewById(R.id.qrdecoderview);
        // Init QREader
        // ------------
        mQRReaderView = new QREader.Builder(getActivity(), mHolder, new QRDataListener() {
            @Override
            public void onDetected(final String data) {
                System.out.println("QR READ "+data);
                if(data.equals("id="+game1[currentPos])){
                    System.out.println("FOUND RIGHT QR CODE");
                }
            }
        }).facing(QREader.BACK_CAM)
                .enableAutofocus(true)
                .height(mHolder.getHeight())
                .width(mHolder.getWidth())
                .build();

        nextColorIndicator = rootView.findViewById(R.id.next_color_indicator);

        Button startGame = (Button) rootView.findViewById(R.id.start_game_btn);
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int colorID = getActivity().getResources().getIdentifier("mov_col_"+game1[currentPos], "color", getActivity().getPackageName());
                //int drawableID = getActivity().getResources().getIdentifier("mov_col_"+game1[currentPos], "drawable", getActivity().getPackageName());
                nextColorIndicator.setBackgroundColor(colorID);
            }
        });
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


}
