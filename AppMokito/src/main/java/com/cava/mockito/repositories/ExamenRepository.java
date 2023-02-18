package com.cava.mockito.repositories;

import java.util.List;

import com.cava.mockito.models.Examen;

public interface ExamenRepository {

	List<Examen> findAll();
	Examen guardar(Examen examen);
}
