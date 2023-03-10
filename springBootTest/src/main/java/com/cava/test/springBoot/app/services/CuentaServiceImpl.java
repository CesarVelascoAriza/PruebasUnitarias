package com.cava.test.springBoot.app.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cava.test.springBoot.app.models.Banco;
import com.cava.test.springBoot.app.models.Cuenta;
import com.cava.test.springBoot.app.repositorys.BancoRepository;
import com.cava.test.springBoot.app.repositorys.CuentaRepository;

@Service
public class CuentaServiceImpl implements CuentaService {

	private CuentaRepository cuentaRepository;
	private BancoRepository  bancoRepository;
	
	public CuentaServiceImpl(CuentaRepository cuentaRepository,BancoRepository  bancoRepository) {
		this.cuentaRepository =cuentaRepository;
		this.bancoRepository =  bancoRepository;
	}
	
	@Override
	@Transactional(readOnly = true) 
	public Cuenta findById(Long  id) {
		// TODO Auto-generated method stub
		return cuentaRepository.findById(id).orElseThrow();
	}

	@Override
	@Transactional(readOnly = true) 
	public int revisarTotalTransFerenias(Long bancoId) {
		// TODO Auto-generated method stub
		Banco banco = bancoRepository.findById(bancoId).orElseThrow();
		return banco.getTotalTransferencias();
	}

	@Override
	@Transactional(readOnly = true) 
	public BigDecimal revisarSaldo(Long cuentaId) {
		// TODO Auto-generated method stub
		Cuenta cuenta = cuentaRepository.findById(cuentaId).orElseThrow();
		return cuenta.getSaldo();
	}

	@Override
	@Transactional 
	public void transferir(Long numeroCuentaOrigen, Long numeroCuentaDestino, BigDecimal monto , Long bancoId) {
		Cuenta cuentaOrigen = cuentaRepository.findById(numeroCuentaOrigen).orElseThrow();
		cuentaOrigen.debito(monto);
		cuentaRepository.save(cuentaOrigen);
		
		Cuenta cuentaDestino = cuentaRepository.findById(numeroCuentaDestino).orElseThrow();
		cuentaDestino.credito(monto);
		cuentaRepository.save(cuentaDestino);
		
		Banco banco = bancoRepository.findById(bancoId).orElseThrow();
		int totalTransferencias= banco.getTotalTransferencias();
		banco.setTotalTransferencias(++totalTransferencias);
		bancoRepository.save(banco);
		

	}

	@Override
	@Transactional(readOnly = true) 
	public List<Cuenta> findAll() {
		// TODO Auto-generated method stub
		return cuentaRepository.findAll();
	}

	@Override
	@Transactional
	public Cuenta save(Cuenta cuenta) {
		// TODO Auto-generated method stub
		return cuentaRepository.save(cuenta);
	}

	@Override
	public void deleteById(Long id) {
		// TODO Auto-generated method stub
		cuentaRepository.deleteById(id);
	}

}
