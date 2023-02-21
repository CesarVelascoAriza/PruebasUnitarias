package com.cava.mockito.services;

import java.util.Arrays;
import java.util.List;

import com.cava.mockito.models.Examen;

public class Datos {

	public final static List<Examen> EXAMENES = Arrays.asList(new Examen(1L, "Matemáticas"),
			new Examen(2L, "Geografia"), new Examen(3L, "Ingles"), new Examen(4L, "Lenguaje"),
			new Examen(5L, "Historia"));
	public final static List<Examen> EXAMENES_ID_NULL = Arrays.asList(new Examen(null, "Matemáticas"),
			new Examen(null, "Geografia"), new Examen(null, "Ingles"), new Examen(null, "Lenguaje"),
			new Examen(null, "Historia"));
	public final static List<Examen> EXAMENES_ID_NEGATIVOS = Arrays.asList(new Examen(-1L, "Matemáticas"),
			new Examen(-2L, "Geografia"), new Examen(-3L, "Ingles"), new Examen(-4L, "Lenguaje"),
			new Examen(-5L, "Historia"));

	public final static List<String> PREGUNTAS = Arrays.asList("aritmetica", "intregales", "trigonometria",
			"geometria");
	
	public final static Examen EXAMEN= new Examen(null,"Fisica"); 
}
