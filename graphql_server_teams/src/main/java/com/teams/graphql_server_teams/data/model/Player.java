package com.teams.graphql_server_teams.data.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Player {
    private int id;
    private String name;
    private int teamId;
}
