package es.uclm.proyecto.controlador.adaptador;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import es.uclm.proyecto.controlador.dialog.EstudiosDialogEdit;
import es.uclm.proyecto.R;
import es.uclm.proyecto.sqlite.ContratoBBDD;


/** * Created by alber on 11/04/2016.
 */


public class EstudiosAdaptador extends RecyclerView.Adapter<EstudiosAdaptador.ViewHolder> {
    private final Context contexto;
    private Cursor itemsEstudios;
    private OnItemClickListener escucha;
    private FragmentActivity fragmentActivity;
    private Fragment fragment;

    public interface OnItemClickListener {
        public void onClick(ViewHolder holder, int idAnimal);
    }

    public EstudiosAdaptador(Context contexto, OnItemClickListener escucha, FragmentActivity fragmentActivity, Fragment fragment) {
        this.contexto = contexto;
        this.escucha = escucha;
        this.fragmentActivity = fragmentActivity;
        this.fragment = fragment;

    }
    public EstudiosAdaptador(Context contexto, OnItemClickListener escucha, FragmentActivity fragmentActivity) {
        this.contexto = contexto;
        this.escucha = escucha;
        this.fragmentActivity = fragmentActivity;


    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // Referencias UI
        public TextView viewCodigoAnimal;
        public TextView viewCodigo;
        public TextView viewFecha;
        public TextView viewHora;
        public TextView viewTiempo;
        public TextView viewInvestigador;
        public TextView viewTipoTest;
        public TextView viewResultado;
        public TextView viewScoreL;
        public TextView viewScoreR;

        public Button btn_eliminar;
        public Button btn_ver_estudio;




        public ViewHolder(View v) {
            super(v);

            viewCodigo  = (TextView) v.findViewById(R.id.estudioCodigo);
            viewFecha  = (TextView) v.findViewById(R.id.estudioFecha);
            viewHora  = (TextView) v.findViewById(R.id.estudioHora);
            viewTiempo  = (TextView) v.findViewById(R.id.estudioTiempo);
            viewInvestigador = (TextView) v.findViewById(R.id.estudioInvestigador);

            viewResultado  = (TextView) v.findViewById(R.id.estudioResultado);
            viewScoreL  = (TextView) v.findViewById(R.id.estudioScoreL);
            viewScoreR  = (TextView) v.findViewById(R.id.estudioScoreR);

            btn_eliminar = (Button) v.findViewById(R.id.btn_eliminar_estudio);
            btn_eliminar.setOnClickListener(this);

            //btn_ver_estudio = (Button) v.findViewById(R.id.btn_ver_estudio);
            //btn_ver_estudio.setOnClickListener(this);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(final View view) {
            escucha.onClick(this, obtenerIdEstudio(getAdapterPosition()));
            Bundle args = new Bundle();
            args.putString("_id", obtenerIdEstudio(getAdapterPosition()) + "");
            String idStudio = obtenerIdEstudio(getAdapterPosition()) + "";

            FragmentActivity activity = (FragmentActivity)(contexto);
            android.support.v4.app.FragmentManager fm = activity.getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction;

            switch (view.getId()) {
                case R.id.btn_eliminar_estudio:

                    Log.d("Prueba", "ID: " + obtenerIdEstudio(getAdapterPosition()));

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(contexto);
                    // Setting Dialog Title
                    alertDialog.setTitle("Confirmar borrado...");
                    alertDialog.setMessage("Esta seguro de que desea borrar este estudio y su detalle asociado?");
                    alertDialog.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            int posicion = getAdapterPosition();

                            contexto.getContentResolver().delete(ContratoBBDD.Resultados.crearUriResultado("" + obtenerIdEstudio(posicion)), ""
                                    + obtenerIdEstudio(getAdapterPosition()), null);

                            contexto.getContentResolver().delete(ContratoBBDD.Estudios.crearUriEstudio("" + obtenerIdEstudio(posicion)), ""
                                    + obtenerIdEstudio(getAdapterPosition()), null);
                            notifyItemRemoved(posicion);

                          /*  fragmentActivity.getSupportFragmentManager().beginTransaction().
                                    detach(fragmentActivity.getSupportFragmentManager().findFragmentByTag("FragmentEstudio")).attach(fragmentActivity.getSupportFragmentManager().findFragmentByTag("FragmentEstudioAnimal")).commit();
*/


                        }
                    });
                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke NO event
                            //Toast.makeText(contexto.getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    });

                    alertDialog.show();
                    break;
                case R.id.btn_ver_estudios_animal:
/*                  StudyFragment fEstudios = new StudyFragment();

                    android.support.v4.app.FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame, fEstudios);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
*/

                    break;
                default:
                    // ABRIMOS EDICION DE ANIMALES
                   /* Log.d("Pulso sobre fila con ID",obtenerIdAnimal(getAdapterPosition())+"");
                    Log.d("Pulso sobre fila",getAdapterPosition()+"");
                    Bundle args = new Bundle();
                    args.putString("_id",obtenerIdAnimal(getAdapterPosition())+"");
                    FragmentActivity activity = (FragmentActivity)(contexto);
                    android.support.v4.app.FragmentManager fm = activity.getSupportFragmentManager();*/


                   /* StudyEditDialog editDialog = new StudyEditDialog ();
                    editDialog.setArguments(args);
                    editDialog.show(fm, "fragment_alert");
*/
                    EstudiosDialogEdit EstudiosDialogEdit = new EstudiosDialogEdit (idStudio);
                    FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.frame, EstudiosDialogEdit );
                    ft.commit();


                    /*
                    AnimalesDialogAdd anadirAnimal= new AnimalesDialogAdd();
                    anadirAnimal.show(fm, "AnadirAnimal");*/

                    break;

            }}

    }

    private int obtenerIdEstudio(int posicion) {
        if (itemsEstudios != null) {
            if (itemsEstudios.moveToPosition(posicion)) {
                return itemsEstudios.getInt(ConsultaEstudio.ID);
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
                .inflate(R.layout.item_estudio, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        itemsEstudios.moveToPosition(position);

        String s;
        // Asignación UI
        s = itemsEstudios.getString(ConsultaEstudio.ANIMAL_CODIGO);
        holder.viewCodigo.setText(s);

        s = itemsEstudios.getString(ConsultaEstudio.FECHA);
        holder.viewFecha.setText(s);

        s = itemsEstudios.getString(ConsultaEstudio.HORA);
        holder.viewHora.setText(s);

        s = itemsEstudios.getString(ConsultaEstudio.TIEMPO);
        holder.viewTiempo.setText(s);

        s = itemsEstudios.getString(ConsultaEstudio.SCOREL);
        holder.viewScoreL.setText(s);


        s = itemsEstudios.getString(ConsultaEstudio.SCORER);
        holder.viewScoreR.setText(s);

        s = itemsEstudios.getString(ConsultaEstudio.RESULTADO);
        holder.viewResultado.setText(s);

        s = itemsEstudios.getString(ConsultaEstudio.INVESTIGADOR_N) + " " + itemsEstudios.getString(ConsultaEstudio.INVESTIGADOR_A);
        holder.viewInvestigador.setText(s);



    }


    @Override
    public int getItemCount() {
        if (itemsEstudios != null)
            return itemsEstudios.getCount();
        return 0;
    }

    public void swapCursor(Cursor nuevoCursor) {
        if (nuevoCursor != null) {
            itemsEstudios = nuevoCursor;
        }
    }

    public Cursor getCursor() {
        return itemsEstudios;
    }

    interface ConsultaEstudio {
        int ID = 0;
        int ID_ANIMAL = 1;
        int FECHA = 2;
        int HORA = 3;
        int TIEMPO = 15;
        int TIPO_TEST = 14; //
        int RESULTADO = 6;
        int INVESTIGADOR_N = 8;
        int INVESTIGADOR_A = 9;
        int SCOREL = 16;
        int SCORER = 17;
        int ANIMAL_CODIGO =18;
    }

/*
0 = {String@4001} "_id"
1 = {String@4002} "id_animal"
2 = {String@4003} "fecha"
3 = {String@4004} "hora"
4 = {String@4005} "id_investigador1"
5 = {String@4006} "id_tipo_test"
6 = {String@4007} "resultado"
7 = {String@4008} "video"
8 = {String@4009} "INV1_NOMBRE"
9 = {String@4010} "INV1_AP"
10 = {String@4011} "INV1_EMAIL"
11 = {String@4012} "INV2_NOMBRE"
12 = {String@4013} "INV2_AP"
13 = {String@4014} "INV2_EMAIL"
14 = {String@4015} "TIPO_TEST_DES"
15 tiempo
16 scorel
17 scorer
18 ANIMAL_CODIGO
------
 */
}