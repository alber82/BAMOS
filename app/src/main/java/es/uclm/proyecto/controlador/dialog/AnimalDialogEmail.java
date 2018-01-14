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
public class AnimalDialogEmail extends DialogFragment implements View.OnClickListener {
    String idAnimal;

    File video;
    File pdf;

    EditText etEmail;
    EditText etNombre;
    EditText etSubject;
    EditText etTextoMail;
    String animalCodigo;

    private static final int CONTACT_PICKER_RESULT = 1001;
    private static final int EMAIL_RESULT = 1002;

    public AnimalDialogEmail() {
        // Required empty public constructor
    }

/*    public AnimalDialogEmail(String idAnimal) {
        this.idAnimal= idAnimal;
    }*/


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Theme_AppCompat_Light_DarkActionBar);
        //android.R.style.Theme_Holo_Light_DarkActionBar);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_email_animal, null);


        Bundle argumentos = getArguments();
        idAnimal = argumentos.getString("_id");
        Log.d("argumento", idAnimal);

        Cursor cAnimal = getActivity().getContentResolver().query(ContratoBBDD.Animales.crearUriAnimal(idAnimal), null, null, null, null);

        cAnimal.moveToFirst();
        while (!cAnimal.isAfterLast()) {
            animalCodigo = cAnimal.getString(cAnimal.getColumnIndex("codigo"));
            cAnimal.moveToNext();
        }
        cAnimal.close();

        ActionBar actionBar = ((AppCompatActivity) getActivity())
                .getSupportActionBar();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        InputMethodManager imm = (InputMethodManager) getActivity().getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = inflater.inflate(R.layout.dialog_email_animal, container, false);

        //LinearLayout Parent = (LinearLayout) view.findViewById(R.id.send_study_layout);
        //Parent.setOnClickListener(new DismissKeyboardListener(getActivity()));

        etEmail = (EditText) view.findViewById(R.id.etMail);
        etNombre = (EditText) view.findViewById(R.id.etNombre);
        etSubject = (EditText) view.findViewById(R.id.etSubject);
        etTextoMail = (EditText) view.findViewById(R.id.etTextoMail);

        etSubject.setText(getString(R.string.email_animal_summary)  + animalCodigo);
        etTextoMail.setText(getString(R.string.email_animal_text )  + animalCodigo);



        Toolbar actionBar = (Toolbar) view.findViewById(R.id.fake_action_bar);
        if (actionBar != null) {
            final AnimalDialogEmail window = this;
            actionBar.setTitle(R.string.animal_send_email);
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
                        case R.id.action_about:
                            android.support.v4.app.FragmentTransaction fragmentTransaction1 = getActivity().getSupportFragmentManager().beginTransaction();
                            DialogAbout dialogAbout= new DialogAbout();
                            dialogAbout.show(fragmentTransaction1, "fragment_alert");
                            break;
                        case R.id.action_done:

                            if (mWifi.isConnected()) {
                                enviarEmail();
                            }
                            else {
                                new AlertDialog.Builder(getActivity())
                                        .setTitle(R.string.dialog_not_wifi)
                                        .setMessage(R.string.dialog_data_use)
                                        .setNegativeButton(R.string.dialog_no, null)
                                        .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {

                                            public void onClick(DialogInterface arg0, int arg1) {
                                                enviarEmail();
                                            }
                                        }).create().show();

                            }
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

    public void enviarEmail() {
        String XLSPath = Utils.exportToExcel(getActivity(), animalCodigo,idAnimal);
        FileProvider fpexcel = new FileProvider();

        File dirPath = new File(getActivity().getApplicationInfo().dataDir, "files");
        File XLSFile = new File(dirPath, XLSPath);

        Uri XLSUriPath = fpexcel.getUriForFile(getActivity(), "es.uclm.fileprovider", XLSFile);

        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setType("vnd.android.cursor.dir/email");
        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        emailIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        String to[] = {etEmail.getText().toString()};
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
        emailIntent.putExtra(Intent.EXTRA_STREAM, XLSUriPath);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, etSubject.getText().toString());
        emailIntent.putExtra(Intent.EXTRA_TEXT, etTextoMail.getText().toString());
        startActivityForResult(emailIntent, EMAIL_RESULT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case CONTACT_PICKER_RESULT:
                    Cursor cursor = null;
                    String email = "", name = "";
                    try {
                        Uri result = data.getData();
                        Log.v("", "Obtenemos contacto: " + result.toString());

                        String id = result.getLastPathSegment();

                        // query for everything email
                        cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=?", new String[]{id}, null);

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
                        etNombre.setText(name);
                        if (email.length() == 0 && name.length() == 0) {
                            Toast.makeText(getActivity(), R.string.dialog_contact_email, Toast.LENGTH_LONG).show();
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


}
