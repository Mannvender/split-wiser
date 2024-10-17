package in.mannvender.splitwise.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity(name = "groupss")
public class Group extends BaseModel {
    private String name;
    private String description;
    @ManyToOne
    private User createdBy;
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroupRole> groupRoles;
    @ManyToMany
    private List<Expense> expenses;
}
