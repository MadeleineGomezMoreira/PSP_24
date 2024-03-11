package com.teams.graphql_server_teams.data.repositories.impl;

import com.teams.graphql_server_teams.data.local.StaticLists;
import com.teams.graphql_server_teams.data.model.Team;
import com.teams.graphql_server_teams.data.repositories.TeamRepository;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class TeamRepositoryImpl implements TeamRepository {

    @Override
    public List<Team> getAll() {
        return StaticLists.teamList;
    }

    @Override
    public Team getById(int id) {
        return StaticLists.teamList.stream().filter(team -> team.getId() == id).findFirst().orElse(null);
    }

    @Override
    public Team save(Team team) {
        StaticLists.teamList.add(team);
        return team;
    }

}
