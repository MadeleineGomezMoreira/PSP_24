package com.teams.graphql_server_teams.ui;

import com.teams.graphql_server_teams.data.model.Player;
import com.teams.graphql_server_teams.domain.model.input.PlayerInput;
import com.teams.graphql_server_teams.domain.services.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService service;

    @QueryMapping
    public List<Player> getPlayers() {
        return service.getPlayers();
    }

//    @SchemaMapping(typeName = "Team")
//    public List<Player> getPlayersByTeam(int id) {
//        return service.getPlayersByTeam(id);
//    }

    @QueryMapping
    public Player getPlayer(@Argument int id) {
        return service.getPlayer(id);
    }


    //TODO: fix --> PlayerInput is retrieved with null name and teamId/id = 0 --> WHY?
    @MutationMapping
    public Player savePlayer(@Argument PlayerInput player) {
        return service.savePlayer(player);
    }
}
