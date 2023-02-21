package com.cava.mockito.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cava.mockito.models.Examen;
import com.cava.mockito.repositories.ExamenRepositoryImpl;
import com.cava.mockito.repositories.PreguntaRepositoryImpl;

@ExtendWith(MockitoExtension.class)
class ExamenServiceImplTestSpy {

	@Spy
	ExamenRepositoryImpl examenRepository;
	@Spy
	PreguntaRepositoryImpl preguntaRepostory;
	@InjectMocks
	ExamenServiceImpl examenService;

	
	
	@Test
	void testSpy() {
		
		//when(preguntaRepostory.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS); 
		doReturn(Datos.PREGUNTAS).when(preguntaRepostory).findPreguntasPorExamenId(anyLong());
		
		Examen examen = examenService.findExamenPorNombreConPreguntas("Historia");
		
		assertEquals(5L, examen.getId());
		assertEquals("Historia", examen.getNombre());
		assertEquals(4, examen.getPreguntas().size());
		assertTrue(examen.getPreguntas().contains("aritmetica"));
		
		
		verify(examenRepository).findAll();
		verify(preguntaRepostory).findPreguntasPorExamenId(anyLong());
	}
}
