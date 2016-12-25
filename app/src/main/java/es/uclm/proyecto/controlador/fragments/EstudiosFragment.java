package es.uclm.proyecto.controlador.fragments;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import es.uclm.proyecto.R;
import es.uclm.proyecto.controlador.dialog.EstudiosDialogAdd;
import es.uclm.proyecto.sqlite.ContratoBBDD;
import es.uclm.proyecto.controlador.adaptador.EstudiosAdaptador;

/**
 * Created by Admin on 04-06-2015.
 */
public class EstudiosFragment extends Fragment   implements EstudiosAdaptador.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView listaUI;
    private LinearLayoutManager linearLayoutManager;
    private EstudiosAdaptador adaptador;
    private Button btn_anadir;
    private Context contexto;

    String animalId;


    public EstudiosFragment(){
        setHasOptionsMenu(true);

    }
    public void onCreate(Bundle savedInstanceState) {

        // Fragment locked in landscape screen orientation
        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_study, container, false);
        contexto = container.getContext();
            /*Bundle argumentos = getArguments();
            animalId = argumentos.getString("_id");
            Log.d("argumento", animalId);
*/


        listaUI = (RecyclerView) v.getRootView().findViewById(R.id.rvEstudios);
//        listaUI.setHasFixedSize(true);

        adaptador = new EstudiosAdaptador(container.getContext(), this, getActivity(),this);
        listaUI.setAdapter(adaptador);

        linearLayoutManager = new LinearLayoutManager(container.getContext());
        listaUI.setLayoutManager(linearLayoutManager);
        listaUI.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getActivity())
                        //.color(Color.RED)
                        .showLastDivider()
                                //.size(5)
                        .build());

        getActivity().getSupportLoaderManager().restartLoader(2, null, this);

        setHasOptionsMenu(true);
        ActionBar actionBar = ((AppCompatActivity) getActivity())
                .getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDefaultDisplayHomeAsUpEnabled(false);
        actionBar.setTitle("Estudios");
        return v;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_ayuda, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
            /*if (id == R.id.action_settings) {

                return true;
            }*/
        if (id == R.id.action_add_estudio) {
            Log.d("Boton", "Añadir");
            EstudiosDialogAdd anadirEstudio = new EstudiosDialogAdd();
            anadirEstudio.show(getFragmentManager(),"nuevoEstudioFragment");
            //crearFullScreenDialog();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }




    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), ContratoBBDD.Estudios.URI_CONTENIDO, null, null, null, null);
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
        if (adaptador != null) {
            adaptador.swapCursor(null);

        }
    }


    @Override
    public void onClick(EstudiosAdaptador.ViewHolder holder, int idAnimal) {

    }
}
