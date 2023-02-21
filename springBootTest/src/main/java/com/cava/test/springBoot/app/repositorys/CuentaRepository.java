package com.cava.test.springBoot.app.repositorys;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cava.test.springBoot.app.models.Cuenta;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {

	@Query("select c from Cuenta c where c.nombre = ?1 ")
	Optional<Cuenta> findByNombre(String nombre);
	
	
}
