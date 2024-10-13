package in.mannvender.splitwise.controllers;

import in.mannvender.splitwise.config.UserContext;
import in.mannvender.splitwise.dtos.user.UserRequestDto;
import in.mannvender.splitwise.dtos.user.UserResponseDto;
import in.mannvender.splitwise.models.User;
import in.mannvender.splitwise.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;

    @GetMapping("/all")
    public List<UserResponseDto> getAllUsers(){
        return null;
    }

    @GetMapping("/me")
    public UserResponseDto getLoggedInUser(){
        // return user from context
        User user = UserContext.getUser();
        if(user == null){
            throw new RuntimeException("UserContext not found");
        }
        return convertToUserResponseDto(user);
    }

    @GetMapping("/{userId}")
    public UserResponseDto getUserById(@PathVariable("userId") Long userId){
        if(userId == null){
            throw new RuntimeException("User Id cannot be null");
        }
        Optional<User> optionalUser = userService.getUserById(userId);
        if(optionalUser.isEmpty()){
            throw new RuntimeException("User not found");
        }
        return convertToUserResponseDto(optionalUser.get());
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
