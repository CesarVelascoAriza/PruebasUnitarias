package com.cava.mockito.repositories;

import java.util.List;

import com.cava.mockito.services.Datos;

public class PreguntaRepositoryImpl implements PreguntaRepostory {

	@Override
	public List<String> findPreguntasPorExamenId(Long id) {
		// TODO Auto-generated method stub
		return  Datos.PREGUNTAS;
	}

	@Override
	public void guardarVariasPreguntas(List<String> preguntas) {
		// TODO Auto-generated method stub
		
	}

}
