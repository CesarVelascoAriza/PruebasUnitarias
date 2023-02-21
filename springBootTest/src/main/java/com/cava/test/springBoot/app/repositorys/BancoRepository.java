package com.cava.test.springBoot.app.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cava.test.springBoot.app.models.Banco;

public interface BancoRepository extends JpaRepository<Banco, Long> {
	
}
