package es.uclm.proyecto.controlador.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.Locale;

import es.uclm.proyecto.R;

import static es.uclm.proyecto.R.id.spEspecies;

/**
 * Created by Admin on 04-06-2015.
 */
public class SettingFragment extends Fragment implements View.OnClickListener{

    private Spinner splanguage;
    private Button btlanguage;
    public void onCreate(Bundle savedInstanceState) {

        // Fragment locked in landscape screen orientation

        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_setting,container,false);


        splanguage = (Spinner) v.findViewById(R.id.splanguage);
        btlanguage = (Button) v.findViewById(R.id.btlanguage);

        ArrayAdapter<CharSequence> aalanguage= ArrayAdapter.createFromResource(
                getActivity().getApplicationContext(), R.array.language, R.layout.simple_spinner_item_perso);
        aalanguage.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        splanguage.setAdapter(aalanguage);
        splanguage.setSelection(aalanguage.getPosition(""));

        btlanguage.setOnClickListener(this);

        setHasOptionsMenu(true);
        ActionBar actionBar = ((AppCompatActivity) getActivity())
                .getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDefaultDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(R.string.setting_string);
        return v;
    }

    @Override
    public void onClick(View view) {

        android.app.AlertDialog.Builder alertDialog;
        switch (view.getId()) {
            case R.id.btlanguage:

                    alertDialog = new android.app.AlertDialog.Builder(getActivity());
                    alertDialog.setTitle(R.string.dialog_warning);
                    alertDialog.setMessage(R.string.reset_language_string);
                    alertDialog.setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String localeString = "en";
                            if (splanguage.getSelectedItem().toString().equals("Spanish")||splanguage.getSelectedItem().toString().equals("Español")){
                                localeString="es";
                            }
                            SharedPreferences prefs = getActivity().getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("idioma",localeString);
                            editor.commit();

                            Intent i = getActivity().getBaseContext().getPackageManager()
                                    .getLaunchIntentForPackage(getActivity().getBaseContext().getPackageName());
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);


                        }
                    });
                    alertDialog.setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.show();

            default:
                break;
        }

    }
}
