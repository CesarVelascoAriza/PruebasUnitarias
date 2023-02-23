package com.cava.test.springBoot.app.services;

import java.math.BigDecimal;
import java.util.List;

import com.cava.test.springBoot.app.models.Cuenta;

public interface CuentaService {

	List<Cuenta> findAll();
	Cuenta findById(Long id);
	void deleteById(Long id);
	int revisarTotalTransFerenias(Long bancoId);
	BigDecimal revisarSaldo(Long cuentaId);
	Cuenta save(Cuenta cuenta);
	void transferir (Long numeroCuentaOrigen, Long numeroCuentaDestino,BigDecimal monto,Long bancoId);
}
