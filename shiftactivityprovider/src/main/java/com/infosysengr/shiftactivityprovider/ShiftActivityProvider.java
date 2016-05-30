package com.infosysengr.shiftactivityprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.infosysengr.shiftactivityprovider.ShiftActivityDataContract.*;

public class ShiftActivityProvider extends ContentProvider {
    private ShiftDatabaseHelper mShiftDatabaseHelper;

    @Override
    public boolean onCreate() {
        mShiftDatabaseHelper = new ShiftDatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(final Uri uri, final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {
        SQLiteDatabase db = mShiftDatabaseHelper.getReadableDatabase();
        SQLBuilder select = new SQLBuilder();
        return select.from(Shift.TABLE_NAME)
                .execute(db, projection, sortOrder);
    }

    @Nullable
    @Override
    public String getType(final Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull final Uri uri, final ContentValues values) {
        SQLiteDatabase db = mShiftDatabaseHelper.getWritableDatabase();
        final long _id = db.insertOrThrow(Shift.TABLE_NAME, null, values);
        final Uri newShiftUri = Uri.parse(SHIFT_CONTENT_URI + "/" + _id);
        Context context = getContext();
        assert context != null;
        context.getContentResolver().notifyChange(newShiftUri, null);
        return newShiftUri;
    }

    @Override
    public int delete(final Uri uri, final String selection, final String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(final Uri uri, final ContentValues values, final String selection, final String[] selectionArgs) {
        return 0;
    }

    static class ShiftDatabaseHelper extends SQLiteOpenHelper {
        public static final int SCHEMA_VERSION = 1;
        private static final String DATABASE_NAME = "shifts.db";

        public ShiftDatabaseHelper(final Context context) {
            super(context, DATABASE_NAME, null, SCHEMA_VERSION);
        }

        private interface ColumnType {
            String TEXT = " TEXT";
            String INTEGER = " INTEGER";
            String PRIMARY_KEY = " INTEGER PRIMARY KEY";
            String FOREIGN_KEY = " INTEGER";
            String TIMESTAMP = " TEXT";

        }
        @Override
        public void onCreate(final SQLiteDatabase db) {
            String createTableSql = String.format(
                    "CREATE TABLE %s (%s, %s, %s, %s, %s)",
                    Shift.TABLE_NAME,
                    Shift.Columns._ID + ColumnType.PRIMARY_KEY,
                    Shift.Columns.LOCATION_NAME + ColumnType.TEXT,
                    Shift.Columns.START_TIMESTAMP + ColumnType.TIMESTAMP,
                    Shift.Columns.END_TIMESTAMP + ColumnType.TIMESTAMP,
                    Shift.Columns.SUPERVISOR_ID + ColumnType.FOREIGN_KEY
            );
            db.execSQL(createTableSql);
            final ContentValues contentValues = new ContentValues();
            contentValues.put(Shift.Columns.LOCATION_NAME, "Pimlico Hills");
            contentValues.put(Shift.Columns.START_TIMESTAMP, "2015-01-05T16:01:44.424");
            contentValues.put(Shift.Columns.END_TIMESTAMP, "2015-01-06T08:00:00.000");
            db.insertOrThrow(Shift.TABLE_NAME, null, contentValues);

        }

        @Override
        public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
            throw new UnsupportedOperationException("Migrations are currently not supported.");
        }
    }
}
