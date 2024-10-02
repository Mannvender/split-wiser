package in.mannvender.splitwise.dtos.group;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupResponseDto {
    private Long id;
    private String name;
    private String description;
    private Long createdByUserId;
    private Long[] memberIds;
}
