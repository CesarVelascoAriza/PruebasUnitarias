package com.cava.test.springBoot.app.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.cava.test.springBoot.app.models.Cuenta;
import com.cava.test.springBoot.app.models.TransaccionTDO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Tag("integaracionClienteTest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CuentaControllerWebTestclientTests {

	ObjectMapper maper;
	@Autowired
	private WebTestClient cliente;

	@BeforeEach
	void setUp() {
		maper = new ObjectMapper();
		
	}
	
	@Test
	@Order(1)
	void testTransferir() throws JsonProcessingException {
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
		
		
		//when
		cliente.post().uri("/api/cuentas/transferir")
		.contentType(MediaType.APPLICATION_JSON)
		.bodyValue(dto)
		.exchange()
		.expectStatus().isOk()
		.expectBody()
		.consumeWith(respuest->{
			try {
				JsonNode json = maper.readTree(respuest.getResponseBody()) ;
				
				assertEquals("Transferencia realizada con exito", json.path("mensaje").asText());
				assertEquals(1L, json.path("transaccion").path("cuentaOrigenId").asLong());
				assertEquals(LocalDate.now().toString(), json.path("date").asText());
				assertEquals("100", json.path("transaccion").path("monto").asText());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		})
		.jsonPath("$.mensaje").isNotEmpty()
		 .jsonPath("$.mensaje").value(is("Transferencia realizada con exito"))
		 .jsonPath("$.mensaje").value(valor->{
			 assertEquals(valor, "Transferencia realizada con exito");
		 })
		 .jsonPath("$.mensaje").isEqualTo("Transferencia realizada con exito")
		 .jsonPath("$.transaccion.cuentaOrigenId").isEqualTo(dto.getCuentaOrigenId())
		 .jsonPath("$.date").isEqualTo(LocalDate.now().toString())
		 .json(maper.writeValueAsString(response));	
	}

	@Test
	@Order(2)
	void testDetalle() throws Exception {
		
		Cuenta cuenta = new Cuenta(1L, "Andres", new BigDecimal("900")); 		
		cliente.get().uri("/api/cuentas/1")
		.exchange()
		.expectStatus().isOk()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBody()
		.jsonPath("$.nombre").isEqualTo("Andres")
		.jsonPath("$.saldo").isEqualTo(900)
		.json(maper.writeValueAsString(cuenta))
		;
	}
	@Test
	@Order(3)
	void testDetalle2() throws Exception {
		
		cliente.get().uri("/api/cuentas/2")
		.exchange()
		.expectStatus().isOk()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBody(Cuenta.class)
		.consumeWith(respuesta->{
			Cuenta c= respuesta.getResponseBody();
			assertEquals("john", c.getNombre());
			assertEquals("2100.00", c.getSaldo().toPlainString());
		})
		;
	}
	
	@Test
	@Order(4)
	void testListar() throws Exception {
		cliente.get().uri("/api/cuentas")
		.exchange()
		.expectStatus().isOk()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBody()
		.jsonPath("$[0].nombre").isEqualTo("Andres")
		.jsonPath("$[0].id").isEqualTo(1)
		.jsonPath("$[1].id").isEqualTo(2)
		.jsonPath("$[1].nombre").isEqualTo("john")
		.jsonPath("$[0].saldo").isEqualTo(900)
		.jsonPath("$[1].saldo").isEqualTo(2100)
		.jsonPath("$").isArray()
		.jsonPath("$").value(hasSize(2));
	}
	@Test
	@Order(5)
	void testListar2() throws Exception {
		cliente.get().uri("/api/cuentas")
		.exchange()
		.expectStatus().isOk()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBodyList(Cuenta.class)
		.consumeWith(response ->{
			List<Cuenta> cuentas = response.getResponseBody();
			assertNotNull(cuentas);
			assertEquals(2,cuentas.size());
			assertEquals("Andres", cuentas.get(0).getNombre());
			assertEquals(1L, cuentas.get(0).getId());
			assertEquals(900, cuentas.get(0).getSaldo().intValue());
			assertEquals("john", cuentas.get(1).getNombre());
			assertEquals(2L, cuentas.get(1).getId());
			assertEquals("2100.0", cuentas.get(1).getSaldo().toPlainString());
		})
		.hasSize(2)
		.value(hasSize(2))
		;
	}
	
	@Test
	@Order(6)
	void testGuardar() throws Exception {
		//givem
		Cuenta cuenta= new Cuenta(null, "Pepe", new BigDecimal("3000"));
		
		cliente.post().uri("/api/cuentas")
		.contentType(MediaType.APPLICATION_JSON)
		.bodyValue(cuenta)
		.exchange()
		.expectStatus().isCreated()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBody()
		.jsonPath("$.nombre").isEqualTo("Pepe")
		.jsonPath("$.nombre").value(is("Pepe"))
		.jsonPath("$.id").isEqualTo(3)
		.jsonPath("$.saldo").isEqualTo(3000)
		;
	}
	@Test
	@Order(7)
	void testGuardar2() throws Exception {
		//givem
		Cuenta cuenta= new Cuenta(null, "Pepa", new BigDecimal("3500"));
		
		cliente.post().uri("/api/cuentas")
		.contentType(MediaType.APPLICATION_JSON)
		.bodyValue(cuenta)
		.exchange()
		.expectStatus().isCreated()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBody(Cuenta.class)
		.consumeWith(response->{
			Cuenta c = response.getResponseBody();
			assertNotNull(c);
			assertEquals(4L, c.getId());
			assertEquals("Pepa", c.getNombre());
			assertEquals("3500", c.getSaldo().toPlainString());
		})
		;
	}
	
	@Test
	@Order(8)
	void testEliminar() throws Exception {
		cliente.get().uri("/api/cuentas")
		.exchange()
		.expectStatus().isOk()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBodyList(Cuenta.class)
		.hasSize(4);
		
		cliente.delete().uri("/api/cuentas/3")
		.exchange()
		.expectStatus().isNoContent()
		.expectBody().isEmpty();
		
		cliente.get().uri("/api/cuentas")
		.exchange()
		.expectStatus().isOk()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBodyList(Cuenta.class)
		.hasSize(3);
		
		
		cliente.get().uri("/api/cuentas/3")
		.exchange()
		//.expectStatus().is5xxServerError()
		.expectStatus().isNotFound()
		.expectBody().isEmpty()
		;
	}
}
