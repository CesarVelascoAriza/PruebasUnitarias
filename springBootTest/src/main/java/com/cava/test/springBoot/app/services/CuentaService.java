package com.cava.test.springBoot.app.services;

import java.math.BigDecimal;

import com.cava.test.springBoot.app.models.Cuenta;

public interface CuentaService {

	Cuenta findById(Long id);
	int revisarTotalTransFerenias(Long bancoId);
	BigDecimal revisarSaldo(Long cuentaId);
	void transferir (Long numeroCuentaOrigen, Long numeroCuentaDestino,BigDecimal monto,Long bancoId);
}
