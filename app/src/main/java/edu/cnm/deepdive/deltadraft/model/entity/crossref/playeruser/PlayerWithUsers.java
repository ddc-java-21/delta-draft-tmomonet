package edu.cnm.deepdive.deltadraft.model.entity.crossref.playeruser;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;
import edu.cnm.deepdive.deltadraft.model.entity.Player;
import edu.cnm.deepdive.deltadraft.model.entity.User;
import java.util.List;

public class PlayerWithUsers {
  @Embedded
  public Player player;

  @Relation(parentColumn = "player_id",
  entityColumn = "user_id",
  associateBy = @Junction(UserPlayerCrossRef.class))

  public List<User> users;

}
