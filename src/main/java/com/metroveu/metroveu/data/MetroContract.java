package com.metroveu.metroveu.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Florencia Tarditti on 13/10/15.
 */
public class MetroContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.metroveu.metroveu";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.metroveu.metroveu/mapa/ is a valid path for
    // looking at mapa data.
    public static final String PATH_MAPA = "mapa";
    public static final String PATH_RUTA = "ruta";
    public static final String PATH_LINIA = "linia";
    public static final String PATH_PERTANY = "pertany";
    public static final String PATH_PARADA = "parada";
    public static final String PATH_TARIFA = "tarifa";
    public static final String PATH_RUTAPARADA = "rutaparada";
    public static final String PATH_TEMA = "tema";

    /* Inner class that defines the table contents of the mapa table */
    public static final class MapaEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MAPA).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MAPA;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MAPA;

        public static final String TABLE_NAME = "mapa";

        public static final String COLUMN_MAPA_NOM = "mapa_nom";

        public static Uri buildMapaUri(long _id) {
            return ContentUris.withAppendedId(CONTENT_URI, _id);
        }
    }

    public static final class RutaEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RUTA).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RUTA;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RUTA;

        public static final String TABLE_NAME = "ruta";

        public static final String COLUMN_RUTA_NOM = "ruta_nom";
        public static final String COLUMN_RUTA_LLOCSINTERES = "ruta_llocsinteres";
        public static final String COLUMN_RUTA_PARADES = "ruta_parades";
        public static final String COLUMN_RUTA_MAPA = "ruta_mapa";

        public static Uri buildRutaUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class LiniaEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_LINIA).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LINIA;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LINIA;

        public static final String TABLE_NAME = "linia";

        public static final String COLUMN_LINIA_NOM = "linia_nom";
        public static final String COLUMN_LINIA_ORDRE = "linia_ordre";
        public static final String COLUMN_LINIA_COLOR = "linia_color";
        public static final String COLUMN_LINIA_FREQUENCIA = "linia_frequencia";
        public static final String COLUMN_LINIA_MAPA = "linia_mapa";

        public static Uri buildLiniaUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class PertanyEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PERTANY).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PERTANY;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PERTANY;

        public static final String TABLE_NAME = "pertany";

        public static final String COLUMN_PERTANY_LINIA = "pertany_linia";
        public static final String COLUMN_PERTANY_PARADA = "pertany_parada";
        public static final String COLUMN_PERTANY_ORDRE = "pertany_ordre";
        public static final String COLUMN_PERTANY_MAPA = "pertany_mapa";

        public static Uri buildPertanyUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class ParadaEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PARADA).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PARADA;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PARADA;

        public static final String TABLE_NAME = "parada";

        public static final String COLUMN_PARADA_NOM = "parada_nom";
        public static final String COLUMN_PARADA_ACCESSIBILITAT = "parada_accessibilitat";

        public static Uri buildParadaUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class TarifaEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TARIFA).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TARIFA;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TARIFA;

        public static final String TABLE_NAME = "tarifa";

        public static final String COLUMN_TARIFA_NOM = "tarifa_nom";
        public static final String COLUMN_TARIFA_TIPUS = "tarifa_tipus";
        public static final String COLUMN_TARIFA_DESCRIPCIO = "tarifa_descripcio";
        public static final String COLUMN_TARIFA_PREU = "tarifa_preu";
        public static final String COLUMN_TARIFA_MAPA = "tarifa_mapa";
        public static final String COLUMN_TARIFA_IDIOMA = "tarifa_idioma";


        public static Uri buildTarifaUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class RutaparadaEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RUTAPARADA).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RUTAPARADA;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RUTAPARADA;

        public static final String TABLE_NAME = "rutaparada";

        public static final String COLUMN_RUTAPARADA_RUTA = "rutaparada_ruta";
        public static final String COLUMN_RUTAPARADA_PARADA = "rutaparada_parada";

        public static Uri buildRutaparadaUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class TemaEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TEMA).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TEMA;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TEMA;

        public static final String TABLE_NAME = "tema";

        public static final String COLUMN_TEMA_ACTUAL = "tema_temaactual";

        public static Uri buildTemaUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

}
