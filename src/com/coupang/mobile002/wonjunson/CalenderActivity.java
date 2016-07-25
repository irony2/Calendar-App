package com.coupang.mobile002.wonjunson;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.coupang.R;
import com.coupang.mobile002.wonjunson.CustomDialog.IReloadListener;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

public class CalenderActivity extends Activity
		implements OnClickListener, android.view.GestureDetector.OnGestureListener {

	public static final Uri URI = Uri.parse("content://" + DataProvider.AUTHORITY + "/calender");
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;

	private GridView mMonthGridView;
	private CalenderData calenderData;
	private TextView mMonthTextView;
	private Button mLeftButton;
	private Button mRightButton;
	private MonthViewAdapter mMonthGridViewAdapter;
	private GestureDetector mGesture;
	private QueryAsyncTask mQueryAsyncTast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calender_activity);

		mMonthGridView = (GridView) findViewById(R.id.month_view);
		mMonthTextView = (TextView) findViewById(R.id.month_text);
		mLeftButton = (Button) findViewById(R.id.left_button);
		mLeftButton.setOnClickListener(this);
		mRightButton = (Button) findViewById(R.id.right_button);
		mRightButton.setOnClickListener(this);
		init();
		mMonthGridViewAdapter = new MonthViewAdapter(this);
		mMonthGridView.setAdapter(mMonthGridViewAdapter);
		mGesture = new GestureDetector(this);
	}

	private class QueryAsyncTask extends AsyncTask<Void, Void, Cursor> {

		@Override
		protected Cursor doInBackground(Void... params) {
			String[] projection = new String[] { "day", "memo" };
			StringBuilder sb = new StringBuilder();
			sb.append("year = " + calenderData.getYear() + " AND month = " + calenderData.getMonth()
					+ " AND memo is NOT NULL");
			Cursor cursor = getContentResolver().query(Uri.parse("content://" + DataProvider.AUTHORITY + "/calender"),
					projection, sb.toString(), null, null);
			return cursor;
		}

		@Override
		protected void onPostExecute(Cursor result) {
			super.onPostExecute(result);
			mMonthGridView.invalidate();
			mMonthGridViewAdapter.setData(result);
			mMonthGridViewAdapter.notifyDataSetChanged();
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		mQueryAsyncTast = new QueryAsyncTask();
		mQueryAsyncTast.execute();
	}

	public void init() {
		mLeftButton.setText("<");
		mRightButton.setText(">");
		calenderData = new CalenderData();
		setCalendarData();
	}

	public void setCalendarData() {
		calenderData.addItem(getResources().getString(R.string.sunday));
		calenderData.addItem(getResources().getString(R.string.monday));
		calenderData.addItem(getResources().getString(R.string.tuesday));
		calenderData.addItem(getResources().getString(R.string.wednesday));
		calenderData.addItem(getResources().getString(R.string.thursday));
		calenderData.addItem(getResources().getString(R.string.friday));
		calenderData.addItem(getResources().getString(R.string.saturday));
		calenderData.addDayOfItem();
		mMonthTextView.setText(calenderData.getYear() + getResources().getString(R.string.year) + " "
				+ calenderData.getMonth() + getResources().getString(R.string.month));
	}

	public class MonthViewAdapter extends BaseAdapter {

		private LayoutInflater mLayoutInflater;
		private Context mContext;
		private Cursor mCursor;

		public MonthViewAdapter(Context context) {
			mContext = context;
			mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public void setData(Cursor result) {
			mCursor = result;

		}

		@Override
		public int getCount() {
			return calenderData.getItems().size();
		}

		@Override
		public Object getItem(int position) {
			return calenderData.getItems().get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mLayoutInflater.inflate(R.layout.item, parent, false);
			}
			String date = calenderData.getItems().get(position);
			TextView mainText = (TextView) convertView.findViewById(R.id.main_text);
			TextView memoImage = (TextView) convertView.findViewById(R.id.memo_image);
			memoImage.setVisibility(View.GONE);

			mainText.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					return mGesture.onTouchEvent(event);
				}
			});
			mainText.setText(date);
			if (position % 7 == 0) {
				mainText.setTextColor(Color.RED);
			} else if (position % 7 == 6) {
				mainText.setTextColor(Color.BLUE);
			}
			if (position < 7) {
				mainText.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {

					}
				});

			} else {
				String memo = null;
				if (mCursor != null && mCursor.moveToFirst()) {
					int day = Integer.valueOf(date);
					do {
						if (day == mCursor.getInt(0)) {
							memoImage.setVisibility(View.VISIBLE);
							memo = mCursor.getString(1);
							break;
						}
					} while (mCursor.moveToNext());
				}

				setPreviousAndNextMonth(mainText, memoImage, position, date, memo);

			}
			return convertView;
		}

		private void setPreviousAndNextMonth(TextView mainText, TextView memoImage, int position, String date,
				final String memo) {
			final int intDate = Integer.valueOf(date);
			if (position > 6 && position < 14 && intDate > 7) {
				mainText.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						setMonth(false);
					}
				});
				memoImage.setVisibility(View.GONE);
			} else if (position > (getCount() - 8) && intDate < 7) {
				mainText.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						setMonth(true);
					}
				});
				memoImage.setVisibility(View.GONE);
			} else {
				mainText.setOnClickListener(new View.OnClickListener() {
					;

					@Override
					public void onClick(View v) {
						IReloadListener listener = new IReloadListener() {

							@Override
							public void reload() {
								calenderData.setDate();
								reloadData();
							}
						};
						CustomDialog dialog = new CustomDialog(mContext, calenderData.getYear(),
								calenderData.getMonth(), intDate, memo, listener);
						dialog.show();
					}
				});
				isToday(intDate, mainText);
			}
		}

		public void isToday(int intDate, TextView mainText) {
			Calendar cal = new GregorianCalendar(Locale.KOREA);
			if (cal.get(Calendar.YEAR) == calenderData.getYear()
					&& (cal.get(Calendar.MONTH) + 1) == calenderData.getMonth()
					&& cal.get(Calendar.DAY_OF_MONTH) == intDate) {
				AnimationSet set = new AnimationSet(true);
				set.setInterpolator(new AccelerateInterpolator());
				Animation animation = new AlphaAnimation(0.0f, 1.0f);
				animation.setDuration(300);
				set.addAnimation(animation);
				animation = new RotateAnimation(0, 45, 50, 50);
				animation.setDuration(300);
				set.addAnimation(animation);
				// animation = new ScaleAnimation(1.4f, 0, 0.6f, 0, 0, 0);
				// set.addAnimation(animation);
				mainText.setAnimation(set);
			}
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_button:
			setMonth(false);
			break;
		case R.id.right_button:
			setMonth(true);
			break;
		}

	}

	public void setMonth(boolean next) {
		if (!next) {
			calenderData.previousMonth();
		} else {
			calenderData.nextMonth();
		}
		reloadData();
	}

	public void reloadData() {
		setCalendarData();
		mQueryAsyncTast = new QueryAsyncTask();
		mQueryAsyncTast.execute();
		mMonthGridViewAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return mGesture.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		try {
			if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
				return false;

			if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				setMonth(true);
			} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				setMonth(false);
			}
		} catch (Exception e) {

		}
		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}
}
