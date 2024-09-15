package in.mannvender.splitwise.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Group extends BaseModel {
    private String name;
    private String description;
    private User createdBy;
    private List<User> members;
    private List<Expense> expenses;
}
