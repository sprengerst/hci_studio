package com.inspirational.qr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import xyz.belvi.qr.R;

/**
 *
 * This fragment handle new event creations
 *
 */
public class MainFragment extends Fragment{

    private View rootView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        Button movementExample = (Button) rootView.findViewById(R.id.movement_btn);

        movementExample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),MovementActivity.class));
            }
        });

        return rootView;
    }


}
