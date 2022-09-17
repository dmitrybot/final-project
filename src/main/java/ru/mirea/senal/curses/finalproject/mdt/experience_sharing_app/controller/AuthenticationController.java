package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.AuthenticationRequestDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.UserEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.DBExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.InvalidPasswordExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.security.JwtTokenProvider;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.service.interfaces.IUserService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private IUserService userService;
    private JwtTokenProvider jwtTokenProvider;

    public AuthenticationController(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, IUserService userService, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@Validated @RequestBody AuthenticationRequestDTO request) throws InvalidPasswordExeption {
        try {
            //authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            UserEntity user = userService.findByEmail(request.getEmail());
            passwordCheck(user.getPassword(), request);
            String token = jwtTokenProvider.createToken(request.getEmail(), user.getRole());
            Map<Object, Object> response = new HashMap<>();
            response.put("email", request.getEmail());
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException | DBExeption e) {
            e.printStackTrace();
            return new ResponseEntity<>("Invalid email/password combination", HttpStatus.FORBIDDEN);
        } catch (Exception exeption) {
            exeption.printStackTrace();
            throw exeption;
        }
    }

    private void passwordCheck(String password, AuthenticationRequestDTO request) throws InvalidPasswordExeption {
        if (!passwordEncoder.matches(request.getPassword(), password)) {
            throw new InvalidPasswordExeption();
        }
    }
}