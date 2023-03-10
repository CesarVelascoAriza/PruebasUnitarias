package com.cava.junit5App.ejemplo;

import java.math.BigDecimal;

import com.cava.junit5App.exeptions.DineroInsuficienteEception;

public class Cuenta {

	private String persona;
	private BigDecimal saldo;
	private Banco banco;

	public Cuenta(String persona, BigDecimal saldo) {
		super();
		this.persona = persona;
		this.saldo = saldo;
	}

	public String getPersona() {
		return persona;
	}

	public void setPersona(String persona) {
		this.persona = persona;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public Banco getBanco() {
		return banco;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Cuenta)) {
			return false;
		}
		Cuenta c = (Cuenta) obj;
		if (this.persona == null || this.saldo == null) {
			return false;
		}
		return this.persona.equals(c.getPersona()) && this.saldo.equals(c.getSaldo());
	}

	public void debito(BigDecimal monto) {
		BigDecimal nuevoSaldo = this.saldo.subtract(monto);

		if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0)
			throw new DineroInsuficienteEception("Dinero insuficiente");

		this.saldo = nuevoSaldo;
	}

	public void credito(BigDecimal monto) {
		this.saldo = this.saldo.add(monto);
	}

}
