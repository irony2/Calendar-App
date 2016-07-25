package com.coupang.webps002.wonjunson;

public class DataSet<K, V> implements KeyList<String, String> {

	private Company[] company;
	private int companySize;
	
	public Company[] getCompany() {
		return company;
	}

	public int getCompanySize() {
		return companySize;
	}

	public DataSet() {
		this.company = new Company[10];
		this.companySize = 0;
	}

	@Override
	public void add(String key, String value) {
		int index = containsKey(key);
		if (index < 0) {
			checkCapacity();
			company[companySize] = new Company();
			company[companySize].setTitle(key);
			company[companySize].addGoods(value);
			companySize++;
		} else {
			company[index].addGoods(value);
		}
	}

	public void checkCapacity() {
		if (company.length < companySize + 1) {
			int size = companySize + 10;
			Company[] com = new Company[size];
			System.arraycopy(company, 0, com, 0, company.length);
			company = com;
		}
	}

	public int containsKey(String key) {
		int index = -1;
		for (int i = 0; i < companySize; i++) {
			if (company[i].getTitle().equals(key)) {
				index = i;
				break;
			}
		}
		return index;
	}

	@Override
	public void put(String key, String value, int index) {
		int keyIndex = containsKey(key);
		if (keyIndex < 0) {
			checkCapacity();
			company[companySize] = new Company();
			company[companySize].setTitle(key);
			company[companySize].putGoods(value, index);
			companySize++;
		} else {
			company[keyIndex].putGoods(value, index);
		}
	}

	@Override
	public String get(String key, int index) {
		int keyIndex = containsKey(key);
		if (keyIndex >= 0) {
			return company[keyIndex].getGoods(index);
		}
		return null;
	}

	@Override
	public String toSting() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < companySize; i++) {
			sb.append(company[i].getTitle() + " : " + company[i].getGoodsSize() + " : " + company[i].toString());
			if (i != companySize - 1) {
				sb.append("\n");
			}
		}
		return sb.toString();
	}

}
