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
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
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
import es.uclm.proyecto.controlador.dialog.AnimalesDialogAdd;
import es.uclm.proyecto.controlador.dialog.DialogAbout;
import es.uclm.proyecto.sqlite.ContratoBBDD.Animales;
import es.uclm.proyecto.controlador.adaptador.AnimalesAdaptador;

public class AnimalesFragment extends Fragment implements AnimalesAdaptador.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor>//,SearchView.OnQueryTextListener
{

    private RecyclerView listaUI;
    private LinearLayoutManager linearLayoutManager;
    private AnimalesAdaptador adaptador;
    private Button btn_anadir;
    private Context contexto;

    public AnimalesFragment(){
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
        View v = inflater.inflate(R.layout.fragment_animal, container, false);
        contexto = container.getContext();

        listaUI = (RecyclerView) v.getRootView().findViewById(R.id.rvAnimales);
        listaUI.addItemDecoration(
        new HorizontalDividerItemDecoration.Builder(getActivity())
                //.color(Color.RED)
                .showLastDivider()
                //.size(5)
                .build());
        listaUI.setHasFixedSize(true);

        adaptador = new AnimalesAdaptador(container.getContext(), this, getActivity());
        listaUI.setAdapter(adaptador);

        linearLayoutManager = new LinearLayoutManager(container.getContext());
        listaUI.setLayoutManager(linearLayoutManager);

        getActivity().getSupportLoaderManager().restartLoader(1, null, this);

        setHasOptionsMenu(true);
        ActionBar actionBar = ((AppCompatActivity) getActivity())
                .getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDefaultDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(R.string.animals);
        return v;
    }


    @Override
      public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add, menu);
        /*final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);*/
        super.onCreateOptionsMenu(menu, inflater);
    }


/*

    @Override
    public boolean onQueryTextChange(String newText) {
        if ( TextUtils.isEmpty ( newText ) ) {
            adaptador.getFilter().filter("");
        } else {
            adaptador.getFilter().filter(newText.toString());
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }
*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        /*if (id == R.id.action_settings) {

            return true;
        }*/
        if (id == R.id.action_add_animal) {
            Log.d("Boton", "Añadir");
            AnimalesDialogAdd anadirAnimal= new AnimalesDialogAdd();
            anadirAnimal.setCancelable(false);
            anadirAnimal.show(getFragmentManager(),"nuevoAnimalFragment");
            //crearFullScreenDialog();
            return true;
        }

        if (id == R.id.action_about) {
        android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        DialogAbout dialogAbout= new DialogAbout();
        dialogAbout.show(fragmentTransaction, "fragment_alert");
        return true;
    }
        return super.onOptionsItemSelected(item);
    }




    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), Animales.URI_CONTENIDO, null, null, null, null);
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
    public void onClick(AnimalesAdaptador.ViewHolder holder, int idAnimal) {


    }




}

