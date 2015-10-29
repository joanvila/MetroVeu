package com.metroveu.metroveu.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.metroveu.metroveu.data.MetroContract.*;

/**
 * Created by Florencia Tarditti on 13/10/15.
 */
public class MetroDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "metroveu.db";

    public MetroDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_MAPA_TABLE = "CREATE TABLE " + MapaEntry.TABLE_NAME + " (" +
                MapaEntry.COLUMN_MAPA_NOM + " TEXT PRIMARY KEY);";

        final String SQL_CREATE_TARIFA_TABLE = "CREATE TABLE " + TarifaEntry.TABLE_NAME + " (" +
                TarifaEntry.COLUMN_TARIFA_NOM + " TEXT ," +
                TarifaEntry.COLUMN_TARIFA_TIPUS + " TEXT ," +
                TarifaEntry.COLUMN_TARIFA_DESCRIPCIO + " TEXT ," +
                TarifaEntry.COLUMN_TARIFA_PREU + " FLOAT NOT NULL ," +
                TarifaEntry.COLUMN_TARIFA_IDIOMA + " TEXT , " +
                TarifaEntry.COLUMN_TARIFA_MAPA + " TEXT ," +
                " FOREIGN KEY (" + TarifaEntry.COLUMN_TARIFA_MAPA + ") REFERENCES " +
                MapaEntry.TABLE_NAME + " (" + MapaEntry.COLUMN_MAPA_NOM + "), " +
                "PRIMARY KEY (" + TarifaEntry.COLUMN_TARIFA_NOM + ", " +
                                  TarifaEntry.COLUMN_TARIFA_MAPA + ", " +
                                    TarifaEntry.COLUMN_TARIFA_IDIOMA + ")" +
                ");";

        final String SQL_CREATE_RUTA_TABLE = "CREATE TABLE " + RutaEntry.TABLE_NAME + " (" +
                RutaEntry.COLUMN_RUTA_NOM + " TEXT PRIMARY KEY ," +
                RutaEntry.COLUMN_RUTA_LLOCSINTERES + " TEXT ," +
                RutaEntry.COLUMN_RUTA_MAPA + " TEXT ," +
                " FOREIGN KEY (" + RutaEntry.COLUMN_RUTA_MAPA + ") REFERENCES " +
                MapaEntry.TABLE_NAME + " (" + MapaEntry.COLUMN_MAPA_NOM + "));";

        final String SQL_CREATE_LINIA_TABLE = "CREATE TABLE " + LiniaEntry.TABLE_NAME + " (" +
                LiniaEntry.COLUMN_LINIA_NOM + " TEXT ," +
                LiniaEntry.COLUMN_LINIA_ORDRE + " INTEGER ," +
                LiniaEntry.COLUMN_LINIA_COLOR + " TEXT ," +
                LiniaEntry.COLUMN_LINIA_FREQUENCIA + " INTEGER ," +
                LiniaEntry.COLUMN_LINIA_MAPA +  " TEXT ," +
                " FOREIGN KEY (" + LiniaEntry.COLUMN_LINIA_MAPA + ") REFERENCES " +
                MapaEntry.TABLE_NAME + " (" + MapaEntry.COLUMN_MAPA_NOM + "), " +
                " PRIMARY KEY (" + LiniaEntry.COLUMN_LINIA_MAPA + ", " +
                LiniaEntry.COLUMN_LINIA_NOM + "));";

        final String SQL_CREATE_PARADA_TABLE = "CREATE TABLE " + ParadaEntry.TABLE_NAME + " (" +
                ParadaEntry.COLUMN_PARADA_NOM + " TEXT PRIMARY KEY ," +
                ParadaEntry.COLUMN_PARADA_ACCESSIBILITAT + " TEXT );";

        final String SQL_CREATE_PERTANY_TABLE = "CREATE TABLE " + PertanyEntry.TABLE_NAME + " (" +
                PertanyEntry.COLUMN_PERTANY_MAPA + " TEXT ," +
                PertanyEntry.COLUMN_PERTANY_LINIA + " TEXT ," +
                PertanyEntry.COLUMN_PERTANY_PARADA + " TEXT ," +
                PertanyEntry.COLUMN_PERTANY_ORDRE + " INTEGER ," +
                " FOREIGN KEY (" + PertanyEntry.COLUMN_PERTANY_MAPA + ") REFERENCES " +
                LiniaEntry.TABLE_NAME + " (" + LiniaEntry.COLUMN_LINIA_MAPA + "), " +
                " FOREIGN KEY (" + PertanyEntry.COLUMN_PERTANY_LINIA + ") REFERENCES " +
                LiniaEntry.TABLE_NAME + " (" + LiniaEntry.COLUMN_LINIA_NOM + "), " +
                " FOREIGN KEY (" + PertanyEntry.COLUMN_PERTANY_PARADA + ") REFERENCES " +
                ParadaEntry.TABLE_NAME + " (" + ParadaEntry.COLUMN_PARADA_NOM + "), " +
                " PRIMARY KEY (" + PertanyEntry.COLUMN_PERTANY_MAPA + ", " +
                                    PertanyEntry.COLUMN_PERTANY_LINIA + ", " +
                                    PertanyEntry.COLUMN_PERTANY_PARADA + "));";

        final String SQL_CREATE_RUTAPARADA_TABLE = "CREATE TABLE " + RutaparadaEntry.TABLE_NAME + " (" +
                RutaparadaEntry.COLUMN_RUTAPARADA_RUTA + " TEXT ," +
                RutaparadaEntry.COLUMN_RUTAPARADA_PARADA + " TEXT ," +
                " FOREIGN KEY (" + RutaparadaEntry.COLUMN_RUTAPARADA_RUTA + ") REFERENCES " +
                RutaEntry.TABLE_NAME + " (" + RutaEntry.COLUMN_RUTA_NOM + "), " +
                " FOREIGN KEY (" + RutaparadaEntry.COLUMN_RUTAPARADA_PARADA + ") REFERENCES " +
                ParadaEntry.TABLE_NAME + " (" + ParadaEntry.COLUMN_PARADA_NOM + "), " +
                " PRIMARY KEY (" + RutaparadaEntry.COLUMN_RUTAPARADA_RUTA + ", " +
                                    RutaparadaEntry.COLUMN_RUTAPARADA_PARADA + "));";

        sqLiteDatabase.execSQL(SQL_CREATE_MAPA_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TARIFA_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_RUTA_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_LINIA_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_PARADA_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_PERTANY_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_RUTAPARADA_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MapaEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TarifaEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RutaEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + LiniaEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ParadaEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PertanyEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RutaparadaEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
