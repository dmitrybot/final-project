package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts;

import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.FiltrationProductDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.ProductInfoEntity;

import java.util.List;

public interface IProductInfoDAO extends GenericDAO<ProductInfoEntity> {
    List<ProductInfoEntity> findByFiltration(FiltrationProductDTO filtrationProductDTO);
}
