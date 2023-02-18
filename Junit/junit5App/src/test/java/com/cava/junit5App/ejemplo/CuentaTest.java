package com.cava.junit5App.ejemplo;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;
import org.junit.jupiter.api.condition.DisabledOnJre;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.cava.junit5App.exeptions.DineroInsuficienteEception;

//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CuentaTest {

	Cuenta cuenta;
	private TestInfo testInfo;
	private TestReporter testReporter;

	@BeforeEach
	void initMetodTest(TestInfo testInfo, TestReporter testReporter) {
		this.cuenta = new Cuenta("Cesar", new BigDecimal("1000.1234"));
		System.out.println("Inicia el metodo.");
		this.testInfo = testInfo;
		this.testReporter = testReporter;
		System.out.printf("ejecutando %s %s  con la etiquita %s \n", testInfo.getDisplayName(), testInfo.getTestMethod().orElse(null).getName(), testInfo.getTags());
		testReporter.publishEntry("ejecutando "+ testInfo.getDisplayName() + " " + testInfo.getTestMethod().orElse(null).getName() + " con la etiqueta" + testInfo.getTags());
	}

	@AfterEach
	void tearDown() {
		System.out.println("Finalizando el metodo");
	}

	@BeforeAll
	static void beforeAll() {
		System.out.println("Inicializando el test");
	}

	@AfterAll
	static void afterAll() {
		System.out.println("finaliza el test");
	}

	@Test
	@DisplayName("probando nombre de la cuenta!")
	void testNombreCuenta() {
		/*
		 * Se cambia para inicializar las variables en cuenta Cuenta cuenta = new
		 * Cuenta("Cesar", new BigDecimal("100000.90"));
		 */
		String esperado = "Cesar";
		String real = cuenta.getPersona();
		// Mensajes de error personalizados cuando se contruye el lambda
		assertNotNull(real, () -> "la cuenta no puede ser nula");
		assertEquals(esperado, real, () -> "el nombre de la cuenta no es el que se esperaba ");
		assertTrue(real.equals(esperado), () -> "nombre de la cuenta esperada debe ser igual a la real");
	}

	@Test
	@DisplayName("probando el saldo de la cuenta corriente que no se null, mayor que 0, valor esperado")
	void testSaldoCuenta() {
		// this.cuenta = new Cuenta("Cesar", new BigDecimal("10000.90"));
		assertNotNull(cuenta.getSaldo());
		assertEquals(1000.1234, cuenta.getSaldo().doubleValue());
		assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
		assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
	}

	@Test
	@Disabled
	void test() {
		fail("Fallla y se desabilita ");
	}

	@Test
	void testReferenciaCuenta() {
		System.out.println(testInfo.getTestMethod().get().getName());
		Cuenta cuenta = new Cuenta("John Doe", new BigDecimal("8700.9997"));
		Cuenta cuenta2 = new Cuenta("John Doe", new BigDecimal("8700.9997"));
		// assertNotEquals(cuenta2, cuenta );
		assertEquals(cuenta2, cuenta);

	}

	@Test
	void testDebitoCuenta() {
		// this.cuenta = new Cuenta("John Doe", new BigDecimal("1000.1234"));
		cuenta.debito(new BigDecimal("100"));
		assertNotNull(cuenta.getSaldo());
		assertEquals(900, cuenta.getSaldo().intValue());
		assertEquals("900.1234", cuenta.getSaldo().toPlainString());

	}

	@DisplayName("Debiando repeticion de cuenta")
	@RepeatedTest(value = 2, name = "{displayName} - Repeticíon numero {currentRepetition} de {totalRepetitions}")
	void testDebitoCuentaRepiteTest(RepetitionInfo inf) {

		if (inf.getCurrentRepetition() == 1) {
			System.out.println("estamos en la repeticion de " + inf.getCurrentRepetition());
		}
		// this.cuenta = new Cuenta("John Doe", new BigDecimal("1000.1234"));
		cuenta.debito(new BigDecimal("100"));
		assertNotNull(cuenta.getSaldo());
		assertEquals(900, cuenta.getSaldo().intValue());
		assertEquals("900.1234", cuenta.getSaldo().toPlainString());

	}

	/*
	 * pruebas parametrizadas
	 */
	@Tag("Param")
	@ParameterizedTest(name = "numero {index}  ejecutando con valor {0} - {argumentsWithNames}")
	@MethodSource("montoList")
	void testDebitoCuentaParametricedMethodSource(String monto) {
		cuenta.debito(new BigDecimal(monto));
		assertNotNull(cuenta.getSaldo());
		assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);

	}

	static List<String> montoList(){
		return Arrays.asList("100", "200", "300", "500", "1000");
	}

	@Tag("Param")
	@Nested
	class PruebasParametrizadas{
	
		@ParameterizedTest(name = "numero {index}  ejecutando con valor {0} - {argumentsWithNames}")
		@ValueSource(strings = { "100", "200", "300", "500", "1000" })
		void testDebitoCuentaParametriced(String monto) {
			// this.cuenta = new Cuenta("John Doe", new BigDecimal("1000.1234"));
			cuenta.debito(new BigDecimal(monto));
			assertNotNull(cuenta.getSaldo());
			assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);

		}
		@ParameterizedTest(name = "numero {index}  ejecutando con valor {0} - {argumentsWithNames}")
		@CsvSource({ "1,100", "2,200", "3,300", "4,500", "5,1000" })
		void testDebitoCuentaParametricedCSVSource(String index,String monto) {
			System.out.println(index +" -> "+ monto );
			cuenta.debito(new BigDecimal(monto));
			assertNotNull(cuenta.getSaldo());
			assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);

		}
		@ParameterizedTest(name = "numero {index}  ejecutando con valor {0} - {argumentsWithNames}")
		@CsvFileSource(resources = "/data.csv")
		void testDebitoCuentaParametricedCSVFileSource(String monto) {
			cuenta.debito(new BigDecimal(monto));
			assertNotNull(cuenta.getSaldo());
			assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);

		}
		
		
		@ParameterizedTest(name = "numero {index}  ejecutando con valor {0} - {argumentsWithNames}")
		@CsvSource({ "200,100", "240,200", "390,300", "5010,500", "1000.1234,1000" })
		void testDebitoCuentaParametricedCSVSource2(String saldo,String monto) {
			System.out.println(saldo +" -> "+ monto );
			cuenta.setSaldo(new BigDecimal(saldo));
			cuenta.debito(new BigDecimal(monto));
			assertNotNull(cuenta.getSaldo());
			assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);

		}
		
		@ParameterizedTest(name = "numero {index}  ejecutando con valor {0} - {argumentsWithNames}")
		@CsvSource({ "200,100,john,john", "240,200, Pepe,Pepe", "390,300,Maria,Maria", "5010,500, Pepa,Pepa", "1000.1234,1000, Luca,Luca" })
		void testDebitoCuentaParametricedCSVSource3(String saldo,String monto,String esperado, String actual) {
			System.out.println(saldo +" -> "+ monto );
			cuenta.setSaldo(new BigDecimal(saldo));
			cuenta.debito(new BigDecimal(monto));
			cuenta.setPersona(actual);
			assertNotNull(cuenta.getPersona());
			assertNotNull(cuenta.getSaldo());
			assertEquals(esperado, actual);
			assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);

		}
		
		@ParameterizedTest(name = "numero {index}  ejecutando con valor {0} - {argumentsWithNames}")
		@CsvFileSource(resources = "/data2.csv")
		void testDebitoCuentaParametricedCSVFileSource2(String saldo,String monto,String esperado, String actual) {
			cuenta.setSaldo(new BigDecimal(saldo));
			cuenta.debito(new BigDecimal(monto));
			cuenta.setPersona(actual);
			assertNotNull(cuenta.getSaldo());
			assertNotNull(cuenta.getPersona());
			assertEquals(esperado, actual);
			assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);

		}
		

	}
		@Test
	void testCreditoCuenta() {
		// this.cuenta = new Cuenta("John Doe", new BigDecimal("1000.1234"));
		cuenta.credito(new BigDecimal("100"));
		assertNotNull(cuenta.getSaldo());
		assertEquals(1100, cuenta.getSaldo().intValue());
		assertEquals("1100.1234", cuenta.getSaldo().toPlainString());
	}

	@Test
	void testDineroInsuficieneException() {

		// this.cuenta = new Cuenta("John Doe", new BigDecimal("1000.1234"));
		Exception excetion = assertThrows(DineroInsuficienteEception.class, () -> {
			cuenta.debito(new BigDecimal(1500));
		});

		String messageActual = excetion.getMessage();
		String messageEsperado = "Dinero insuficiente";

		assertEquals(messageEsperado, messageActual);

	}

	@Test
	void testTransferirDineroCuentas() {
		Cuenta cuenta1 = new Cuenta("Cesar", new BigDecimal(2500));
		Cuenta cuenta2 = new Cuenta("John doe", new BigDecimal("1000.1234"));
		Banco banco = new Banco();
		banco.setNombre("Banco del estado");
		banco.transferir(cuenta2, cuenta1, new BigDecimal("500"));
		assertEquals("500.1234", cuenta2.getSaldo().toPlainString());
		assertEquals("3000", cuenta1.getSaldo().toPlainString());

	}

	@Test
	@DisplayName("probando realaciones entre las cuentas y los bancos con assert All")
	void testRelacionBancoCuentas() {
		Cuenta cuenta1 = new Cuenta("Cesar", new BigDecimal(2500));
		Cuenta cuenta2 = new Cuenta("John doe", new BigDecimal("1000.1234"));
		Banco banco = new Banco();
		banco.addCuenta(cuenta1);
		banco.addCuenta(cuenta2);
		banco.setNombre("Banco del estado");
		banco.transferir(cuenta2, cuenta1, new BigDecimal("500"));
		assertAll(() -> assertEquals("500.1234", cuenta2.getSaldo().toPlainString()), () -> {
			assertEquals("3000", cuenta1.getSaldo().toPlainString());
		}, () -> {
			assertEquals(2, banco.getCuentas().size());
		}, () -> {
			assertEquals("Banco del estado", cuenta1.getBanco().getNombre());
		}, () -> {
			assertEquals("Cesar", banco.getCuentas().stream().filter(c -> c.getPersona().equals("Cesar")).findFirst()
					.get().getPersona());
		}, () -> {
			assertTrue(banco.getCuentas().stream().filter(c -> c.getPersona().equals("Cesar")).findFirst().isPresent());

		}, () -> {
			assertTrue(banco.getCuentas().stream().anyMatch(c -> c.getPersona().equals("John doe")));
		});

	}

	@Nested
	@DisplayName("probando tipos de sistemas operativos")
	class SistemaOperativo {

		@Test
		@EnabledOnOs(OS.WINDOWS)
		void testSoloWindows() {

		}

		@Test
		@EnabledOnOs({ OS.LINUX, OS.MAC })
		void testSoloLinuxMac() {

		}

		@Test
		@DisabledOnOs(OS.WINDOWS)
		void testNoWindows() {

		}

	}

	@Nested
	class JavaVersionTest {

		@Test
		@EnabledOnJre(JRE.JAVA_8)
		void jdkSolo8() {

		}

		@Test
		@EnabledOnJre(JRE.JAVA_15)
		void jdkSolo15() {

		}

		@Test
		@DisabledOnJre(JRE.JAVA_15)
		void jdkSoloNot15() {

		}

	}

	@Nested
	class SistemPorpertiesTest {

		@Test
		void imprimirSystemProperties() {
			Properties properties = System.getProperties();
			properties.forEach((k, v) -> System.out.println(k + ":" + v));
		}

		@Test
		@EnabledIfSystemProperty(named = "java.version", matches = ".*17.*")
		void testJavaVersion() {

		}

		@Test
		@DisabledIfSystemProperty(named = "os.arch", matches = ".*32.*")
		void testSolo64() {

		}

		@Test
		@EnabledIfSystemProperty(named = "os.arch", matches = ".*32.*")
		void testNo64() {

		}

		@Test
		@EnabledIfSystemProperty(named = "user.name", matches = "adolfo")
		void testUserName() {

		}

		@Test
		@EnabledIfSystemProperty(named = "ENV", matches = "dev")
		void testDev() {

		}
	}

	@Nested
	class VariablesAmbienteTest {

		@Test
		void testImprimirVariablesAmbiente() {
			Map<String, String> getEnv = System.getenv();
			getEnv.forEach((k, v) -> System.out.println(k + ": " + v));
		}

		@Test
		@EnabledIfEnvironmentVariable(named = "JAVA_HOME", matches = ".*jdk.*")
		void testJavaHome() {

		}

		@Test
		@EnabledIfEnvironmentVariable(named = "NUMBER_OF_PROCESSORS", matches = "8")
		void testProcesadores() {

		}

		@Test
		@EnabledIfEnvironmentVariable(named = "ENVIROMENT", matches = "DEV")
		void testEnv() {

		}

		@Test
		@DisabledIfEnvironmentVariable(named = "ENVIROMENT", matches = "DEV")
		void testDisableEnv() {

		}

		@Test
		@DisabledIfEnvironmentVariable(named = "ENVIROMENT", matches = "PROD")
		void testDisableProd() {

		}

		@Test
		@DisplayName("test saldo cuenta dev")
		void testSaldoCuentaDev() {

			boolean isDev = "dev".equals(System.getenv("ENV"));
			// this.cuenta = new Cuenta("Cesar", new BigDecimal("10000.90"));
			assumeTrue(isDev);
			assertNotNull(cuenta.getSaldo());
			assertEquals(1000.1234, cuenta.getSaldo().doubleValue());
			assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
			assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
		}

		@Test
		@DisplayName("test saldo cuenta dev 2")
		void testSaldoCuentaDev2() {

			boolean isDev = "dev".equals(System.getenv("ENV"));
			// this.cuenta = new Cuenta("Cesar", new BigDecimal("10000.90"));
			assumingThat(isDev, () -> {
				assertNotNull(cuenta.getSaldo());
				assertEquals(1000.1234, cuenta.getSaldo().doubleValue());
			});
			assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
			assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
		}

	}
	
	@Nested
	@Tag("TimeOut")
	class timeOaut{
		@Test
		@Timeout(value = 5)
		void pruebaTimeOaut() throws InterruptedException {
			TimeUnit.SECONDS.sleep(4);
		}
		@Test
		@Timeout(value = 1000 , unit = TimeUnit.MILLISECONDS)
		void pruebaTimeOaut2() throws InterruptedException {
			TimeUnit.MILLISECONDS.sleep(900);
		}
		@Test
		void testTimeOutAssertions() {
			assertTimeout(Duration.ofSeconds(5), ()->{
				TimeUnit.MILLISECONDS.sleep(4000);
			});
		}
	}

}
