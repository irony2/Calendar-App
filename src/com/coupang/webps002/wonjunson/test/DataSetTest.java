package com.coupang.webps002.wonjunson.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.coupang.webps002.wonjunson.DataSet;

public class DataSetTest {

	@Test
	public void testDataSet() {
		DataSet<String, String> ds = new DataSet<String, String>();
		assertNotEquals(null, ds);
	}

	@Test
	public void testAdd() {
		DataSet<String, String> ds = new DataSet<String, String>();
		String key1 = "A업체";
		ds.add(key1, "A0");
		ds.add(key1, "A1");
		ds.add(key1, "A2");
		ds.add(key1, "A3");
		ds.add(key1, "A7");
		assertEquals("A7", ds.getCompany()[0].getGoods(4));
	}

	@Test
	public void testCheckCapacity() {
		DataSet<String, String> ds = new DataSet<String, String>();
		String key1 = "A업체";
		ds.add(key1, "A0");
		ds.add(key1, "A1");
		ds.add(key1, "A2");
		ds.add(key1, "A3");
		ds.add(key1, "A4");
		ds.add(key1, "A5");
		ds.add(key1, "A6");
		ds.add(key1, "A7");
		ds.add(key1, "A8");
		ds.add(key1, "A9");
		ds.add(key1, "A10");
		ds.add(key1, "A11");
		ds.add(key1, "A12");
		assertEquals(1, ds.getCompanySize());
		assertEquals(13, ds.getCompany()[0].getGoodsSize());
	}

	@Test
	public void testContainsKey() {
		DataSet<String, String> ds = new DataSet<String, String>();
		String key1 = "A업체";
		ds.add(key1, "A0");
		ds.add(key1, "A1");
		ds.add(key1, "A2");
		String key2 = "B업체";
		ds.add(key2, "B0");
		assertEquals(1, ds.containsKey(key2));
		
	}

	@Test
	public void testPut() {
		DataSet<String, String> ds = new DataSet<String, String>();
		String key1 = "A업체";
		ds.put(key1, "A3", 4);
		assertEquals("A3", ds.get(key1, 4));
	}

	@Test
	public void testGet() {
		DataSet<String, String> ds = new DataSet<String, String>();
		String key1 = "A업체";
		ds.add(key1, "A0");
		ds.put(key1, "A3", 4);
		assertEquals("null", ds.get(key1, 3));
	}

	@Test
	public void testToSting() {
		DataSet<String, String> ds = new DataSet<String, String>();
		String key1 = "A업체";
		ds.add(key1, "A0");
		
		String toString = ds.getCompany()[0].getTitle() + " : " + ds.getCompany()[0].getGoodsSize() + 
				" : " + ds.getCompany()[0].toString();
		assertEquals(toString, ds.toSting());
	}

}
