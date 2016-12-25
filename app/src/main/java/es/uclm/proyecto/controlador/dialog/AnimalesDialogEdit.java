package es.uclm.proyecto.controlador.dialog;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import es.uclm.proyecto.R;
import es.uclm.proyecto.modelo.Animal;
import es.uclm.proyecto.modelo.Especie;
import es.uclm.proyecto.sqlite.ContratoBBDD;
import es.uclm.proyecto.util.DismissKeyboardListener;
import es.uclm.proyecto.util.Utils;

public class AnimalesDialogEdit extends DialogFragment implements View.OnClickListener,View.OnTouchListener {

    private AnimalesDialogAdd button;
    private Animal animal;


    private EditText etFechaNac;
    private EditText etFechaCir;
    private EditText etTratamiento;
    private EditText etModeloLesion;
    private EditText etCEPA;
    private EditText etTransgenico;
    private EditText etCodigo;
    private EditText etOrigen;
    private EditText etExperimento;

    private Spinner spEspecies;
    private Spinner spSexo;

    ArrayList<Especie> ALespecies;
    ArrayList<String> ALSespecies;
    ArrayAdapter<String> aaspEspecies;

    String animalId;
    String animalCodigo;
    String animalFechaNac;
    String animalFechaCir;
    String animalCEPA        ;
    String animalEspecie     ;
    String animalSexo        ;
    String animalTransgenico ;
    String animalModeloLesion;
    String animalTratamiento ;
    String animalOrigen;
    String animalExperimento;

    public AnimalesDialogEdit() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Theme_AppCompat_Light_DarkActionBar);
        //android.R.style.Theme_Holo_Light_DarkActionBar);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_edit_animal, null);


        Bundle argumentos = getArguments();
        animalId = argumentos.getString("_id");
        Log.d("argumento",animalId);

        Cursor cAnimal = getActivity().getContentResolver().query(ContratoBBDD.Animales.crearUriAnimal(animalId),null,null,null,null);

        cAnimal.moveToFirst();
        while (!cAnimal.isAfterLast()) {
            animalCodigo    = cAnimal.getString(cAnimal.getColumnIndex("codigo"));
            animalFechaNac  = cAnimal.getString(cAnimal.getColumnIndex("fecha_nac"));
            animalFechaCir  = cAnimal.getString(cAnimal.getColumnIndex("fecha_cir"));
            animalCEPA      = cAnimal.getString(cAnimal.getColumnIndex("cepa"));
            animalEspecie   = cAnimal.getString(cAnimal.getColumnIndex("descripcion"));
            animalSexo      = cAnimal.getString(cAnimal.getColumnIndex("sexo"));
            animalTransgenico= cAnimal.getString(cAnimal.getColumnIndex("transgenico"));
            animalModeloLesion= cAnimal.getString(cAnimal.getColumnIndex("modelo_lesion"));
            animalTratamiento= cAnimal.getString(cAnimal.getColumnIndex("tratamiento"));
            animalOrigen    = cAnimal.getString(cAnimal.getColumnIndex("origen"));
            animalExperimento= cAnimal.getString(cAnimal.getColumnIndex("experimento"));
            cAnimal.moveToNext();
        }
        cAnimal.close();


        ALespecies = new ArrayList<Especie>();
        ALSespecies = new ArrayList<String>();

        Cursor cEspecies = getActivity().getContentResolver().query(ContratoBBDD.Especies.URI_CONTENIDO, null, null, null, null);

        cEspecies.moveToFirst();
        while (!cEspecies.isAfterLast()) {
            Especie especie = new Especie(cEspecies.getInt(cEspecies.getColumnIndex("_id")), cEspecies.getString(cEspecies.getColumnIndex("descripcion")));
            ALespecies.add(especie);
            ALSespecies.add(cEspecies.getString(cEspecies.getColumnIndex("descripcion")));
            cEspecies.moveToNext();
        }
        cEspecies.close();


            ActionBar actionBar = ((AppCompatActivity) getActivity())
                .getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(false);
 //       actionBar.setDefaultDisplayHomeAsUpEnabled(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        InputMethodManager imm = (InputMethodManager) getActivity().getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);

//        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
//        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
        View view = inflater.inflate(R.layout.dialog_edit_animal, container, false);

        LinearLayout Parent = (LinearLayout) view.findViewById(R.id.edit_animal_layout);
        Parent.setOnClickListener(new DismissKeyboardListener(getActivity()));


        Toolbar actionBar = (Toolbar) view.findViewById(R.id.fake_action_bar);
        if (actionBar != null) {
            final AnimalesDialogEdit window = this;
            actionBar.setTitle(R.string.animal_edit);
            actionBar.setTitleTextColor(R.color.white);
            actionBar.inflateMenu(R.menu.menu_animal);
            actionBar.setNavigationOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    window.dismiss();
                }
            });
            actionBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Bundle args = new Bundle();
                    args.putString("_id", animalId);
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    int id = item.getItemId();
                    switch (id) {
                        case R.id.action_done:
                            actualizarAnimal();
                            break;
                        case R.id.action_send:
                            AnimalDialogEmail emailDialog = new AnimalDialogEmail();
                            emailDialog.setArguments(args);
                            emailDialog.show(fragmentTransaction, "fragment_email");
                            break;
                    }
                    return true;
                }
            });
        }
        setHasOptionsMenu(true);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        etTratamiento   = (EditText) view.findViewById(R.id.etTratamiento);
        etModeloLesion  = (EditText) view.findViewById(R.id.etModeloLesion);
        etCEPA          = (EditText) view.findViewById(R.id.etCEPA);
        etCodigo        = (EditText) view.findViewById(R.id.etCodigo);
        etTransgenico   = (EditText) view.findViewById(R.id.etTrans);
        etOrigen        = (EditText) view.findViewById(R.id.etOrigen);
        etExperimento   = (EditText) view.findViewById(R.id.etExperimento);

        etFechaCir      = (EditText) view.findViewById(R.id.etFechaCir);
        etFechaNac      = (EditText) view.findViewById(R.id.etFechaNac);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            etFechaCir.setShowSoftInputOnFocus(false);
            etFechaNac.setShowSoftInputOnFocus(false);

        }

        spEspecies          = (Spinner) view.findViewById(R.id.spEspecies);
        spSexo              = (Spinner) view.findViewById(R.id.spSexo);

        aaspEspecies = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.simple_spinner_item_perso, ALSespecies);
        aaspEspecies.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spEspecies.setAdapter(aaspEspecies);

        etFechaNac.setOnTouchListener(this);
        etFechaCir.setOnTouchListener(this);

        etTratamiento.setText(animalTratamiento);
        etModeloLesion.setText(animalModeloLesion);
        etCEPA.setText( animalCEPA);
        etTransgenico.setText( animalTransgenico);
        etCodigo.setText( animalCodigo);
        etExperimento.setText(animalExperimento);
        etOrigen.setText(animalOrigen);
        etFechaCir.setText( animalFechaCir);
        etFechaNac.setText(animalFechaNac);

        spEspecies.setSelection(((ArrayAdapter) spEspecies.getAdapter()).getPosition(animalEspecie));
        //spInvestigadores.setSelection(((ArrayAdapter) spInvestigadores.getAdapter()).getPosition(animalInvestigador));
        spSexo.setSelection(((ArrayAdapter) spSexo.getAdapter()).getPosition(animalSexo));
        //spTransgenico.setSelection(((ArrayAdapter) spTransgenico.getAdapter()).getPosition(animalTransgenico));

        return view;
    }




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.fullscreen_dialog, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                // procesarDescartar()
                break;

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            default:

                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int mYear, mMonth, mDay, mHour, mMinute;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        Log.d("arriba", "log");
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        int id = v.getId();
        switch (id) {
            case R.id.etFechaNac:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        try {
                            Date fecha = formatter.parse(String.valueOf(etFechaNac.getText()));
                            Calendar cFechaNac = Calendar.getInstance();
                            cFechaNac.setTime(fecha);
                            mYear = cFechaNac.get(Calendar.YEAR);
                            mMonth = cFechaNac.get(Calendar.MONTH);
                            mDay = cFechaNac.get(Calendar.DAY_OF_MONTH);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        DatePickerDialog dpd = new DatePickerDialog(getDialog().getContext(),
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        try {
                                            String dt;
                                            dt = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); // Set your date format
                                            Date currentDt = null;
                                            currentDt = sdf.parse(dt);
                                            String d = sdf.format(currentDt);
                                            etFechaNac.setText(d);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }, mYear, mMonth, mDay);

                        dpd.show();
                        break;
                    case MotionEvent.ACTION_UP:
                        etFechaNac.requestFocus();
                        break;
                }
                return  true;

            case R.id.etFechaCir:


                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        try {
                            Date fecha = formatter.parse(String.valueOf(etFechaCir.getText()));
                            Calendar cFechaCir = Calendar.getInstance();
                            cFechaCir.setTime(fecha);
                            mYear = cFechaCir.get(Calendar.YEAR);
                            mMonth = cFechaCir.get(Calendar.MONTH);
                            mDay = cFechaCir.get(Calendar.DAY_OF_MONTH);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        DatePickerDialog dpd = new DatePickerDialog(getDialog().getContext(),
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        try {
                                            String dt;
                                            dt = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); // Set your date format
                                            Date currentDt = null;
                                            currentDt = sdf.parse(dt);
                                            String d = sdf.format(currentDt);
                                            etFechaCir.setText(d);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }, mYear, mMonth, mDay);
                        dpd.show();

                        break;
                    case MotionEvent.ACTION_UP:
                        etFechaCir.requestFocus();
                        break;
                }
                return true;
        }

        return false;
    }
    public void actualizarAnimal(){
        int id_especie = 0;
        int id_investigador = 0;
        String _id;
        Uri insercion;

        String strCodigo        =   etCodigo.getText().toString();
        String strFechaNac      =   etFechaNac.getText().toString();
        String strFechaCir      =   etFechaCir.getText().toString();
        String strCEPA          =   etCEPA.getText().toString();
        String strEspecie       =   spEspecies.getSelectedItem().toString();
        String strSexo          =   spSexo.getSelectedItem().toString();
        String strTransgenico   =   etTransgenico.getText().toString();
        String strModeloLesion =   etModeloLesion.getText().toString();
        String strTratamiento   =   etTratamiento.getText().toString();
        String strOrigen        =   etOrigen.getText().toString();
        String strExperimento   =   etExperimento.getText().toString();

        // Declaramos el Iterador e imprimimos los Elementos del ArrayList
        Iterator<Especie> especieIterator = ALespecies.iterator();
        while(especieIterator.hasNext()) {
            Especie especie = especieIterator.next();
            if (especie.descripcion.equals(strEspecie)){
                id_especie = especie._id;
                Log.d("A", "" + id_especie);
            }
        }


        if (strCodigo.equals("") ||
                strFechaNac.equals("") ||
                strFechaCir.equals("") ||
                strCEPA.equals("") ||
                strEspecie.equals("") ||
                strSexo.equals("") ||
                strTransgenico.equals("")  ) {
            Log.d("Algo falta", "A");
            new AlertDialog.Builder(getDialog().getContext())
                    .setTitle(R.string.dialog_warning)
                    .setMessage(R.string.dialog_required_data)
                    .setCancelable(false)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Whatever...
                        }
                    }).create().show();
        }else if (strCodigo.equals(animalCodigo)    &&
                strFechaNac.equals(animalFechaNac)  &&
                strFechaCir.equals(animalFechaCir)  &&
                strCEPA.equals(animalCEPA)          &&
                strEspecie.equals(animalEspecie)    &&
                strExperimento.equals(animalExperimento) &&
                strOrigen.equals(animalOrigen) &&
                strSexo.equals(animalSexo) &&
                strTransgenico.equals(animalTransgenico) &&
                strModeloLesion.equals(animalModeloLesion) &&
                strTratamiento .equals(animalTratamiento) ) {
            new AlertDialog.Builder(getDialog().getContext())
                    .setTitle(R.string.dialog_warning)
                    .setMessage(R.string.dialog_no_change)
                    .setCancelable(false)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Whatever...
                        }
                    }).create().show();}
        else{
            Log.d("OK", "A");
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getDialog().getContext());
            final String idActualizar = animalId;
            animal = new Animal(strCodigo, id_especie, strCEPA, strTransgenico, strSexo, strFechaNac, strFechaCir, strTratamiento, strModeloLesion, strOrigen,strExperimento);


            final ContentValues cvAnimal = new ContentValues();
            cvAnimal.put(ContratoBBDD.Animales.codigo,strCodigo);
            cvAnimal.put(ContratoBBDD.Animales.cepa,strCEPA);
            cvAnimal.put(ContratoBBDD.Animales.fecha_nac,strFechaNac);
            cvAnimal.put(ContratoBBDD.Animales.fecha_cir,strFechaCir);
            cvAnimal.put(ContratoBBDD.Animales.id_especie,id_especie);
            cvAnimal.put(ContratoBBDD.Animales.sexo,strSexo);
            cvAnimal.put(ContratoBBDD.Animales.transgenico, strTransgenico);
            cvAnimal.put(ContratoBBDD.Animales.experimento, strExperimento);
            cvAnimal.put(ContratoBBDD.Animales.origen, strOrigen);
            cvAnimal.put(ContratoBBDD.Animales.modelo_lesion, strModeloLesion);
            cvAnimal.put(ContratoBBDD.Animales.tratamiento, strTratamiento);


            // Setting Dialog Title
            alertDialog.setTitle(R.string.diagog_warning_edir);
            alertDialog.setMessage(R.string.dialog_animal_edit_warning);
            alertDialog.setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    getActivity().getContentResolver().update(ContratoBBDD.Animales.crearUriAnimal(animalId), cvAnimal, null, null);
                    new AlertDialog.Builder(getDialog().getContext())
                            .setTitle(R.string.dialog_warning)
                            .setMessage(R.string.dialog_animal_edit
                            )
                            .setCancelable(false)
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).create().show();
                }
            });
            alertDialog.setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //EJEMPLO TOAST   -- Toast.makeText(contexto.getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                }
            });
            alertDialog.show();

        }

    }
}


