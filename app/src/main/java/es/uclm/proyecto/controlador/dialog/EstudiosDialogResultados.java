package es.uclm.proyecto.controlador.dialog;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import es.uclm.proyecto.R;
import es.uclm.proyecto.modelo.Resultado;
import es.uclm.proyecto.sqlite.ContratoBBDD;
import es.uclm.proyecto.util.Utils;

/**
 * Created by alber on 25/04/2016.
 */
public class EstudiosDialogResultados extends DialogFragment implements View.OnClickListener{
    private Resultado rInicial;
    private Resultado rFinal;

    //Ankle Movement
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
    private TextView subscore;
    private TextView scorel;
    private TextView scorer;


    private String studyId;

    public EstudiosDialogResultados() {

    }

    public EstudiosDialogResultados(Resultado rInicial, Resultado rFinal) {
        this.rInicial= rInicial;
        this.rFinal = rFinal;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Theme_AppCompat_Light_DarkActionBar);
        //android.R.style.Theme_Holo_Light_DarkActionBar);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_resultados, null);


        Bundle argumentos = getArguments();
        studyId = argumentos.getString("_id");
        Log.d("argumento", studyId);

        ActionBar actionBar = ((AppCompatActivity) getActivity())
                .getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDefaultDisplayHomeAsUpEnabled(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        InputMethodManager imm = (InputMethodManager) getActivity().getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);

//        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
//        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
        View view = inflater.inflate(R.layout.dialog_resultados, container, false);

        subscore = (TextView) view.findViewById(R.id.subscore);
        scorer = (TextView) view.findViewById(R.id.scorer);
        scorel = (TextView) view.findViewById(R.id.scorel);
        spsaml = (Spinner) view.findViewById(R.id.spsraml);
        spsamr = (Spinner) view.findViewById(R.id.spsramr);
        swspwol  = (Switch) view.findViewById(R.id.swsrpwol);
        swspwor  = (Switch) view.findViewById(R.id.swsrpwor);
        swspwl = (Switch) view.findViewById(R.id.swsrpwl);
        swspwr = (Switch) view.findViewById(R.id.swsrpwr);
        spssdl = (Spinner) view.findViewById(R.id.spsrsdl);
        spssdr= (Spinner) view.findViewById(R.id.spsrsdr);
        spsspl= (Spinner) view.findViewById(R.id.spsrspl);
        spsspr= (Spinner) view.findViewById(R.id.spsrspr);
        spsppicl= (Spinner) view.findViewById(R.id.spsrppicl);
        spsppicr= (Spinner) view.findViewById(R.id.spsrppicr);
        spspplol= (Spinner) view.findViewById(R.id.spsrpplol);
        spspplor= (Spinner) view.findViewById(R.id.spsrpplor);
        spscoor= (Spinner) view.findViewById(R.id.spsrcoor);
        spsti= (Spinner) view.findViewById(R.id.spsrtrunk);
        spstail= (Spinner) view.findViewById(R.id.spsrtail);


        ArrayAdapter<CharSequence> aaam = ArrayAdapter.createFromResource(
                getActivity().getApplicationContext(), R.array.am, R.layout.simple_spinner_item_perso);
        aaam.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spsaml.setAdapter(aaam);
        spsamr.setAdapter(aaam);
        spsaml.setSelection(aaam.getPosition(rInicial.am_l));
        spsamr.setSelection(aaam.getPosition(rInicial.am_r));

        if (rInicial.pwo_l.equals("S"))
            swspwol.setChecked(true);
        else swspwol.setChecked(false);

        if (rInicial.pwo_r.equals("S"))
            swspwor.setChecked(true);
        else swspwor.setChecked(false);

        if (rInicial.pw_l.equals("S"))
            swspwl.setChecked(true);
        else swspwl.setChecked(false);

        if (rInicial.pw_r.equals("S"))
            swspwr.setChecked(true);
        else swspwr.setChecked(false);

        ArrayAdapter<CharSequence> aasd = ArrayAdapter.createFromResource(
                getActivity().getApplicationContext(), R.array.stepd, R.layout.simple_spinner_item_perso);
        aasd.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spssdl.setAdapter(aasd);
        spssdr.setAdapter(aasd);
        spssdl.setSelection(aasd.getPosition(rInicial.sd_l));
        spssdr.setSelection(aasd.getPosition(rInicial.sd_r));


        ArrayAdapter<CharSequence> aasp = ArrayAdapter.createFromResource(
                getActivity().getApplicationContext(), R.array.stepp, R.layout.simple_spinner_item_perso);
        aasp.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spsspl.setAdapter(aasp);
        spsspr.setAdapter(aasp);
        spsspl.setSelection(aasp.getPosition(rInicial.sp_l));
        spsspr.setSelection(aasp.getPosition(rInicial.sp_r));

        ArrayAdapter<CharSequence> aapp = ArrayAdapter.createFromResource(
                getActivity().getApplicationContext(), R.array.pp, R.layout.simple_spinner_item_perso);
        aasp.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spsppicl.setAdapter(aapp);
        spsppicr.setAdapter(aapp);
        spspplol.setAdapter(aapp);
        spspplor.setAdapter(aapp);

        spsppicl.setSelection(aapp.getPosition(rInicial.pp_ic_l));
        spsppicr.setSelection(aapp.getPosition(rInicial.pp_ic_r));
        spspplol.setSelection(aapp.getPosition(rInicial.pp_lo_l));
        spspplor.setSelection(aapp.getPosition(rInicial.pp_lo_r));

        ArrayAdapter<CharSequence> aacoor = ArrayAdapter.createFromResource(
                getActivity().getApplicationContext(), R.array.coord, R.layout.simple_spinner_item_perso);
        aacoor.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);

        spscoor.setAdapter(aacoor);
        spscoor.setSelection(aacoor.getPosition(rInicial.coord));

        ArrayAdapter<CharSequence> aati = ArrayAdapter.createFromResource(
                getActivity().getApplicationContext(), R.array.ti, R.layout.simple_spinner_item_perso);
        aati.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);

        spsti.setAdapter(aati);
        spsti.setSelection(aati.getPosition(rInicial.ti));

        ArrayAdapter<CharSequence> aatail = ArrayAdapter.createFromResource(
                getActivity().getApplicationContext(), R.array.tail, R.layout.simple_spinner_item_perso);
        aatail.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);

        spstail.setAdapter(aatail);
        spstail.setSelection(aatail.getPosition(rInicial.tail));

        subscore.setText(Utils.calcularSubscore(rInicial) + "");
        scorel.setText(Utils.calcularScore(rInicial, "L")+"");
        scorer.setText(Utils.calcularScore(rInicial, "R") + "");


        Toolbar actionBar = (Toolbar) view.findViewById(R.id.fake_action_bar);

        if (actionBar != null) {
            final EstudiosDialogResultados window = this;
            actionBar.setTitle(R.string.study_detail_edit);
            actionBar.setTitleTextColor(R.color.white);
            actionBar.inflateMenu(R.menu.menu_aceptar);
            actionBar.setNavigationOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int id = v.getId();
                    switch (id) {
                        default:
                            window.dismiss();
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
                            actualizarResultados();
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

    public void actualizarResultados(){
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
        rFinal= new Resultado(rInicial._id,spsaml.getSelectedItem().toString(),spsamr.getSelectedItem().toString(),strspwol,strspwor,strspwl,strspwr,
                spssdl.getSelectedItem().toString(),spssdr.getSelectedItem().toString(),spsspl.getSelectedItem().toString(),spsspr.getSelectedItem().toString(),
                spscoor.getSelectedItem().toString(),spsppicl.getSelectedItem().toString(),spsppicr.getSelectedItem().toString(),spspplol.getSelectedItem().toString(),
                spspplor.getSelectedItem().toString(),spsti.getSelectedItem().toString(),spstail.getSelectedItem().toString());


        final ContentValues cvResultado = new ContentValues();
        cvResultado.put(ContratoBBDD.Resultados._id, rInicial._id);
        cvResultado.put(ContratoBBDD.Resultados.am_l ,spsaml.getSelectedItem().toString());
        cvResultado.put(ContratoBBDD.Resultados.am_r ,spsamr.getSelectedItem().toString());
        cvResultado.put(ContratoBBDD.Resultados.pwo_l,strspwol);
        cvResultado.put(ContratoBBDD.Resultados.pwo_r,strspwor);
        cvResultado.put(ContratoBBDD.Resultados.pw_l,strspwl);
        cvResultado.put(ContratoBBDD.Resultados.pw_r,strspwr);
        cvResultado.put(ContratoBBDD.Resultados.sd_l,spssdl.getSelectedItem().toString());
        cvResultado.put(ContratoBBDD.Resultados.sd_r,spssdr.getSelectedItem().toString());
        cvResultado.put(ContratoBBDD.Resultados.sp_l,spsspl.getSelectedItem().toString());
        cvResultado.put(ContratoBBDD.Resultados.sp_r,spsspr.getSelectedItem().toString());

        cvResultado.put(ContratoBBDD.Resultados.pp_ic_l,spsppicl.getSelectedItem().toString());
        cvResultado.put(ContratoBBDD.Resultados.pp_ic_r,spsppicr.getSelectedItem().toString());
        cvResultado.put(ContratoBBDD.Resultados.pp_lo_l,spspplol.getSelectedItem().toString());
        cvResultado.put(ContratoBBDD.Resultados.pp_lo_r,spspplor.getSelectedItem().toString());

        cvResultado.put(ContratoBBDD.Resultados.coord,spscoor.getSelectedItem().toString());
        cvResultado.put(ContratoBBDD.Resultados.ti,spsti.getSelectedItem().toString());
        cvResultado.put(ContratoBBDD.Resultados.tail,spstail.getSelectedItem().toString());

        int subscore_int = Utils.calcularSubscore(rFinal);
        final int scoreL_int = Utils.calcularScore(rFinal, "L");
        int scoreR_int = Utils.calcularScore(rFinal, "R");
        final ContentValues cvSubscore = new ContentValues();
        cvSubscore.put(ContratoBBDD.Estudios.resultado,subscore_int+"");
        cvSubscore.put(ContratoBBDD.Estudios.scorel,scoreL_int+"");
        cvSubscore.put(ContratoBBDD.Estudios.scorer,scoreR_int+"");


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getDialog().getContext());
        alertDialog.setTitle(R.string.diagog_warning_edir);
        alertDialog.setMessage(R.string.dialog_study_edit_warning);
        alertDialog.setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                getActivity().getContentResolver().update(ContratoBBDD.Estudios.crearUriEstudio(rInicial._id + ""), cvSubscore, null, null);
                getActivity().getContentResolver().update(ContratoBBDD.Resultados.crearUriResultado(rInicial._id + ""), cvResultado, null, null);
                subscore.setText(Utils.calcularSubscore(rFinal) + "");
                scorel.setText(Utils.calcularScore(rFinal, "L") + "");
                scorer.setText(Utils.calcularScore(rFinal, "R") + "");
                //rInicial = new Resultado(rFinal);
                new AlertDialog.Builder(getDialog().getContext())
                        .setTitle(R.string.dialog_warning)
                        .setMessage(R.string.study_animal_edit)
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            default:
                break;
        }
    }
}
