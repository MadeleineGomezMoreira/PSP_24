package com.teams.graphql_server_teams.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Team {

    private int id;
    private String name;
    private List<Player> players;

}
