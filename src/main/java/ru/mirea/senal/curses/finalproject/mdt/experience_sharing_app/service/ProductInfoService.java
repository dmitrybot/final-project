package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.service;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.IProductInfoDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.IReportDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.IUserDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.FiltrationProductDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.ProductDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.ReportDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.ProductInfoEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.ReportEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.TagEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.UserEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.DBExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.service.interfaces.IProductInfoService;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.util.IMappingUtils;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductInfoService implements IProductInfoService {
    private IProductInfoDAO productInfoDAO;
    private IMappingUtils mappingUtils;
    private IUserDAO userDAO;
    private IReportDAO reportDAO;

    private static final Logger log = Logger.getLogger(ProductInfoService.class);

    public ProductInfoService(IProductInfoDAO productInfoDAO, IMappingUtils mappingUtils, IUserDAO userDAO, IReportDAO reportDAO) {
        this.productInfoDAO = productInfoDAO;
        this.mappingUtils = mappingUtils;
        this.userDAO = userDAO;
        this.reportDAO = reportDAO;
    }

    @Transactional
    public List<ReportDTO> getReports(long id) throws DBExeption {
        try {
            ProductInfoEntity productInfoEntity = productInfoDAO.findOne(id);
            List<ReportEntity> reports = productInfoEntity.getReports();
            return reports
                    .stream()
                    .map(o -> mappingUtils.mapToReportDTO(o))
                    .collect(Collectors.toList());
        } catch (HibernateException | NoResultException | NullPointerException e) {
            log.error("db product's reports get operation failed", e);
            e.printStackTrace();
            throw new DBExeption();
        }
    }

    @Transactional
    public Boolean addReport(long id, ReportEntity report, long userId) throws DBExeption {
        try {
            ProductInfoEntity productInfoEntity = productInfoDAO.findOne(id);
            UserEntity userEntity = userDAO.findOne(userId);
            productInfoEntity.addReport(report);
            userEntity.addReport(report);
            report.setProduct(productInfoEntity);
            report.setAuthor(userEntity);
            reportDAO.create(report);
            return true;
        } catch (HibernateException | NoResultException | NullPointerException e) {
            log.error("db product's report add operation failed", e);
            e.printStackTrace();
            throw new DBExeption();
        }
    }

    @Transactional
    public Boolean updateProductTags(Set<TagEntity> tags, long id) throws DBExeption {
        try {
            ProductInfoEntity productInfoEntity = productInfoDAO.findOne(id);
            productInfoEntity.setTags(tags);
            productInfoDAO.update(productInfoEntity);
            return true;
        } catch (HibernateException | NoResultException | NullPointerException e) {
            log.error("db product's tags update operation failed", e);
            e.printStackTrace();
            throw new DBExeption();
        }
    }

    @Transactional
    public List<ProductDTO> getFiltrationProducts(FiltrationProductDTO filtrationProductDTO) throws DBExeption {
        try {
            return productInfoDAO.findByFiltration(filtrationProductDTO)
                    .stream()
                    .map(o -> mappingUtils.mapToProductDTO(o))
                    .collect(Collectors.toList());
        } catch (HibernateException | NoResultException | NullPointerException e) {
            log.error("db products get with filtration operation failed", e);
            e.printStackTrace();
            throw new DBExeption();
        }
    }
}
