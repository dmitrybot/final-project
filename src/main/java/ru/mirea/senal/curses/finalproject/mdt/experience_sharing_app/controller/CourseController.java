package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.constants.Status;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.CourseDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.CourseInfoDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.LessonScheduleDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.ProductDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.CourseUpdate;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.New;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.NewCourse;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.Update;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.UpdateCourse;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.CourseInfoEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.CantDeleteObjectExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.DBExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.IsAlreadyStartedExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.security.SecurityUser;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.service.interfaces.ICourseInfoService;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.service.interfaces.ICourseService;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.service.interfaces.IProductInfoService;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.util.IMappingUtils;

import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {

    private ICourseInfoService courseInfoService;
    private ICourseService courseService;
    private IMappingUtils mappingUtils;
    private IProductInfoService productInfoService;

    public CourseController(ICourseInfoService courseInfoService, ICourseService courseService, IMappingUtils mappingUtils, IProductInfoService productInfoService) {
        this.courseInfoService = courseInfoService;
        this.courseService = courseService;
        this.mappingUtils = mappingUtils;
        this.productInfoService = productInfoService;
    }

    @GetMapping(value = "/schedule/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<LessonScheduleDTO> getCourseSchedule(@PathVariable("id") long id) throws DBExeption {
        return courseService.getCourseSchedule(id);
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public CourseInfoDTO createCourse(@AuthenticationPrincipal SecurityUser userDetails, @Validated(NewCourse.class) @RequestBody ProductDTO productDTO) throws DBExeption {
        CourseInfoEntity course = new CourseInfoEntity();
        course.setProductInfoEntity(mappingUtils.mapToProductInfoEntity(productDTO));
        return courseInfoService.createCourse(course, userDetails.getId());
    }

    @JsonView(CourseUpdate.class)
    @PatchMapping(value = "/updateinfo/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CourseInfoDTO updateCourseInfo(@PathVariable("id") long id,
                                          @Validated(UpdateCourse.class) @RequestBody ProductDTO productDTO) throws DBExeption {
        return courseInfoService.updateCourse(mappingUtils.mapToProductInfoEntity(productDTO), id);
    }

    @DeleteMapping(value = "/info/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean deleteCourseInfo(@AuthenticationPrincipal SecurityUser userDetails,
                                    @PathVariable("id") long id) throws CantDeleteObjectExeption, DBExeption {
        return courseInfoService.deleteCourse(id, userDetails.getId());
    }

    @PostMapping(value = "/start/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CourseDTO startCourse(@PathVariable("id") long id, @Validated(New.class) @RequestBody CourseDTO course) throws DBExeption {
        return courseService.startCourse(id, mappingUtils.mapToCourseEntity(course));
    }

    @PatchMapping(value = "/updatecourse", produces = MediaType.APPLICATION_JSON_VALUE)
    public CourseDTO updateCourse(@Validated(Update.class) @RequestBody CourseDTO course) throws IsAlreadyStartedExeption, DBExeption {
        return courseService.updateCourse(mappingUtils.mapToCourseEntity(course));
    }

    @PatchMapping(value = "/update/status/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean updateCourseStatus(@PathVariable("id") long id, @RequestParam(name = "status") Status status) throws IsAlreadyStartedExeption, DBExeption {
        return courseService.updateCourseStatus(id, status);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean deleteCourse(@PathVariable("id") long id) throws CantDeleteObjectExeption, DBExeption {
        return courseService.deleteCourse(id);
    }
}
