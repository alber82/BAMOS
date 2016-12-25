package es.uclm.proyecto.controlador.dialog;

import android.app.Activity;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import es.uclm.proyecto.R;
import es.uclm.proyecto.modelo.Animal;
import es.uclm.proyecto.modelo.Estudio;
import es.uclm.proyecto.sqlite.ContratoBBDD;
import es.uclm.proyecto.controlador.adaptador.EstudiosAnimalesAdaptador;

/**
 * Created by alber on 22/04/2016.
 */
public class AnimalesEstudiosDialog extends DialogFragment implements EstudiosAnimalesAdaptador.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private Animal animal;
    private Estudio estudio;

    private RecyclerView listaUI;
    private LinearLayoutManager linearLayoutManager;
    private EstudiosAnimalesAdaptador adaptador;
    private Context contexto;

    String animalId;

    public AnimalesEstudiosDialog() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_FRAME, R.style.Theme_AppCompat_Light_DarkActionBar);
        //android.R.style.Theme_Holo_Light_DarkActionBar);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_studies_animal, null);


        Bundle argumentos = getArguments();
        animalId = argumentos.getString("_id");
        Log.d("argumento", animalId);



        ActionBar actionBar = ((AppCompatActivity) getActivity())
                .getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDefaultDisplayHomeAsUpEnabled(false);
        actionBar.setTitle("Estudios de " + animalId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        InputMethodManager imm = (InputMethodManager) getActivity().getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);

//        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
//        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
        View view = inflater.inflate(R.layout.fragment_studies_animal, container, false);
        contexto = getActivity().getApplicationContext();


        listaUI = (RecyclerView) view.getRootView().findViewById(R.id.rvEstudios);
        listaUI.setHasFixedSize(true);

        adaptador = new EstudiosAnimalesAdaptador(contexto, this, getActivity());

        listaUI.setAdapter(adaptador);

        linearLayoutManager = new LinearLayoutManager(contexto);
        listaUI.setLayoutManager(linearLayoutManager);

        getActivity().getSupportLoaderManager().restartLoader(0 , null, this);

        setHasOptionsMenu(true);

        Toolbar actionBar = (Toolbar) view.findViewById(R.id.fake_action_bar);
        if (actionBar != null) {
            final AnimalesEstudiosDialog window = this;
            actionBar.setTitle("Editar animal");
            actionBar.setTitleTextColor(R.color.white);
            actionBar.setNavigationOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

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
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), ContratoBBDD.Animales.crearUriParaEstudios(animalId), null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (adaptador != null) {
            adaptador.swapCursor(data);
            adaptador.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adaptador.swapCursor(null);

    }

    @Override
    public void onClick(EstudiosAnimalesAdaptador.ViewHolder holder, int idAnimal) {

    }


}
