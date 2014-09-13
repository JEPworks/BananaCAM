package com.ripeyet.bananacam.tasks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ripeyet.bananacam.R;

/**
 * Created by James on 9/13/2014.
 */
public class BananaViewerFragment extends Fragment {

        private int position;

        public BananaViewerFragment(int position) {
            this.position = position;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_room_selection, container, false);
            return rootView;
        }
    }
}
