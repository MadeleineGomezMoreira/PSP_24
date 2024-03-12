package com.teams.graphql_server_teams.domain.services.impl;

import com.teams.graphql_server_teams.data.model.Player;
import com.teams.graphql_server_teams.data.repositories.PlayerRepository;
import com.teams.graphql_server_teams.domain.model.error.NotFoundException;
import com.teams.graphql_server_teams.domain.model.input.PlayerInput;
import com.teams.graphql_server_teams.domain.model.input.PlayerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements com.teams.graphql_server_teams.domain.services.PlayerService {

    private final PlayerRepository repository;
    private final PlayerMapper mapper;

    @Override
    public Player getPlayer(int id) {
        Player player = repository.getById(id);
        if (player == null) {
            throw new NotFoundException("Player not found");
        } else {
            return player;
        }
    }

    @Override
    public List<Player> getPlayers() {
        return repository.getAll();
    }

    @Override
    public List<Player> getPlayersByTeam(int id) {
        return repository.getAllByTeam(id);
    }

    @Override
    public Player savePlayer(PlayerInput player) {
        return repository.save(new Player(
                player.getId(),
                player.getName(),
                player.getTeamId()
        ));
    }

    //The mapper (mapstruct) does not work
//    @Override
//    public Player savePlayer(PlayerInput player) {
//        return repository.save(mapper.toPlayer(player));
//    }

}
