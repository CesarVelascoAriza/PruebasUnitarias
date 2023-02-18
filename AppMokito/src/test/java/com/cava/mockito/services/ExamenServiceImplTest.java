package com.cava.mockito.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import com.cava.mockito.models.Examen;
import com.cava.mockito.repositories.ExamenRepository;
import com.cava.mockito.repositories.PreguntaRepostory;

@ExtendWith(MockitoExtension.class)
class ExamenServiceImplTest {

	@Mock
	ExamenRepository examenRepository;
	@Mock
	PreguntaRepostory preguntaRepostory;
	@InjectMocks
	ExamenServiceImpl examenService;

	@BeforeEach
	void initMethodTest() {
		/// instancia las clases de repositorios
		// MockitoAnnotations.openMocks(this);
		// examenRepository = mock(ExamenRepository.class);
		// preguntaRepostory = mock(PreguntaRepostory.class);
		// examenService = new ExamenServiceImpl(examenRepository,preguntaRepostory);
	}

	@Test
	void testFindExamenPorNombre() {

		when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
		Optional<Examen> examen = examenService.findExamenPorNombre("Ingles");
		assertTrue(examen.isPresent());
		assertEquals(3L, examen.orElseThrow().getId());
		assertEquals("Ingles", examen.orElseThrow().getNombre());
	}

	@Test
	void testFindExamenPorNombreListaVacia() { 
		when(examenRepository.findAll()).thenReturn(Collections.emptyList());
		Optional<Examen> examen = examenService.findExamenPorNombre("Ingles");
		assertFalse(examen.isPresent());
	}

	@Test
	void tespreguntasExamen() {
		when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
		when(preguntaRepostory.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
		Examen examenes = examenService.findExamenPorNombreConPreguntas("Historia");
		assertNotNull(examenes);
		assertEquals(4, examenes.getPreguntas().size());
		assertTrue(examenes.getPreguntas().contains("aritmetica"));
	}

	@Test
	void tespreguntasExamenVerify() {
		when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
		when(preguntaRepostory.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
		Examen examenes = examenService.findExamenPorNombreConPreguntas("Historia");
		assertNotNull(examenes);
		assertEquals(4, examenes.getPreguntas().size());
		assertTrue(examenes.getPreguntas().contains("aritmetica"));
		verify(examenRepository).findAll();
		verify(preguntaRepostory).findPreguntasPorExamenId(anyLong());
	}

	@Test
	void tesNoExamenVerify() {
		// given
		// when(examenRepository.findAll()).thenReturn(Collections.emptyList() );
		// when
		// (preguntaRepostory.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
		// when
		Examen examenes = examenService.findExamenPorNombreConPreguntas("Historia");
		// then
		assertNull(examenes);
		// verify(examenRepository).findAll();
		// verify(preguntaRepostory).findPreguntasPorExamenId(5L);
	}

	@Test
	void testGuardaarExamen() {

		/// Given
		Examen newExamen = Datos.EXAMEN;
		newExamen.setPreguntas(Datos.PREGUNTAS);

		when(examenRepository.guardar(any(Examen.class))).then(new Answer<Examen>() {

			Long secuencia = 6L;

			@Override
			public Examen answer(InvocationOnMock invocation) throws Throwable {
				Examen examen = invocation.getArgument(0);
				examen.setId(secuencia++);
				return examen;
			}

		});
		// when
		Examen examen = examenService.guardarExamen(Datos.EXAMEN);
		// then
		assertNotNull(examen.getId());
		assertEquals(6L, examen.getId());
		assertEquals("Fisica", examen.getNombre());

		verify(examenRepository).guardar(any(Examen.class));
		verify(preguntaRepostory).guardarVariasPreguntas(anyList());
	}

	@Test
	void testManejoException() {
		when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
		when(preguntaRepostory.findPreguntasPorExamenId(anyLong())).thenThrow(IllegalArgumentException.class);
		IllegalArgumentException exception= assertThrows(IllegalArgumentException.class, ()->{
			examenService.findExamenPorNombreConPreguntas("Matemáticas");
		});
		assertEquals(IllegalArgumentException.class, exception.getClass());
		verify(examenRepository).findAll();
		verify(preguntaRepostory).findPreguntasPorExamenId(anyLong());
	}

	@Test
	void testManejoIdNullException() {
		when(examenRepository.findAll()).thenReturn(Datos.EXAMENES_ID_NULL);
		when(preguntaRepostory.findPreguntasPorExamenId(isNull())).thenThrow(IllegalArgumentException.class);
		IllegalArgumentException exception= assertThrows(IllegalArgumentException.class, ()->{
			examenService.findExamenPorNombreConPreguntas("Matemáticas");
		});
		assertEquals(IllegalArgumentException.class, exception.getClass());
		verify(examenRepository).findAll();
		verify(preguntaRepostory).findPreguntasPorExamenId(isNull());
	}

	@Test
	void testArgumentoMatcher() {
		when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
		when(preguntaRepostory.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
		examenService.findExamenPorNombreConPreguntas("Matemáticas");
		verify(examenRepository).findAll();
		verify(preguntaRepostory).findPreguntasPorExamenId(argThat(arg-> arg!= null && arg.equals(1L)));
	}
}
