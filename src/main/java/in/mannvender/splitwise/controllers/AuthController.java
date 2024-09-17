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
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
    public ResponseEntity<AuthSignupResponseDto> signUp(@RequestBody AuthSignupRequestDto requestDto) {
        try {
            if (requestDto == null ||
                    requestDto.getName() == null ||
                    requestDto.getEmail() == null ||
                    requestDto.getPassword() == null ||
                    requestDto.getName().isEmpty() ||
                    requestDto.getEmail().isEmpty() ||
                    requestDto.getPassword().isEmpty()
            ) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            User createdUser = authService.register(requestDto.getName(), requestDto.getEmail(), requestDto.getPassword());
            AuthSignupResponseDto responseDto = convertToAuthSignupResponseDto(createdUser);
            ResponseEntity<AuthSignupResponseDto> responseEntity = new ResponseEntity<>(responseDto, HttpStatus.CREATED);
            return responseEntity;

        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthLoginResponseDto> login(@RequestBody AuthLoginRequestDto requestDto) {
        if (requestDto == null || requestDto.getEmail() == null || requestDto.getPassword() == null || requestDto.getEmail().isEmpty() || requestDto.getPassword().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Pair<User, String> userTokenPair = authService.login(requestDto.getEmail(), requestDto.getPassword());
        User user = userTokenPair.getFirst();
        String token = userTokenPair.getSecond();
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.SET_COOKIE, token);

        if(user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        AuthLoginResponseDto responseDto = convertToAuthLoginResponseDto(user);
        ResponseEntity<AuthLoginResponseDto> responseEntity = new ResponseEntity<>(responseDto, headers, HttpStatus.OK);
        return responseEntity;
    }

    private AuthLoginResponseDto convertToAuthLoginResponseDto(User user) {
        AuthLoginResponseDto responseDto = new AuthLoginResponseDto();
        responseDto.setId(user.getId());
        responseDto.setName(user.getName());
        responseDto.setEmail(user.getEmail());
        return responseDto;
    }

    private AuthSignupResponseDto convertToAuthSignupResponseDto(User user) {
        AuthSignupResponseDto responseDto = new AuthSignupResponseDto();
        responseDto.setId(user.getId());
        responseDto.setName(user.getName());
        responseDto.setEmail(user.getEmail());
        return responseDto;
    }

    private User convertAuthSignupRequestDtoToUser(AuthSignupRequestDto responseDto) {
        User user = new User();
        user.setName(responseDto.getName());
        user.setEmail(responseDto.getEmail());
        user.setPassword(responseDto.getPassword());
        return user;
    }

    private UserResponseDto convertToUserResponseDto(User user) {
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(user.getId());
        responseDto.setName(user.getName());
        responseDto.setEmail(user.getEmail());
        return responseDto;
    }
}
