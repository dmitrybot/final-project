package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.ProductDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.UserDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.Base;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.Details;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.New;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.Owner;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.SubscribedUser;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.DBExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.security.Role;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.security.SecurityUser;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.service.interfaces.IUserService;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.util.IMappingUtils;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private IUserService userService;
    private IMappingUtils mappingUtils;

    public UserController(IUserService userService, IMappingUtils mappingUtils) {
        this.userService = userService;
        this.mappingUtils = mappingUtils;
    }

    @JsonView(Details.class)
    @PutMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO updateUser(@Validated(Details.class) @RequestBody UserDTO user) throws DBExeption {
        return userService.updateUser(mappingUtils.mapToUserEntity(user));
    }

    @JsonView(Base.class)
    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO createUser(@Validated(New.class) @RequestBody UserDTO user) throws DBExeption {
        user.setRole(Role.USER);
        return userService.createUser(mappingUtils.mapToUserEntity(user));
    }

    @JsonView(Base.class)
    @PostMapping(value = "/admin", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO createAdmin(@Validated(New.class) @RequestBody UserDTO user) throws DBExeption {
        user.setRole(Role.ADMIN);
        return userService.createUser(mappingUtils.mapToUserEntity(user));
    }

    @JsonView(Details.class)
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO getUser(@PathVariable("id") long id) throws DBExeption {
        return userService.getUser(id);
    }

    @JsonView(Owner.class)
    @GetMapping(value = "/createdproducts", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ProductDTO> getUserCreatedProducts(@AuthenticationPrincipal SecurityUser userDetails) throws DBExeption {
        return userService.getUserCreatedProducts(userDetails.getId());
    }

    @JsonView(SubscribedUser.class)
    @GetMapping(value = "/subscribedusers", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDTO> getSubscribedUsers(@AuthenticationPrincipal SecurityUser userDetails) throws DBExeption {
        return userService.getSubscribedUsers(userDetails.getId());
    }

    @PostMapping(value = "/donation/{lesson_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean makeDonation(@AuthenticationPrincipal SecurityUser userDetails,
                                @PathVariable("lesson_id") long lessonId,
                                @RequestParam(name = "cost") double cost) throws DBExeption {
        return userService.makeDonation(userDetails.getId(), lessonId, cost);
    }

    @PostMapping(value = "/buy/{product_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean buyProduct(@AuthenticationPrincipal SecurityUser userDetails,
                              @PathVariable("product_id") long productId,
                              @RequestParam(name = "cost") double cost) throws DBExeption {
        return userService.buyProduct(userDetails.getId(), productId, cost);
    }

    @PatchMapping(value = "/subscribe/course/{course_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean subscribeCourse(@AuthenticationPrincipal SecurityUser userDetails,
                                   @PathVariable("course_id") long courseId) throws DBExeption {
        return userService.subscribeCourse(userDetails.getId(), courseId);
    }

    @PatchMapping(value = "/subscribe/lesson/{lesson_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean subscribeLesson(@AuthenticationPrincipal SecurityUser userDetails,
                                   @PathVariable("lesson_id") long lessonId) throws DBExeption {
        return userService.subscribeLesson(userDetails.getId(), lessonId);
    }
}
