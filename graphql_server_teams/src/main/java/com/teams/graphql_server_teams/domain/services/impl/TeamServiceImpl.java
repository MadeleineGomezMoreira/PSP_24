package com.teams.graphql_server_teams.domain.services.impl;

import com.teams.graphql_server_teams.data.model.Team;
import com.teams.graphql_server_teams.data.repositories.TeamRepository;
import com.teams.graphql_server_teams.domain.model.error.NotFoundException;
import com.teams.graphql_server_teams.domain.services.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository repository;

    @Override
    public Team getTeam(int id){
        Team team = repository.getById(id);
        if(team == null){
            throw new NotFoundException("Player not found");
        } else {
            return team;
        }
    }

    @Override
    public List<Team> getTeams(){
        return repository.getAll();
    }
}
