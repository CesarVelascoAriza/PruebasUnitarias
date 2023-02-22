package com.cava.test.springBoot.app.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.cava.test.springBoot.app.models.Cuenta;
import com.cava.test.springBoot.app.models.TransaccionTDO;
import com.cava.test.springBoot.app.services.CuentaService;
import com.cava.test.springBoot.app.services.Datos;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;



@WebMvcTest(controllers = CuentaController.class)
class CuentaControllerTest {

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private CuentaService cuentaService;
	
	ObjectMapper maper;
	
	@BeforeEach
	void setUp() {
		maper = new ObjectMapper();
		
	}
	
	@Test
	void detalle() throws Exception {
		//given
		when(cuentaService.findById(1L)).thenReturn(Datos.crearCuenta001().orElseThrow());
		//when
		mvc.perform(MockMvcRequestBuilders.get("/api/cuentas/1")
				.contentType(MediaType.APPLICATION_JSON)
				).andExpect(status().isOk())
				 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
				 .andExpect(jsonPath("$.nombre").value("Andres"))
				 .andExpect(jsonPath("$.saldo").value("1000"))		 
		;
		
		verify(cuentaService).findById(1L);
	}
	@Test
	void testTransferir() throws JsonProcessingException, Exception {
		//given
		TransaccionTDO dto = new TransaccionTDO();
		dto.setCuentaOrigenId(1L);
		dto.setCuentaDestinoId(2L);
		dto.setMonto(new BigDecimal("100"));
		dto.setBancoId(1L);
		
		Map<String , Object> response = new HashMap<>();
		response.put("date", LocalDate.now().toString());
		response.put("status", "ok" );
		response.put("mensaje", "Transferencia realizada con exito");
		response.put("transaccion", dto);
		
		System.out.println(maper.writeValueAsString(response));
		
		//when 
		mvc.perform(
				MockMvcRequestBuilders.post("/api/cuentas/transferir")
				.contentType(MediaType.APPLICATION_JSON)
				.content(maper.writeValueAsString(dto))
				)
		//then
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.date").value(LocalDate.now().toString()))
		.andExpect(jsonPath("$.mensaje").value("Transferencia realizada con exito"))
		.andExpect(jsonPath("$.transaccion.cuentaOrigenId").value(dto.getCuentaOrigenId()))
		.andExpect(content().json(maper.writeValueAsString(response)))
		;
		
		
	}

	@Test
	void testListar() throws Exception {
		//given  
		List<Cuenta> cuentas = Arrays.asList(
				Datos.crearCuenta001().orElseThrow(),
				Datos.crearCuenta002().orElseThrow()
				);
		
		when(cuentaService.findAll()).thenReturn(cuentas);
		//when
		
		mvc.perform(
				MockMvcRequestBuilders.get("/api/cuentas")
				.contentType(MediaType.APPLICATION_JSON)
				)
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$[0].nombre").value("Andres"))
		.andExpect(jsonPath("$[1].nombre").value("John"))
		.andExpect(jsonPath("$[0].saldo").value("1000"))
		.andExpect(jsonPath("$[1].saldo").value("2000"))
		.andExpect(jsonPath("$", Matchers.hasSize(2)))
		.andExpect(content().json(maper.writeValueAsString(cuentas)))
		;
		
		verify(cuentaService).findAll();
		
	}
	
	@Test
	void testGuardar() throws Exception {
		Cuenta cuenta = new Cuenta (null, "Pepe", new BigDecimal("3000"));
		when(cuentaService.save(any())).then( invocation -> 
		{
			Cuenta c = invocation.getArgument(0);
			c.setId(3L);
			return c;
			
		});
		
		mvc.perform(
				MockMvcRequestBuilders.post("/api/cuentas")
				.contentType(MediaType.APPLICATION_JSON)
				.content(maper.writeValueAsString(cuenta))
				)
		.andExpect(status().isCreated())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.id",Matchers.is(3)))
		.andExpect(jsonPath("$.nombre",Matchers.is("Pepe")))
		.andExpect(jsonPath("$.saldo",Matchers.is(3000)))
		;
		verify(cuentaService).save(any());
		
	}
	
	
}
