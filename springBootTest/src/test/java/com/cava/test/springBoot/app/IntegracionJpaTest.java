package com.cava.test.springBoot.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.cava.test.springBoot.app.models.Cuenta;
import com.cava.test.springBoot.app.repositorys.CuentaRepository;

@Tag("Data_jp")
@DataJpaTest
public class IntegracionJpaTest {

	@Autowired
	CuentaRepository cuentaRepository;
	
	@Test
	void testFindById() {
		Optional<Cuenta> cuenta = cuentaRepository.findById(1L);
		assertTrue(cuenta.isPresent());
		assertEquals("Andres", cuenta.orElseThrow().getNombre());
	}
	@Test
	void testFindByNombre() {
		Optional<Cuenta> cuenta = cuentaRepository.findByNombre("Andres");
		assertTrue(cuenta.isPresent());
		assertEquals("Andres", cuenta.orElseThrow().getNombre());
		assertEquals("1000.00", cuenta.orElseThrow().getSaldo().toPlainString());
	}
	@Test
	void testFindByNombreTrowExeption() {
		Optional<Cuenta> cuenta = cuentaRepository.findByNombre("Rosa");
		assertThrows(NoSuchElementException.class, cuenta::orElseThrow);
		assertFalse(cuenta.isPresent());
	}
	
	@Test
	void testFindAll() {
		List<Cuenta> cuentas = cuentaRepository.findAll();
		assertFalse(cuentas.isEmpty());
		assertEquals(2, cuentas.size());
	}
	@Test
	void testSave() {
		//given
		Cuenta cuenta  = new Cuenta(null, "Pepe", new  BigDecimal("3000"));
		//when
		//Cuenta cuentaEsperado = cuentaRepository.findByNombre("Pepe").orElseThrow();
		Cuenta cuetaSAve = cuentaRepository.save(cuenta);
//		Cuenta cuentaEsperado = cuentaRepository.findByNombre("Pepe").orElseThrow();
		//then 
		assertEquals("Pepe", cuetaSAve.getNombre());
		assertEquals("3000", cuetaSAve.getSaldo().toPlainString());
		
		
		
		
	}
	@Test
	void testUpdate() {
		//given
		
		Cuenta cuenta  = new Cuenta(null, "Pepe", new  BigDecimal("3000"));
		
		//when
		
		Cuenta cuetaSAve = cuentaRepository.save(cuenta);

		//Cuenta cuentaEsperado = cuentaRepository.findByNombre("Pepe").orElseThrow();
//		Cuenta cuentaEsperado = cuentaRepository.findByNombre("Pepe").orElseThrow();
		
		//then 
		assertEquals("Pepe", cuetaSAve.getNombre());
		assertEquals("3000", cuetaSAve.getSaldo().toPlainString());
		
		//when
		cuetaSAve.setSaldo(new BigDecimal("3800"));
		Cuenta cuentaActualizada = cuentaRepository.save(cuetaSAve);
		//then
		assertEquals("Pepe", cuentaActualizada.getNombre());
		assertEquals("3800", cuentaActualizada.getSaldo().toPlainString());
		
	}
	
	@Test
	void testDelete() {
		Cuenta cuenta = cuentaRepository.findById(2L).orElseThrow();
		assertEquals("john", cuenta.getNombre());
		
		cuentaRepository.delete(cuenta);
		
		assertThrows(NoSuchElementException.class, ()->{ 
			//cuentaRepository.findByNombre("john").orElseThrow(); 
			cuentaRepository.findById(2L).orElseThrow();
			});
		assertEquals(1, cuentaRepository.findAll().size());
	}
}
