package es.uclm.proyecto.sqlite;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import es.uclm.proyecto.modelo.Resultado;
import es.uclm.proyecto.sqlite.ContratoBBDD.Animales;
import es.uclm.proyecto.sqlite.ContratoBBDD.Especies;
import es.uclm.proyecto.sqlite.ContratoBBDD.Estudios;
import es.uclm.proyecto.sqlite.ContratoBBDD.Investigadores;
import es.uclm.proyecto.sqlite.ContratoBBDD.Resultados;

public class BaseDeDatos extends SQLiteOpenHelper {

    private static String DB_PATH;
    private static final String NOMBRE_BASE_DATOS = "bd.db";
    private static final int VERSION_ACTUAL = 1;
    private final Context contexto;
    private SQLiteDatabase database;

    interface Tablas {
        String ANIMAL       = "ANIMAL";
        String ESPECIE      = "ESPECIE";
        String ESTUDIO      = "ESTUDIO";
        String RESULTADO    = "RESULTADO";
        String INVESTIGADOR = "INVESTIGADOR";
        String TIPO_TEST    = "TIPO_TEST";
    }

    interface Referencias {

        String CODIGO_ANIMAL = String.format("REFERENCES %s(%s) ON DELETE CASCADE",
                Tablas.ANIMAL, Animales._id);

        String CODIGO_ESPECIE = String.format("REFERENCES %s(%s)",
                Tablas.ESPECIE, Especies._id);

        String CODIGO_ESTUDIO = String.format("REFERENCES %s(%s)",
                Tablas.ESTUDIO, Estudios._id);

        String CODIGO_RESULTADO = String.format("REFERENCES %s(%s)",
                Tablas.RESULTADO, Resultados._id);

        String CODIGO_INVESTIGADOR = String.format("REFERENCES %s(%s)",
                Tablas.INVESTIGADOR, Investigadores._id);
    }

    public BaseDeDatos(Context contexto) {
        super(contexto, NOMBRE_BASE_DATOS, null, VERSION_ACTUAL);
        this.contexto = contexto;
        String packageName = contexto.getPackageName();
        DB_PATH = String.format("//data//data//%s//databases//", packageName);
        openDataBase();
    }


    //This piece of code will create a database if it’s not yet created
    public void createDataBase() {
        boolean dbExiste = checkDataBase();
        if (!dbExiste) {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                Log.e(this.getClass().toString(), "Error copiando");
                throw new Error("Error copiando Base de datos!");
            }
        } else {
            Log.i(this.getClass().toString(), "Existe base de datos");
        }
    }

    //Performing a database existence check
    private boolean checkDataBase() {
        SQLiteDatabase checkDb = null;
        try {
            String path = DB_PATH + NOMBRE_BASE_DATOS;
            checkDb = SQLiteDatabase.openDatabase(path, null,
                    SQLiteDatabase.OPEN_READONLY);
        } catch (SQLException e) {
            Log.e(this.getClass().toString(), "Error while checking db");
        }
        //Android doesn’t like resource leaks, everything should
        // be closed
        if (checkDb != null) {
            checkDb.close();
        }
        return checkDb != null;
    }

    //Method for copying the database
    private void copyDataBase() throws IOException {
        //Open a stream for reading from our ready-made database
        //The stream source is located in the assets
        InputStream externalDbStream = contexto.getAssets().open(NOMBRE_BASE_DATOS);

        //Path to the created empty database on your Android device
        String outFileName = DB_PATH + NOMBRE_BASE_DATOS;

        //Now create a stream for writing the database byte by byte
        OutputStream localDbStream = new FileOutputStream(outFileName);

        //Copying the database
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = externalDbStream.read(buffer)) > 0) {
            localDbStream.write(buffer, 0, bytesRead);
        }
        //Don’t forget to close the streams
        localDbStream.close();
        externalDbStream.close();
    }

    public SQLiteDatabase openDataBase() throws SQLException {
        String path = DB_PATH + NOMBRE_BASE_DATOS;
        if (database == null) {
            createDataBase();
            database = SQLiteDatabase.openDatabase(path, null,
                    SQLiteDatabase.OPEN_READWRITE);
        }
        return database;
    }

    @Override
    public synchronized void close() {
        if (database != null) {
            database.close();
        }
        super.close();
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                db.setForeignKeyConstraintsEnabled(true);
            } else {
                db.execSQL("PRAGMA foreign_keys=OFF");
            }
        }
    }

    public SQLiteDatabase getDb() {
        return database;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {}
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}