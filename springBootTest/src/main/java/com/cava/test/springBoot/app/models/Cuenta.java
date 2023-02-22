package com.cava.test.springBoot.app.models;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cava.test.springBoot.app.exceptios.DineroInsuficienteException;

@Entity
@Table(name = "cuentas")
public class Cuenta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nombre;
	private BigDecimal saldo;
	
	public Cuenta() {
		// TODO Auto-generated constructor stub
	}

	public Cuenta(Long id, String nombre, BigDecimal saldo) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.saldo = saldo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, nombre, saldo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cuenta other = (Cuenta) obj;
		return Objects.equals(id, other.id) && Objects.equals(nombre, other.nombre)
				&& Objects.equals(saldo, other.saldo);
	}
	
	public void debito(BigDecimal monto) {
		BigDecimal nuevoSaldo= this.saldo.subtract(monto);
		if(nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
			throw new DineroInsuficienteException("Dinero insuficiente en la cuenta");
		}
		
		this.saldo = nuevoSaldo;
	}
	public void credito(BigDecimal monto) {
		this.saldo = this.saldo.add(monto);
		
	}
	
}
