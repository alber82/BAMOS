package es.uclm.proyecto.controlador.dialog;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
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
import android.widget.ImageView;

import com.itextpdf.text.DocumentException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import es.uclm.proyecto.R;
import es.uclm.proyecto.modelo.Resultado;
import es.uclm.proyecto.util.Utils;

/**
 * Created by alber on 02/05/2016.
 */
public class EstudiosDialogPDF extends DialogFragment implements View.OnClickListener {
    private Resultado resultado;

    String animalCodigo;
    String animalEspecie;
    String animalFechaCir;
    String strsFecha;
    String strsHora;
    String strsTiempo;
    String strInvestigador1;
    String strsScoreL;
    String strsScoreR;
    String strsResultado;
    String animalExperimento;
    String animalOrigen;
    String estudioComentarios;
    ImageView pdfView;
    File file;
    String path = "";

    public EstudiosDialogPDF() {
        // Required empty public constructor
    }

    public EstudiosDialogPDF(Resultado resultado, String animalCodigo, String animalEspecie, String animalFechaCir, String strsFecha, String strsHora,
                             String strsTiempo, String strInvestigador1, String strsScoreL, String strsScoreR, String strsResultado,
                             String animalExperimento, String animalOrigen, String estudioComentarios) {
        this.resultado = resultado;
        this.animalCodigo = animalCodigo;
        this.animalEspecie = animalEspecie;
        this.animalFechaCir = animalFechaCir;
        this.strsFecha = strsFecha;
        this.strsHora = strsHora;
        this.strsTiempo = strsTiempo;
        this.strInvestigador1 = strInvestigador1;
        this.strsScoreL = strsScoreL;
        this.strsScoreR = strsScoreR;
        this.strsResultado = strsResultado;
        this.animalExperimento= animalExperimento;
        this.animalOrigen = animalOrigen;
        this.estudioComentarios= estudioComentarios;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Theme_AppCompat_Light_DarkActionBar);
        //android.R.style.Theme_Holo_Light_DarkActionBar);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_pdf_study, null);



/*
        Bundle argumentos = getArguments();
        studyId = argumentos.getString("_id");
        Log.d("argumento", studyId);*/

        ActionBar actionBar = ((AppCompatActivity) getActivity())
                .getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(false);
        //actionBar.setDefaultDisplayHomeAsUpEnabled(false);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        InputMethodManager imm = (InputMethodManager) getActivity().getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = inflater.inflate(R.layout.dialog_pdf_study, container, false);
        pdfView = (ImageView) view.findViewById(R.id.PDFView);


        try {
            path = getActivity().getApplicationInfo().dataDir + "/files/"+ Utils.generarPDF(getActivity(),resultado, animalCodigo, animalEspecie, animalFechaCir, strsFecha, strsHora, strsTiempo, strInvestigador1, strsScoreL, strsScoreR, strsResultado,
                    animalExperimento, animalOrigen, estudioComentarios)+".pdf";
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }


        file = new File(path);

            ParcelFileDescriptor mFileDescriptor = null;
            try {
                mFileDescriptor = ParcelFileDescriptor.open(file,
                        ParcelFileDescriptor.MODE_READ_ONLY);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            PdfRenderer mPdfRenderer = null;
            try {
                mPdfRenderer = new PdfRenderer(mFileDescriptor);
            } catch (IOException e) {
                e.printStackTrace();
            }


            PdfRenderer.Page mCurrentPage = mPdfRenderer.openPage(0);
            Bitmap bitmap = Bitmap.createBitmap(mCurrentPage.getWidth()*2,
                    mCurrentPage.getHeight()*2, Bitmap.Config.ARGB_8888);


            mCurrentPage.render(bitmap, null, null,
                    PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
// Set rendered bitmap to ImageView (pdfView in my case)
            pdfView.setImageBitmap(bitmap);

            mCurrentPage.close();
            mPdfRenderer.close();
            try {
                mFileDescriptor.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


            Toolbar actionBar = (Toolbar) view.findViewById(R.id.fake_action_bar);
            if (actionBar != null) {
                final EstudiosDialogPDF window = this;
                actionBar.setTitle("Editar detalle estudio");
                actionBar.setTitleTextColor(R.color.white);
                actionBar.setNavigationOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        file.delete();
                        window.dismiss();
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

    }

    @Override
    public void onDestroy() {
        file.delete();
        super.onDestroy();
        Log.e("RET", "onDestroy");
    }

}
