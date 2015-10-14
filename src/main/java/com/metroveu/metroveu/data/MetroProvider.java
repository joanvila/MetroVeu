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

    static final int MAPA = 200;
    static final int PARADA = 100;

    static UriMatcher buildUriMatcher() {

        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MetroContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MetroContract.PATH_MAPA, MAPA);
        matcher.addURI(authority, MetroContract.PATH_PARADA, PARADA);
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
        return null;
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
                if ( _id > 0 ) {
                    returnUri = MetroContract.MapaEntry.buildMapaUri(_id);
                }
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
