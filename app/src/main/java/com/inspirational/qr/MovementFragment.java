package com.inspirational.qr;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.samples.vision.barcodereader.BarcodeCapture;
import com.google.android.gms.samples.vision.barcodereader.BarcodeGraphic;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.List;

import xyz.belvi.mobilevisionbarcodescanner.BarcodeRetriever;
import xyz.belvi.qr.R;


public class MovementFragment extends Fragment  implements BarcodeRetriever {

    private View rootView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_movement, container, false);

        final BarcodeCapture barcodeCapture = (BarcodeCapture) getChildFragmentManager().findFragmentById(R.id.barcode);
        barcodeCapture.setRetrieval(this);

        barcodeCapture.setShowDrawRect(true);
        barcodeCapture.setSupportMultipleScan(true);
        barcodeCapture.setTouchAsCallback(false);
        barcodeCapture.shouldAutoFocus(true);
        barcodeCapture.setShouldShowText(false);
        barcodeCapture.refresh();
        return rootView;
    }



//        findViewById(R.id.refresh).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (fromXMl.isChecked()) {
//
//                } else {
//                    barcodeCapture.setShowDrawRect(drawRect.isChecked());
//                    barcodeCapture.setSupportMultipleScan(supportMultiple.isChecked());
//
//                    barcodeCapture.setTouchAsCallback(touchBack.isChecked());
//                    barcodeCapture.shouldAutoFocus(autoFocus.isChecked());
//                    barcodeCapture.setShouldShowText(drawText.isChecked());
//                    barcodeCapture.refresh();
//                }
//            }
//        });




    @Override
    public void onRetrieved(final Barcode barcode) {
        Log.d("MFG", "Barcode read: " + barcode.displayValue);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                        .setTitle("code retrieved")
                        .setMessage(barcode.displayValue);
                builder.show();
            }
        });


    }

    @Override
    public void onRetrievedMultiple(final Barcode closetToClick, final List<BarcodeGraphic> barcodeGraphics) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String message = "Code selected : " + closetToClick.displayValue + "\n\nother " +
                        "codes in frame include : \n";
                for (int index = 0; index < barcodeGraphics.size(); index++) {
                    Barcode barcode = barcodeGraphics.get(index).getBarcode();
                    message += (index + 1) + ". " + barcode.displayValue + "\n";
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                        .setTitle("code retrieved")
                        .setMessage(message);
                builder.show();
            }
        });

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onRetrievedFailed(String reason) {

    }

}
