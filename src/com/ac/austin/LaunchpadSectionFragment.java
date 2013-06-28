package com.ac.austin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

/**
 * Created by austin on 27/06/13.
 */
public class LaunchpadSectionFragment extends Fragment {
    //IMPLEMENT THIS SHIT
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_section_launchpad, container, false);
        ToggleButton buttonPressed=(ToggleButton)rootView.findViewById(R.id.encodeButton);
        buttonPressed.setChecked(true);
        return rootView;
    }


}
