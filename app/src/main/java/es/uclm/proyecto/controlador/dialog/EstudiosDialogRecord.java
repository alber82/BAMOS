package es.uclm.proyecto.controlador.dialog;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import es.uclm.proyecto.controlador.fragments.AnimalesFragment;
import es.uclm.proyecto.R;
import es.uclm.proyecto.controlador.fragments.RecordFragment;
import es.uclm.proyecto.modelo.Estudio;
import es.uclm.proyecto.modelo.Investigador;
import es.uclm.proyecto.modelo.Tipo_test;
import es.uclm.proyecto.sqlite.ContratoBBDD;

/**
 * Created by alber on 26/04/2016.
 */
public class EstudiosDialogRecord extends Fragment implements View.OnClickListener,View.OnTouchListener{
   // private StudyRecordDialogListener callback;
    private Estudio estudio;
    private String animalId;
    private String animalCodigo;
    private String animalFechaCir;
    private String animalEspecie;
    private String animalIdEspecie;


    String estudioTiempo;

    private TextView tvCodigoAnimal;
    private TextView tvEspecie;
    private EditText etFecha;
    private EditText etHora;
    private Spinner  spInvestigador1;
    private Spinner  spInvestigador2;
    private Spinner  spTipoTest;

    ArrayList<Investigador> ALInvestigadores;
    ArrayList<String> ALSInvestigadores;
    ArrayAdapter<String> aaspInvestigadores;

    ArrayList<Tipo_test> ALTipoTest;
    ArrayList<String> ALSTipoTest;
    ArrayAdapter<String> aaspTipoTest;


/*    public interface StudyRecordDialogListener{
        void onFinishEditDialog(Estudio estudio);
    }*/

    public EstudiosDialogRecord(){

    }

    @SuppressLint("ValidFragment")
    public EstudiosDialogRecord(String animalId){
    this.animalId = animalId;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Bundle argumentos = getArguments();
        //animalId = argumentos.getString("_id");

        //setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Theme_AppCompat_Light_DarkActionBar);
        //android.R.style.Theme_Holo_Light_DarkActionBar);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_record_study, null);

        Cursor cAnimal = getActivity().getContentResolver().query(ContratoBBDD.Animales.crearUriAnimal(animalId),null,null,null,null);

        cAnimal.moveToFirst();
        while (!cAnimal.isAfterLast()) {
            animalCodigo        = cAnimal.getString(cAnimal.getColumnIndex("codigo"));
            animalFechaCir      = cAnimal.getString(cAnimal.getColumnIndex("fecha_cir"));
            animalEspecie       = cAnimal.getString(cAnimal.getColumnIndex("descripcion"));
            animalIdEspecie     = cAnimal.getString(cAnimal.getColumnIndex("id_especie"));
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
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //InputMethodManager imm = (InputMethodManager) getActivity().getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);

//        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
//        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
        View view = inflater.inflate(R.layout.dialog_record_study, container, false);



        tvCodigoAnimal = (TextView)  view.findViewById(R.id.etvrCodigoAnimal);
        tvEspecie  = (TextView)  view.findViewById(R.id.etvrEspecie);
        etFecha = (EditText)  view.findViewById(R.id.etvrFecha);
        etHora = (EditText)  view.findViewById(R.id.etvrHora);
        spInvestigador1 = (Spinner) view.findViewById(R.id.sprInvestigador1);
        spInvestigador2 = (Spinner) view.findViewById(R.id.sprInvestigador2);
        spTipoTest = (Spinner) view.findViewById(R.id.sprTipoTest);

        aaspInvestigadores = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.simple_spinner_item_perso, ALSInvestigadores);
        aaspInvestigadores.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spInvestigador1.setAdapter(aaspInvestigadores);
        spInvestigador2.setAdapter(aaspInvestigadores);

        aaspTipoTest = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.simple_spinner_item_perso, ALSTipoTest);
        aaspTipoTest.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spTipoTest.setAdapter(aaspTipoTest);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            etHora.setShowSoftInputOnFocus(false);
            tvCodigoAnimal.setShowSoftInputOnFocus(false);
            tvEspecie.setShowSoftInputOnFocus(false);
            etFecha.setShowSoftInputOnFocus(false);
        }

        tvCodigoAnimal.setText(animalCodigo);
        tvEspecie.setText(animalEspecie);

        etFecha.setOnTouchListener(this);
        etHora.setOnTouchListener(this);

         Calendar cal = Calendar.getInstance();

        int mYear, mMonth, mDay, mHour, mMinute;
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);
        mHour =cal.get(Calendar.HOUR_OF_DAY);
        mMinute =cal.get(Calendar.MINUTE);

        try {
            String dt;
            dt = mDay + "/" + (mMonth + 1) + "/" + mYear;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); // Set your date format
            Date currentDt =  sdf.parse(dt);
            String d = sdf.format(currentDt);
            etFecha.setText(d);
            Date fechaCirugia = sdf.parse(String.valueOf(animalFechaCir));
            long tiempo = (currentDt.getTime() - fechaCirugia.getTime()) / ( 24 * 60 * 60 * 1000);
            estudioTiempo = tiempo +"";


            dt = mHour + ":" + mMinute;
            sdf = new SimpleDateFormat("HH:mm"); // Set your date format
            currentDt = sdf.parse(dt);
            d = sdf.format(currentDt);
            etHora.setText(d);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_sino, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        android.app.AlertDialog.Builder alertDialog;
        switch (id) {
            case android.R.id.home:
                // procesarDescartar()
                break;
            case R.id.action_about:
                android.support.v4.app.FragmentTransaction fragmentTransaction1 = getActivity().getSupportFragmentManager().beginTransaction();
                DialogAbout dialogAbout= new DialogAbout();
                dialogAbout.show(fragmentTransaction1, "fragment_alert");
                break;
            case R.id.action_back:
                alertDialog = new android.app.AlertDialog.Builder(getActivity());
                alertDialog.setTitle(R.string.dialog_warning);
                alertDialog.setMessage(R.string.return_animal_string);
                alertDialog.setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        //callback.onFinishEditDialog(estudio);
                        AnimalesFragment AnimalFragment = new AnimalesFragment();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame,AnimalFragment);
                        fragmentTransaction.commit();

                    }
                });
                alertDialog.setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //EJEMPLO TOAST   -- Toast.makeText(contexto.getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });
                alertDialog.show();
                break;
            case R.id.action_done:

                int id_animal = Integer.parseInt(animalId);
                int id_investigador1= 0;
                int id_investigador2= 0;
                int id_tipo_test =0;

                String estudioFecha = etFecha.getText().toString();
                String estudioHora = etHora.getText().toString();
                String strTipoTest   =   spTipoTest.getSelectedItem().toString();
                String strInvestigador1  =   spInvestigador1.getSelectedItem().toString();
                String strInvestigador2  =   spInvestigador2.getSelectedItem().toString();

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
                estudio = new Estudio(id_animal,estudioFecha,estudioHora,Integer.valueOf(estudioTiempo),id_investigador1,id_investigador2,id_tipo_test,0,0,0,null,null);


                alertDialog = new android.app.AlertDialog.Builder(getActivity());
                alertDialog.setTitle(R.string.new_study_string);
                alertDialog.setMessage(R.string.study_correct_string);
                alertDialog.setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        RecordFragment RecordFragment = new RecordFragment(animalId,estudio);
                        android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame,RecordFragment);
                        fragmentTransaction.commit();

                    }
                });
                alertDialog.setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //EJEMPLO TOAST   -- Toast.makeText(contexto.getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });
                alertDialog.show();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        android.app.AlertDialog.Builder alertDialog;
        switch (id) {
            default:

                break;
        }

    }

    public boolean onTouch(View v, MotionEvent event) {
        int mYear, mMonth, mDay, mHour, mMinute;
        Log.d("arriba", "log");
        final Calendar c = Calendar.getInstance();
        //c.setTimeZone(TimeZone.getTimeZone("Europe/Madrid"));

        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour =c.get(Calendar.HOUR);
        mMinute =c.get(Calendar.MINUTE);

        int id = v.getId();
        switch (id) {
            case R.id.etvrFecha:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        DatePickerDialog dpd = new DatePickerDialog(getActivity(),
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
                                            etFecha.setText(d);

                                            Date fechaCirugia = sdf.parse(String.valueOf(animalFechaCir));

                                            long tiempo = (currentDt.getTime() - fechaCirugia.getTime()) / ( 24 * 60 * 60 * 1000);

                                            estudioTiempo = tiempo +"";
                                            //Date tiempoFecha = new Date(suma);
                                            //String res = sdf.format(tiempoFecha);
                                            //etTiempo.setText(""+resta   );

                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        //etFechaNac.setText(dayOfMonth + "-"+ (monthOfYear + 1) + "-" + year);
                                    }
                                }, mYear, mMonth, mDay);
                        dpd.show();
                        break;
                    case MotionEvent.ACTION_UP:
                        etFecha.requestFocus();
                        break;
                }
                return  true;
            case R.id.etvrHora:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        TimePickerDialog tpd = new TimePickerDialog(getActivity(),
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
                                            etHora.setText(d);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }


                                }, mHour, mMinute, true);
                        tpd.show();
                        break;
                    case MotionEvent.ACTION_UP:
                        etHora.requestFocus();
                        break;
                }
                return true;

        }

        return false;
    }
}
