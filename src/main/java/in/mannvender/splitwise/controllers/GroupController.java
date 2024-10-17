package in.mannvender.splitwise.controllers;

import ch.qos.logback.core.util.StringUtil;
import in.mannvender.splitwise.config.UserContext;
import in.mannvender.splitwise.dtos.group.GroupRequestDto;
import in.mannvender.splitwise.dtos.group.GroupResponseDto;
import in.mannvender.splitwise.models.*;
import in.mannvender.splitwise.repositories.GroupRepo;
import in.mannvender.splitwise.services.interfaces.IGroupService;
import in.mannvender.splitwise.services.interfaces.IUserService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/group")
public class GroupController {
    private final Logger logger = org.slf4j.LoggerFactory.getLogger(GroupController.class);
    @Autowired
    private IGroupService groupService;
    @Autowired
    private IUserService userService;
    @Autowired
    private GroupRepo groupRepo;

    @PostMapping
    public GroupResponseDto createGroup(@RequestBody GroupRequestDto requestDto){
        // validate requestDto
        if(requestDto == null){
            throw new RuntimeException("Group cannot be null");
        }
        // validate name
        if(requestDto.getName() == null || requestDto.getName().isEmpty()){
            throw new RuntimeException("Group name cannot be null or empty");
        }
        // validate createdByUserId
        if(requestDto.getCreatedByUserId() == null){
            throw new RuntimeException("Created by user id cannot be null");
        }
        // validate createdByUserId is same as User in UserContext
        if(UserContext.getUser().getId() != requestDto.getCreatedByUserId()){
            throw new RuntimeException("Created by user id should be same as logged in user id");
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
        // validate all admin ids: admin ids can be null or empty list
        if(requestDto.getAdminIds() != null){
            for(Long adminId: requestDto.getAdminIds()){
                if(StringUtil.isNullOrEmpty(adminId.toString())){
                    throw new RuntimeException("Admin id cannot be null or empty");
                }
            }
        }
        List<User> members = userService.getUsersByIds(requestDto.getMemberIds());
        // make sure all members are found
        if(members.size() != requestDto.getMemberIds().size()){
            throw new RuntimeException("Some members not found");
        }

        List<User> admins = userService.getUsersByIds(requestDto.getAdminIds());
        // make sure all admins are found
        if(admins.size() != requestDto.getAdminIds().size()){
            throw new RuntimeException("Some admins not found");
        }

        // make sure createdBy user is found
        Optional<User> optionalCreatedBy = userService.getUserById(requestDto.getCreatedByUserId());
        if(optionalCreatedBy.isEmpty()){
            throw new RuntimeException("Created by user not found");
        }

        // map members and admins to groupRoles
        List<GroupRole> groupRoles = new ArrayList<>();
        for(User member: members){
            GroupRole groupRole = new GroupRole();
            groupRole.setUser(member);
            groupRole.setRoleType(RoleType.MEMBER);
            groupRoles.add(groupRole);
        }
        for(User admin: admins){
            GroupRole groupRole = new GroupRole();
            groupRole.setUser(admin);
            groupRole.setRoleType(RoleType.ADMIN);
            groupRoles.add(groupRole);
        }
        // creator is admin as well
        GroupRole groupRole = new GroupRole();
        groupRole.setUser(optionalCreatedBy.get());
        groupRole.setRoleType(RoleType.ADMIN);
        groupRoles.add(groupRole);

        Group group = groupService.createGroup(requestDto.getName(), requestDto.getDescription(), optionalCreatedBy.get(), groupRoles);
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

    // get mapping for getting all groups of a user
    @GetMapping("/me")
    public List<GroupResponseDto> getGroupsByUserId(){
        List<Group> groups = groupService.getGroupsByLoggedInUser();
        logger.info("Groups: {}", groups);

        return groups.stream().map(this::mapToGroupResponseDto).toList();
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
        // extract memberIds from GroupRoles
        responseDto.setMemberIds(group.getGroupRoles().stream().filter(groupRole -> groupRole.getRoleType() == RoleType.MEMBER).map(groupRole -> groupRole.getUser().getId()).toList());
        // extract adminIds from GroupRoles
        responseDto.setAdminIds(group.getGroupRoles().stream().filter(groupRole -> groupRole.getRoleType() == RoleType.ADMIN).map(groupRole -> groupRole.getUser().getId()).toList());
        return responseDto;
    }

}
