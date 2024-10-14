package in.mannvender.splitwise.dtos.group;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class GroupRequestDto {
    private String name;
    private String description;
    private Long createdByUserId;
    private List<Long> memberIds;
    private List<Long> adminIds = new ArrayList<>();
}
