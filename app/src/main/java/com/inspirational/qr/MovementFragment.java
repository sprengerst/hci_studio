package com.inspirational.qr;

import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

public class MovementFragment extends Fragment implements QRCodeReaderView.OnQRCodeReadListener {

    private QRCodeReaderView mQRReaderView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movement, container, false);

        mQRReaderView = (QRCodeReaderView) rootView.findViewById(R.id.qrdecoderview);
        mQRReaderView.setOnQRCodeReadListener(this);

        // Use this function to enable/disable decoding
        mQRReaderView.setQRDecodingEnabled(true);

        // Use this function to change the autofocus interval (default is 5 secs)
        // 500ms
        mQRReaderView.setAutofocusInterval(500L);

        // Use this function to enable/disable Torch
        mQRReaderView.setTorchEnabled(true);

        // Use this function to set front camera preview
        mQRReaderView.setFrontCamera();

        // Use this function to set back camera preview
        mQRReaderView.setBackCamera();
        return rootView;
    }


    // Called when a QR is decoded
    // "text" : the text encoded in QR
    // "points" : points where QR control points are placed in View
    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        //resultTextView.setText(text);
        System.out.println("QR READ "+text);
    }

    @Override
    public void onResume() {
        super.onResume();
        mQRReaderView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mQRReaderView.stopCamera();
    }
}
