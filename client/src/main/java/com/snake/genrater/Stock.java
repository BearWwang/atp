package com.snake.genrater;

public class Stock {

	private final Genrater genrater;

	protected Stock(Genrater genrater) {
		this.genrater = genrater;
	}

	public String nsdqSymbol() {
		return genrater.fakeValuesService().resolve("stock.symbol_nsdq", this, genrater);
	}
	
	public String nyseSymbol() {
		return genrater.fakeValuesService().resolve("stock.symbol_nyse", this, genrater);
	}
	
}
