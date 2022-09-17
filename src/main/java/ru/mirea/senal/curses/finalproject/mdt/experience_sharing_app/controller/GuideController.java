package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.DirectionDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.SectionDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.TagDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.Details;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.New;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.NewSection;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.CantDeleteObjectExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.DBExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.service.interfaces.IDirectionService;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.service.interfaces.ISectionService;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.service.interfaces.ITagService;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.util.IMappingUtils;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/guide")
public class GuideController {
    private IMappingUtils mappingUtils;
    private ITagService tagService;
    private IDirectionService directionService;
    private ISectionService sectionService;

    public GuideController(IMappingUtils mappingUtils, ITagService tagService, IDirectionService directionService, ISectionService sectionService) {
        this.mappingUtils = mappingUtils;
        this.tagService = tagService;
        this.directionService = directionService;
        this.sectionService = sectionService;
    }

    @JsonView(Details.class)
    @GetMapping(value = "/tag", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TagDTO> getTags() throws DBExeption {
        return tagService.getTags();
    }

    @JsonView(Details.class)
    @GetMapping(value = "/section", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SectionDTO> getSections() throws DBExeption {
        return sectionService.getSections();
    }

    @JsonView(Details.class)
    @GetMapping(value = "/direction/section/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SectionDTO> getDirectionSections(@PathVariable("id") long id) throws DBExeption {
        return directionService.getSections(id);
    }

    @JsonView(Details.class)
    @GetMapping(value = "/direction", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DirectionDTO> getDirections() throws DBExeption {
        return directionService.getDirections();
    }

    @PostMapping(value = "/tag", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public TagDTO createTag(@Validated(New.class) @RequestBody TagDTO tag) throws DBExeption {
        return tagService.createTag(mappingUtils.mapToTagEntity(tag));
    }

    @PostMapping(value = "/direction/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public DirectionDTO createDirection(@PathVariable("id") long tagId) throws DBExeption {
        return directionService.createDirection(tagId);
    }

    @PostMapping(value = "/section", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public SectionDTO createSection(@Validated(NewSection.class) @RequestBody SectionDTO directionSectionDTO) throws DBExeption {
        SectionDTO sectionDTO = sectionService.createSection(mappingUtils.mapToSectionEntity(directionSectionDTO));
        sectionDTO = sectionService.updateSectionTags(directionSectionDTO.getTags()
                .stream()
                .map(o -> mappingUtils.mapToTagEntity(o))
                .collect(Collectors.toSet()), sectionDTO.getId());
        return sectionDTO;
    }

    @PutMapping (value = "/section/tags/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public SectionDTO addTagsToSection(@PathVariable("id") long id, @Validated(Details.class) @RequestBody List<TagDTO> tag) throws DBExeption {
        return sectionService.updateSectionTags(tag
                .stream()
                .map(o -> mappingUtils.mapToTagEntity(o))
                .collect(Collectors.toSet()), id);
    }

    @PutMapping (value = "/section/tag/{id}/{tag_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public SectionDTO addTagToSection(@PathVariable("id") long id, @PathVariable("tag_id") long tagId) throws DBExeption {
        return sectionService.addTagToSection(id, tagId);
    }

    @PutMapping (value = "/direction/section/{id}/{section_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean addSectionToDirection(@PathVariable("id") long id, @PathVariable("section_id") long sectionId) throws DBExeption {
        return directionService.addSectionToDirection(id, sectionId);
    }

    @PutMapping (value = "/direction/sections/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean addSectionsToDirection(@PathVariable("id") long id, @RequestBody List<SectionDTO> section) throws DBExeption {
        return directionService.addSectionsToDirection(id, section
                .stream()
                .map(o -> mappingUtils.mapToSectionEntity(o))
                .collect(Collectors.toSet()));
    }

    @DeleteMapping (value = "/tag/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean deleteTag(@PathVariable("id") long id) throws CantDeleteObjectExeption, DBExeption {
        return tagService.deleteTag(id);
    }

    @DeleteMapping (value = "/section/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean deleteSection(@PathVariable("id") long id) throws CantDeleteObjectExeption, DBExeption {
        return sectionService.deleteSection(id);
    }

    @DeleteMapping (value = "/direction/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean deleteDirection(@PathVariable("id") long id) throws CantDeleteObjectExeption, DBExeption {
        return directionService.deleteDirection(id);
    }
}
