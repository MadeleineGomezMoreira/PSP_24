package com.teams.graphql_server_teams.data.repositories.impl;

import com.teams.graphql_server_teams.data.local.StaticLists;
import com.teams.graphql_server_teams.data.model.Player;
import com.teams.graphql_server_teams.data.repositories.PlayerRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class PlayerRepositoryImpl implements PlayerRepository {

    private final List<Player> mutablePlayerList = new ArrayList<>(StaticLists.playerList);

    @Override
    public List<Player> getAll() {
        return StaticLists.playerList;
    }

    @Override
    public Player getById(int id) {
        return StaticLists.playerList.stream().filter(player -> player.getId() == id).findFirst().orElse(null);
    }

    @Override
    public List<Player> getAllByTeam(int id) {
        return StaticLists.playerList.stream().filter(player -> player.getTeamId() == id).toList();
    }

    @Override
    public Player save(Player player) {
        StaticLists.playerList.add(player);

        //find the team and add the player to the team
        StaticLists.teamList.stream().filter(team -> team.getId() == player.getTeamId()).findFirst().ifPresent(team -> team.getPlayers().add(player));

        return player;
    }
}
