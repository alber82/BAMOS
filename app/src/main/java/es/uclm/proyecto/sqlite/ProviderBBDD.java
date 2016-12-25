/*
* PENDIENTE
*
* - DEFINIR GENERACION DE PK PARA CADA OBJETO
* */

package es.uclm.proyecto.sqlite;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;

import es.uclm.proyecto.modelo.Especie;
import es.uclm.proyecto.modelo.Estudio;
import es.uclm.proyecto.sqlite.ContratoBBDD.Animales;
import es.uclm.proyecto.sqlite.ContratoBBDD.Especies;
import es.uclm.proyecto.sqlite.ContratoBBDD.Estudios;
import es.uclm.proyecto.sqlite.ContratoBBDD.Resultados;
import es.uclm.proyecto.sqlite.ContratoBBDD.Investigadores;
import es.uclm.proyecto.sqlite.ContratoBBDD.Tipo_Tests;
import es.uclm.proyecto.sqlite.BaseDeDatos.Tablas;

public class ProviderBBDD extends ContentProvider {

    public static final String TAG = "Provider";
    public static final String URI_NO_SOPORTADA = "Uri no soportada";

    private BaseDeDatos helper;
    private ContentResolver resolver;

    public ProviderBBDD() {
    }

    // [URI_MATCHER]
    public static final UriMatcher uriMatcher;

    // Casos
    public static final int ANIMALES = 100;  //OK
    public static final int ANIMALES_ID = 101; //OK
    public static final int ANIMALES_ID_ESTUDIOS = 102; //OK
    public static final int ANIMALES_ID_ESTUDIOS_RESULTADOS = 103;

    public static final int ESTUDIOS = 200;
    public static final int ESTUDIOS_ID = 201;
    public static final int ESTUDIOS_ID_RESULTADOS = 202;

    public static final int RESULTADOS = 300;
    public static final int RESULTADOS_ID = 301;

    public static final int ESPECIES = 400; //OK
    public static final int ESPECIES_ID = 401; //OK

    public static final int INVESTIGADORES = 500; //OK
    public static final int INVESTIGADORES_ID = 501;//OK

    public static final int TIPO_TESTS = 600; //OK
    public static final int TIPO_TESTS_ID = 601;//OK
    public static final int TIPO_TESTS_ID_ESPECIE = 602;//OK

    public static final String AUTORIDAD = "es.uclm.proyecto";

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(AUTORIDAD, "animales", ANIMALES);
        uriMatcher.addURI(AUTORIDAD, "animales/*", ANIMALES_ID);
        uriMatcher.addURI(AUTORIDAD, "animales/*/estudios", ANIMALES_ID_ESTUDIOS);
        uriMatcher.addURI(AUTORIDAD, "animales/*/estudios/resultados", ANIMALES_ID_ESTUDIOS_RESULTADOS);

        uriMatcher.addURI(AUTORIDAD, "estudios", ESTUDIOS);
        uriMatcher.addURI(AUTORIDAD, "estudios/*", ESTUDIOS_ID);
        uriMatcher.addURI(AUTORIDAD, "estudios/*/resultados", ESTUDIOS_ID_RESULTADOS);

        uriMatcher.addURI(AUTORIDAD, "resultados", RESULTADOS);
        uriMatcher.addURI(AUTORIDAD, "resultados/*", RESULTADOS_ID);

        uriMatcher.addURI(AUTORIDAD, "especies", ESPECIES);
        uriMatcher.addURI(AUTORIDAD, "especies/*", ESPECIES_ID);

        uriMatcher.addURI(AUTORIDAD, "investigadores", INVESTIGADORES);
        uriMatcher.addURI(AUTORIDAD, "investigadores/*", INVESTIGADORES_ID);

        uriMatcher.addURI(AUTORIDAD, "tipo_tests", TIPO_TESTS);
        uriMatcher.addURI(AUTORIDAD, "tipo_tests/*", TIPO_TESTS_ID);
        uriMatcher.addURI(AUTORIDAD, "tipo_tests/*/especies", TIPO_TESTS_ID_ESPECIE);
    }

    /*SECUENCIAS CONSULTA*/
    private static final String ANIMALES_JOIN_ESPECIE = "animal" +
        " INNER JOIN especie" +
        " ON animal.id_especie = especie._id";

    private static final String ANIMALES_JOIN_ESTUDIO_ESPECIE_Y_INVESTIGADOR = "animal" +
            " INNER JOIN estudio" +
            " ON animal._id = estudio.id_animal" +
            " INNER JOIN especie" +
            " ON animal.id_especie = especie._id" +
            " INNER JOIN tipo_test" +
            " ON estudio.id_tipo_test = tipo_test._id" +
            " INNER JOIN investigador i1 " +
            " ON estudio.id_investigador1 = i1._id" +
            " INNER JOIN investigador i2 " +
            " ON estudio.id_investigador2 = i2._id";

    private static final String ANIMALES_JOIN_ESTUDIO_RESULTADO_ESPECIE_Y_INVESTIGADOR = "animal" +
            " INNER JOIN estudio" +
            " ON animal._id = estudio.id_animal" +
            " INNER JOIN resultado" +
            " ON estudio._id = resultado._id" +
            " INNER JOIN especie" +
            " ON animal.id_especie = especie._id" +
            " INNER JOIN tipo_test" +
            " ON estudio.id_tipo_test = tipo_test._id" +
            " INNER JOIN investigador i1 " +
            " ON estudio.id_investigador1 = i1._id" +
            " INNER JOIN investigador i2 " +
            " ON estudio.id_investigador2 = i2._id";


    private static final String ESTUDIO_JOIN_INVESTIGADOR = "estudio" +
            " INNER JOIN tipo_test" +
            " ON estudio.id_tipo_test = tipo_test._id" +
            " INNER JOIN animal" +
            " ON estudio.id_animal = animal._id" +
            " INNER JOIN investigador i1" +
            " ON estudio.id_investigador1 = i1._id" +
            " INNER JOIN investigador i2 " +
            " ON estudio.id_investigador2 = i2._id" ;

    private static final String ESTUDIO_JOIN_RESULTADO_Y_INVESTIGADOR = "estudio" +
            " INNER JOIN tipo_test" +
            " ON estudio.id_tipo_test = tipo_test._id" +
            " INNER JOIN resultado" +
            " ON estudio._id = resultado._id" +
            " INNER JOIN animal" +
            " ON estudio.id_animal = animal._id" +
            " INNER JOIN investigador i1" +
            " ON estudio.id_investigador1 = i1._id" +
            " INNER JOIN investigador i2 " +
            " ON estudio.id_investigador2 = i2._id" ;

    private static final String RESULTADO_CONSULTA = "resultado";

    private static final String INVESTIGADORES_CONSULTA = "investigador";

    private static final String ESPECIES_CONSULTA = "especie";

    private static final String TIPO_TESTS_CONSULTA = "tipo_test";

    private static final String TIPO_TESTS_JOIN_ESPECIE = "tipo_test" +
            " INNER JOIN especie" +
            " ON tipo_test.id_especie = especie._id";

    /*PROYECCIONES*/
    private final String[] proyAnimal = new String[]{
            Tablas.ANIMAL + "." + Animales._id,
            //Tablas.ANIMAL + ".*",
            Animales.codigo,
            Animales.cepa,
            Animales.sexo,
            Animales.transgenico,
            Animales.id_especie,
            Animales.fecha_cir,
            Animales.fecha_nac,
            Animales.modelo_lesion,
            Animales.tratamiento,
            Animales.origen,
            Animales.experimento,
            Especies.descripcion,
            };

    private final String[] proyAnimalEstudio = new String[]{
            Tablas.ANIMAL + "." + Animales._id,
            //Tablas.ANIMAL + ".*",
            Animales.codigo,
            Animales.cepa,
            Animales.sexo,
            Animales.transgenico,
            Tablas.ANIMAL + "." + Animales.id_especie,
            Animales.fecha_cir,
            Animales.fecha_nac,
            Animales.modelo_lesion,
            Animales.tratamiento,
            Animales.origen,
            Animales.experimento,
            Tablas.ESPECIE + "." +Especies.descripcion + " AS ESPECIE_DES",
            "I1." +Investigadores.nombre    + " AS INV1_NOMBRE",
            "I1." +Investigadores.apellidos + " AS INV1_AP",
            "I1." +Investigadores.email     + " AS INV1_EMAIL",
            "I2." +Investigadores.nombre    + " AS INV2_NOMBRE",
            "I2." +Investigadores.apellidos + " AS INV2_AP",
            "I2." +Investigadores.email     + " AS INV2_EMAIL",
            Tablas.ESTUDIO + "." + Estudios._id + " AS ID_ESTUDIO",
            Estudios.fecha,
            Estudios.hora,
            Estudios.tiempo,
            Estudios.id_investigador1,
            Estudios.id_tipo_test,
            Estudios.resultado,
            Estudios.video,
            Tablas.TIPO_TEST + "." +Tipo_Tests.descripcion + " AS TIPO_TEST_DES",
            Estudios.scorel,
            Estudios.scorer,
            Estudios.comentarios,
            };

    private final String[] proyAnimalEstudioResultado = new String[]{
            Tablas.ANIMAL + "." + Animales._id,
            //Tablas.ANIMAL + ".*",
            Animales.codigo,
            Animales.cepa,
            Animales.sexo,
            Animales.transgenico,
            Tablas.ANIMAL + "." + Animales.id_especie,
            Animales.fecha_cir,
            Animales.fecha_nac,
            Animales.modelo_lesion,
            Animales.tratamiento,
            Animales.origen,
            Animales.experimento,
            Tablas.ESPECIE + "." +Especies.descripcion + " AS ESPECIE_DES",
            "I1." +Investigadores.nombre    + " AS INV1_NOMBRE",
            "I1." +Investigadores.apellidos + " AS INV1_AP",
            "I1." +Investigadores.email     + " AS INV1_EMAIL",
            "I2." +Investigadores.nombre    + " AS INV2_NOMBRE",
            "I2." +Investigadores.apellidos + " AS INV2_AP",
            "I2." +Investigadores.email     + " AS INV2_EMAIL",
            Tablas.ESTUDIO + "." + Estudios._id + " AS ID_ESTUDIO",
            Estudios.fecha,
            Estudios.hora,
            Estudios.tiempo,
            Estudios.id_investigador1,
            Estudios.id_tipo_test,
            Estudios.resultado,
            Estudios.video,
            Tablas.TIPO_TEST + "." +Tipo_Tests.descripcion + " AS TIPO_TEST_DES",
            Estudios.scorel,
            Estudios.scorer,
            Estudios.comentarios,
            Resultados.am_l,
            Resultados.am_r,
            Resultados.pwo_l,
            Resultados.pwo_r,
            Resultados.pw_l,
            Resultados.pw_r,
            Resultados.sd_l,
            Resultados.sd_r,
            Resultados.sp_l,
            Resultados.sp_r,
            Resultados.coord,
            Resultados.pp_ic_l,
            Resultados.pp_ic_r,
            Resultados.pp_lo_l,
            Resultados.pp_lo_r,
            Resultados.ti,
            Resultados.tail
    };

    private String[] proyEstudio = {
            Tablas.ESTUDIO + "." + Estudios._id,
            Estudios.id_animal,
            Estudios.fecha,
            Estudios.hora,
            Estudios.id_investigador1,
            Estudios.id_tipo_test,
            Estudios.resultado,
            Estudios.video,
            "I1." + Investigadores.nombre     + " AS INV1_NOMBRE",
            "I1." + Investigadores.apellidos + " AS INV1_AP",
            "I1." + Investigadores.email + " AS INV1_EMAIL",
            "I2." + Investigadores.nombre + " AS INV2_NOMBRE",
            "I2." + Investigadores.apellidos + " AS INV2_AP",
            "I2." + Investigadores.email + " AS INV2_EMAIL",
            Tablas.TIPO_TEST + "." +Tipo_Tests.descripcion + " AS TIPO_TEST_DES",
            Estudios.tiempo,
            Estudios.scorel,
            Estudios.scorer,
            Animales.codigo + " AS ANIMAL_CODIGO",
            Estudios.comentarios};

    private String[] proyEstudioResultado = {
            Tablas.ESTUDIO + "." + Estudios._id,
            Estudios.id_animal,
            Estudios.fecha,
            Estudios.hora,
            Estudios.tiempo,
            Estudios.id_investigador1,
            Estudios.id_investigador2,
            Estudios.id_tipo_test,
            Estudios.resultado,
            Estudios.video,
            "I1." + Investigadores.nombre + " AS INV1_NOMBRE",
            "I1." + Investigadores.apellidos + " AS INV1_AP",
            "I1." + Investigadores.email + " AS INV2_EMAIL",
            "I2." + Investigadores.nombre + " AS INV2_NOMBRE",
            "I2." + Investigadores.apellidos+ " AS INV2_AP",
            "I2." + Investigadores.email + " AS INV2_EMAIL",
            Resultados.am_l,
            Resultados.am_r,
            Resultados.pwo_l,
            Resultados.pwo_r,
            Resultados.pw_l,
            Resultados.pw_r,
            Resultados.sd_l,
            Resultados.sd_r,
            Resultados.sp_l,
            Resultados.sp_r,
            Resultados.coord,
            Resultados.pp_ic_l,
            Resultados.pp_ic_r,
            Resultados.pp_lo_l,
            Resultados.pp_lo_r,
            Resultados.ti,
            Resultados.tail,
            Tablas.TIPO_TEST + "." + Tipo_Tests.descripcion + " AS TIPO_TEST_DES",
            Estudios.scorel,
            Estudios.scorer,
            Estudios.comentarios
    };

    private String[] proyResultado = {
            Tablas.RESULTADO + "." +Resultados._id,
            Resultados.am_l,
            Resultados.am_r,
            Resultados.pwo_l,
            Resultados.pwo_r,
            Resultados.pw_l,
            Resultados.pw_r,
            Resultados.sd_l,
            Resultados.sd_r,
            Resultados.sp_l,
            Resultados.sp_r,
            Resultados.coord,
            Resultados.pp_ic_l,
            Resultados.pp_ic_r,
            Resultados.pp_lo_l,
            Resultados.pp_lo_r,
            Resultados.ti,
            Resultados.tail
    };

    private final String[] proyInvestigador = new String[]{
            Tablas.INVESTIGADOR + "." + Investigadores._id,
            Investigadores.nombre,
            Investigadores.apellidos,
            Investigadores.email};

    private final String[] proyEspecie = new String[]{
            Tablas.ESPECIE + "." + Especies._id,
            Especies.descripcion};

    private final String[] proyTipoTest = new String[]{
            Tablas.TIPO_TEST + "." + Tipo_Tests._id,
            Tipo_Tests.descripcion,
            Tipo_Tests.id_especie};

    private final String[] proyTipoTestEspecie = new String[]{
            Tablas.TIPO_TEST + "." + Tipo_Tests._id,
            "TIPO_TEST." + Tipo_Tests.descripcion + " AS TIPO_TEST_DES",
            Tipo_Tests.id_especie,
            "ESPECIE." + Especies.descripcion + " AS ESPECIE_DES"};

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case ANIMALES:
                return ContratoBBDD.generarMime("animales");
            case ANIMALES_ID:
                return ContratoBBDD.generarMimeItem("animales");
            case ESTUDIOS:
                return ContratoBBDD.generarMime("estudios");
            case ESTUDIOS_ID:
                return ContratoBBDD.generarMimeItem("estudios");
            case RESULTADOS:
                return ContratoBBDD.generarMime("resultados");
            case RESULTADOS_ID:
                return ContratoBBDD.generarMimeItem("resultados");
            case ESPECIES:
                return ContratoBBDD.generarMime("especies");
            case ESPECIES_ID:
                return ContratoBBDD.generarMimeItem("especies");
            case INVESTIGADORES:
                return ContratoBBDD.generarMime("investigadores");
            case INVESTIGADORES_ID:
                return ContratoBBDD.generarMimeItem("investigadores");
            case TIPO_TESTS:
                return ContratoBBDD.generarMime("tipo_tests");
            case TIPO_TESTS_ID:
                return ContratoBBDD.generarMimeItem("tipo_tests");
            default:
                throw new UnsupportedOperationException("Uri desconocida =>" + uri);
        }
    }

    @Override
    public boolean onCreate() {
        helper = new BaseDeDatos(getContext());
        resolver = getContext().getContentResolver();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // Obtener base de datos
        SQLiteDatabase db = helper.getReadableDatabase();
        // Comparar Uri
        int match = uriMatcher.match(uri);

        // string auxiliar para los codigos
        String id;

        Cursor c;

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        switch (match) {
            case ANIMALES:
                // Obtener filtro
                String filtro = Animales.tieneFiltro(uri)
                        ? construirFiltro(uri.getQueryParameter("filtro")) : null;

                builder.setTables(ANIMALES_JOIN_ESPECIE);
                c = builder.query(db, proyAnimal,
                        null, null, null, null, filtro);
                break;
            case ANIMALES_ID:    // Consultando una cabecera de pedido
                String idAnimal =Animales.obtenerCodigoAnimal(uri);
                builder.setTables(ANIMALES_JOIN_ESPECIE);
                c = builder.query(db, proyAnimal,
                        "ANIMAL."+Animales._id + "= " + "\'" + idAnimal + "\'"
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs, null, null, null);
                break;

            case ANIMALES_ID_ESTUDIOS:
                id  = Animales.obtenerCodigoAnimal(uri);
                builder.setTables(ANIMALES_JOIN_ESTUDIO_ESPECIE_Y_INVESTIGADOR);
                c = builder.query(db, proyAnimalEstudio,
                        "ESTUDIO." + Estudios.id_animal + "=" + "\'" + id + "\'"
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs, null, null, sortOrder);
                break;


            case ANIMALES_ID_ESTUDIOS_RESULTADOS:
                id  = Animales.obtenerCodigoAnimal(uri);
                builder.setTables(ANIMALES_JOIN_ESTUDIO_RESULTADO_ESPECIE_Y_INVESTIGADOR);
                c = builder.query(db, proyAnimalEstudioResultado,
                        "ESTUDIO." + Estudios.id_animal + "=" + "\'" + id + "\'"
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs, null, null, sortOrder);
                break;


            case ESTUDIOS:
                builder.setTables(ESTUDIO_JOIN_INVESTIGADOR);
                c = builder.query(db, proyEstudio,
                        null, null, null, null, null);
                break;

            case ESTUDIOS_ID:    // Consultando una cabecera de pedido
                id  =Estudios.obtenerCodigoEstudio(uri);
                builder.setTables(ESTUDIO_JOIN_INVESTIGADOR);
                c = builder.query(db, proyEstudio,
                        "ESTUDIO." + Estudios._id + "=" + "\'" + id + "\'"
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs, null, null, null);
                break;
            case ESTUDIOS_ID_RESULTADOS:
                id = Estudios.obtenerCodigoEstudio(uri);
                builder.setTables(ESTUDIO_JOIN_RESULTADO_Y_INVESTIGADOR);
                c = builder.query(db, proyEstudioResultado,
                        "ESTUDIO." + Estudios._id + "=" + "\'" + id + "\'"
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs, null, null, sortOrder);
                break;

            case RESULTADOS:
                builder.setTables(RESULTADO_CONSULTA);
                c = builder.query(db, proyResultado,
                        null, null, null, null, null);
                break;

            case RESULTADOS_ID:    // Consultando una cabecera de pedido
                id =Resultados.obtenerCodigoResultado(uri);
                builder.setTables(ESPECIES_CONSULTA);
                c = builder.query(db, proyResultado,
                        Resultados._id + "=" + "\'" + id + "\'"
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs, null, null, null);
                break;

            case ESPECIES:
                builder.setTables(ESPECIES_CONSULTA);
                c = builder.query(db, proyEspecie,
                        null, null, null, null, null);
                break;

            case ESPECIES_ID:    // Consultando una cabecera de pedido
                id =Especies.obtenerCodigoEspecie(uri);
                builder.setTables(ESPECIES_CONSULTA);
                c = builder.query(db, proyEspecie,
                        Especies._id + "=" + "\'" + id + "\'"
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs, null, null, null);
                break;

            case INVESTIGADORES:
                builder.setTables(INVESTIGADORES_CONSULTA);
                c = builder.query(db, proyInvestigador,
                        null, null, null, null, null);
                break;
            case INVESTIGADORES_ID:    // Consultando una cabecera de pedido
                id =Investigadores.obtenerCodigoInvestigador(uri);
                builder.setTables(INVESTIGADORES_CONSULTA);
                c = builder.query(db, proyInvestigador,
                        Investigadores._id + "=" + "\'" + id + "\'"
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs, null, null, null);
                break;
            case TIPO_TESTS:
                builder.setTables(TIPO_TESTS_CONSULTA);
                c = builder.query(db, proyTipoTest,
                        null, null, null, null, null);
                break;
            case TIPO_TESTS_ID:    // Consultando una cabecera de pedido
                id = ContratoBBDD.Tipo_Tests.obtenerCodigoTipoTest(uri);
                builder.setTables(TIPO_TESTS_CONSULTA);
                c = builder.query(db, proyTipoTest,
                        Tipo_Tests._id + "=" + "\'" + id + "\'"
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs, null, null, null);
                break;
            case TIPO_TESTS_ID_ESPECIE:    // Consultando una cabecera de pedido
                id = ContratoBBDD.Tipo_Tests.obtenerCodigoTipoTest(uri);
                builder.setTables(TIPO_TESTS_JOIN_ESPECIE);
                c = builder.query(db, proyTipoTestEspecie,
                        Tipo_Tests.id_especie + "=" + "\'" + id + "\'"
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs, null, null, null);
                break;

            default:
                throw new UnsupportedOperationException("Uri no soportada");
        }

        c.setNotificationUri(resolver, uri);

        return c;

    }

    private String construirFiltro(String filtro) {
        String sentencia = null;

        switch (filtro) {
            case Animales.FILTRO_FECHA:
                sentencia = "animales.fecha_nam";
                break;
            case Animales.FILTRO_SEXO:
                sentencia = "animales.sexo";
                break;
            case Animales.FILTRO_ANIMAL:
                sentencia = "animales.codigo";
                break;
        }

        return sentencia;
    }

    private void notificarCambio(Uri uri) {
        resolver.notifyChange(uri, null);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(TAG, "Inserción en " + uri + "( " + values.toString() + " )\n");

        SQLiteDatabase bd = helper.getWritableDatabase();
        String codigo = null;
        int id;

        switch (uriMatcher.match(uri)) {
            case ANIMALES:
                // Generar Pk
                if (null == values.getAsString(Animales.codigo)) {
                    codigo = Animales.generarCodigoAnimal();
                    values.put(Animales.codigo, codigo);
                }

                id = (int) bd.insertOrThrow(Tablas.ANIMAL, null, values);
                values.put(Animales._id, id);
                notificarCambio(uri);
                return Animales.crearUriAnimal(""+id);

            case ESTUDIOS:
                id = (int) bd.insertOrThrow(Tablas.ESTUDIO, null, values);
                values.put(Estudios._id, id);
                notificarCambio(uri);
                return Estudios.crearUriEstudio(""+id);
                //return Estudios.crearUriEstudio(codigo);

            case RESULTADOS:
                /* Generar Pk
                if (null == values.getAsString(Animales.CODIGO)) {
                    codigo = Animales.generarCodigoAnimal();
                    values.put(Animales.CODIGO, codigo);
                }*/

                bd.insertOrThrow(Tablas.RESULTADO, null, values);
                notificarCambio(uri);
                return Resultados.crearUriResultado(codigo);
            case ESPECIES:
                id = (int) bd.insertOrThrow(Tablas.ESPECIE, null, values);
                values.put(Especies._id, id);
                notificarCambio(uri);
                return Especies.crearUriEspecie(codigo);
            case INVESTIGADORES:
                id = (int) bd.insertOrThrow(Tablas.INVESTIGADOR, null, values);
                values.put(Investigadores._id, id);
                notificarCambio(uri);
                return Investigadores.crearUriInvestigador(codigo);
            case TIPO_TESTS:
                id = (int) bd.insertOrThrow(Tablas.TIPO_TEST, null, values);
                values.put(ContratoBBDD.Tipo_Tests._id, id);
                notificarCambio(uri);
                return ContratoBBDD.Tipo_Tests.crearUriTipoTest(codigo);
            default:
                throw new UnsupportedOperationException("Uri no soportada");
        }


    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String codigo;
        int afectados;

        switch (uriMatcher.match(uri)) {
            case ANIMALES_ID:
                codigo = Animales.obtenerCodigoAnimal(uri);
                afectados = db.update(Tablas.ANIMAL, values,
                        Animales._id + " = ?", new String[]{codigo});
                notificarCambio(uri);
                break;
            case ESTUDIOS_ID:
                codigo = Estudios.obtenerCodigoEstudio(uri);
                afectados = db.update(Tablas.ESTUDIO, values,
                        Estudios._id + " = ?", new String[]{codigo});
                notificarCambio(uri);
                break;
            case RESULTADOS_ID:
                codigo = Estudios.obtenerCodigoEstudio(uri);
                afectados = db.update(Tablas.RESULTADO, values,
                        Resultados._id + " = ?", new String[]{codigo});
                notificarCambio(uri);
                break;
            case INVESTIGADORES_ID:
                codigo = Investigadores.obtenerCodigoInvestigador(uri);
                afectados = db.update(Tablas.INVESTIGADOR, values,
                        Investigadores._id + " = ?", new String[]{codigo});
                notificarCambio(uri);
                break;
            case TIPO_TESTS_ID:
                codigo = Tipo_Tests.obtenerCodigoTipoTest(uri);
                afectados = db.update(Tablas.TIPO_TEST, values,
                        Tipo_Tests._id + " = ?", new String[]{codigo});
                notificarCambio(uri);
                break;
            default:
                throw new UnsupportedOperationException("Uri no soportada");
        }

        return afectados;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(TAG, "delete: " + uri);

        SQLiteDatabase bd = helper.getWritableDatabase();
        String codigo;
        int afectadas;

        switch (uriMatcher.match(uri)) {
            case ANIMALES_ID:
                // Obtener codigo
                codigo = Animales.obtenerCodigoAnimal(uri);
                afectadas = bd.delete(
                        Tablas.ANIMAL,
                        Animales._id + " = ? ",
                        new String[]{codigo}
                );
                notificarCambio(uri);
                break;
            case ESTUDIOS_ID:
                // Obtener codigo
                codigo = Estudios.obtenerCodigoEstudio(uri);
                afectadas = bd.delete(
                        Tablas.ESTUDIO,
                        Estudios._id + " = ? ",
                        new String[]{codigo}
                );
                notificarCambio(uri);
                break;
            case RESULTADOS_ID:
                // Obtener codigo
                codigo = Resultados.obtenerCodigoResultado(uri);
                afectadas = bd.delete(
                        Tablas.RESULTADO,
                        Resultados._id + " = ? ",
                        new String[]{codigo}
                );
                notificarCambio(uri);
                break;
            case ESPECIES_ID:
                // Obtener codigo
                codigo = Especies.obtenerCodigoEspecie(uri);
                afectadas = bd.delete(
                        Tablas.ESPECIE,
                        Especies._id + " = ? ",
                        new String[]{codigo}
                );
                notificarCambio(uri);
                break;
            case INVESTIGADORES_ID:
                // Obtener codigo
                codigo = Investigadores.obtenerCodigoInvestigador(uri);
                afectadas = bd.delete(
                        Tablas.INVESTIGADOR,
                        Investigadores._id + " = ? ",
                        new String[]{codigo}
                );
                notificarCambio(uri);
                break;
            default:
                throw new UnsupportedOperationException(URI_NO_SOPORTADA);
        }
        return afectadas;
    }

    @Override
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {
        final SQLiteDatabase db = helper.getWritableDatabase();
        db.beginTransaction();
        try {
            final int numOperations = operations.size();
            final ContentProviderResult[] results = new ContentProviderResult[numOperations];
            for (int i = 0; i < numOperations; i++) {
                results[i] = operations.get(i).apply(this, results, i);
            }
            db.setTransactionSuccessful();
            return results;
        } finally {
            db.endTransaction();
        }
    }

}
