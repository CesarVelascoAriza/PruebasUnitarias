package com.cava.test.springBoot.app;

import static com.cava.test.springBoot.app.services.Datos.crearBanco;
import static com.cava.test.springBoot.app.services.Datos.crearCuenta001;
import static com.cava.test.springBoot.app.services.Datos.crearCuenta002;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.cava.test.springBoot.app.exceptios.DineroInsuficienteException;
import com.cava.test.springBoot.app.models.Banco;
import com.cava.test.springBoot.app.models.Cuenta;
import com.cava.test.springBoot.app.repositorys.BancoRepository;
import com.cava.test.springBoot.app.repositorys.CuentaRepository;
import com.cava.test.springBoot.app.services.CuentaService;
import com.cava.test.springBoot.app.services.Datos;

@SpringBootTest
class SpringBootTestApplicationTests {

	//@Mock injeccion de dependencias De juni5
	//forma de injectar las dependencias de spring boot
	@MockBean
	CuentaRepository cuentaRepository;
	//@Mock
	@MockBean
	BancoRepository  bancoRepository;
	//@InjectMocks
	@Autowired
	CuentaService service;
	
	
	@BeforeEach
	void setUp() {
		//cuentaRepository = mock(CuentaRepository.class);
		//bancoRepository = mock(BancoRepository.class);
		//service = new CuentaServiceImpl(cuentaRepository, bancoRepository);
		//Datos.CUENTA_001.setSaldo(new BigDecimal("1000"));
		//Datos.CUENTA_002.setSaldo(new BigDecimal("2000"));
		//Datos.BANCO.setTotalTransferencias(0);
	}
	
	@Test
	void contexto() {
		
		when(cuentaRepository.findById(1L)).thenReturn(crearCuenta001());
		when(cuentaRepository.findById(2L)).thenReturn(crearCuenta002());
		when(bancoRepository.findById(1L)).thenReturn(crearBanco());
		
		BigDecimal saldoOrigen = service.revisarSaldo(1L);
		BigDecimal saldoDestino = service.revisarSaldo(2L);
		
		assertEquals("1000", saldoOrigen.toPlainString());
		assertEquals("2000", saldoDestino.toPlainString());
		
		service.transferir(1L, 2L,new BigDecimal("100") , 1L);
		
		saldoOrigen = service.revisarSaldo(1L);
		saldoDestino = service.revisarSaldo(2L);
		
		assertEquals("900", saldoOrigen.toPlainString());
		assertEquals("2100", saldoDestino.toPlainString());
		
		int totalTransferencias = service.revisarTotalTransFerenias(1L);
		
		assertEquals(1, totalTransferencias);
		
		verify(cuentaRepository,times(3)).findById(1L);
		verify(cuentaRepository,times(3)).findById(2L);
		verify(cuentaRepository,times(2)).save(any(Cuenta.class));
		
		
		verify(bancoRepository,times(2)).findById(1L);
		verify(bancoRepository).save(any(Banco.class));
		
		verify(cuentaRepository,times(6)).findById(anyLong());
		verify(cuentaRepository, never()).findAll();
		
	}
	
	@Test
	void contextoExceptions() {
		
		when(cuentaRepository.findById(1L)).thenReturn(crearCuenta001());
		when(cuentaRepository.findById(2L)).thenReturn(crearCuenta002());
		when(bancoRepository.findById(1L)).thenReturn(crearBanco());
		
		BigDecimal saldoOrigen = service.revisarSaldo(1L);
		BigDecimal saldoDestino = service.revisarSaldo(2L);
		
		assertEquals("1000", saldoOrigen.toPlainString());
		assertEquals("2000", saldoDestino.toPlainString());
		
		assertThrows(DineroInsuficienteException.class, ()->{
			service.transferir(1L, 2L,new BigDecimal("1200") , 1L);
			
		});
		
		
		saldoOrigen = service.revisarSaldo(1L);
		saldoDestino = service.revisarSaldo(2L);
		
		assertEquals("1000", saldoOrigen.toPlainString());
		assertEquals("2000", saldoDestino.toPlainString());
		
		int totalTransferencias = service.revisarTotalTransFerenias(1L);
		
		assertEquals(0, totalTransferencias);
		
		verify(cuentaRepository,times(3)).findById(1L);
		verify(cuentaRepository,times(2)).findById(2L);
		verify(cuentaRepository,never()).save(any(Cuenta.class));
		
		
		verify(bancoRepository,times(1)).findById(1L);
		verify(bancoRepository,never()).save(any(Banco.class));
		
		verify(cuentaRepository,times(5)).findById(anyLong());
		verify(cuentaRepository, never()).findAll();
		
	}
	
	@Test
	void contextLoadv3() {
		
		when(cuentaRepository.findById(1L)).thenReturn(crearCuenta001());
		Cuenta cuenta1 = service.findById(1L);
		Cuenta cuenta2 = service.findById(1L);
		assertSame(cuenta1, cuenta2);
		assertTrue(cuenta1.equals(cuenta2));
		assertTrue(cuenta1 == cuenta2);
		assertEquals(cuenta1, cuenta2);
		assertEquals("Andres", cuenta1.getNombre());
		assertEquals("Andres", cuenta2.getNombre());
		
		verify(cuentaRepository,times(2)).findById(anyLong());
		
	}
	
	@Test
	void testFindAll() throws Exception {
		//Given
		List<Cuenta> listaDeDatos= Arrays.asList(Datos.crearCuenta001().orElseThrow(),
				Datos.crearCuenta002().orElseThrow()
				);
		when(cuentaRepository.findAll()).thenReturn(listaDeDatos);
		
		//when 
		List<Cuenta> cuentas= service.findAll();
		
		
		//then
		assertFalse(cuentas.isEmpty());
		assertEquals(2, cuentas.size());
		
		assertTrue(cuentas.contains(Datos.crearCuenta002().orElseThrow()));
		
		verify(cuentaRepository).findAll();
		
		
	}
	
	@Test
	void testCuentaSave() throws Exception {
		
		Cuenta cuentaPepe = new Cuenta(null, "Pepe",new BigDecimal("3000"));
		when(cuentaRepository.save(any())).then(invocation->{
			Cuenta c = invocation.getArgument(0);
			c.setId(3L);
			return c;
		});
		//when
		Cuenta cuenta = service.save(cuentaPepe);
		//then 
		assertEquals("Pepe", cuenta.getNombre());
		assertEquals(3, cuenta.getId());
		assertEquals("3000", cuenta.getSaldo().toPlainString());
		
		verify(cuentaRepository).save(any());
	}

}
