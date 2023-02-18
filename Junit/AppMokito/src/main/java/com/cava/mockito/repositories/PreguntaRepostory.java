package com.cava.mockito.repositories;

import java.util.List;

public interface PreguntaRepostory  {

	List<String> findPreguntasPorExamenId(Long id);
	void guardarVariasPreguntas(List<String> preguntas);
}
