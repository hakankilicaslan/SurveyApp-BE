package com.bilgeadam.banket.controller;

import com.bilgeadam.banket.dto.request.AddManagerRequestDto;
import com.bilgeadam.banket.dto.request.RemoveUserRequestDto;
import com.bilgeadam.banket.dto.request.SaveUserRequestDto;
import com.bilgeadam.banket.dto.request.UpdateUserRequestDto;
import com.bilgeadam.banket.entity.User;
import com.bilgeadam.banket.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bilgeadam.banket.constant.Endpoint.*;

@RestController
@RequestMapping(ROOT + USER)
@RequiredArgsConstructor
@CrossOrigin
public class UserController {

    private final UserService userService;

    // Volkan: Response dtolar kurguya göre belirlenecek.
    @PostMapping(SAVE)
    public ResponseEntity<String> saveUser(@RequestBody @Valid SaveUserRequestDto dto){
        return ResponseEntity.ok(userService.saveUser(dto));
    }

    @PutMapping(UPDATE)
    public ResponseEntity<String> updateUser(@RequestBody @Valid UpdateUserRequestDto dto) {
        return ResponseEntity.ok(userService.updateUser(dto));
    }

    @GetMapping(GET_BY_TOKEN)
    public ResponseEntity<User> getUser(@PathVariable String token) {
        return ResponseEntity.ok(userService.getUserByToken(token));
    }

    @GetMapping(GET_ALL)
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    // Volkan: soft-delete
    @DeleteMapping(DELETE_BY_TOKEN)
    public ResponseEntity<String> deleteUser(@PathVariable String token) {
        return ResponseEntity.ok(userService.deleteUser(token));
    }

    //Volkan: hard-delete
    @DeleteMapping(REMOVE)
    public ResponseEntity<String> removeUser(RemoveUserRequestDto dto) {
        return ResponseEntity.ok(userService.removeUser(dto));
    }


    // Volkan: Aşağıdaki endpointler dummy endpoint, security final haline geçerken
    // neyin nasıl olucağına dair fikir vermesi amaçlı durmasına karar verdim.
    @DeleteMapping("/test/dummy-endpoint/admin/{yourText}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String testAdminArea(@PathVariable String yourText) {
        return "This is admin area and your text: " + yourText;
    }

    @DeleteMapping("/test/dummy-endpoint/user/{yourText}")
    @PreAuthorize("hasAuthority('USER')")
    public String testUserArea(@PathVariable String yourText) {
        return "This is user area and your text: " + yourText;
    }

    @DeleteMapping("/test/dummy-endpoint/adminanduser/{yourText}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public String testAdminAndUserArea(@PathVariable String yourText) {
        return "This is admin and user area and your text: " + yourText;
    }

    @DeleteMapping("/test/dummy-endpoint/manager/{yourText}")
    @PreAuthorize("hasAuthority('MANAGER')")
    public String testManagerArea(@PathVariable String yourText) {
        return "This is manager area and your text: " + yourText;
    }

    @DeleteMapping("/test/dummy-endpoint/adminandmanager/{yourText}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public String testManagerAndAdminArea(@PathVariable String yourText) {
        return "This is admin and manager area and your text: " + yourText;
    }

    @PostMapping(ADD_MANAGER)
    public ResponseEntity<String> addManager(@RequestBody @Valid AddManagerRequestDto dto){
        return ResponseEntity.ok(userService.addManager(dto));
    }

}
