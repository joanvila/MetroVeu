package com.metroveu.metroveu.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Florencia Tarditti on 13/10/15.
 */
public class MetroProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MetroDbHelper mOpenHelper;

    static final int MAPA = 100;
    static final int LINIA = 200;
    static final int PARADA = 300;
    static final int ACCESSIBILITAT = 400;

    static UriMatcher buildUriMatcher() {

        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MetroContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MetroContract.PATH_MAPA, MAPA);
        matcher.addURI(authority, MetroContract.PATH_LINIA, LINIA);
        matcher.addURI(authority, MetroContract.PATH_PARADA, PARADA);
        matcher.addURI(authority, MetroContract.PATH_ACCESSIBILITAT, ACCESSIBILITAT);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MetroDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case MAPA: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MetroContract.MapaEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case LINIA: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MetroContract.LiniaEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case PARADA: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MetroContract.ParadaEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case ACCESSIBILITAT: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MetroContract.AccessibilitatEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MAPA: {
                long _id = db.insert(MetroContract.MapaEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MetroContract.MapaEntry.buildMapaUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case LINIA: {
                long _id = db.insert(MetroContract.LiniaEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MetroContract.LiniaEntry.buildLiniaUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case PARADA: {
                long _id = db.insert(MetroContract.ParadaEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MetroContract.ParadaEntry.buildParadaUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case ACCESSIBILITAT: {
                long _id = db.insert(MetroContract.AccessibilitatEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MetroContract.AccessibilitatEntry.buildAccessibilitatUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
