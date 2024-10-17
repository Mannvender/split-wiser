package in.mannvender.splitwise.dtos.group;

import in.mannvender.splitwise.dtos.user.UserResponseDto;
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
    private List<UserResponseDto> members;
    private List<UserResponseDto> admins;
}
