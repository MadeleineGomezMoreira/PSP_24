package com.teams.graphql_server_teams.domain.model.input;

import com.teams.graphql_server_teams.data.model.Player;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlayerMapper {
    Player toPlayer(PlayerInput playerInput);
}
