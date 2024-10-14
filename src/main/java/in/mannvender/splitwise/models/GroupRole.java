package in.mannvender.splitwise.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "group_roles")
public class GroupRole extends BaseModel {
   @ManyToOne
   private User user;
   @ManyToOne
    private Group group;
   @Enumerated(EnumType.STRING)
    private RoleType roleType;
}
