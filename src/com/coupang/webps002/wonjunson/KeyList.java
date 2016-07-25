package com.coupang.webps002.wonjunson;

public interface KeyList<K, V> {

	public void add(K key, V value);

	public void put(K key, V value, int index);

	public V get(K key, int index);

	public String toSting();
}
