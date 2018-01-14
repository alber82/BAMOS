package es.uclm.proyecto.controlador.fragments;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import es.uclm.proyecto.R;
import es.uclm.proyecto.controlador.dialog.DialogAbout;
import es.uclm.proyecto.controlador.dialog.EstudiosDialogEdit;
import es.uclm.proyecto.controlador.dialog.EstudiosDialogRecord;
import es.uclm.proyecto.modelo.Estudio;
import es.uclm.proyecto.modelo.Resultado;
import es.uclm.proyecto.sqlite.ContratoBBDD;
import es.uclm.proyecto.util.Utils;


/**
 * Created by Admin on 04-06-2015.
 */
public class RecordFragment extends Fragment implements View.OnClickListener, SurfaceHolder.Callback {

    private Button btnParar;

    RelativeLayout rrlayout;
    RelativeLayout rllayout;
    RelativeLayout rrlayout_bottom;
    RelativeLayout rllayout_bottom;


    private File outfile = null;
    MediaRecorder recorder;
    SurfaceHolder holder;
    boolean recording = false;
    int grabado = 0;
    int guardado = 0;
    TextView contador;

    RelativeLayout leftLayaout;

    String idAnimal;
    Estudio estudio;
    String _id;

    private Spinner spsaml;
    private Spinner spsamr;
    private Switch swspwol;
    private Switch swspwor;
    private Switch swspwl;
    private Switch swspwr;
    private Spinner spssdl;
    private Spinner spssdr;
    private Spinner spsspl;
    private Spinner spsspr;
    private Spinner spsppicl;
    private Spinner spsppicr;
    private Spinner spspplol;
    private Spinner spspplor;
    private Spinner spscoor;
    private Spinner spsti;
    private Spinner spstail;

    MenuItem grabacionItem;

    public RecordFragment() {

    }

    @SuppressLint("ValidFragment")
    public RecordFragment(String idAnimal) {
        this.idAnimal = idAnimal;
    }

    @SuppressLint("ValidFragment")
    public RecordFragment(String idAnimal, Estudio estudio) {
        this.idAnimal = idAnimal;
        this.estudio = estudio;
    }

    public void onCreate(Bundle savedInstanceState) {

        // Fragment locked in landscape screen orientation
        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_record, container, false);
        recorder = new MediaRecorder();

        leftLayaout = (RelativeLayout) v.findViewById(R.id.rllayout);
        //leftLayaout.setVisibility(View.INVISIBLE);


        if (estudio == null) {

            EstudiosDialogRecord EstudiosDialogRecord = new EstudiosDialogRecord(idAnimal);
            android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame, EstudiosDialogRecord);
            fragmentTransaction.commit();
            return v;

        }

        spsaml = (Spinner) v.findViewById(R.id.spaml);
        spsamr = (Spinner) v.findViewById(R.id.spamr);
        swspwol = (Switch) v.findViewById(R.id.swpwol);
        swspwor = (Switch) v.findViewById(R.id.swpwor);
        swspwl = (Switch) v.findViewById(R.id.swpwl);
        swspwr = (Switch) v.findViewById(R.id.swpwr);

        spssdl = (Spinner) v.findViewById(R.id.spsdl);
        spssdr = (Spinner) v.findViewById(R.id.spsdr);

        spsspl = (Spinner) v.findViewById(R.id.spspl);
        spsspr = (Spinner) v.findViewById(R.id.spspr);

        spsppicl = (Spinner) v.findViewById(R.id.spppicl);
        spsppicr = (Spinner) v.findViewById(R.id.spppicr);

        spspplol = (Spinner) v.findViewById(R.id.sppplol);
        spspplor = (Spinner) v.findViewById(R.id.sppplor);
        spscoor = (Spinner) v.findViewById(R.id.spcor);
        spsti = (Spinner) v.findViewById(R.id.spti);
        spstail = (Spinner) v.findViewById(R.id.sptail);

        initRecorder();
        btnParar = (Button) v.findViewById(R.id.btnCaptureVideo);
        contador = (TextView) v.findViewById(R.id.tvContador);

        btnParar.setOnClickListener(this);
        btnParar.setVisibility(View.INVISIBLE);

        SurfaceView cameraView = (SurfaceView) v.findViewById(R.id.surfaceCamera);
        holder = cameraView.getHolder();
        holder.addCallback(this);
        //holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        cameraView.setClickable(true);
        cameraView.setOnClickListener(this);

        rrlayout = (RelativeLayout) v.findViewById(R.id.rrlayout);
        rllayout = (RelativeLayout) v.findViewById(R.id.rllayout);
        rrlayout_bottom = (RelativeLayout) v.findViewById(R.id.rrlayout_bottom);
        rllayout_bottom = (RelativeLayout) v.findViewById(R.id.rllayout_bottom);

        rrlayout_bottom.setVisibility(View.INVISIBLE);
        rllayout_bottom.setVisibility(View.INVISIBLE);

        setHasOptionsMenu(true);
        ActionBar actionBar = ((AppCompatActivity) getActivity())
                .getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDefaultDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(R.string.recording_string);


        return v;
    }

    @SuppressLint({"SdCardPath", "NewApi"})
    private void initRecorder() {
        recording = false;
/*
        recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_480P));
*/
        recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setVideoSize(720,480);
        recorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        DateFormat df = new SimpleDateFormat("yyyymmddHHmmss");
        Calendar calobj = Calendar.getInstance();

        File storageDir = new File(getActivity().getApplicationInfo().dataDir + "/files/video/");
        storageDir.mkdir();

        String path = getActivity().getApplicationInfo().dataDir + "/files/video/A" + idAnimal + "F" + df.format(calobj.getTime()) + ".mp4";
        outfile = new File(path);



        recorder.setOutputFile(outfile.getAbsolutePath());
    }

    private void prepareRecorder() {
        recorder.setPreviewDisplay(holder.getSurface());
        try {
            recorder.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            //finish();
        } catch (IOException e) {
            e.printStackTrace();
            //finish();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_record, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        android.app.AlertDialog.Builder alertDialog;

        switch (item.getItemId()) {
            case R.id.action_record:
                try {
                    /*if (recording) {
                        recorder.stop();
                        recorder.release();
                        recording = false;
                    } else*/
                    if (!recording && grabado == 0) {
                        btnParar.setVisibility(View.VISIBLE);
                        recording = true;
                        recorder.start();

                        SharedPreferences prefs = getActivity().getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
                        String timePreference = prefs.getString("time","240");
                        int timeInt = Integer.parseInt(timePreference) * 1000;

                        new CountDownTimer(timeInt, 1000) {
                            public void onTick(long millisUntilFinished) {
                                contador.setText("" + String.format("%d min, %d sec",
                                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                            }

                            public void onFinish() {
                                if (recording) {
                                    recorder.stop();
                                    recorder.release();
                                    recording = false;
                                    grabado = 1;
                                    btnParar.setVisibility(View.INVISIBLE);
                                    //}
                                    Canvas canvas = holder.lockCanvas();
                                    canvas.drawColor(Color.BLACK, PorterDuff.Mode.CLEAR);
                                    holder.unlockCanvasAndPost(canvas);
                                    contador.setVisibility(View.INVISIBLE);
                                }
                            }
                        }.start();
                    } else if (grabado == 1) {
                        new AlertDialog.Builder(getActivity())
                                .setTitle(R.string.dialog_warning)
                                .setMessage(R.string.video_recorded)
                                .setCancelable(false)
                                .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).create().show();
                    }

                } catch (Exception e) {

                }
                return true;
            case R.id.action_about:
                android.support.v4.app.FragmentTransaction fragmentTransaction1 = getActivity().getSupportFragmentManager().beginTransaction();
                DialogAbout dialogAbout= new DialogAbout();
                dialogAbout.show(fragmentTransaction1, "fragment_alert");
                break;

            case R.id.action_done:
                if (!recording && grabado == 1 && guardado == 0) {
                    Uri insercion;

                    String strsaml = spsaml.getSelectedItem().toString();
                    String strsamr = spsamr.getSelectedItem().toString();
                    String strspwol;
                    if (swspwol.isChecked()) {
                        strspwol = "S";
                    } else {
                        strspwol = "N";
                    }

                    String strspwor;
                    if (swspwor.isChecked()) {
                        strspwor = "S";
                    } else {
                        strspwor = "N";
                    }

                    String strspwl;
                    if (swspwl.isChecked()) {
                        strspwl = "S";
                    } else {
                        strspwl = "N";
                    }

                    String strspwr;
                    if (swspwr.isChecked()) {
                        strspwr = "S";
                    } else {
                        strspwr = "N";
                    }

                    String strssdl = spssdl.getSelectedItem().toString();
                    String strssdr = spssdr.getSelectedItem().toString();

                    String strsspl = spsspl.getSelectedItem().toString();
                    String strsspr = spsspr.getSelectedItem().toString();

                    String strsppicl = spsppicl.getSelectedItem().toString();
                    String strsppicr = spsppicr.getSelectedItem().toString();

                    String strspplol = spspplol.getSelectedItem().toString();
                    String strspplor = spspplor.getSelectedItem().toString();

                    String strscoor = spscoor.getSelectedItem().toString();
                    String strsti = spsti.getSelectedItem().toString();
                    String strstail = spstail.getSelectedItem().toString();

                    String strVideo = outfile.getAbsolutePath();

                    ContentValues cvEstudio = new ContentValues();
                    cvEstudio.put(ContratoBBDD.Estudios.id_animal, estudio.idAnimal);
                    cvEstudio.put(ContratoBBDD.Estudios.fecha, estudio.fecha);
                    cvEstudio.put(ContratoBBDD.Estudios.hora, estudio.hora);
                    cvEstudio.put(ContratoBBDD.Estudios.tiempo, estudio.tiempo);
                    cvEstudio.put(ContratoBBDD.Estudios.id_investigador1, estudio.idInvestigador1);
                    cvEstudio.put(ContratoBBDD.Estudios.id_investigador2, estudio.idInvestigador2);
                    cvEstudio.put(ContratoBBDD.Estudios.id_tipo_test, estudio.idTipoTest);
                    cvEstudio.put(ContratoBBDD.Estudios.resultado, 0);
                    cvEstudio.put(ContratoBBDD.Estudios.video, strVideo);
                    cvEstudio.put(ContratoBBDD.Estudios.comentarios, "");

                    ContentValues cvResultado = new ContentValues();
                    //cvResultado.put(ContratoBBDD.Resultados._id, _id);
                    cvResultado.put(ContratoBBDD.Resultados.am_l, strsaml);
                    cvResultado.put(ContratoBBDD.Resultados.am_r, strsamr);
                    cvResultado.put(ContratoBBDD.Resultados.pwo_l, strspwol);
                    cvResultado.put(ContratoBBDD.Resultados.pwo_r, strspwor);
                    cvResultado.put(ContratoBBDD.Resultados.pw_l, strspwl);
                    cvResultado.put(ContratoBBDD.Resultados.pw_r, strspwr);
                    cvResultado.put(ContratoBBDD.Resultados.sd_l, strssdl);
                    cvResultado.put(ContratoBBDD.Resultados.sd_r, strssdr);
                    cvResultado.put(ContratoBBDD.Resultados.sp_l, strsspl);
                    cvResultado.put(ContratoBBDD.Resultados.sp_r, strsspr);

                    cvResultado.put(ContratoBBDD.Resultados.pp_ic_l, strsppicl);
                    cvResultado.put(ContratoBBDD.Resultados.pp_ic_r, strsppicr);
                    cvResultado.put(ContratoBBDD.Resultados.pp_lo_l, strspplol);
                    cvResultado.put(ContratoBBDD.Resultados.pp_lo_r, strspplor);

                    cvResultado.put(ContratoBBDD.Resultados.coord, strscoor);
                    cvResultado.put(ContratoBBDD.Resultados.ti, strsti);
                    cvResultado.put(ContratoBBDD.Resultados.tail, strstail);

                    Resultado resultado = new Resultado(strsaml, strsamr, strspwol, strspwor, strspwl, strspwr, strssdl, strssdr
                            , strsspl, strsspr, strscoor, strsppicl, strsppicr, strspplol, strspplor, strsti, strstail);


                    cvEstudio.put(ContratoBBDD.Estudios.resultado, "" + Utils.calcularSubscore(resultado));
                    cvEstudio.put(ContratoBBDD.Estudios.scorel, "" + Utils.calcularScore(resultado, "L"));
                    cvEstudio.put(ContratoBBDD.Estudios.scorer, "" + Utils.calcularScore(resultado, "R"));

                    insercion = getActivity().getContentResolver().insert(ContratoBBDD.Estudios.URI_CONTENIDO, cvEstudio);
                    _id = ContratoBBDD.Estudios.obtenerCodigoEstudio(insercion);

                    cvResultado.put(ContratoBBDD.Resultados._id, _id);


                    insercion = getActivity().getContentResolver().insert(ContratoBBDD.Resultados.URI_CONTENIDO, cvResultado);
                    ContratoBBDD.Resultados.obtenerCodigoResultado(insercion);


                    guardado = 1;

                    Log.d("A", _id);
                    new AlertDialog.Builder(getActivity())
                            .setTitle(R.string.dialog_warning)
                            .setMessage(R.string.registry_correct_string)
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).create().show();

                } else if (!recording && grabado == 0) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle(R.string.dialog_warning)
                            .setMessage(R.string.must_record_string)
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).create().show();
                } else if (!recording && grabado == 1 && guardado == 1) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle(R.string.dialog_warning)
                            .setMessage(R.string.study_saved_string)
                            .setCancelable(false)
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).create().show();
                }
                return true;
            case R.id.action_back:
                if (!recording) {
                    alertDialog = new android.app.AlertDialog.Builder(getActivity());
                    alertDialog.setTitle(R.string.dialog_warning);

                    if (grabado == 0) {
                        alertDialog.setMessage(R.string.no_video_recorded_string);
                        alertDialog.setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //BORRAR EL VIDEO
                                AnimalesFragment AnimalFragment = new AnimalesFragment();
                                android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.frame, AnimalFragment);
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
                    } else {
                        if (guardado == 0) {

                            alertDialog.setMessage(R.string.no_study_saved_string);
                            alertDialog.setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //BORRAR EL VIDEO
                                    outfile.delete();
                                    AnimalesFragment AnimalFragment = new AnimalesFragment();
                                    android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.frame, AnimalFragment);
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
                        } else {
                            alertDialog.setMessage(R.string.access_study_string);
                            alertDialog.setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    if (_id == null){
                                        AnimalesFragment AnimalFragment = new AnimalesFragment();
                                        android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                        fragmentTransaction.replace(R.id.frame, AnimalFragment);
                                        fragmentTransaction.commit();

                                    }else {
                                        EstudiosDialogEdit EstudiosDialogEdit = new EstudiosDialogEdit(_id);
                                        android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                        fragmentTransaction.replace(R.id.frame, EstudiosDialogEdit);
                                        fragmentTransaction.commit();
                                    }
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
                    return true;
                }
            case R.id.action_loop:
                if (rrlayout.getVisibility() == View.VISIBLE && rllayout.getVisibility() == View.VISIBLE) {
                    rrlayout.setVisibility(View.INVISIBLE);
                    rllayout.setVisibility(View.INVISIBLE);
                    rrlayout_bottom.setVisibility(View.VISIBLE);
                    rllayout_bottom.setVisibility(View.VISIBLE);
                } else {
                    rrlayout.setVisibility(View.VISIBLE);
                    rllayout.setVisibility(View.VISIBLE);
                    rrlayout_bottom.setVisibility(View.INVISIBLE);
                    rllayout_bottom.setVisibility(View.INVISIBLE);
                }


                return true;
            default:
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {
        android.app.AlertDialog.Builder alertDialog;
        switch (v.getId()) {
            case R.id.btnCaptureVideo:
                if (recording) {
                    alertDialog = new android.app.AlertDialog.Builder(getActivity());
                    alertDialog.setTitle(R.string.dialog_warning);
                    alertDialog.setMessage(R.string.recording_lost_string);
                    alertDialog.setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            recorder.stop();
                            recorder.release();
                            recording = false;
                            Canvas canvas = holder.lockCanvas();
                            canvas.drawColor(Color.BLACK, PorterDuff.Mode.CLEAR);
                            holder.unlockCanvasAndPost(canvas);
                            outfile.delete();
                            //BORRAR EL VIDEO
                            //getActivity().getSupportFragmentManager().beginTransaction().
                            //       detach(getActivity().getSupportFragmentManager().findFragmentByTag("recordFragment")).attach(getActivity().getSupportFragmentManager().findFragmentByTag("recordFragment")).commit();

                            AnimalesFragment AnimalFragment = new AnimalesFragment();
                            android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.frame, AnimalFragment);
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
                }


            default:
                break;
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {
        prepareRecorder();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        try {
            if (recording) {
                recorder.stop();
                recording = false;
            }
            recorder.release();
            // finish();
        } catch (Exception e) {

        }
    }

}