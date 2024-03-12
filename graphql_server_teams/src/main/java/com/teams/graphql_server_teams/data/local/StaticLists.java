package com.teams.graphql_server_teams.data.local;

import com.teams.graphql_server_teams.data.model.Player;
import com.teams.graphql_server_teams.data.model.Team;

import java.util.ArrayList;
import java.util.List;

public class StaticLists {

    public static final List<Team> teamList = new ArrayList<>();
    public static final List<Player> playerList = new ArrayList<>();

    static {
        teamList.add(
                new Team(1, "Manchester United", new ArrayList<>(List.of(
                        new Player(101, "Cristiano Ronaldo", 1),
                        new Player(102, "Bruno Fernandes", 1),
                        new Player(103, "Marcus Rashford", 1)
                ))));
        teamList.add(
                new Team(2, "Barcelona", new ArrayList<>(List.of(
                        new Player(201, "Lionel Messi", 2),
                        new Player(202, "Sergio Busquets", 2),
                        new Player(203, "Ansu Fati", 2)
                ))));
        teamList.add(
                new Team(3, "Real Madrid", new ArrayList<>(List.of(
                        new Player(301, "Karim Benzema", 3),
                        new Player(302, "Luka Modric", 3),
                        new Player(303, "Eder Militao", 3)
                ))));

        playerList.add(new Player(101, "Cristiano Ronaldo", 1));
        playerList.add(new Player(102, "Bruno Fernandes", 1));
        playerList.add(new Player(103, "Marcus Rashford", 1));
        playerList.add(new Player(201, "Lionel Messi", 2));
        playerList.add(new Player(202, "Sergio Busquets", 2));
        playerList.add(new Player(203, "Ansu Fati", 2));
        playerList.add(new Player(301, "Karim Benzema", 3));
        playerList.add(new Player(302, "Luka Modric", 3));
        playerList.add(new Player(303, "Eder Militao", 3));
    }

    private StaticLists() {
    }

}
