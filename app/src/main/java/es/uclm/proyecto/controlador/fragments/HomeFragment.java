package es.uclm.proyecto.controlador.fragments;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import es.uclm.proyecto.R;

/**
 * Created by Admin on 04-06-2015.
 */
public class HomeFragment extends Fragment {
    public void onCreate(Bundle savedInstanceState) {

        // Fragment locked in landscape screen orientation
        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home,container,false);

        setHasOptionsMenu(true);
        ActionBar actionBar = ((AppCompatActivity) getActivity())
                .getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDefaultDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(R.string.home);
        return v;
    }


}
