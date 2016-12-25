package es.uclm.proyecto.controlador.adaptador;


import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import es.uclm.proyecto.controlador.dialog.AnimalesDialogEdit;
import es.uclm.proyecto.controlador.fragments.AnimalesEstudiosFragment;
import es.uclm.proyecto.R;
import es.uclm.proyecto.sqlite.ContratoBBDD;


public class AnimalesAdaptador extends RecyclerView.Adapter<AnimalesAdaptador.ViewHolder> {
    private final Context contexto;
    private Cursor items;
    private OnItemClickListener escucha;
    private FragmentActivity fragmentActivity;

    public interface OnItemClickListener {
        public void onClick(ViewHolder holder, int idAnimal);
    }

    public AnimalesAdaptador(Context contexto, OnItemClickListener escucha, FragmentActivity fragmentActivity) {
        this.contexto = contexto;
        this.escucha = escucha;
        this.fragmentActivity = fragmentActivity;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Referencias UI
        public TextView viewCodigoAnimal;
        public TextView viewEspecie;
        public TextView viewExperimento;
        public TextView viewFechaNac;
        public TextView viewFechaCir;
        public Button btn_eliminar;
        public Button btn_ver_estudios_animal;

        public ViewHolder(View v) {
            super(v);
            viewCodigoAnimal = (TextView) v.findViewById(R.id.codigoAnimal);
            viewEspecie  = (TextView) v.findViewById(R.id.especie);
            viewExperimento = (TextView) v.findViewById(R.id.experimento);
            viewFechaNac  = (TextView) v.findViewById(R.id.fecha_nac);
            viewFechaCir  = (TextView) v.findViewById(R.id.fecha_cir);

            btn_eliminar = (Button) v.findViewById(R.id.btn_eliminar_animal);
            btn_eliminar.setOnClickListener(this);

            btn_ver_estudios_animal = (Button) v.findViewById(R.id.btn_ver_estudios_animal);
            btn_ver_estudios_animal.setOnClickListener(this);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            escucha.onClick(this, obtenerIdAnimal(getAdapterPosition()));
            Bundle args = new Bundle();
            args.putString("_id", obtenerIdAnimal(getAdapterPosition()) + "");

            final FragmentActivity activity = (FragmentActivity)(contexto);
            android.support.v4.app.FragmentManager fm = activity.getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction;
            switch (view.getId()) {
                case R.id.btn_eliminar_animal:
                    Log.d("Prueba", "ID: " + obtenerIdAnimal(getAdapterPosition()));
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(contexto);
                    // Setting Dialog Title
                    alertDialog.setTitle("Confirmar borrado...");
                    alertDialog.setMessage("Esta seguro de que desea borrar este animal?");
                    alertDialog.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                contexto.getContentResolver().delete(ContratoBBDD.Animales.crearUriAnimal("" + obtenerIdAnimal(getAdapterPosition())), ""
                                        + obtenerIdAnimal(getAdapterPosition()), null);
                                notifyItemRemoved(getAdapterPosition());
                            } catch (SQLiteConstraintException e) {
                                new AlertDialog.Builder(activity)
                                        .setTitle("Atencion")
                                        .setMessage("El registro no se ha podido eliminar porque tiene registros asociados. Borrelos primero")
                                        .setCancelable(false)
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        }).create().show();
                            }

                        }
                    });
                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,	int which) {
                            // Write your code here to invoke NO event
                            //Toast.makeText(contexto.getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    });
                    alertDialog.show();
                    break;
                case R.id.btn_ver_estudios_animal:
                    Log.d("Pulso sobre fila con ID",obtenerIdAnimal(getAdapterPosition())+"");
                    Log.d("Pulso sobre fila", getAdapterPosition() + "");

                   AnimalesEstudiosFragment AnimalesEstudiosFragment = new AnimalesEstudiosFragment(activity, "" + obtenerIdAnimal(getAdapterPosition()));
                    fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame, AnimalesEstudiosFragment,"FragmentEstudioAnimal");
                    fragmentTransaction.commit();

                    break;

                default:
                    // ABRIMOS EDICION DE ANIMALES
                    Log.d("Pulso sobre fila con ID",obtenerIdAnimal(getAdapterPosition())+"");
                    Log.d("Pulso sobre fila",getAdapterPosition()+"");

                    AnimalesDialogEdit editDialog = new AnimalesDialogEdit();

                    editDialog.setArguments(args);
                    editDialog.show(fm, "fragment_alert");
                    break;
            }
        }

    }

    private int obtenerIdAnimal(int posicion) {
        if (items != null) {
            if (items.moveToPosition(posicion)) {
                return items.getInt(ConsultaAnimales.ID);
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_animal, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        items.moveToPosition(position);

        String s;

        // Asignación UI
        s = items.getString(ConsultaAnimales.CODIGO);
        holder.viewCodigoAnimal.setText(s);

        s = items.getString(ConsultaAnimales.ESPECIE);
        holder.viewEspecie.setText(s);

        s = items.getString(ConsultaAnimales.EXPERIMENTO);
        holder.viewExperimento.setText(s);


        s = items.getString(ConsultaAnimales.FECHA_NAC);
        holder.viewFechaNac.setText(s);

        s = items.getString(ConsultaAnimales.FECHA_CIR);
        holder.viewFechaCir.setText(s);



    }


    @Override
    public int getItemCount() {
        if (items != null)
            return items.getCount();
        return 0;
    }

    public void swapCursor(Cursor nuevoCursor) {
        if (nuevoCursor != null) {
            items = nuevoCursor;
        }
    }
/*
    public void flushFilter(){
        visibleObjects=new ArrayList<>();
        visibleObjects.addAll(allObjects);
        notifyDataSetChanged();
    }

    public void setFilter(String queryText) {

        visibleObjects = new ArrayList<>();
        constraint = constraint.toString().toLowerCase();
        for (String item: allObjects) {
            if (item.toLowerCase().contains(queryText))
                visibleObjects.add(item);
        }
        notifyDataSetChanged();
    }
*/

    public Cursor getCursor() {
        return items;
    }

    interface ConsultaAnimales {
        int ID = 0;
        int CODIGO = 1;
       // int CEPA = 2;
       // int SEXO = 3;
       // int TRANSGENICO = 4;
       // int ID_ESPECIE = 5; //
        int FECHA_CIR = 6;
        int FECHA_NAC = 7;
        int EXPERIMENTO = 11;
        int ESPECIE = 12; // ESPECIE
    }



}