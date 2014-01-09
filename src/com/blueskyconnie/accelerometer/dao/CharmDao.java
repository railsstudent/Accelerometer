package com.blueskyconnie.accelerometer.dao;

import java.util.Random;

public class CharmDao {

	private int num;
	private Random  rnd;
	
	public CharmDao(int num) {
		this.num = num;
		rnd = new Random();
	}
	
	public int getCharmIdx() {
		return rnd.nextInt(num);
	}
}
