package com.teams.graphql_server_teams.domain.services;

import com.teams.graphql_server_teams.data.model.Team;

import java.util.List;

public interface TeamService {
    Team getTeam(int id);

    List<Team> getTeams();
}
