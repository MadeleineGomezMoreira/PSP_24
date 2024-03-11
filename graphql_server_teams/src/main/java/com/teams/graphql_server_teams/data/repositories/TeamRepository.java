package com.teams.graphql_server_teams.data.repositories;

import com.teams.graphql_server_teams.data.model.Team;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TeamRepository {
    List<Team> getAll();

    Team getById(int id);

    Team save(Team team);
}
