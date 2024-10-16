package in.mannvender.splitwise.controllers;

import in.mannvender.splitwise.dtos.auth.*;
import in.mannvender.splitwise.dtos.user.UserResponseDto;
import in.mannvender.splitwise.models.User;
import in.mannvender.splitwise.services.interfaces.IAuthService;
import in.mannvender.splitwise.services.interfaces.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
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
    public ResponseEntity<AuthSignupResponseDto> signUp(@Valid @RequestBody AuthSignupRequestDto requestDto) {
        try {
            User createdUser = authService.register(requestDto.getName(), requestDto.getEmail(), requestDto.getPassword());
            AuthSignupResponseDto responseDto = convertToAuthSignupResponseDto(createdUser);
            return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthLoginResponseDto> login(@Valid @RequestBody AuthLoginRequestDto requestDto) {
        Pair<User, String> userTokenPair = authService.login(requestDto.getEmail(), requestDto.getPassword());
        User user = userTokenPair.getFirst();
        String token = userTokenPair.getSecond();

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        ResponseCookie cookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .secure(false)
                .maxAge(60 * 60 * 24 * 7) // 1 week
                .path("/")
                .build();

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());

        AuthLoginResponseDto responseDto = convertToAuthLoginResponseDto(user);
        return new ResponseEntity<>(responseDto, headers, HttpStatus.OK);
    }

    @PostMapping("/validate-token")
    public ResponseEntity<AuthValidateTokenResponseDto> validateToken(@Valid @RequestBody AuthValidateTokenRequestDto requestDto) {
        boolean isValid = authService.validateToken(requestDto.getToken(), requestDto.getUserId());
        AuthValidateTokenResponseDto responseDto = new AuthValidateTokenResponseDto();
        responseDto.setTokenValid(isValid);
        responseDto.setUserId(requestDto.getUserId());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
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