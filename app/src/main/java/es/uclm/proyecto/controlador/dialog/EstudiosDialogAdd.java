package es.uclm.proyecto.controlador.dialog;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import es.uclm.proyecto.controlador.fragments.AnimalesEstudiosFragment;
import es.uclm.proyecto.R;
import es.uclm.proyecto.modelo.Investigador;
import es.uclm.proyecto.modelo.Resultado;
import es.uclm.proyecto.modelo.Tipo_test;
import es.uclm.proyecto.sqlite.ContratoBBDD;
import es.uclm.proyecto.util.DismissKeyboardListener;
import es.uclm.proyecto.util.Utils;

/**
 * Created by alber on 24/04/2016.
 */
public class EstudiosDialogAdd extends DialogFragment implements View.OnClickListener,View.OnTouchListener {

    private EditText etsFecha;
    private EditText etsHora;
    private TextView etsTiempo;
    private Spinner spsinvestigador1;
    private Spinner spsinvestigador2;
    private Spinner spstipotest;
    private TextView etsResultado;
    private TextView etsScoreL;
    private TextView etsScoreR;
    private EditText etsComentarios;

    private int guardado = 0;

    //Ankle Movement
    private Spinner spsaml;
    private Spinner spsamr;
    //Plan W/O Supp
    private Switch swspwol;
    private Switch swspwor;
    //Plan W Supp
    private Switch swspwl;
    private Switch swspwr;
    //Stepping Dorsal
    private Spinner spssdl;
    private Spinner spssdr;
    //Stepping Plantar
    private Spinner spsspl;
    private Spinner spsspr;
    // Paw possition IC
    private Spinner spsppicl;
    private Spinner spsppicr;
    // Paw possition IC
    private Spinner spspplol;
    private Spinner spspplor;

    private Spinner spscoor;
    private Spinner spsti;
    private Spinner spstail;

    private String animalId;
    private String animalCodigo;
    private String animalFechaCir;
    private String animalIdEspecie;
    private String animalEspecie;

    ArrayList<Tipo_test> ALTipoTest;
    ArrayList<String> ALSTipoTest;
    ArrayAdapter<String> aaspTipoTest;

    ArrayList<Investigador> ALInvestigadores;
    ArrayList<String> ALSInvestigadores;
    ArrayAdapter<String> aaspInvestigadores;

    public EstudiosDialogAdd() {
        // Required empty public constructor
    }

    public EstudiosDialogAdd(String animal) {
        this.animalId =animal;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Theme_AppCompat_Light_DarkActionBar);
        //android.R.style.Theme_Holo_Light_DarkActionBar);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_add_study, null);


        Cursor cAnimal = getActivity().getContentResolver().query(ContratoBBDD.Animales.crearUriAnimal(animalId),null,null,null,null);

        cAnimal.moveToFirst();
        while (!cAnimal.isAfterLast()) {
            animalCodigo        = cAnimal.getString(cAnimal.getColumnIndex("codigo"));
            animalFechaCir      = cAnimal.getString(cAnimal.getColumnIndex("fecha_cir"));
            animalIdEspecie       = cAnimal.getString(cAnimal.getColumnIndex("id_especie"));
            animalEspecie       = cAnimal.getString(cAnimal.getColumnIndex("descripcion"));


            cAnimal.moveToNext();
        }
        cAnimal.close();

        ALTipoTest = new ArrayList<Tipo_test>();
        ALSTipoTest= new ArrayList<String>();

        Cursor cTipoTest = getActivity().getContentResolver().query(ContratoBBDD.Tipo_Tests.crearUriTipoTestEspecie(animalIdEspecie), null, null, null, null);

        cTipoTest.moveToFirst();
        while (!cTipoTest.isAfterLast()) {
            Tipo_test tipoTest = new Tipo_test (cTipoTest.getInt(cTipoTest.getColumnIndex("_id")), cTipoTest.getString(cTipoTest.getColumnIndex("TIPO_TEST_DES")),
                    cTipoTest.getInt(cTipoTest.getColumnIndex("id_especie")));
            ALTipoTest.add(tipoTest);
            ALSTipoTest.add(cTipoTest.getString(cTipoTest.getColumnIndex("TIPO_TEST_DES")));
            cTipoTest.moveToNext();
        }
        cTipoTest.close();

        ALInvestigadores = new ArrayList<Investigador>();
        ALSInvestigadores = new ArrayList<String>();

        Cursor cInvestigadores = getActivity().getContentResolver().query(ContratoBBDD.Investigadores.URI_CONTENIDO, null, null, null, null);

        cInvestigadores.moveToFirst();
        while (!cInvestigadores.isAfterLast()) {
            Investigador investigador = new Investigador(
                    cInvestigadores.getInt(cInvestigadores.getColumnIndex("_id")),
                    cInvestigadores.getString(cInvestigadores.getColumnIndex("nombre")),
                    cInvestigadores.getString(cInvestigadores.getColumnIndex("apellidos")),
                    cInvestigadores.getString(cInvestigadores.getColumnIndex("email")));

            ALInvestigadores.add(investigador);
            ALSInvestigadores.add(cInvestigadores.getString(cInvestigadores.getColumnIndex("nombre")) + " " + cInvestigadores.getString(cInvestigadores.getColumnIndex("apellidos")));
            cInvestigadores.moveToNext();
        }
        cInvestigadores.close();

        setHasOptionsMenu(true);
        ActionBar actionBar = ((AppCompatActivity) getActivity())
                .getSupportActionBar();
        actionBar.setTitle(R.string.animal_study_add);


}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        InputMethodManager imm = (InputMethodManager) getActivity().getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);

        View view = inflater.inflate(R.layout.dialog_add_study, container, false);

        setHasOptionsMenu(true);

        LinearLayout Parent = (LinearLayout) view.findViewById(R.id.add_study_layout);
        Parent.setOnClickListener(new DismissKeyboardListener(getActivity()));

        final Toolbar actionBar = (Toolbar) view.findViewById(R.id.fake_action_bar);
        if (actionBar != null) {
            final EstudiosDialogAdd window = this;


            actionBar.setTitleTextColor(R.color.white);
            actionBar.inflateMenu(R.menu.menu_aceptar);
            actionBar.setNavigationOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int id = v.getId();
                    switch (id) {
                        default:
                            if(guardado==0) {
                                new AlertDialog.Builder(getActivity())
                                        .setTitle(R.string.dialog_exit)
                                        .setMessage(R.string.dialog_exit_study)
                                        .setNegativeButton(R.string.dialog_no, null)
                                        .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {

                                            public void onClick(DialogInterface arg0, int arg1) {
                                                AnimalesEstudiosFragment AnimalesEstudiosFragment = new AnimalesEstudiosFragment(getActivity(), animalId);
                                                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                                fragmentTransaction.replace(R.id.frame, AnimalesEstudiosFragment, "FragmentEstudioAnimal");
                                                fragmentTransaction.commit();
                                                window.dismiss();
                                            }
                                        }).create().show();
                            }
                            else {
                                AnimalesEstudiosFragment AnimalesEstudiosFragment = new AnimalesEstudiosFragment(getActivity(), animalId);
                                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.frame, AnimalesEstudiosFragment, "FragmentEstudioAnimal");
                                fragmentTransaction.commit();
                                window.dismiss();

                            }
                            break;
                    }


                }
            });
            actionBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int id = item.getItemId();
                    switch (id) {
                        case R.id.action_done:
                            nuevoEstudio();
                            guardado = 1;
                            AnimalesEstudiosFragment AnimalesEstudiosFragment = new AnimalesEstudiosFragment(getActivity(), animalId);
                            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.frame, AnimalesEstudiosFragment, "FragmentEstudioAnimal");
                            fragmentTransaction.commit();
                            window.dismiss();

                            break;
                        case R.id.action_about:
                            android.support.v4.app.FragmentTransaction fragmentTransaction1 = getActivity().getSupportFragmentManager().beginTransaction();
                            DialogAbout dialogAbout= new DialogAbout();
                            dialogAbout.show(fragmentTransaction1, "fragment_alert");
                            break;
                    }
                    return true;
                }
            });
        }
        //actionBar.inflateMenu(R.menu.menu_add);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        etsFecha            = (EditText) view.findViewById(R.id.etsFecha);
        etsHora             = (EditText) view.findViewById(R.id.etsHora);
        etsTiempo           = (TextView) view.findViewById(R.id.etsTiempo);
        spsinvestigador1    = (Spinner) view.findViewById(R.id.spsinvestigador1);
        spsinvestigador2    = (Spinner) view.findViewById(R.id.spsinvestigador2);
        spstipotest         = (Spinner) view.findViewById(R.id.spstipotest);
        etsResultado        = (TextView) view.findViewById(R.id.etsResultado);
        etsScoreL           = (TextView) view.findViewById(R.id.etsScoreL);
        etsScoreR           = (TextView) view.findViewById(R.id.etsScoreR);

        etsComentarios      = (EditText) view.findViewById(R.id.etsComentario);
        etsComentarios.setText("");
        spsaml              = (Spinner) view.findViewById(R.id.spsaml);
        spsamr              = (Spinner) view.findViewById(R.id.spsamr);
        swspwol             = (Switch) view.findViewById(R.id.swspwol);
        swspwor             = (Switch) view.findViewById(R.id.swspwor);
        swspwl              = (Switch) view.findViewById(R.id.swspwl);
        swspwr              = (Switch) view.findViewById(R.id.swspwr);

        spssdl              =(Spinner) view.findViewById(R.id.spssdl);
        spssdr              =(Spinner) view.findViewById(R.id.spssdr);

        spsspl              =(Spinner) view.findViewById(R.id.spsspl);
        spsspr              =(Spinner) view.findViewById(R.id.spsspr);

        spsppicl            = (Spinner) view.findViewById(R.id.spsppicl);
        spsppicr            = (Spinner) view.findViewById(R.id.spsppicr);

        spspplol            = (Spinner) view.findViewById(R.id.spspplol);
        spspplor            = (Spinner) view.findViewById(R.id.spspplor);
        spscoor             = (Spinner) view.findViewById(R.id.spscoor);
        spsti               = (Spinner) view.findViewById(R.id.spstrunk);
        spstail             = (Spinner) view.findViewById(R.id.spstail);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            etsFecha.setShowSoftInputOnFocus(false);
            etsHora.setShowSoftInputOnFocus(false);
            etsTiempo.setShowSoftInputOnFocus(false);
        }


        aaspInvestigadores = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.simple_spinner_item_perso, ALSInvestigadores);
        aaspInvestigadores.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spsinvestigador1.setAdapter(aaspInvestigadores);
        spsinvestigador2.setAdapter(aaspInvestigadores);

        aaspTipoTest = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.simple_spinner_item_perso, ALSTipoTest);
        aaspTipoTest.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spstipotest.setAdapter(aaspTipoTest);

        etsFecha.setOnTouchListener(this);
        etsHora.setOnTouchListener(this);

    return view;
}


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.fullscreen_dialog, menu);
        inflater.inflate(R.menu.menu_add,menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                // procesarDescartar()
                break;
            case R.id.action_add_animal:
                Log.d(" Botonzaco de arriba","");
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

    public boolean onTouch(View v, MotionEvent event) {
        int mYear, mMonth, mDay, mHour, mMinute;
        Log.d("arriba", "log");
        final Calendar c = Calendar.getInstance();

        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour =c.get(Calendar.HOUR_OF_DAY);
        mMinute =c.get(Calendar.MINUTE);

        int id = v.getId();
        switch (id) {
            case R.id.etsFecha:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
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
                                            etsFecha.setText(d);

                                            Date fechaCirugia = sdf.parse(String.valueOf(animalFechaCir));
                                            long resta = (currentDt.getTime() - fechaCirugia.getTime()) / ( 24 * 60 * 60 * 1000);
                                            etsTiempo.setText(""+resta   );

                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, mYear, mMonth, mDay);
                        dpd.show();
                        break;
                    case MotionEvent.ACTION_UP:
                        etsFecha.clearFocus();//.requestFocus();
                        break;
                }
                return  true;
            case R.id.etsHora:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        TimePickerDialog tpd = new TimePickerDialog(getDialog().getContext(),
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        try {
                                        String dt;
                                        dt = hourOfDay + ":" + minute;
                                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm"); // Set your date format
                                        Date currentDt = null;
                                        currentDt = sdf.parse(dt);
                                        String d = sdf.format(currentDt);
                                        etsHora.setText(d);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }


                                }, mHour, mMinute, true);
                        tpd.show();
                        break;
                    case MotionEvent.ACTION_UP:
                        etsHora.clearFocus();//.requestFocus();
                        break;
                }
                return true;
        }
        return false;
    }

    public void nuevoEstudio(){
        int id_tipo_test = 0;
        int id_investigador1 = 0;
        int id_investigador2 = 0;
        String _id;
        Uri insercion;


        String strsFecha = etsFecha.getText().toString();
        String strsHora = etsHora.getText().toString();
        String strsTiempo = etsTiempo.getText().toString();
        String strsComentario = etsComentarios.getText().toString();

        String strInvestigador1  =   spsinvestigador1.getSelectedItem().toString();
        String strInvestigador2  =   spsinvestigador2.getSelectedItem().toString();
        String strTipoTest  =   spstipotest.getSelectedItem().toString();

        String strVideo = "";

        /// CALCULAR RESULTADO
        //etsResultado

        String strsResultado  =   etsResultado.getText().toString();
        String strsScoreL;
        String strsScoreR;

        String strsaml = spsaml.getSelectedItem().toString();
        String strsamr = spsamr.getSelectedItem().toString();
        String strspwol;
        if(swspwol.isChecked()) {   strspwol ="S";
        }else{                      strspwol ="N";  }

        String strspwor;
        if(swspwor.isChecked()) { strspwor ="S";
        }else{                    strspwor ="N";    }

        String strspwl;
        if(swspwl.isChecked()) {    strspwl ="S";
        }else{                      strspwl ="N";   }

        String strspwr;
        if(swspwr.isChecked()) {    strspwr ="S";
        }else{                      strspwr ="N";  }

        String strssdl  =spssdl.getSelectedItem().toString();
        String strssdr  =spssdr.getSelectedItem().toString();

        String strsspl  =spsspl.getSelectedItem().toString();
        String strsspr  =spsspr.getSelectedItem().toString();

        String strsppicl =spsppicl.getSelectedItem().toString();
        String strsppicr =spsppicr.getSelectedItem().toString();

        String strspplol =spspplol.getSelectedItem().toString();
        String strspplor =spspplor.getSelectedItem().toString();

        String strscoor  =spscoor.getSelectedItem().toString();
        String strsti   =spsti.getSelectedItem().toString();
        String strstail =spstail.getSelectedItem().toString();

        // Declaramos el Iterador e imprimimos los Elementos del ArrayList
        Iterator<Tipo_test> tipoTestIterator = ALTipoTest.iterator();
        while(tipoTestIterator.hasNext()) {
            Tipo_test tipo_test = tipoTestIterator.next();
            if (tipo_test.descripcion.equals(strTipoTest)){
                id_tipo_test = tipo_test._id;
                Log.d("A", "" + id_tipo_test);
            }
        }

        Iterator<Investigador> InvestigadorIterator1 = ALInvestigadores.iterator();
        while(InvestigadorIterator1.hasNext()){
            Investigador investigador = InvestigadorIterator1.next();
            if ((investigador.nombre  + " " + investigador.apellidos).equals(strInvestigador1)) {
                id_investigador1 = investigador._id;
                Log.d("A", ""+id_investigador1);
            }
        }

        Iterator<Investigador> InvestigadorIterator2 = ALInvestigadores.iterator();
        while(InvestigadorIterator2.hasNext()){
            Investigador investigador = InvestigadorIterator2.next();
            if ((investigador.nombre  + " " + investigador.apellidos).equals( strInvestigador2)) {
                id_investigador2 = investigador._id;
                Log.d("A", ""+id_investigador2);
            }
        }

        if (strsFecha.equals("") ||
                strsHora.equals("")  ) {
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
        }else {
            Log.d("OK", "A");

            ContentValues cvEstudio = new ContentValues();
            cvEstudio.put(ContratoBBDD.Estudios.id_animal,animalId);
            cvEstudio.put(ContratoBBDD.Estudios.fecha,strsFecha);
            cvEstudio.put(ContratoBBDD.Estudios.hora,strsHora);
            cvEstudio.put(ContratoBBDD.Estudios.tiempo,strsTiempo);
            cvEstudio.put(ContratoBBDD.Estudios.id_investigador1,id_investigador1);
            cvEstudio.put(ContratoBBDD.Estudios.id_investigador2,id_investigador2);
            cvEstudio.put(ContratoBBDD.Estudios.id_tipo_test,id_tipo_test);
            cvEstudio.put(ContratoBBDD.Estudios.comentarios,strsComentario);
            cvEstudio.put(ContratoBBDD.Estudios.video,strVideo);

            ContentValues cvResultado = new ContentValues();
            cvResultado.put(ContratoBBDD.Resultados.am_l ,strsaml);
            cvResultado.put(ContratoBBDD.Resultados.am_r ,strsamr);
            cvResultado.put(ContratoBBDD.Resultados.pwo_l,strspwol);
            cvResultado.put(ContratoBBDD.Resultados.pwo_r,strspwor);
            cvResultado.put(ContratoBBDD.Resultados.pw_l,strspwl);
            cvResultado.put(ContratoBBDD.Resultados.pw_r,strspwr);
            cvResultado.put(ContratoBBDD.Resultados.sd_l,strssdl);
            cvResultado.put(ContratoBBDD.Resultados.sd_r,strssdr);
            cvResultado.put(ContratoBBDD.Resultados.sp_l,strsspl);
            cvResultado.put(ContratoBBDD.Resultados.sp_r,strsspr);
            cvResultado.put(ContratoBBDD.Resultados.pp_ic_l,strsppicl);
            cvResultado.put(ContratoBBDD.Resultados.pp_ic_r,strsppicr);
            cvResultado.put(ContratoBBDD.Resultados.pp_lo_l,strspplol);
            cvResultado.put(ContratoBBDD.Resultados.pp_lo_r,strspplor);
            cvResultado.put(ContratoBBDD.Resultados.coord,strscoor);
            cvResultado.put(ContratoBBDD.Resultados.ti,strsti);
            cvResultado.put(ContratoBBDD.Resultados.tail,strstail);

            Resultado resultado = new Resultado(strsaml,strsamr,strspwol,strspwor,strspwl,strspwr,strssdl,strssdr
                    ,strsspl,strsspr,strscoor,strsppicl,strsppicr,strspplol,strspplor,strsti,strstail);

            strsResultado = "" + Utils.calcularSubscore(resultado);
            strsScoreL    = "" + Utils.calcularScore(resultado, "L");
            strsScoreR    = "" + Utils.calcularScore(resultado, "R");
            cvEstudio.put(ContratoBBDD.Estudios.resultado,strsResultado);
            cvEstudio.put(ContratoBBDD.Estudios.scorel, strsScoreL);
            cvEstudio.put(ContratoBBDD.Estudios.scorer, strsScoreR);
            etsResultado.setText(strsResultado);
            etsScoreL.setText(strsScoreL);
            etsScoreR.setText(strsScoreR);

            insercion = getActivity().getContentResolver().insert(ContratoBBDD.Estudios.URI_CONTENIDO, cvEstudio);
            _id= ContratoBBDD.Estudios.obtenerCodigoEstudio(insercion);

            cvResultado.put(ContratoBBDD.Resultados._id, _id);

            insercion = getActivity().getContentResolver().insert(ContratoBBDD.Resultados.URI_CONTENIDO, cvResultado);
            _id= ContratoBBDD.Resultados.obtenerCodigoResultado(insercion);

            new AlertDialog.Builder(getDialog().getContext())
                    .setTitle(R.string.dialog_warning)
                    .setMessage(R.string.dialog_study_record)
                    .setCancelable(false)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create().show();
        }
    }
}


