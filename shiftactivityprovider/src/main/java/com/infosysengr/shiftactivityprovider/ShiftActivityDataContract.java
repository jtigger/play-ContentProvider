package com.infosysengr.shiftactivityprovider;

import android.net.Uri;
import android.provider.BaseColumns;

public class ShiftActivityDataContract {
    public static final String CONTENT_AUTHORITY = "com.infosysengr.shiftactivityprovider";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final Uri SHIFT_CONTENT_URI = BASE_CONTENT_URI.buildUpon()
            .appendPath(Shift.TABLE_NAME).build();

    public interface Shift {
        String TABLE_NAME = "shifts";

        interface Columns extends BaseColumns {
            String LOCATION_NAME = "loc_name";
            String START_TIMESTAMP = "start_timestamp";
            String END_TIMESTAMP = "end_timestamp";
            String SUPERVISOR_ID = "supervisor_id";
        }

    }

}
