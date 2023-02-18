package com.cava.mockito.services;

import java.util.List;
import java.util.Optional;

import com.cava.mockito.models.Examen;
import com.cava.mockito.repositories.ExamenRepository;
import com.cava.mockito.repositories.PreguntaRepostory;

public class ExamenServiceImpl implements ExamenService {

	private ExamenRepository examenRepository;
	private PreguntaRepostory preguntaRepostory;

	public ExamenServiceImpl(ExamenRepository examenRepository, PreguntaRepostory preguntaRepostory) {
		this.examenRepository = examenRepository;
		this.preguntaRepostory = preguntaRepostory;
	}

	@Override
	public Optional<Examen> findExamenPorNombre(String nombre) {
		return examenRepository.findAll().stream().filter(e -> e.getNombre().contains(nombre)).findFirst();

	}

	@Override
	public Examen findExamenPorNombreConPreguntas(String nombre) {
		Optional<Examen> examenOptinal = findExamenPorNombre(nombre);
		Examen examen = null;
		if (examenOptinal.isPresent()) {
			examen = examenOptinal.orElseThrow();
			List<String> pregunta = preguntaRepostory.findPreguntasPorExamenId(examen.getId());
			examen.setPreguntas(pregunta);
		}
		return examen;
	}

	@Override
	public Examen guardarExamen(Examen examen) {
		if(!examen.getPreguntas().isEmpty()) {
			preguntaRepostory.guardarVariasPreguntas(examen.getPreguntas());
		}
		return examenRepository.guardar(examen);
	}

}
