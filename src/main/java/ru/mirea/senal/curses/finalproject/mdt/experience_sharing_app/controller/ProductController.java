package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.controller;

import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.FiltrationProductDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.ProductDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.ReportDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.TagDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.transfer.New;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.ReportEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.DBExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.service.interfaces.IProductInfoService;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.util.IMappingUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/product")
public class ProductController {
    private IMappingUtils mappingUtils;
    private IProductInfoService productInfoService;

    public ProductController(IMappingUtils mappingUtils, IProductInfoService productInfoService) {
        this.mappingUtils = mappingUtils;
        this.productInfoService = productInfoService;
    }

    @GetMapping(value = "/report/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ReportDTO> getReports(@PathVariable("id") long id) throws DBExeption {
        List<ReportDTO> reports = productInfoService.getReports(id);
        return reports;
    }

    @PostMapping(value = "/report/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean addReport(@PathVariable("id") long id, @Validated(New.class) @RequestBody ReportDTO report) throws DBExeption {
        ReportEntity r = mappingUtils.mapToReportEntity(report);
        return productInfoService.addReport(id, r, report.getAuthor_id());
    }

    @PatchMapping(value = "/tag/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean updateProductTags(@PathVariable("id") long id, @RequestBody Set<TagDTO> tags) throws DBExeption {
        return productInfoService.updateProductTags(tags
                .stream()
                .map(o -> mappingUtils.mapToTagEntity(o))
                .collect(Collectors.toSet()), id);
    }

    @GetMapping(value = "/filtration", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ProductDTO> getFiltrationProducts(@Validated @RequestBody FiltrationProductDTO filtrationProductDTO) throws DBExeption {
        return productInfoService.getFiltrationProducts(filtrationProductDTO);
    }
}
