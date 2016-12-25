package es.uclm.proyecto.sqlite;

import android.net.Uri;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Clase que establece los nombres a usar en la base de datos
 */
public class ContratoBBDD {

    public static final String AUTORIDAD_CONTENIDO = "es.uclm.proyecto";

    public static final Uri URI_BASE = Uri.parse("content://" + AUTORIDAD_CONTENIDO);

    private static final String RUTA_ANIMALES = "animales";
    private static final String RUTA_ESTUDIOS = "estudios";
    private static final String RUTA_RESULTADO = "resultados";
    private static final String RUTA_ESPECIES = "especies";
    private static final String RUTA_TIPO_TESTS = "tipo_tests";
    private static final String RUTA_INVESTIGADORES = "investigadores";


    // [TIPOS_MIME]
    public static final String BASE_CONTENIDOS = "proyecto.";

    public static final String TIPO_CONTENIDO = "vnd.android.cursor.dir/vnd."
            + BASE_CONTENIDOS;

    public static final String TIPO_CONTENIDO_ITEM = "vnd.android.cursor.item/vnd."
            + BASE_CONTENIDOS;

    public static String generarMime(String id) {
        if (id != null) {
            return TIPO_CONTENIDO + id;
        } else {
            return null;
        }
    }

    public static String generarMimeItem(String id) {
        if (id != null) {
            return TIPO_CONTENIDO_ITEM + id;
        } else {
            return null;
        }
    }
    // [/TIPOS_MIME]

    interface ColumnasAnimal {
        String _id               = "_id";
        String codigo           = "codigo";
        String id_especie       = "id_especie";
        String cepa             = "cepa";
        String transgenico      = "transgenico";
        String sexo             = "sexo";
        String fecha_nac        = "fecha_nac";
        String fecha_cir        = "fecha_cir";
        String tratamiento      = "tratamiento";
        String modelo_lesion    = "modelo_lesion";
        String origen           = "origen";
        String experimento      = "experimento";
    }

    interface ColumnasEspecie {
        String _id               = "_id";
        String descripcion      = "descripcion";
    }

    interface ColumnasEstudio {
        String _id               = "_id";
        String id_animal        = "id_animal";
        String fecha            = "fecha";
        String hora             = "hora";
        String tiempo           = "tiempo";
        String id_investigador1 = "id_investigador1";
        String id_investigador2 = "id_investigador2";
        String id_tipo_test     = "id_tipo_test";
        String resultado        = "resultado";
        String scorel           = "scorel";
        String scorer           = "scorer";
        String video            = "video";
        String comentarios      = "comentarios";

    }

    interface ColumnasInvestigador {
        String _id               = "_id";
        String nombre           = "nombre";
        String apellidos        = "apellidos";
        String email            = "email";
    }

    interface ColumnasTipoTest {
        String _id               = "_id";
        String descripcion      = "descripcion";
        String id_especie       = "id_especie";
    }

    interface ColumnasResultado{
        String _id               = "_id";
        String am_l             = "am_l";
        String am_r             = "am_r";
        String pwo_l            = "pwo_l";
        String pwo_r            = "pwo_r";
        String pw_l             = "pw_l";
        String pw_r             = "pw_r";
        String sd_l             = "sd_l";
        String sd_r             = "sd_r";
        String sp_l             = "sp_l";
        String sp_r             = "sp_r";
        String coord            = "coord";
        String pp_ic_l          = "pp_ic_l";
        String pp_ic_r          = "pp_ic_r";
        String pp_lo_l          = "pp_lo_l";
        String pp_lo_r          = "pp_lo_r";
        String ti               ="ti";
        String tail             ="tail";

    }

    public static class Animales implements ColumnasAnimal {


        public static final Uri URI_CONTENIDO = URI_BASE.buildUpon().appendPath(RUTA_ANIMALES).build();

        public static final String PARAMETRO_FILTRO = "filtro";
        public static final String FILTRO_ANIMAL = "codigo";
        public static final String FILTRO_SEXO = "sexo";
        public static final String FILTRO_FECHA = "fecha_nac";

        public static String obtenerCodigoAnimal(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static Uri crearUriAnimal(String codigo) {
            return URI_CONTENIDO.buildUpon().appendPath(codigo).build();
        }

        public static Uri crearUriParaEstudios(String codigo) {
            return URI_CONTENIDO.buildUpon().appendPath(codigo).appendPath("estudios").build();
        }

        public static Uri crearUriParaEstudiosResultados(String codigo) {
            return URI_CONTENIDO.buildUpon().appendPath(codigo).appendPath("estudios").appendPath("resultados").build();
        }

        public static boolean tieneFiltro(Uri uri) {
            return uri != null && uri.getQueryParameter(PARAMETRO_FILTRO) != null;
        }

        public static String generarCodigoAnimal() {
            String timeStamp = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
            return "ANI" + timeStamp; //UUID.randomUUID().toString();
        }
    }

    public static class Estudios implements ColumnasEstudio {

        public static final Uri URI_CONTENIDO = URI_BASE.buildUpon().appendPath(RUTA_ESTUDIOS).build();

        public static String obtenerCodigoEstudio(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static Uri crearUriEstudio(String codigo) {
            return URI_CONTENIDO.buildUpon().appendPath(codigo).build();
        }

        public static Uri crearUriParaResultados(String codigo) {
            return URI_CONTENIDO.buildUpon().appendPath(codigo).appendPath("resultados").build();
        }
    }

    public static class Resultados implements ColumnasResultado {

        public static final Uri URI_CONTENIDO = URI_BASE.buildUpon().appendPath(RUTA_RESULTADO).build();

        public static String obtenerCodigoResultado(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static Uri crearUriResultado(String codigo) {
            return URI_CONTENIDO.buildUpon().appendPath(codigo).build();
        }

    }

    public static class Especies implements ColumnasEspecie {

        public static final Uri URI_CONTENIDO = URI_BASE.buildUpon().appendPath(RUTA_ESPECIES).build();

        public static Uri crearUriEspecie(String codigo) {
            return URI_CONTENIDO.buildUpon().appendPath(codigo).build();
        }

        public static String obtenerCodigoEspecie(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static class Investigadores implements ColumnasInvestigador {

        public static final Uri URI_CONTENIDO = URI_BASE.buildUpon().appendPath(RUTA_INVESTIGADORES).build();

        public static Uri crearUriInvestigador(String codigo) {
            return URI_CONTENIDO.buildUpon().appendPath(codigo).build();
        }

        public static String obtenerCodigoInvestigador(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static class Tipo_Tests implements ColumnasTipoTest {

        public static final Uri URI_CONTENIDO = URI_BASE.buildUpon().appendPath(RUTA_TIPO_TESTS).build();

        public static Uri crearUriTipoTest(String codigo) {
            return URI_CONTENIDO.buildUpon().appendPath(codigo).build();
        }
        public static Uri crearUriTipoTestEspecie(String codigo) {
            return URI_CONTENIDO.buildUpon().appendPath(codigo).appendPath("especies").build();
        }

        public static String obtenerCodigoTipoTest(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }


    private ContratoBBDD() {
    }
}
