package com.coupang.mobile002.wonjunson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import com.coupang.R;

public class CalenderData {
	private List<String> mItems;
	private int mYear;
	private int mMonth;
	private int mStartDay;
	private int mEndDay;
	private GregorianCalendar mCalander;

	public CalenderData() {
		init();
	}

	public void init() {
		mCalander = new GregorianCalendar(Locale.KOREA);
		setDate();
	}

	public void setDate() {
		mYear = mCalander.get(Calendar.YEAR);
		mMonth = mCalander.get(Calendar.MONTH) + 1;
		mStartDay = mCalander.get(Calendar.DAY_OF_WEEK);
		mEndDay = mCalander.getActualMaximum(Calendar.DATE);
		if (mItems == null) {
			mItems = new ArrayList<String>();
		} else {
			mItems.clear();
		}
	}

	public void addDayOfItem() {
		Calendar cal = (Calendar) mCalander.clone();
		cal.add(Calendar.MONTH, -1);
		int endDayOfPreviousMonth = cal.getActualMaximum(Calendar.DATE);
		for (int i = 1; i < mStartDay; i++) {
			mItems.add("" + (endDayOfPreviousMonth - (mStartDay - i - 1)));
		}
		for (int i = 0; i < mEndDay; i++) {
			mItems.add("" + (i + 1));
		}
		cal.add(Calendar.MONTH, 2);
		int remainDay = remainDay(mStartDay, mEndDay);
		for (int i = 1; i <= remainDay; i++) {
			mItems.add("" + i);
		}
	}

	public int remainDay(int startDay, int endDay) {
		int sumOfDay = (startDay + endDay - 1) % 7;
		return 7 - sumOfDay;
	}

	public List<String> getItems() {
		return this.mItems;
	}

	public void addItem(String item) {
		this.mItems.add(item);
	}

	public int getYear() {
		return this.mYear;
	}

	public int getMonth() {
		return this.mMonth;
	}

	public void previousMonth() {
		mCalander.add(Calendar.MONTH, -1);
		setDate();
	}

	public void nextMonth() {
		mCalander.add(Calendar.MONTH, 1);
		setDate();
	}
}
