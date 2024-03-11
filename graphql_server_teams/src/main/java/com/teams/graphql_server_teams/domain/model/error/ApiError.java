package com.teams.graphql_server_teams.domain.model.error;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@ToString
public class ApiError {
    private String message;
}
