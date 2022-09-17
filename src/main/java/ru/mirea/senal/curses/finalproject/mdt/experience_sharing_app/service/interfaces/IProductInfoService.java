package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.service.interfaces;

import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.FiltrationProductDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.ProductDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.ReportDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.ReportEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.TagEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.DBExeption;

import java.util.List;
import java.util.Set;

public interface IProductInfoService {
    List<ReportDTO> getReports(long id) throws DBExeption;
    Boolean addReport(long id, ReportEntity report, long userId) throws DBExeption;
    Boolean updateProductTags(Set<TagEntity> tags, long id) throws DBExeption;
    List<ProductDTO> getFiltrationProducts(FiltrationProductDTO filtrationProductDTO) throws DBExeption;
}
