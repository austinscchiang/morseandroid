package com.ac.austin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by austin on 27/06/13.
 */
public class LightSectionFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootLightView = inflater.inflate(R.layout.fragment_section_light, container, false);


        return rootLightView;
    }

}
