package in.mannvender.splitwise.controllers;

import ch.qos.logback.core.util.StringUtil;
import in.mannvender.splitwise.dtos.group.GroupRequestDto;
import in.mannvender.splitwise.dtos.group.GroupResponseDto;
import in.mannvender.splitwise.models.BaseModel;
import in.mannvender.splitwise.models.Group;
import in.mannvender.splitwise.models.User;
import in.mannvender.splitwise.services.interfaces.IGroupService;
import in.mannvender.splitwise.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/group")
public class GroupController {
    @Autowired
    private IGroupService groupService;
    @Autowired
    private IUserService userService;

    @PostMapping
    public GroupResponseDto createGroup(@RequestBody GroupRequestDto requestDto){
        if(requestDto == null){
            throw new RuntimeException("Group cannot be null");
        }
        if(requestDto.getName() == null || requestDto.getName().isEmpty()){
            throw new RuntimeException("Group name cannot be null or empty");
        }
        if(requestDto.getCreatedByUserId() == null){
            throw new RuntimeException("Created by user id cannot be null");
        }
        // validate all member ids
        if(requestDto.getMemberIds() == null){
            throw new RuntimeException("Member ids cannot be null");
        }
        for(Long memberId: requestDto.getMemberIds()){
            if(StringUtil.isNullOrEmpty(memberId.toString())){
                throw new RuntimeException("Member id cannot be null or empty");
            }
        }
        List<User> members = userService.getUsersByIds(requestDto.getMemberIds());
        Optional<User> optionalCreatedBy = userService.getUserById(requestDto.getCreatedByUserId());
        if(optionalCreatedBy.isEmpty()){
            throw new RuntimeException("Created by user not found");
        }

        Group group = groupService.createGroup(requestDto.getName(), requestDto.getDescription(), optionalCreatedBy.get(), members);
        return mapToGroupResponseDto(group);
    }

    @GetMapping("/{groupId}")
    public GroupResponseDto getGroupById(@PathVariable("groupId") Long groupId){
        if(groupId == null){
            throw new RuntimeException("Group Id cannot be null");
        }
        Optional<Group> optionalGroup = groupService.getGroupById(groupId);
        if(optionalGroup.isEmpty()){
            throw new RuntimeException("Group not found");
        }
        return mapToGroupResponseDto(optionalGroup.get());
    }

    @DeleteMapping("/{groupId}")
    public void deleteGroup(@PathVariable("groupId") Long groupId){
        if(groupId == null){
            throw new RuntimeException("Group Id cannot be null");
        }
        groupService.deleteGroup(groupId);
    }

    private GroupResponseDto mapToGroupResponseDto(Group group){
        GroupResponseDto responseDto = new GroupResponseDto();
        responseDto.setId(group.getId());
        responseDto.setName(group.getName());
        responseDto.setDescription(group.getDescription());
        responseDto.setCreatedByUserId(group.getCreatedBy().getId());
        responseDto.setMemberIds(group.getMembers().stream().map(BaseModel::getId).toArray(Long[]::new));
        return responseDto;
    }

}
