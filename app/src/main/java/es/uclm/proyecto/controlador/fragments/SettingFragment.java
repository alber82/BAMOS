package es.uclm.proyecto.controlador.fragments;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import es.uclm.proyecto.R;

/**
 * Created by Admin on 04-06-2015.
 */
public class SettingFragment extends Fragment {
    public void onCreate(Bundle savedInstanceState) {

        // Fragment locked in landscape screen orientation

        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_setting,container,false);
        return v;
    }
}
