package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao;

import org.springframework.stereotype.Repository;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.AbstractJpaDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.IProductInfoDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.FiltrationProductDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.ProductInfoEntity;

import java.util.List;

@Repository
public class ProductInfoDAO extends AbstractJpaDAO<ProductInfoEntity> implements IProductInfoDAO {

    public List<ProductInfoEntity> findByFiltration(FiltrationProductDTO filtrationProductDTO) {
        String qlQuery = "select p" +
                " from ProductInfoEntity as p" +
                " inner join p.tags as t" +
                " where p.price >= " + filtrationProductDTO.getMinPrice() + " and" +
                " p.price <= " + filtrationProductDTO.getMaxPrice() + " and" +
                " p.capacity >= " + filtrationProductDTO.getMinCapacity() + " and" +
                " p.capacity <= " + filtrationProductDTO.getMaxCapacity() + " and" +
                " p.status = 'ACTIVE' and" +
                " p.productType in ( " + filtrationProductDTO.getProductTypes() + " ) and" +
                " t.id in ( " + filtrationProductDTO.getTagsId() + " )" +
                " group by p.id, p.name" +
                " having count(distinct t.id) = " + filtrationProductDTO.getTags().size();
        return getEntityManager().createQuery(qlQuery).getResultList();
    }
}