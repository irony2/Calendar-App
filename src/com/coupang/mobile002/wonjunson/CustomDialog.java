package com.coupang.mobile002.wonjunson;

import com.coupang.R;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.Window;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CustomDialog extends Dialog implements OnClickListener {

	public static final Uri URI = Uri.parse("content://" + DataProvider.AUTHORITY + "/calender");
	private Button mSaveButton;
	private Button mCancelButton;
	private Context mContext;
	private EditText mMemoEditText;
	private TextView mTitleText;
	private int year;
	private int month;
	private int day;
	private String memo;
	private IReloadListener mReloadListener;

	public interface IReloadListener {
		public void reload();
	}

	public CustomDialog(Context context) {
		super(context);

		mContext = context;

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.custom_dialog);

	}

	public CustomDialog(Context context, int year, int month, int intDate, String memo, IReloadListener listener) {
		this(context);
		this.year = year;
		this.month = month;
		this.day = intDate;
		this.memo = memo;
		this.mReloadListener = listener;
		init();
	}

	public void init() {
		mSaveButton = (Button) findViewById(R.id.save);
		mCancelButton = (Button) findViewById(R.id.cancel);
		mSaveButton.setOnClickListener(this);
		mCancelButton.setOnClickListener(this);
		mMemoEditText = (EditText) findViewById(R.id.memo_edit_text);
		mTitleText = (TextView) findViewById(R.id.title_text);
		mSaveButton.setText(mContext.getResources().getString(R.string.save));
		mCancelButton.setText(mContext.getResources().getString(R.string.cancel));
		mTitleText.setText(year + mContext.getResources().getString(R.string.year) + " " + month
				+ mContext.getResources().getString(R.string.month) + " " + day
				+ mContext.getResources().getString(R.string.day));
		mMemoEditText.setText(memo);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancel:
			dismiss();
			break;
		case R.id.save:
			saveMemo();
			break;
		}

	}

	private void saveMemo() {
		DataSaveAsyncTask dataSaveAsync = new DataSaveAsyncTask();
		dataSaveAsync.execute();
	}

	private class DataSaveAsyncTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			String[] projection = new String[] { DataOpenHelper.DataColumns.DAY, 
					DataOpenHelper.DataColumns.MEMO };
			
			StringBuilder sb = new StringBuilder();
			sb.append("year = " + year + " AND month = " + month + " AND day = " + day);
			Cursor cursor = mContext.getContentResolver().query(URI, projection, sb.toString(), null,
					null);

			ContentValues cv = new ContentValues();
			String editMemo = mMemoEditText.getText().toString();
			cv.put(DataOpenHelper.DataColumns.YEAR, year);
			cv.put(DataOpenHelper.DataColumns.MONTH, month);
			cv.put(DataOpenHelper.DataColumns.DAY, day);
			cv.put(DataOpenHelper.DataColumns.MEMO, editMemo);
			if (cursor != null && cursor.getCount() > 0) {
				if (editMemo.equals("") || editMemo.length() == 0) {
					mContext.getContentResolver().delete(URI,
							sb.toString(), null);
				} else {
					mContext.getContentResolver().update(URI,
							cv, sb.toString(), null);
				}
			} else if (!editMemo.equals("") || editMemo.length() > 0) {
				mContext.getContentResolver().insert(URI,
						cv);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			dismiss();
			mReloadListener.reload();
		}

	}

}
