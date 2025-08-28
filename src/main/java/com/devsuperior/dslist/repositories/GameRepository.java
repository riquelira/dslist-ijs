package com.devsuperior.dslist.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devsuperior.dslist.Game;

public interface GameRepository extends JpaRepository<Game, Long> {
	
}
