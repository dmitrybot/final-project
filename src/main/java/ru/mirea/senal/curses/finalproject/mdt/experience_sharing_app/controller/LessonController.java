package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.controller;

import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.constants.Status;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.LessonDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.LessonInfoDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.ProductDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.NewLessonInfo;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.NewLessonInfoWithoutCourse;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.StartLessonForCourse;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.UpdateLessonInfo;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.StartLesson;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.UpdateLesson;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.LessonInfoEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.CantDeleteObjectExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.DBExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.IsAlreadyStartedExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.security.SecurityUser;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.service.interfaces.ILessonInfoService;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.service.interfaces.ILessonService;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.service.interfaces.IProductInfoService;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.util.IMappingUtils;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/lesson")
public class LessonController {
    private ILessonInfoService lessonInfoService;
    private ILessonService lessonService;
    private IMappingUtils mappingUtils;
    private IProductInfoService productInfoService;

    public LessonController(ILessonInfoService lessonInfoService, ILessonService lessonService, IMappingUtils mappingUtils, IProductInfoService productInfoService) {
        this.lessonInfoService = lessonInfoService;
        this.lessonService = lessonService;
        this.mappingUtils = mappingUtils;
        this.productInfoService = productInfoService;
    }

    @PostMapping(value = "/create/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public LessonInfoDTO createLessonForCourse(@PathVariable("id") long id,
                                               @Validated(NewLessonInfo.class) @RequestBody ProductDTO productDTO) throws DBExeption {
        LessonInfoEntity lessonInfoEntity = new LessonInfoEntity();
        lessonInfoEntity.setProductInfoEntity(mappingUtils.mapToProductInfoEntity(productDTO));
        return lessonInfoService.createLessonForCourse(id, lessonInfoEntity);
    }

    @PostMapping(value = "/create/list/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean createLessonsForCourse(@PathVariable("id") long id,
                                          @Validated(NewLessonInfo.class) @RequestBody List<ProductDTO> products) throws DBExeption {
        List<LessonInfoEntity> lessons = new ArrayList<>();
        for (ProductDTO p: products) {
            LessonInfoEntity l = new LessonInfoEntity();
            l.setProductInfoEntity(mappingUtils.mapToProductInfoEntity(p));
            lessons.add(l);
        }
        return lessonInfoService.createLessonsForCourse(id, lessons);
    }

    @PostMapping(value = "/create/solo", produces = MediaType.APPLICATION_JSON_VALUE)
    public LessonInfoDTO createLesson(@AuthenticationPrincipal SecurityUser userDetails,
                                      @Validated(NewLessonInfoWithoutCourse.class) @RequestBody ProductDTO productDTO) throws DBExeption {
        LessonInfoEntity lessonInfoEntity = new LessonInfoEntity();
        lessonInfoEntity.setProductInfoEntity(mappingUtils.mapToProductInfoEntity(productDTO));
        return lessonInfoService.createLesson(lessonInfoEntity, userDetails.getId());
    }

    @PatchMapping(value = "/info/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public LessonInfoDTO updateLessonInfo(@PathVariable("id") long id,
                                          @Validated(UpdateLessonInfo.class) @RequestBody ProductDTO productDTO) throws DBExeption {
        return lessonInfoService.updateLesson(mappingUtils.mapToProductInfoEntity(productDTO), id);
    }

    @DeleteMapping(value = "/info/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean deleteLessonInfo(@AuthenticationPrincipal SecurityUser userDetails,
                                    @PathVariable("id") long id) throws CantDeleteObjectExeption, DBExeption {
        return lessonInfoService.deleteLesson(id, userDetails.getId());
    }

    @PostMapping(value = "/start/{courseid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public LessonDTO startLessonForCourse(@PathVariable("courseid") long id,
                                          @Validated(StartLessonForCourse.class) @RequestBody LessonDTO lesson) throws DBExeption {
        return lessonService.startLessonForCourse(id, mappingUtils.mapToLessonEntity(lesson), lesson.getLessonInfoId());
    }

    @PostMapping(value = "/start/solo", produces = MediaType.APPLICATION_JSON_VALUE)
    public LessonDTO startLesson(@Validated(StartLesson.class) @RequestBody LessonDTO lesson) throws DBExeption {
        return lessonService.startLesson(mappingUtils.mapToLessonEntity(lesson), lesson.getLessonInfoId());
    }

    @PatchMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public LessonDTO updateLesson(@Validated(UpdateLesson.class) @RequestBody LessonDTO lesson) throws DBExeption, IsAlreadyStartedExeption {
        return lessonService.updateLesson(mappingUtils.mapToLessonEntity(lesson));
    }

    @PatchMapping(value = "/status/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean updateLessonStatus(@PathVariable("id") long id, @RequestParam(name = "status") Status status) throws DBExeption, IsAlreadyStartedExeption {
        return lessonService.updateLessonStatus(id, status);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean deleteLesson(@PathVariable("id") long id) throws CantDeleteObjectExeption, DBExeption {
        return lessonService.deleteLesson(id);
    }
}
