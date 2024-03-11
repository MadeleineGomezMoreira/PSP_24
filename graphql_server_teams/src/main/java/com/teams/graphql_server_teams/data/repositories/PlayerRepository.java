package com.teams.graphql_server_teams.data.repositories;

import com.teams.graphql_server_teams.data.model.Player;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PlayerRepository {
    List<Player> getAll();

    Player getById(int id);

    List<Player> getAllByTeam(int id);

    Player save(Player player);
}
