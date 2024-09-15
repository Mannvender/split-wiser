package in.mannvender.splitwise.controllers;

import in.mannvender.splitwise.dtos.auth.AuthLoginRequestDto;
import in.mannvender.splitwise.dtos.auth.AuthLoginResponseDto;
import in.mannvender.splitwise.dtos.auth.AuthSignupRequestDto;
import in.mannvender.splitwise.dtos.auth.AuthSignupResponseDto;
import in.mannvender.splitwise.dtos.user.UserResponseDto;
import in.mannvender.splitwise.models.User;
import in.mannvender.splitwise.services.interfaces.IAuthService;
import in.mannvender.splitwise.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IAuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthSignupResponseDto> signUp(@RequestBody AuthSignupRequestDto requestDto){
        if(requestDto == null || requestDto.getName() == null || requestDto.getEmail() == null || requestDto.getPassword() == null){
            throw new RuntimeException("User cannot be null");
        }
        User user = convertAuthSignupRequestDtoToUser(requestDto);
        User createdUser = userService.createUser(user);
        AuthSignupResponseDto responseDto = convertToAuthSignupResponseDto(createdUser);
        ResponseEntity<AuthSignupResponseDto> responseEntity = new ResponseEntity<>(responseDto, HttpStatus.CREATED);
        return responseEntity;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthLoginResponseDto> login(@RequestBody AuthLoginRequestDto requestDto){
        if(requestDto == null || requestDto.getEmail() == null || requestDto.getPassword() == null){
            throw new RuntimeException("User cannot be null");
        }
        User user = userService.getUserByEmail(requestDto.getEmail());
        AuthLoginResponseDto responseDto = convertToAuthLoginResponseDto(user);
        ResponseEntity<AuthLoginResponseDto> responseEntity = new ResponseEntity<>(responseDto, HttpStatus.OK);
        return responseEntity;
    }

    private AuthLoginResponseDto convertToAuthLoginResponseDto(User user){
        AuthLoginResponseDto responseDto = new AuthLoginResponseDto();
        responseDto.setId(user.getId());
        responseDto.setName(user.getName());
        responseDto.setEmail(user.getEmail());
        return responseDto;
    }

    private AuthSignupResponseDto convertToAuthSignupResponseDto(User user){
        AuthSignupResponseDto responseDto = new AuthSignupResponseDto();
        responseDto.setId(user.getId());
        responseDto.setName(user.getName());
        responseDto.setEmail(user.getEmail());
        return responseDto;
    }

    private User convertAuthSignupRequestDtoToUser(AuthSignupRequestDto responseDto){
        User user = new User();
        user.setName(responseDto.getName());
        user.setEmail(responseDto.getEmail());
        user.setPassword(responseDto.getPassword());
        return user;
    }

    private UserResponseDto convertToUserResponseDto(User user){
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(user.getId());
        responseDto.setName(user.getName());
        responseDto.setEmail(user.getEmail());
        return responseDto;
    }
}
