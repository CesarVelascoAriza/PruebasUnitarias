package com.cava.mockito.repositories;

import java.util.Arrays;
import java.util.List;

import com.cava.mockito.models.Examen;

public class ExamenRepositoryImpl implements ExamenRepository {

	@Override
	public List<Examen> findAll() {
		return Arrays.asList(
				new Examen(1L, "Matermaticas"),
				new Examen(2L, "Geografia"),
				new Examen(3L, "Ingles"),
				new Examen(4L, "Lenguaje"),
				new Examen(5L, "Historia")
				);
	}

	@Override
	public Examen guardar(Examen examen) {
		// TODO Auto-generated method stub
		return null;
	}

}
