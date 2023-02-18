package com.cava.mockito.services;

import java.util.Optional;

import com.cava.mockito.models.Examen;

public interface ExamenService {
	
	Optional<Examen> findExamenPorNombre(String nombre);
	Examen findExamenPorNombreConPreguntas(String nombre);
	Examen guardarExamen(Examen examen);

}
