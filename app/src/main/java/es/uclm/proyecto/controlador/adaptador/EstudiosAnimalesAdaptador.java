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

import java.io.File;

import es.uclm.proyecto.controlador.dialog.EstudiosDialogEdit;
import es.uclm.proyecto.R;
import es.uclm.proyecto.sqlite.ContratoBBDD;


/**
 * Created by alber on 11/04/2016.
 */


public class EstudiosAnimalesAdaptador extends RecyclerView.Adapter<EstudiosAnimalesAdaptador.ViewHolder> {
    private final Context contexto;
    private Cursor itemsEstudios;
    private OnItemClickListener escucha;
    private FragmentActivity fragmentActivity;
    private Fragment fragment;


    public interface OnItemClickListener {
        public void onClick(ViewHolder holder, int idAnimal);
    }

    public EstudiosAnimalesAdaptador(Context contexto, OnItemClickListener escucha, FragmentActivity fragmentActivity, Fragment fragment) {
        this.contexto = contexto;
        this.escucha = escucha;
        this.fragmentActivity = fragmentActivity;
        this.fragment = fragment;

    }
    public EstudiosAnimalesAdaptador(Context contexto, OnItemClickListener escucha, FragmentActivity fragmentActivity) {
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
        public TextView viewScoreL;
        public TextView viewScoreR;
        public TextView viewResultado;

        public Button btn_eliminar;
        public Button btn_ver_estudio;




        public ViewHolder(View v) {
            super(v);
            //viewCodigoAnimal = (TextView) v.findViewById(R.id.estudioAnimalCodigo);
            viewCodigo  = (TextView) v.findViewById(R.id.estudioCodigo);
            viewFecha  = (TextView) v.findViewById(R.id.estudioFecha);
            viewHora  = (TextView) v.findViewById(R.id.estudioHora);
            viewTiempo  = (TextView) v.findViewById(R.id.estudioTiempo);
            viewInvestigador = (TextView) v.findViewById(R.id.estudioInvestigador);
            //viewTipoTest  = (TextView) v.findViewById(R.id.estudioTipoTest);
            viewResultado  = (TextView) v.findViewById(R.id.estudioResultado);
            viewScoreL = (TextView) v.findViewById(R.id.estudioScoreL);
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
            final String[] video = {""};
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

                            int posicionArray = itemsEstudios.getPosition();
//                          itemsEstudios.moveToPosition(posicion);
                            video[0] = itemsEstudios.getString(ConsultaEstudio.VIDEO);
                            //itemsEstudios.moveToPosition(posicionArray);


                            contexto.getContentResolver().delete(ContratoBBDD.Resultados.crearUriResultado("" + obtenerIdEstudio(posicion)), ""
                                    + obtenerIdEstudio(getAdapterPosition()), null);

                            contexto.getContentResolver().delete(ContratoBBDD.Estudios.crearUriEstudio("" + obtenerIdEstudio(posicion)), ""
                                    + obtenerIdEstudio(getAdapterPosition()), null);
                            notifyItemRemoved(posicion);

                            if (!(video[0].equals(""))){
                                File file = new File(video[0]);
                                file.delete();
                            }


                           fragmentActivity.getSupportFragmentManager().beginTransaction().
                                detach(fragmentActivity.getSupportFragmentManager().findFragmentByTag("FragmentEstudioAnimal")).attach(fragmentActivity.getSupportFragmentManager().findFragmentByTag("FragmentEstudioAnimal")).commit();



                        }
                    });
                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
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

                    EstudiosDialogEdit EstudiosDialogEdit = new EstudiosDialogEdit (idStudio);
                    FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.frame, EstudiosDialogEdit );
                    ft.commit();

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
        //s = itemsEstudios.getString(ConsultaEstudio.ID);
        //holder.viewCodigo.setText(s);

      s = itemsEstudios.getString(ConsultaEstudio.ID);
        holder.viewCodigo.setText(s);

        s = itemsEstudios.getString(ConsultaEstudio.FECHA);
        holder.viewFecha.setText(s);

        s = itemsEstudios.getString(ConsultaEstudio.HORA);
        holder.viewHora.setText(s);

        s = itemsEstudios.getString(ConsultaEstudio.TIEMPO);
        holder.viewTiempo.setText(s);

        s = itemsEstudios.getString(ConsultaEstudio.RESULTADO);
        holder.viewResultado.setText(s);

        s = itemsEstudios.getString(ConsultaEstudio.SCOREL);
        holder.viewScoreL.setText(s);

        s = itemsEstudios.getString(ConsultaEstudio.SCORER);
        holder.viewScoreR.setText(s);

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
        int ID = 19;
        int ID_ANIMAL = 1;
        int FECHA = 20;
        int HORA = 21;
        int TIEMPO = 22;
        int TIPO_TEST = 27; //
        int RESULTADO = 25;
        int INVESTIGADOR_N = 13;
        int INVESTIGADOR_A = 14;
        int VIDEO = 26 ;
        int SCOREL = 28;
        int SCORER = 29;
    }

/*
    0   "_id"
            1    "codigo"
            2    "cepa"
            3    "sexo"
            4    "transgenico"
            5    "id_especie"
            6    "fecha_cir"
            7    "fecha_nac"
            8    "modelo_lesion"
            9    "tratamiento"
            10    "id_investigador"
            11    "ESPECIE_DES"
            12    "INVRESP_NOMBRE"
            13    "INVRESP_AP"
            14    "INVRESP_EMAIL"
            15    "INV1_NOMBRE"
            16    "INV1_AP"
            17    "INV1_EMAIL"
            18    "INV2_NOMBRE"
            19    "INV2_AP"
            20    "INV2_EMAIL"
            21    "ID_ESTUDIO"
            22    "fecha"
            23    "hora"
            24    "tiempo"
            25    "id_investigador1"
            26    "id_tipo_test"
            27    "resultado"
            28    "video"
            29    "TIPO_TEST_DES"
            30      scorel
            31     scorer    */
}