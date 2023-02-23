package com.cava.test.springBoot.app.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
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
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.cava.test.springBoot.app.models.Cuenta;
import com.cava.test.springBoot.app.models.TransaccionTDO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Tag("integration_rtp")
class CuentaControllerRestTemplateTest {

	@Autowired
	private TestRestTemplate cliente;

	private ObjectMapper maper;
	@LocalServerPort
	private int puerto;

	@BeforeEach
	void setUp() throws Exception {
		maper = new ObjectMapper();
	}

	@Test
	@Order(3)
	void testListar() throws JsonMappingException, JsonProcessingException {
		ResponseEntity<Cuenta[]> resp = cliente.getForEntity(crearUri("/api/cuentas"), Cuenta[].class);
		List<Cuenta> cuentas = Arrays.asList(resp.getBody());

		assertEquals(HttpStatus.OK, resp.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, resp.getHeaders().getContentType());
		assertEquals(2, cuentas.size());

		assertEquals(1L, cuentas.get(0).getId());
		assertEquals("Andres", cuentas.get(0).getNombre());
		assertEquals("900.00", cuentas.get(0).getSaldo().toPlainString());

		assertEquals(2L, cuentas.get(1).getId());
		assertEquals("john", cuentas.get(1).getNombre());
		assertEquals("2100.00", cuentas.get(1).getSaldo().toPlainString());

		JsonNode jsonNode = maper.readTree(maper.writeValueAsString(cuentas));
		assertEquals(1L, jsonNode.get(0).path("id").asLong());
		assertEquals("Andres", jsonNode.get(0).path("nombre").asText());
		assertEquals("900.0", jsonNode.get(0).path("saldo").asText());

		assertEquals(2L, jsonNode.get(1).path("id").asLong());
		assertEquals("john", jsonNode.get(1).path("nombre").asText());
		assertEquals("2100.0", jsonNode.get(1).path("saldo").asText());

	}

	@Test
	@Order(4)
	void testGuardar() {
		// given
		Cuenta cuenta = new Cuenta(null, "Pepa", new BigDecimal("3800"));

		ResponseEntity<Cuenta> resp = cliente.postForEntity(crearUri("/api/cuentas"), cuenta, Cuenta.class);
		assertEquals(HttpStatus.CREATED, resp.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, resp.getHeaders().getContentType());

		Cuenta newCuenta = resp.getBody();

		assertNotNull(newCuenta);
		assertEquals(3L, newCuenta.getId());
		assertEquals("Pepa", newCuenta.getNombre());
		assertEquals("3800", newCuenta.getSaldo().toPlainString());
	}

	@Test
	@Order(2)
	void testDetalle() {
		ResponseEntity<Cuenta> resp = cliente.getForEntity(crearUri("/api/cuentas/1"), Cuenta.class);

		Cuenta cuenta = resp.getBody();
		assertEquals(HttpStatus.OK, resp.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, resp.getHeaders().getContentType());

		assertNotNull(cuenta);
		assertEquals(1L, cuenta.getId());
		assertEquals("Andres", cuenta.getNombre());
		assertEquals("900.00", cuenta.getSaldo().toPlainString());
		assertEquals(new Cuenta(1L, "Andres", new BigDecimal("900.00")), cuenta);

	}

	@Test
	@Order(1)
	void testTransferir() throws JsonMappingException, JsonProcessingException {
		// given
		TransaccionTDO dto = new TransaccionTDO();
		dto.setCuentaOrigenId(1L);
		dto.setCuentaDestinoId(2L);
		dto.setMonto(new BigDecimal("100"));
		dto.setBancoId(1L);

		Map<String, Object> response = new HashMap<>();
		response.put("date", LocalDate.now().toString());
		response.put("status", "ok");
		response.put("mensaje", "Transferencia realizada con exito");
		response.put("transaccion", dto);

		ResponseEntity<String> resp = cliente.postForEntity(crearUri("/api/cuentas/transferir"), dto, String.class);
		System.out.println(puerto);
		String json = resp.getBody();
		System.out.println(json);
		assertEquals(HttpStatus.OK, resp.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, resp.getHeaders().getContentType());
		assertNotNull(json);
		assertTrue(json.contains("Transferencia realizada con exito"));

		JsonNode jsonNode = maper.readTree(json);
		assertEquals("Transferencia realizada con exito", jsonNode.path("mensaje").asText());
		assertEquals(LocalDate.now().toString(), jsonNode.path("date").asText());
		assertEquals(100, jsonNode.path("transaccion").path("monto").asInt());
		assertEquals(1L, jsonNode.path("transaccion").path("cuentaOrigenId").asLong());

		assertEquals(maper.writeValueAsString(response), json);

	}

	private String crearUri(String uri) {
		return "http://localhost:" + puerto + uri;
	}

	@Test
	@Order(5)
	void testEliminar() {

		ResponseEntity<Cuenta[]> resp = cliente.getForEntity(crearUri("/api/cuentas"), Cuenta[].class);
		List<Cuenta> cuentas = Arrays.asList(resp.getBody());

		assertEquals(3, cuentas.size());

		Map<String ,Long> pvariables= new HashMap<>();
		pvariables.put("id", 3L);
		
		//cliente.delete(crearUri("/api/cuentas/3"));
		ResponseEntity<Void> rdel =cliente.exchange(crearUri("/api/cuentas/{id}"),
				HttpMethod.DELETE, null, Void.class, pvariables);
		assertEquals(HttpStatus.NO_CONTENT, rdel.getStatusCode());
		assertFalse(rdel.hasBody());
		
		
		
		resp = cliente.getForEntity(crearUri("/api/cuentas"), Cuenta[].class);
		
		cuentas = Arrays.asList(resp.getBody());
		System.out.println(cuentas);
		assertEquals(2, cuentas.size());

		ResponseEntity<Cuenta>  resp2 = cliente.getForEntity(crearUri("/api/cuentas/3"), Cuenta.class);
		System.out.println(resp2);
		System.out.println(resp2.getStatusCode());
		System.out.println(resp2.getBody());
		assertEquals(HttpStatus.NOT_FOUND, resp2.getStatusCode());
		assertFalse(resp2.hasBody());
	}

}
