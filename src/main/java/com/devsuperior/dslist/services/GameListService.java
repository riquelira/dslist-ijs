package com.devsuperior.dslist.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dslist.dto.GameListDTO;
import com.devsuperior.dslist.entities.GameList;
import com.devsuperior.dslist.projections.GameMinProjection;
import com.devsuperior.dslist.repositories.GameListRepository;
import com.devsuperior.dslist.repositories.GameRepository;

@Service
public class GameListService {

	@Autowired
	private GameListRepository gameListRepository;
	
	@Autowired
	private GameRepository gameRepository;
	
	@Transactional(readOnly = true)
	public GameListDTO findById(Long id) {
		GameList result = gameListRepository.findById(id).get();
		// sem tratativa de erro caso o ID não exista
		return new GameListDTO(result);
	}
	
	@Transactional(readOnly = true)
	public List<GameListDTO> findAll() {
		List<GameList> result = gameListRepository.findAll();
		return result.stream().map(entity -> new GameListDTO(entity)).toList();
	}
	
	@Transactional
	public void move(Long listId, int sourcePositionIndex, int destinationPositionIndex) {
		List<GameMinProjection> list = gameRepository.searchByList(listId);
		
		// reposicionar lista em memória
		GameMinProjection objRemoved = list.remove(sourcePositionIndex);
		list.add(destinationPositionIndex, objRemoved);
		
		// encontrar intervalo de items reposicionados na lista
		int min = sourcePositionIndex < destinationPositionIndex ? sourcePositionIndex : destinationPositionIndex;
		int max = sourcePositionIndex < destinationPositionIndex ? destinationPositionIndex :sourcePositionIndex;
		
		// reposicionar lista no banco de dados
		for (int i = min; i <= max; i++) {
			gameListRepository.updateBelongingPosition(listId, list.get(i).getId(), i);
		}
	}
}
