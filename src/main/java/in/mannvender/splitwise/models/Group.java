package in.mannvender.splitwise.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
    @ManyToMany
    private List<User> members;
    @ManyToMany
    private List<Expense> expenses;
}
