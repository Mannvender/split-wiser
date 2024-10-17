package in.mannvender.splitwise.dtos.group;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GroupResponseDto {
    private Long id;
    private String name;
    private String description;
    private Long createdByUserId;
    private List<Long> memberIds;
    private List<Long> adminIds;
}
