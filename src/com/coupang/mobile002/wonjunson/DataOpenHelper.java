package com.coupang.mobile002.wonjunson;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataOpenHelper extends SQLiteOpenHelper {

	private static DataOpenHelper sSingleton;

	public static final String DB_NAME = "date";
	
	public interface DataColumns {
		public static final String _ID = "_id";
		public static final String YEAR = "year";
		public static final String MONTH = "month";
		public static final String DAY = "day";
		public static final String MEMO = "memo";
	}

	public DataOpenHelper(Context context, String name) {
		super(context, name, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE date (" 
				+ DataColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ DataColumns.YEAR + " INTEGER, " 
				+ DataColumns.MONTH + " INTEGER, " 
				+ DataColumns.DAY + " INTEGER, " 
				+ DataColumns.MEMO + " TEXT" + ");");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	public static synchronized DataOpenHelper getInstance(Context context) {
		if (sSingleton == null) {
			sSingleton = new DataOpenHelper(context, "calender");
		}
		return sSingleton;
	}

}
