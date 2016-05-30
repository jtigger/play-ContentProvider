package com.infosysengr.shiftactivityprovider;


import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;
import android.test.suitebuilder.annotation.LargeTest;

import com.infosysengr.androidtest.provider.ProviderTestCase3;
import com.infosysengr.shiftactivityprovider.ShiftActivityDataContract.Shift;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ProviderTest extends ProviderTestCase2<ShiftActivityProvider> {

    public ProviderTest() {
        super(ShiftActivityProvider.class, ShiftActivityDataContract.CONTENT_AUTHORITY);
    }

    @Before
    @Override
    public void setUp() throws Exception {
        setContext(InstrumentationRegistry.getTargetContext());
        super.setUp();
    }

    @Test
    public void query_byId_whenShiftExists_returnsThatShift() {
        final Cursor query = getMockContentResolver().query(
                ShiftActivityDataContract.BASE_CONTENT_URI,
                null,
                null,
                null,
                null);

        assertThat(query,is(notNullValue())); assert query != null;
        assertThat(query.getCount(), equalTo(1));
        query.moveToFirst();
        assertThat(query.getInt(0), equalTo(1));
        assertThat(query.getString(1), equalTo("Pimlico Hills"));
    }


    @Test
    public void insert_withValidShiftData_returnsUriToNewShift() {
        final MockContentResolver mockContentResolver = getMockContentResolver();
        ContentValues newShiftValues = new ContentValues();
        newShiftValues.put(Shift.Columns.LOCATION_NAME, "Los Angeles, Mid City");
        newShiftValues.put(Shift.Columns.START_TIMESTAMP, "2016-06-01T08:00:00.000");
        newShiftValues.put(Shift.Columns.END_TIMESTAMP, "2016-06-01T04:00:00.000");
        newShiftValues.put(Shift.Columns.SUPERVISOR_ID, 124);
        final Uri newShift = mockContentResolver.insert(ShiftActivityDataContract.SHIFT_CONTENT_URI, newShiftValues);
        assertThat(newShift, is(notNullValue())); assert newShift != null;
        assertThat(newShift.getAuthority(), equalTo(ShiftActivityDataContract.CONTENT_AUTHORITY));
        assertThat(newShift.getLastPathSegment(), equalTo("2"));
    }

    @Test
    public void insert_withValidShiftData_notifiesContentObserversOfNewData() {
        final TestContentObserver contentObserverSpy = new TestContentObserver();
        getMockContentResolver().registerContentObserver(ShiftActivityDataContract.SHIFT_CONTENT_URI, true, contentObserverSpy);

        ContentValues newShiftValues = new ContentValues();
        newShiftValues.put(Shift.Columns.LOCATION_NAME, "Los Angeles, Mid City");
        final Uri newShift = getMockContentResolver().insert(ShiftActivityDataContract.SHIFT_CONTENT_URI, newShiftValues);

        contentObserverSpy.verifyWasNotified();
    }

    private class TestContentObserver extends ContentObserver {
        private boolean wasNotified = false;
        public TestContentObserver() {
            super(null);
        }

        @Override
        public boolean deliverSelfNotifications() {
            return true;
        }

        @Override
        public void onChange(final boolean selfChange) {
            super.onChange(selfChange);
            wasNotified = true;
        }

        @Override
        public void onChange(final boolean selfChange, final Uri uri) {
            onChange(selfChange, uri);
        }

        public void verifyWasNotified() {
            assertThat("Expected to be notified, but was not.", wasNotified, equalTo(true));
        }
    }

}