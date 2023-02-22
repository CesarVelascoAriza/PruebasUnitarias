package com.cava.test.springBoot.app.models;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;



@Entity
@Table(name = "bancos")
public class Banco {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nombre;
	@Column(name = "total_transferencia")
	private int totalTransferencias;
	
	public Banco() {
		// TODO Auto-generated constructor stub
	}

	public Banco(Long id, String nombre, Integer totalTransferencias) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.totalTransferencias = totalTransferencias;
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

	public int getTotalTransferencias() {
		return totalTransferencias;
	}

	public void setTotalTransferencias(int totalTransferencias) {
		this.totalTransferencias = totalTransferencias;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id, nombre, totalTransferencias);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Banco other = (Banco) obj;
		return Objects.equals(id, other.id) && Objects.equals(nombre, other.nombre)
				&& totalTransferencias == other.totalTransferencias;
	}
	
	
}
