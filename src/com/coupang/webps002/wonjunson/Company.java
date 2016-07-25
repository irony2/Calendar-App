package com.coupang.webps002.wonjunson;

public class Company {

	private String title;
	private String[] goods;
	private int goodsSize;

	public Company() {
		this.goods = new String[10];
		this.goodsSize = 0;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	public void addGoods(String value) {
		checkCapacity();
		goods[goodsSize] = new String(value == null ? "null" : value);
		goodsSize++;
	}

	public void putGoods(String value, int index) {
		if (index < goodsSize) {
			goods[index] = value;
		} else {
			while (index > goodsSize - 1) {
				if (index == goodsSize) {
					addGoods(value);
				} else {
					addGoods(null);
				}
			}
		}
	}

	public String getGoods(int index) {
		if (index < goodsSize) {
			return goods[index];
		}
		return null;
	}

	public int getGoodsSize() {
		return goodsSize;
	}

	public void checkCapacity() {
		if (goods.length < goodsSize + 1) {
			int size = goodsSize + 10;
			String[] newGoods = new String[size];
			System.arraycopy(goods, 0, newGoods, 0, goods.length);
			goods = newGoods;
		}
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for (int i = 0; i < goodsSize; i++) {
			sb.append(goods[i]);
			if (i != goodsSize - 1) {
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}
}
