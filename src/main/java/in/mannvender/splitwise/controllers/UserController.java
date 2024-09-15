package in.mannvender.splitwise.controllers;

import in.mannvender.splitwise.dtos.user.UserRequestDto;
import in.mannvender.splitwise.dtos.user.UserResponseDto;
import in.mannvender.splitwise.models.User;
import in.mannvender.splitwise.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;

    @GetMapping("/all")
    public List<UserResponseDto> getAllUsers(){
        return null;
    }

    @GetMapping("/{userId}")
    public UserResponseDto getUserById(@PathVariable("userId") Long userId){
        if(userId == null){
            throw new RuntimeException("User Id cannot be null");
        }
        User user = userService.getUserById(userId);
        if(user == null){
            throw new RuntimeException("User not found");
        }
        return convertToUserResponseDto(user);
    }

//    @PostMapping("/")
//    public UserResponseDto createUser(@RequestBody UserRequestDto requestDto){
//        if(requestDto == null){
//            throw new RuntimeException("User cannot be null");
//        }
//        User user = convertToUser(requestDto);
//        User createdUser = userService.createUser(user);
//        return convertToUserResponseDto(createdUser);
//    }

    public UserResponseDto convertToUserResponseDto(User user){
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(user.getId());
        responseDto.setName(user.getName());
        responseDto.setEmail(user.getEmail());
        return responseDto;
    }

    public User convertToUser(UserRequestDto requestDto){
        User user = new User();
        user.setName(requestDto.getName());
        user.setEmail(requestDto.getEmail());
        return user;
    }
}
