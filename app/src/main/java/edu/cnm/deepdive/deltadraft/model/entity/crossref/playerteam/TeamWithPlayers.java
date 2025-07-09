package edu.cnm.deepdive.deltadraft.model.entity.crossref.playerteam;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;
import edu.cnm.deepdive.deltadraft.model.entity.Player;
import edu.cnm.deepdive.deltadraft.model.entity.Team;
import java.util.List;

public class TeamWithPlayers {
  @Embedded
  public Team team;

  @Relation(parentColumn = "team_id",
      entityColumn = "player_id",
      associateBy = @Junction(PlayerTeamCrossRef.class))

  public List<Player> players;


}
