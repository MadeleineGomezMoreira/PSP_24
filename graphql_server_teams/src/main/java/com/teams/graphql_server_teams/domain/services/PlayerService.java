package com.teams.graphql_server_teams.domain.services;

import com.teams.graphql_server_teams.data.model.Player;
import com.teams.graphql_server_teams.domain.model.input.PlayerInput;

import java.util.List;

public interface PlayerService {
    Player getPlayer(int id);

    List<Player> getPlayers();

    List<Player> getPlayersByTeam(int id);

    Player savePlayer(PlayerInput player);
}
