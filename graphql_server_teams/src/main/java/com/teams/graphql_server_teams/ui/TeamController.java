package com.teams.graphql_server_teams.ui;

import com.teams.graphql_server_teams.data.model.Team;
import com.teams.graphql_server_teams.domain.services.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TeamController {

    private final TeamService service;

    @QueryMapping
    public List<Team> getTeams() {
        return service.getTeams();
    }

    @QueryMapping
    public Team getTeam(@Argument int id) {
        return service.getTeam(id);
    }

}
