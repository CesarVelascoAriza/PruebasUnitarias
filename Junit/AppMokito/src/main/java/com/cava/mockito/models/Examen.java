package com.cava.mockito.models;

import java.util.ArrayList;
import java.util.List;

public class Examen {

	private Long id;
	private String nombre;
	private List<String> preguntas;
	
	public Examen() {
		this.preguntas = new ArrayList<>();
	}
	
	public Examen(Long id, String nombre) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.preguntas = new ArrayList<>();
	}
	
	public Examen(Long id, String nombre, List<String> preguntas) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.preguntas = preguntas;
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


	public List<String> getPreguntas() {
		return preguntas;
	}


	public void setPreguntas(List<String> preguntas) {
		this.preguntas = preguntas;
	}


	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}
	
	
}
