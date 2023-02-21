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
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import com.cava.mockito.models.Examen;
import com.cava.mockito.repositories.ExamenRepository;
import com.cava.mockito.repositories.ExamenRepositoryImpl;
import com.cava.mockito.repositories.PreguntaRepositoryImpl;
import com.cava.mockito.repositories.PreguntaRepostory;

@ExtendWith(MockitoExtension.class)
class ExamenServiceImplTest {

	@Mock
	ExamenRepositoryImpl examenRepository;
	@Mock
	PreguntaRepositoryImpl preguntaRepostory;
	@InjectMocks
	ExamenServiceImpl examenService;

	@Captor 
	ArgumentCaptor<Long> captor;

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
		//verify(preguntaRepostory).findPreguntasPorExamenId(argThat(arg-> arg!= null && arg.equals(1L)));
		verify(preguntaRepostory).findPreguntasPorExamenId(argThat(arg-> arg!= null && arg >= 1L));
	}

	@Test
	void testArgumentoMatcher2() {
		when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
		when(preguntaRepostory.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
		examenService.findExamenPorNombreConPreguntas("Matemáticas");
		verify(examenRepository).findAll();
		verify(preguntaRepostory).findPreguntasPorExamenId(argThat(new MiArgsMatchers()));
	}

	@Test
	void testArgumentoMatcher3() {
		when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
		when(preguntaRepostory.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
		examenService.findExamenPorNombreConPreguntas("Matemáticas");
		verify(examenRepository).findAll();
		verify(preguntaRepostory).findPreguntasPorExamenId(argThat((argument)-> argument != null  && argument > 0));
	}

	public static class MiArgsMatchers implements ArgumentMatcher<Long>{

		private Long argument;

		@Override
		public boolean matches(Long argument){
			this.argument =argument;
			return argument != null  && argument > 0;
		}
		@Override
		public String toString() {
			return "es para un mensaje  perzonalizado de error"+
			"que imprime mockito en caso de que falle el test" + argument + "debe ser un etero positivo";
		}
	}

	@Test
	void testArgumentCaptor(){
		when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
		when(preguntaRepostory.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
		examenService.findExamenPorNombreConPreguntas("Historia");

		//capturar los argumentos con la instanciacion del metodo propio de mockito de forma explicita (manual)

		//ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
		verify(preguntaRepostory).findPreguntasPorExamenId(captor.capture());
		
		assertEquals(5L,captor.getValue());

	}

	@Test
	void testDoThrow(){
		Examen examen = Datos.EXAMEN;
		examen.setPreguntas(Datos.PREGUNTAS);
		//se realiza el cambio de la implementacion dado que se tiene que realizar la llamada primero de el do para el metodo void 
		doThrow(IllegalArgumentException.class).when(preguntaRepostory).guardarVariasPreguntas(anyList());
		//when(preguntaRepostory.guardarVariasPreguntas(anyList())).thenThrow(IllegalArgumentException.class);

		assertThrows(IllegalArgumentException.class, ()->{
			examenService.guardarExamen(examen);
		});
	}


	@Test
	void testDoAnswer() {
		when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
//		when(preguntaRepostory.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
		doAnswer(invocation->{
			Long id = invocation.getArgument(0);
			return id == 5 ? Datos.PREGUNTAS : null;
		}).when(preguntaRepostory).findPreguntasPorExamenId(anyLong());
		Examen examen = examenService.findExamenPorNombreConPreguntas("Historia");
		
		assertEquals(5L, examen.getId());
		assertEquals("Historia", examen.getNombre());
		assertEquals(4, examen.getPreguntas().size());
		assertTrue(examen.getPreguntas().contains("intregales"));
		
		verify(preguntaRepostory).findPreguntasPorExamenId(anyLong());
		
	}
	
	@Test
	void testDoAnsweGuardaarExamen() {

		/// Given
		Examen newExamen = Datos.EXAMEN;
		newExamen.setPreguntas(Datos.PREGUNTAS);
		doAnswer(new Answer<Examen>() {

			Long secuencia = 6L;

			@Override
			public Examen answer(InvocationOnMock invocation) throws Throwable {
				Examen examen = invocation.getArgument(0);
				examen.setId(secuencia++);
				return examen;
			}

		}).when(examenRepository).guardar(any(Examen.class));
		
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
	void testDoCallRealMethod() {
		when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
		doCallRealMethod().when(preguntaRepostory).findPreguntasPorExamenId(anyLong());
		Examen examen = examenService.findExamenPorNombreConPreguntas("Historia");
		assertEquals(5L, examen.getId());
		assertEquals("Historia", examen.getNombre());
	}
	
	@Test
	void testSpy() {
		ExamenRepository examenRepository = spy(ExamenRepositoryImpl.class);
		PreguntaRepostory preguntaRepostory = spy(PreguntaRepositoryImpl.class);
		ExamenService service  = new ExamenServiceImpl(examenRepository, preguntaRepostory);
		
		//when(preguntaRepostory.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS); 
		doReturn(Datos.PREGUNTAS).when(preguntaRepostory).findPreguntasPorExamenId(anyLong());
		
		Examen examen = service.findExamenPorNombreConPreguntas("Historia");
		
		assertEquals(5L, examen.getId());
		assertEquals("Historia", examen.getNombre());
		assertEquals(4, examen.getPreguntas().size());
		assertTrue(examen.getPreguntas().contains("aritmetica"));
		
		
		verify(examenRepository).findAll();
		verify(preguntaRepostory).findPreguntasPorExamenId(anyLong());
	}
	
	
	@Test
	void testOrdenDeInvocaciones () {
		
		when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
		examenService.findExamenPorNombreConPreguntas("Historia");
		examenService.findExamenPorNombreConPreguntas("Matemáticas");
		InOrder inOrder =  inOrder(preguntaRepostory);
		inOrder.verify(preguntaRepostory).findPreguntasPorExamenId(5L);
		inOrder.verify(preguntaRepostory).findPreguntasPorExamenId(1L);
	}
	@Test
	void testOrdenDeInvocaciones2 () {
		
		when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
		examenService.findExamenPorNombreConPreguntas("Historia");
		examenService.findExamenPorNombreConPreguntas("Matemáticas");
		InOrder inOrder =  inOrder(examenRepository,preguntaRepostory);
		
		inOrder.verify(preguntaRepostory).findPreguntasPorExamenId(5L);
		inOrder.verify(preguntaRepostory).findPreguntasPorExamenId(1L);
	}
	
	@Test
	void testNumeroDeInvocaciones () {
		when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
		examenService.findExamenPorNombreConPreguntas("Historia");
		
		verify(preguntaRepostory).findPreguntasPorExamenId(5L);
		verify(preguntaRepostory,times(1)).findPreguntasPorExamenId(5L);
		verify(preguntaRepostory,atLeast(1)).findPreguntasPorExamenId(5L);
		verify(preguntaRepostory,atLeastOnce()).findPreguntasPorExamenId(5L);
		verify(preguntaRepostory,atMost(10)).findPreguntasPorExamenId(5L);
		verify(preguntaRepostory,atMostOnce()).findPreguntasPorExamenId(5L);
		
		
	}
	
	
	@Test
	void testNumeroDeInvocaciones2 () {
		when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
		examenService.findExamenPorNombreConPreguntas("Historia");
		
		//verify(preguntaRepostory).findPreguntasPorExamenId(5L); falla
		verify(preguntaRepostory,times(2)).findPreguntasPorExamenId(5L);
		verify(preguntaRepostory,atLeast(1)).findPreguntasPorExamenId(5L);
		verify(preguntaRepostory,atLeastOnce()).findPreguntasPorExamenId(5L);
		verify(preguntaRepostory,atMost(10)).findPreguntasPorExamenId(5L);
	//	verify(preguntaRepostory,atMostOnce()).findPreguntasPorExamenId(5L); falla
		
		
	}
	@Test
	void testNumeroDeInvocaciones3 () {
		when(examenRepository.findAll()).thenReturn(Collections.EMPTY_LIST);
		examenService.findExamenPorNombreConPreguntas("Historia");
		
		verify(preguntaRepostory,never()).findPreguntasPorExamenId(5L); 
		verifyNoInteractions(preguntaRepostory);
		
		verify(examenRepository,times(1)).findAll();
		verify(examenRepository,atLeast(1)).findAll();
		verify(examenRepository,atLeastOnce()).findAll();
		verify(examenRepository,atMost(10)).findAll();
		verify(examenRepository,atMostOnce()).findAll();
	}
}
