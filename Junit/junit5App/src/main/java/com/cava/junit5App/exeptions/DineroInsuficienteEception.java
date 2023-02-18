package com.cava.junit5App.exeptions;

public class DineroInsuficienteEception extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DineroInsuficienteEception(String message) {
		super(message);
	}
}
