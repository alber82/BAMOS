package es.uclm.proyecto.controlador.dialog;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.VideoView;

import com.itextpdf.text.DocumentException;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import es.uclm.proyecto.controlador.fragments.AnimalesEstudiosFragment;
import es.uclm.proyecto.R;
import es.uclm.proyecto.modelo.Estudio;
import es.uclm.proyecto.modelo.Investigador;
import es.uclm.proyecto.modelo.Resultado;
import es.uclm.proyecto.modelo.Tipo_test;
import es.uclm.proyecto.sqlite.ContratoBBDD;
import es.uclm.proyecto.util.DismissKeyboardListener;
import es.uclm.proyecto.util.Utils;

/**
 * Created by alber on 25/04/2016.
 */
public class EstudiosDialogEdit extends Fragment implements View.OnClickListener,View.OnTouchListener {

    String studyId;


    private EditText etsFecha;
    private EditText etsHora;
    private TextView etsTiempo;
    private Spinner spsinvestigador1;
    private Spinner spsinvestigador2;
    private Spinner spstipotest;
    private TextView etsResultado;
    private TextView etsScores;
    private EditText etsComentarios;

    private Button btn_play;
    private Button btn_pause;

    private String estudioFecha;
    private String estudioHora;
    private String estudioTiempo;
    private String estudioinvestigador1;
    private String estudioinvestigador2;
    private String estudiotipotest;
    private String estudioScoreL;
    private String estudioScoreR;
    private String estudioResultado;
    private String estudioVideo ="";
    private String estudioComentarios;

    private String animalId;
    private String animalCodigo;
    private String animalFechaCir;
    private String animalIdEspecie;
    private String animalEspecie;
    private String animalExperimento;
    private String animalOrigen;

    private String resultadoaml;
    private String resultadoamr;
    private String resultadopwol;
    private String resultadopwor;
    private String resultadopwl;
    private String resultadopwr;
    private String resultadosdl;
    private String resultadosdr;
    private String resultadospl;
    private String resultadospr;
    private String resultadoppicl;
    private String resultadoppicr;
    private String resultadopplol;
    private String resultadopplor;
    private String resultadocoor;
    private String resultadoti;
    private String resultadotail;

    private Estudio estudio;
    private Resultado resultado;
    private Resultado resultadovuelta;

    VideoView vSurface;
    private int position = 0;
    private ProgressDialog progressDialog;
    private MediaController mediaControls;

    ArrayList<Tipo_test> ALTipoTest;
    ArrayList<String> ALSTipoTest;
    ArrayAdapter<String> aaspTipoTest;

    ArrayList<Investigador> ALInvestigadores;
    ArrayList<String> ALSInvestigadores;
    ArrayAdapter<String> aaspInvestigadores;

    public EstudiosDialogEdit() {
        // Required empty public constructor
    }

    public EstudiosDialogEdit(String studyId) {
        // Required empty public constructor
        this.studyId=studyId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_edit_study, null);

        Cursor cEstudioResultado = getActivity().getContentResolver().query(ContratoBBDD.Estudios.crearUriParaResultados(studyId), null, null, null, null);

        cEstudioResultado.moveToFirst();
        while (!cEstudioResultado.isAfterLast()) {

            animalId            = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("id_animal"));
            estudioFecha        = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("fecha"));
            estudioHora         = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("hora"));
            estudioTiempo       = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("tiempo"));
            estudioinvestigador1 = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("INV1_NOMBRE")) + " " + cEstudioResultado.getString(cEstudioResultado.getColumnIndex("INV1_AP"));
            estudioinvestigador2 = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("INV2_NOMBRE")) + " " + cEstudioResultado.getString(cEstudioResultado.getColumnIndex("INV2_AP"));
            estudiotipotest  = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("TIPO_TEST_DES"));
            estudioResultado = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("resultado"));
            estudioScoreL = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("scorel"));
            estudioScoreR= cEstudioResultado.getString(cEstudioResultado.getColumnIndex("scorer"));
            estudioVideo = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("video"));
            estudioComentarios = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("comentarios"));

            resultadoaml  = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("am_l"));
            resultadoamr  = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("am_r"));
            resultadopwol = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("pwo_l"));
            resultadopwor = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("pwo_r"));
            resultadopwl = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("pw_l"));
            resultadopwr = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("pw_r"));
            resultadosdl  = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("sd_l"));
            resultadosdr   = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("sd_r"));
            resultadospl   = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("sp_l"));
            resultadospr = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("sp_r"));
            resultadoppicl = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("pp_ic_l"));
            resultadoppicr = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("pp_ic_r"));
            resultadopplol = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("pp_lo_l"));
            resultadopplor = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("pp_lo_r"));
            resultadocoor = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("coord"));
            resultadoti = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("ti"));
            resultadotail = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("tail"));

            cEstudioResultado.moveToNext();
        }
        cEstudioResultado.close();

        Cursor cAnimal = getActivity().getContentResolver().query(ContratoBBDD.Animales.crearUriAnimal(animalId),null,null,null,null);

        cAnimal.moveToFirst();
        while (!cAnimal.isAfterLast()) {
            animalCodigo    = cAnimal.getString(cAnimal.getColumnIndex("codigo"));
            animalFechaCir  = cAnimal.getString(cAnimal.getColumnIndex("fecha_cir"));
            animalEspecie   = cAnimal.getString(cAnimal.getColumnIndex("descripcion"));
            animalIdEspecie  = cAnimal.getString(cAnimal.getColumnIndex("id_especie"));
            animalExperimento  = cAnimal.getString(cAnimal.getColumnIndex("experimento"));
            animalOrigen  = cAnimal.getString(cAnimal.getColumnIndex("origen"));
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
        actionBar.setTitle(R.string.animal_study_edit);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        InputMethodManager imm = (InputMethodManager) getActivity().getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);

        View view = inflater.inflate(R.layout.dialog_edit_study, container, false);

        LinearLayout Parent = (LinearLayout) view.findViewById(R.id.edit_study_layout);
        Parent.setOnClickListener(new DismissKeyboardListener(getActivity()));

        int id_resultado = Integer.valueOf(studyId);

        etsFecha            = (EditText) view.findViewById(R.id.etsFecha);
        etsHora             = (EditText) view.findViewById(R.id.etsHora);
        etsTiempo           = (TextView) view.findViewById(R.id.etsTiempo);
        spsinvestigador1    = (Spinner) view.findViewById(R.id.spsInvestigador1);
        spsinvestigador2    = (Spinner) view.findViewById(R.id.spsInvestigador2);
        spstipotest         = (Spinner) view.findViewById(R.id.spsTipoTest);
        etsResultado        = (TextView) view.findViewById(R.id.etsResultado);
        etsScores           = (TextView) view.findViewById(R.id.etsScore);
        etsComentarios      = (EditText) view.findViewById(R.id.etsComentario);

        btn_play = (Button) view.findViewById(R.id.btn_play);
        btn_pause = (Button) view.findViewById(R.id.btn_pause);

        aaspInvestigadores = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.simple_spinner_item_perso, ALSInvestigadores);
        aaspInvestigadores.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spsinvestigador1.setAdapter(aaspInvestigadores);
        spsinvestigador2.setAdapter(aaspInvestigadores);

        aaspTipoTest = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.simple_spinner_item_perso, ALSTipoTest);
        aaspTipoTest.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spstipotest.setAdapter(aaspTipoTest);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            etsHora.setShowSoftInputOnFocus(false);
            etsFecha.setShowSoftInputOnFocus(false);
        }

        etsFecha.setOnTouchListener(this);
        etsHora.setOnTouchListener(this);

        btn_play .setOnClickListener(this);
        btn_pause.setOnClickListener(this);

        etsFecha.setText(estudioFecha);
        etsHora.setText(estudioHora);
        etsTiempo.setText(estudioTiempo);
        etsResultado.setText(estudioResultado);
        etsScores.setText(estudioScoreL + " / "+ estudioScoreR);
        etsComentarios.setText(estudioComentarios);

        spstipotest.setSelection(((ArrayAdapter) spstipotest.getAdapter()).getPosition(estudiotipotest));
        spsinvestigador1.setSelection(((ArrayAdapter) spsinvestigador1.getAdapter()).getPosition(estudioinvestigador1));
        spsinvestigador2.setSelection(((ArrayAdapter) spsinvestigador2.getAdapter()).getPosition(estudioinvestigador2));


        vSurface = (VideoView) view.findViewById(R.id.videoview);

        if(estudioVideo.equals("")){
            RelativeLayout video = (RelativeLayout) view.findViewById(R.id.videoLayout);
            video.setVisibility(View.INVISIBLE);
        }

        /*MediaController mediaController = new MediaController(getActivity().getApplicationContext());
        mediaController.setAnchorView(vSurface);
        vSurface.setMediaController(mediaController);
*/
        /*if(estudioVideo.equals(""))
             vSurface.setVideoPath("/storage/sdcard/DCIM/proyecto/prueba1554548802.3gp");
        //vSurface.setVideoPath("/data/media/prueba1554548802.3gp");
        else vSurface.setVideoPath(estudioVideo);
       // vSurface.start();*/
        if(!estudioVideo.equals("")) vSurface.setVideoPath(estudioVideo);
        //MediaController controller = new MediaController(getActivity().getApplicationContext());
        //vSurface.setMediaController(controller);
        //controller.setAnchorView(controller);
        //Estudio = new Estudio(studyId,animalId,estudioFecha,estudioHora,estudioTiempo)
        resultado = new Resultado(id_resultado,resultadoaml,resultadoamr,resultadopwol,resultadopwor,resultadopwl,resultadopwr,
                resultadosdl,resultadosdr,resultadospl,resultadospr,resultadocoor,resultadoppicl,resultadoppicr,resultadopplol,
                resultadopplor,resultadoti,resultadotail);

        return view;
    }

    @Override

    public void onSaveInstanceState(Bundle savedInstanceState) {
            super.onSaveInstanceState(savedInstanceState);
        //we use onSaveInstanceState in order to store the video playback position for orientation change
        savedInstanceState.putInt("Position", vSurface.getCurrentPosition());

        vSurface.pause();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.fullscreen_dialog, menu);
        inflater.inflate(R.menu.menu_detalles_estudio, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Bundle args = new Bundle();
        args.putString("_id", studyId);
        android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                // procesarDescartar()
                break;
            case R.id.action_back:

                AnimalesEstudiosFragment AnimalesEstudiosFragment = new AnimalesEstudiosFragment(getActivity(),animalId);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame, AnimalesEstudiosFragment,"FragmentEstudioAnimal" );
                ft.commit();
                return true;

            case R.id.action_details:
                if(vSurface.isPlaying()) {
                    vSurface.pause();
                    vSurface.seekTo(1);
                }else vSurface.seekTo(1);

                Cursor cEstudioResultado = getActivity().getContentResolver().query(ContratoBBDD.Estudios.crearUriParaResultados(studyId), null, null, null, null);

                cEstudioResultado.moveToFirst();
                while (!cEstudioResultado.isAfterLast()) {

                    animalId            = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("id_animal"));
                    estudioFecha        = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("fecha"));
                    estudioHora         = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("hora"));
                    estudioTiempo       = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("tiempo"));
                    estudioinvestigador1 = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("INV1_NOMBRE")) + " " + cEstudioResultado.getString(cEstudioResultado.getColumnIndex("INV1_AP"));
                    estudioinvestigador2 = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("INV2_NOMBRE")) + " " + cEstudioResultado.getString(cEstudioResultado.getColumnIndex("INV2_AP"));
                    estudiotipotest      = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("TIPO_TEST_DES"));
                    estudioResultado = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("resultado"));
                    estudioScoreL = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("scorel"));
                    estudioScoreR= cEstudioResultado.getString(cEstudioResultado.getColumnIndex("scorer"));
                    estudioVideo = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("video"));
                    estudioComentarios = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("comentarios"));

                    resultadoaml  = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("am_l"));
                    resultadoamr  = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("am_r"));
                    resultadopwol = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("pwo_l"));
                    resultadopwor = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("pwo_r"));
                    resultadopwl = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("pw_l"));
                    resultadopwr = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("pw_r"));
                    resultadosdl  = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("sd_l"));
                    resultadosdr   = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("sd_r"));
                    resultadospl   = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("sp_l"));
                    resultadospr = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("sp_r"));
                    resultadoppicl = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("pp_ic_l"));
                    resultadoppicr = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("pp_ic_r"));
                    resultadopplol = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("pp_lo_l"));
                    resultadopplor = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("pp_lo_r"));
                    resultadocoor = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("coord"));
                    resultadoti = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("ti"));
                    resultadotail = cEstudioResultado.getString(cEstudioResultado.getColumnIndex("tail"));

                    cEstudioResultado.moveToNext();
                }
                cEstudioResultado.close();

                int id_resultado = Integer.valueOf(studyId);
                resultado = new Resultado(id_resultado,resultadoaml,resultadoamr,resultadopwol,resultadopwor,resultadopwl,resultadopwr,
                        resultadosdl,resultadosdr,resultadospl,resultadospr,resultadocoor,resultadoppicl,resultadoppicr,resultadopplol,resultadopplor,resultadoti,resultadotail);

                EstudiosDialogResultados resultadosDialog = new EstudiosDialogResultados(resultado,resultadovuelta);
                resultadosDialog.setArguments(args);
                resultadosDialog.show(fragmentTransaction, "fragment_alert");

                break;
            case R.id.action_done:
                int id_tipo_test = 0;
                int id_investigador1 = 0;
                int id_investigador2 = 0;
                String _id;
                Uri insercion;

                String strsFecha = etsFecha.getText().toString();
                String strsHora = etsHora.getText().toString();
                String strsTiempo = etsTiempo.getText().toString();
                String strsComentarios = etsComentarios.getText().toString();

                String strInvestigador1  =   spsinvestigador1.getSelectedItem().toString();
                String strInvestigador2  =   spsinvestigador2.getSelectedItem().toString();
                String strTipoTest  =   spstipotest.getSelectedItem().toString();

                /// CALCULAR RESULTADO
                String strsResultado  =   etsResultado.getText().toString();

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

                final ContentValues cvEstudio = new ContentValues();
                cvEstudio.put(ContratoBBDD.Estudios._id,studyId);
                cvEstudio.put(ContratoBBDD.Estudios.id_animal,animalId);
                cvEstudio.put(ContratoBBDD.Estudios.fecha,strsFecha);
                cvEstudio.put(ContratoBBDD.Estudios.hora,strsHora);
                cvEstudio.put(ContratoBBDD.Estudios.tiempo,strsTiempo);
                cvEstudio.put(ContratoBBDD.Estudios.id_investigador1,id_investigador1);
                cvEstudio.put(ContratoBBDD.Estudios.id_investigador2,id_investigador2);
                cvEstudio.put(ContratoBBDD.Estudios.id_tipo_test,id_tipo_test);
                cvEstudio.put(ContratoBBDD.Estudios.resultado, strsResultado);
                cvEstudio.put(ContratoBBDD.Estudios.comentarios, strsComentarios);

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle(R.string.diagog_warning_edir);
                alertDialog.setMessage(R.string.dialog_study_edit_warning);
                alertDialog.setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().getContentResolver().update(ContratoBBDD.Estudios.crearUriEstudio(studyId), cvEstudio, null, null);
                        //rInicial = new Resultado(rFinal);
                        new AlertDialog.Builder(getActivity())
                                .setTitle(R.string.dialog_warning)
                                .setMessage(R.string.study_animal_edit)
                                .setCancelable(false)
                                .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
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
                return true;

                //break;

            case R.id.action_pdf:
                if(vSurface.isPlaying()) {
                    vSurface.pause();
                    vSurface.seekTo(1);
                }else vSurface.seekTo(1);

                Cursor cEstudioPDF = getActivity().getContentResolver().query(ContratoBBDD.Estudios.crearUriParaResultados(studyId), null, null, null, null);

                cEstudioPDF.moveToFirst();
                while (!cEstudioPDF.isAfterLast()) {

                    animalId                = cEstudioPDF.getString(cEstudioPDF.getColumnIndex("id_animal"));
                    estudioFecha            = cEstudioPDF.getString(cEstudioPDF.getColumnIndex("fecha"));
                    estudioHora             = cEstudioPDF.getString(cEstudioPDF.getColumnIndex("hora"));
                    estudioTiempo           = cEstudioPDF.getString(cEstudioPDF.getColumnIndex("tiempo"));
                    estudioinvestigador1    = cEstudioPDF.getString(cEstudioPDF.getColumnIndex("INV1_NOMBRE")) + " " + cEstudioPDF.getString(cEstudioPDF.getColumnIndex("INV1_AP"));
                    estudioinvestigador2    = cEstudioPDF.getString(cEstudioPDF.getColumnIndex("INV2_NOMBRE")) + " " + cEstudioPDF.getString(cEstudioPDF.getColumnIndex("INV2_AP"));
                    estudiotipotest         = cEstudioPDF.getString(cEstudioPDF.getColumnIndex("TIPO_TEST_DES"));
                    estudioResultado        = cEstudioPDF.getString(cEstudioPDF.getColumnIndex("resultado"));
                    estudioScoreL           = cEstudioPDF.getString(cEstudioPDF.getColumnIndex("scorel"));
                    estudioScoreR           = cEstudioPDF.getString(cEstudioPDF.getColumnIndex("scorer"));
                    estudioVideo            = cEstudioPDF.getString(cEstudioPDF.getColumnIndex("video"));
                    estudioComentarios      = cEstudioPDF.getString(cEstudioPDF.getColumnIndex("comentarios"));

                    resultadoaml    = cEstudioPDF.getString(cEstudioPDF.getColumnIndex("am_l"));
                    resultadoamr    = cEstudioPDF.getString(cEstudioPDF.getColumnIndex("am_r"));
                    resultadopwol   = cEstudioPDF.getString(cEstudioPDF.getColumnIndex("pwo_l"));
                    resultadopwor   = cEstudioPDF.getString(cEstudioPDF.getColumnIndex("pwo_r"));
                    resultadopwl    = cEstudioPDF.getString(cEstudioPDF.getColumnIndex("pw_l"));
                    resultadopwr    = cEstudioPDF.getString(cEstudioPDF.getColumnIndex("pw_r"));
                    resultadosdl    = cEstudioPDF.getString(cEstudioPDF.getColumnIndex("sd_l"));
                    resultadosdr    = cEstudioPDF.getString(cEstudioPDF.getColumnIndex("sd_r"));
                    resultadospl    = cEstudioPDF.getString(cEstudioPDF.getColumnIndex("sp_l"));
                    resultadospr    = cEstudioPDF.getString(cEstudioPDF.getColumnIndex("sp_r"));
                    resultadoppicl  = cEstudioPDF.getString(cEstudioPDF.getColumnIndex("pp_ic_l"));
                    resultadoppicr  = cEstudioPDF.getString(cEstudioPDF.getColumnIndex("pp_ic_r"));
                    resultadopplol  = cEstudioPDF.getString(cEstudioPDF.getColumnIndex("pp_lo_l"));
                    resultadopplor  = cEstudioPDF.getString(cEstudioPDF.getColumnIndex("pp_lo_r"));
                    resultadocoor   = cEstudioPDF.getString(cEstudioPDF.getColumnIndex("coord"));
                    resultadoti     = cEstudioPDF.getString(cEstudioPDF.getColumnIndex("ti"));
                    resultadotail   = cEstudioPDF.getString(cEstudioPDF.getColumnIndex("tail"));

                    cEstudioPDF.moveToNext();
                }
                cEstudioPDF.close();

                int id_resultado_pdf = Integer.valueOf(studyId);
                resultado = new Resultado(id_resultado_pdf,resultadoaml,resultadoamr,resultadopwol,resultadopwor,resultadopwl,resultadopwr,
                        resultadosdl,resultadosdr,resultadospl,resultadospr,resultadocoor,resultadoppicl,resultadoppicr,resultadopplol,resultadopplor,resultadoti,resultadotail);


                EstudiosDialogPDF pdfDialog = new EstudiosDialogPDF(resultado,animalCodigo,animalEspecie,animalFechaCir,estudioFecha,estudioHora,estudioTiempo,estudioinvestigador1
                        ,estudioScoreL,estudioScoreR,estudioResultado, animalExperimento,animalOrigen,estudioComentarios);
                pdfDialog.setArguments(args);
                pdfDialog.show(fragmentTransaction, "fragment_alert");
                break;

            case R.id.action_about:
                DialogAbout dialogAbout= new DialogAbout();
                dialogAbout.show(fragmentTransaction, "fragment_alert");
                break;

            case R.id.action_email:
                if(vSurface.isPlaying()) {
                    vSurface.pause();
                    vSurface.seekTo(1);
                }else vSurface.seekTo(1);

                Cursor cEstudioEmail = getActivity().getContentResolver().query(ContratoBBDD.Estudios.crearUriParaResultados(studyId), null, null, null, null);

                String pdfPath="";
                String videoPath="";

                cEstudioEmail.moveToFirst();
                while (!cEstudioEmail.isAfterLast()) {

                    animalId                = cEstudioEmail.getString(cEstudioEmail.getColumnIndex("id_animal"));
                    estudioFecha            = cEstudioEmail.getString(cEstudioEmail.getColumnIndex("fecha"));
                    estudioHora             = cEstudioEmail.getString(cEstudioEmail.getColumnIndex("hora"));
                    estudioTiempo           = cEstudioEmail.getString(cEstudioEmail.getColumnIndex("tiempo"));
                    estudioinvestigador1    = cEstudioEmail.getString(cEstudioEmail.getColumnIndex("INV1_NOMBRE")) + " " + cEstudioEmail.getString(cEstudioEmail.getColumnIndex("INV1_AP"));
                    estudioinvestigador2    = cEstudioEmail.getString(cEstudioEmail.getColumnIndex("INV2_NOMBRE")) + " " + cEstudioEmail.getString(cEstudioEmail.getColumnIndex("INV2_AP"));
                    estudiotipotest         = cEstudioEmail.getString(cEstudioEmail.getColumnIndex("TIPO_TEST_DES"));
                    estudioResultado        = cEstudioEmail.getString(cEstudioEmail.getColumnIndex("resultado"));
                    estudioScoreL           = cEstudioEmail.getString(cEstudioEmail.getColumnIndex("scorel"));
                    estudioScoreR           = cEstudioEmail.getString(cEstudioEmail.getColumnIndex("scorer"));
                    estudioVideo            = cEstudioEmail.getString(cEstudioEmail.getColumnIndex("video"));
                    estudioComentarios      = cEstudioEmail.getString(cEstudioEmail.getColumnIndex("comentarios"));

                    resultadoaml    = cEstudioEmail.getString(cEstudioEmail.getColumnIndex("am_l"));
                    resultadoamr    = cEstudioEmail.getString(cEstudioEmail.getColumnIndex("am_r"));
                    resultadopwol   = cEstudioEmail.getString(cEstudioEmail.getColumnIndex("pwo_l"));
                    resultadopwor   = cEstudioEmail.getString(cEstudioEmail.getColumnIndex("pwo_r"));
                    resultadopwl    = cEstudioEmail.getString(cEstudioEmail.getColumnIndex("pw_l"));
                    resultadopwr    = cEstudioEmail.getString(cEstudioEmail.getColumnIndex("pw_r"));
                    resultadosdl    = cEstudioEmail.getString(cEstudioEmail.getColumnIndex("sd_l"));
                    resultadosdr    = cEstudioEmail.getString(cEstudioEmail.getColumnIndex("sd_r"));
                    resultadospl    = cEstudioEmail.getString(cEstudioEmail.getColumnIndex("sp_l"));
                    resultadospr    = cEstudioEmail.getString(cEstudioEmail.getColumnIndex("sp_r"));
                    resultadoppicl  = cEstudioEmail.getString(cEstudioEmail.getColumnIndex("pp_ic_l"));
                    resultadoppicr  = cEstudioEmail.getString(cEstudioEmail.getColumnIndex("pp_ic_r"));
                    resultadopplol  = cEstudioEmail.getString(cEstudioEmail.getColumnIndex("pp_lo_l"));
                    resultadopplor  = cEstudioEmail.getString(cEstudioEmail.getColumnIndex("pp_lo_r"));
                    resultadocoor   = cEstudioEmail.getString(cEstudioEmail.getColumnIndex("coord"));
                    resultadoti     = cEstudioEmail.getString(cEstudioEmail.getColumnIndex("ti"));
                    resultadotail   = cEstudioEmail.getString(cEstudioEmail.getColumnIndex("tail"));


                    cEstudioEmail.moveToNext();
                }
                cEstudioEmail.close();

                int id_resultado_email = Integer.valueOf(studyId);
                resultado = new Resultado(id_resultado_email,resultadoaml,resultadoamr,resultadopwol,resultadopwor,resultadopwl,resultadopwr,
                        resultadosdl,resultadosdr,resultadospl,resultadospr,resultadocoor,resultadoppicl,resultadoppicr,resultadopplol,resultadopplor,resultadoti,resultadotail);

                try {
                    pdfPath = Utils.generarPDF(getActivity(), resultado, animalCodigo, animalEspecie, animalFechaCir, estudioFecha,estudioHora,estudioTiempo,estudioinvestigador1
                            ,estudioScoreL,estudioScoreR,estudioResultado,
                            animalExperimento, animalOrigen, estudioComentarios);
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                if (estudioVideo != "") videoPath = estudioVideo;

                EstudiosDialogEmail emailDialog = new EstudiosDialogEmail(pdfPath,videoPath,studyId);
                emailDialog.setArguments(args);
                emailDialog.show(fragmentTransaction, "fragment_alert");

                break;

            case R.id.action_delete_video:

                AlertDialog.Builder alertDialogVideo = new AlertDialog.Builder(getActivity());
                alertDialogVideo.setTitle(R.string.dialog_video_delete);
                alertDialogVideo.setMessage(R.string.dialog_video_delete_msg);
                alertDialogVideo.setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                       // if(vSurface.isPlaying()){
                       // vSurface.stopPlayback();}
                        File fileVideo = new File(estudioVideo);
                        fileVideo.delete();
                        final ContentValues cvVideo = new ContentValues();
                        cvVideo.put(ContratoBBDD.Estudios.video,"");

                        getActivity().getContentResolver().update(ContratoBBDD.Estudios.crearUriEstudio(studyId), cvVideo, null, null);
                        //rInicial = new Resultado(rFinal);
                        new AlertDialog.Builder(getActivity())
                                .setTitle(R.string.dialog_warning)
                                .setMessage(R.string.dialog_video_deleted)
                                .setCancelable(false)
                                .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).create().show();
                    }
                });
                alertDialogVideo.setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //EJEMPLO TOAST   -- Toast.makeText(contexto.getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });
                alertDialogVideo.show();
                return true;
     }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Bundle args = new Bundle();
        args.putString("_id", studyId);
        android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

        int id = v.getId();
        switch (id) {
            case R.id.btn_play:
                if(!vSurface.isPlaying())
                    vSurface.start();
                break;
            case R.id.btn_pause:
                if(vSurface.isPlaying())
                    vSurface.pause();
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
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute =c.get(Calendar.MINUTE);
        int id = v.getId();
        switch (id) {
            case R.id.etsFecha:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        try {
                            Date fecha = formatter.parse(String.valueOf(etsFecha.getText()));
                            Calendar cFecha = Calendar.getInstance();
                            cFecha.setTime(fecha);
                            mYear = cFecha.get(Calendar.YEAR);
                            mMonth = cFecha.get(Calendar.MONTH);
                            mDay = cFecha.get(Calendar.DAY_OF_MONTH);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
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
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                            //Date fecha = formatter.parse(String.valueOf("01/01/1900 " + etsHora.getText()));
                            Date fecha = sdf.parse(etsHora.getText().toString());
                            Calendar cHora = Calendar.getInstance();
                            cHora.setTime(fecha);
                            mHour = cHora.get(Calendar.HOUR);
                            mMinute = cHora.get(Calendar.MINUTE);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
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
        }

        return false;
    }



}

