package com.coupang.mobile002.wonjunson;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class DataProvider extends ContentProvider {

	public static final String AUTHORITY = "com.coupang.wonjunson.data";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
	public static final int CALENDER = 1;
	public static final int CALENDER_ID = 2;
	static final UriMatcher Matcher;
	static {
		Matcher = new UriMatcher(UriMatcher.NO_MATCH);
		Matcher.addURI(AUTHORITY, "calender", CALENDER);
		Matcher.addURI(AUTHORITY, "calender/*", CALENDER_ID);
	}

	private DataOpenHelper mHepler;

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int count = mHepler.getWritableDatabase().delete("date", selection, selectionArgs);
		return count;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		long rowID = mHepler.getWritableDatabase().insert(DataOpenHelper.DB_NAME, "", values);
		if (rowID > 0) {
			Uri _uri = ContentUris.withAppendedId(uri, rowID);
			getContext().getContentResolver().notifyChange(_uri, null);
			return _uri;
		}
		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public boolean onCreate() {
		mHepler = DataOpenHelper.getInstance(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		final SQLiteDatabase db = mHepler.getReadableDatabase();
		final int match = Matcher.match(uri);
		SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
		switch (match) {
		case CALENDER:
			sqlBuilder.setTables(DataOpenHelper.DB_NAME);
			break;
		case CALENDER_ID:
			break;
		default:
			break;
		}
		Cursor c = sqlBuilder.query(mHepler.getWritableDatabase(), projection, selection, selectionArgs, null, null,
				sortOrder);
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		int count = mHepler.getWritableDatabase().update(DataOpenHelper.DB_NAME, values, selection, selectionArgs);
		return count;
	}

}
