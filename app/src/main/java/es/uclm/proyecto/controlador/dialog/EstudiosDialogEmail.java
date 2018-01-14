package es.uclm.proyecto.controlador.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
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
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import es.uclm.proyecto.R;
import es.uclm.proyecto.sqlite.ContratoBBDD;
import es.uclm.proyecto.util.Utils;

/**
 * Created by alber on 04/05/2016.
 */
public class EstudiosDialogEmail extends DialogFragment implements View.OnClickListener{
    String pathPDF;
    String pathVideo;

    File video;
    File pdf;
    String idEstudio;
    String animalCodigo;
    String estudioFecha;

    EditText etEmail;
    EditText etNombre;
    EditText etSubject;
    EditText etTextoMail;
    CheckBox cbVideo;

    private static final int CONTACT_PICKER_RESULT = 1001;
    private static final int EMAIL_RESULT = 1002;
    public EstudiosDialogEmail() {
        // Required empty public constructor
    }

    public EstudiosDialogEmail(String pathPDF, String pathVideo, String idEstudio) {
        this.pathPDF= pathPDF;
        this.pathVideo = pathVideo;
        this.idEstudio = idEstudio;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Theme_AppCompat_Light_DarkActionBar);
        //android.R.style.Theme_Holo_Light_DarkActionBar);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_email_study, null);


        Cursor cEstudioPDF = getActivity().getContentResolver().query(ContratoBBDD.Estudios.crearUriEstudio(idEstudio), null, null, null, null);

        cEstudioPDF.moveToFirst();
        while (!cEstudioPDF.isAfterLast()) {

            estudioFecha            = cEstudioPDF.getString(cEstudioPDF.getColumnIndex("fecha"));
            animalCodigo            = cEstudioPDF.getString(cEstudioPDF.getColumnIndex("ANIMAL_CODIGO"));

            cEstudioPDF.moveToNext();
        }
        cEstudioPDF.close();

        ActionBar actionBar = ((AppCompatActivity) getActivity())
                .getSupportActionBar();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        InputMethodManager imm = (InputMethodManager) getActivity().getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = inflater.inflate(R.layout.dialog_email_study, container, false);

        //LinearLayout Parent = (LinearLayout) view.findViewById(R.id.send_study_layout);
        //Parent.setOnClickListener(new DismissKeyboardListener(getActivity()));

        etEmail = (EditText) view.findViewById(R.id.etMail);
        etNombre = (EditText)  view.findViewById(R.id.etNombre);
        etSubject = (EditText) view.findViewById(R.id.etSubject);
        etTextoMail = (EditText) view.findViewById(R.id.etTextoMail);

        etSubject.setText("Estudio: "+ animalCodigo + " -  Fecha: " +estudioFecha);
        etTextoMail.setText("Envio de estudio: "+ animalCodigo + " -  Fecha: " +estudioFecha);

        cbVideo = (CheckBox) view.findViewById(R.id.cbVideo);

        if(pathVideo.equals("")) cbVideo.setEnabled(false);
        else cbVideo.setChecked(true);

        Toolbar actionBar = (Toolbar) view.findViewById(R.id.fake_action_bar);
        if (actionBar != null) {
            final EstudiosDialogEmail window = this;
            actionBar.setTitle("Enviar Email");
            actionBar.setTitleTextColor(R.color.white);
            actionBar.inflateMenu(R.menu.menu_email);
            actionBar.setNavigationOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    window.dismiss();
                }
            });
            actionBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    ConnectivityManager connManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                    int id = item.getItemId();
                    switch (id) {
                        case R.id.action_contact:
                            Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                            startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
                            break;

                        case R.id.action_done:
                            if (mWifi.isConnected()) {
                                enviarEmail();
                            }
                            else {
                                new AlertDialog.Builder(getActivity())
                                        .setTitle("WIFI no conectado")
                                        .setMessage("En caso de pulsar SI, se usara la conexion de datos")
                                        .setNegativeButton("No", null)
                                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {

                                            public void onClick(DialogInterface arg0, int arg1) {
                                                enviarEmail();
                                            }
                                        }).create().show();

                            }

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
        setHasOptionsMenu(true);


        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

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
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode)
            {
                case CONTACT_PICKER_RESULT:
                    Cursor cursor = null;
                    String email = "", name = "";
                    try {
                        Uri result = data.getData();
                        Log.v("", "Obtenemos contacto: " + result.toString());

                        String id = result.getLastPathSegment();

                        // query for everything email
                        cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,  null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=?", new String[] { id }, null);

                        int nameId = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

                        int emailIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);

                        // let's just get the first email
                        if (cursor.moveToFirst()) {
                            email = cursor.getString(emailIdx);
                            name = cursor.getString(nameId);
                            Log.v("", "Got email: " + email);
                        } else {
                            Log.w("", "Sin Resultados");
                        }
                    } catch (Exception e) {
                        Log.e("", "Failed to get email data", e);
                    } finally {
                        if (cursor != null) {
                            cursor.close();
                        }

                         etEmail.setText(email);
                        etNombre .setText(name);
                        if (email.length() == 0 && name.length() == 0)
                        {
                            Toast.makeText(getActivity(), "El contacto seleccionado no tiene email", Toast.LENGTH_LONG).show();
                        }
                    }
                    break;
                case EMAIL_RESULT:
                    Log.w("", "Sin Resultados");
                    break;

            }

        } else {
            Log.w("", "Warning: activity result not ok");
        }
    }

    public void enviarEmail(){
        String PDF  = getActivity().getApplicationInfo().dataDir + "/files/" + pathPDF +".pdf";
        String pathZip  = getActivity().getApplicationInfo().dataDir + "/files/"+ pathPDF + ".zip";
        String archivos[];
        if (pathVideo.equals(""))   { archivos = new String[]{PDF}; }
        else {
            if ( cbVideo.isChecked()) {
                archivos = new String[]{PDF, pathVideo};
            } else
                archivos = new String[]{PDF};
        }

        try {Utils.zip(archivos,pathZip);}
        catch (IOException e) {e.printStackTrace();}

        FileProvider fppdf = new FileProvider();
        File dirPath = new File(getActivity().getApplicationInfo().dataDir , "files");
        File zipFile = new File(dirPath, pathPDF+".zip");
        Uri ZIPUriPath = fppdf.getUriForFile(getActivity(),"es.uclm.fileprovider",zipFile);

        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent .setType("vnd.android.cursor.dir/email");
        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        emailIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        String to[] = {etEmail.getText().toString()};
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
        emailIntent .putExtra(Intent.EXTRA_STREAM, ZIPUriPath);
        emailIntent .putExtra(Intent.EXTRA_SUBJECT, etSubject.getText().toString());
        emailIntent .putExtra(Intent.EXTRA_TEXT, etTextoMail.getText().toString());
        startActivityForResult(emailIntent,EMAIL_RESULT);

        //File pdfFile = new File(PDF);
        //pdfFile.delete();
        //zipFile.delete();
    }
}
