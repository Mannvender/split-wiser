package in.mannvender.splitwise.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class User extends BaseModel {
    private String name;
    private String email;
    private String password;
}
