package es.uclm.proyecto.controlador.fragments;

import android.content.pm.ActivityInfo;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
import es.uclm.proyecto.controlador.dialog.DialogAbout;
import es.uclm.proyecto.controlador.dialog.EstudiosDialogAdd;
import es.uclm.proyecto.sqlite.ContratoBBDD;
import es.uclm.proyecto.controlador.adaptador.EstudiosAnimalesAdaptador;

/**
 * Created by Admin on 04-06-2015.
 */
public class AnimalesEstudiosFragment extends Fragment   implements EstudiosAnimalesAdaptador.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

        private RecyclerView listaUI;
        private LinearLayoutManager linearLayoutManager;
        private EstudiosAnimalesAdaptador adaptador;
        private Button btn_anadir;
        private Context contexto;
        private FragmentActivity activity;
     String animalId;

    public AnimalesEstudiosFragment(){

    }
    public AnimalesEstudiosFragment(FragmentActivity activity, String animalId){
            setHasOptionsMenu(true);
            this.animalId=animalId;
        this.activity = activity;
        }

    public AnimalesEstudiosFragment(String animalId){
        setHasOptionsMenu(true);
        this.animalId=animalId;

    }
    public void onCreate(Bundle savedInstanceState) {

        // Fragment locked in landscape screen orientation
        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);
    }
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_studies_animal, container, false);
            contexto = container.getContext();
            /*Bundle argumentos = getArguments();
            animalId = argumentos.getString("_id");
            Log.d("argumento", animalId);
*/


            listaUI = (RecyclerView) v.getRootView().findViewById(R.id.rvEstudiosAnimal);
            listaUI.setHasFixedSize(true);

            adaptador = new EstudiosAnimalesAdaptador(container.getContext(), this, getActivity(),this);
            listaUI.setAdapter(adaptador);

            linearLayoutManager = new LinearLayoutManager(container.getContext());
            listaUI.setLayoutManager(linearLayoutManager);
            listaUI.addItemDecoration(
                    new HorizontalDividerItemDecoration.Builder(getActivity())
                            //.color(Color.RED)
                            .showLastDivider()
                                    //.size(5)
                            .build());

            getActivity().getSupportLoaderManager().restartLoader(0, null, this);

            setHasOptionsMenu(true);
            ActionBar actionBar = ((AppCompatActivity) getActivity())
                    .getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDefaultDisplayHomeAsUpEnabled(false);
            actionBar.setTitle(R.string.Animal_studies);
            return v;
        }


        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_estudio_animal, menu);
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
                EstudiosDialogAdd anadirEstudio = new EstudiosDialogAdd(animalId);
                anadirEstudio.setCancelable(false);
                anadirEstudio.show(getFragmentManager(),"nuevoEstudioFragment");
                return true;
            }
            if (id == R.id.action_record_estudio) {
                RecordFragment RecordFragment= new RecordFragment(animalId);
                FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, RecordFragment,"recordFragment");
                fragmentTransaction.commit();
                return true;
            }
            if (id == R.id.action_back) {

                AnimalesFragment AnimalFragment= new AnimalesFragment();
                FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, AnimalFragment);
                fragmentTransaction.commit();
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
            if (adaptador != null) {
                adaptador.swapCursor(null);

            }
        }


    @Override
    public void onClick(EstudiosAnimalesAdaptador.ViewHolder holder, int idAnimal) {

    }
}
