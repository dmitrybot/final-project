package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.constants.Status;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.*;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.CourseDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.FiltrationProductDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.ProductDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.ReportDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.*;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.DBExeption;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.util.IMappingUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class ProductInfoServiceTest {
    @Mock
    private IProductInfoDAO productInfoDAO;
    @Mock
    private IUserDAO userDAO;
    @Mock
    private IMappingUtils mappingUtils;
    @Mock
    private IReportDAO reportDAO;
    @InjectMocks
    private ProductInfoService productInfoService;

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getReports_positive() {
        ProductInfoEntity productInfoEntity = mock(ProductInfoEntity.class);
        List<ReportEntity> reportEntityList = mock(List.class);
        Stream<ReportEntity> reportEntityStream = mock(Stream.class);
        Stream<ReportDTO> reportDTOStream = mock(Stream.class);
        List<ReportDTO> reportDTOList = mock(List.class);

        when(productInfoDAO.findOne(anyLong())).thenReturn(productInfoEntity);
        when(productInfoEntity.getReports()).thenReturn(reportEntityList);
        when(reportEntityList.stream()).thenReturn(reportEntityStream);
        when(reportEntityStream.map(o -> mappingUtils.mapToReportDTO(o))).thenReturn(reportDTOStream);
        when(reportDTOStream.collect(Collectors.toList())).thenReturn(reportDTOList);

        productInfoService.getReports(1L);

        verify(productInfoDAO).findOne(anyLong());
        verify(productInfoEntity).getReports();
        verify(reportEntityList).stream();
        verify(reportEntityStream).map(any());
    }

    @Test
    void getReports_negative() {
        when(productInfoDAO.findOne(anyLong())).thenThrow(NullPointerException.class);

        Assertions.assertThrows(DBExeption.class, () -> {
            productInfoService.getReports(1L);
        }, "Cant get reports, error while finding data in database");

        verify(productInfoDAO).findOne(anyLong());
    }

    @Test
    void addReport_positive() {
        ProductInfoEntity productInfoEntity = mock(ProductInfoEntity.class);
        UserEntity userEntity = mock(UserEntity.class);
        ReportEntity reportEntity = mock(ReportEntity.class);

        when(productInfoDAO.findOne(anyLong())).thenReturn(productInfoEntity);
        when(userDAO.findOne(anyLong())).thenReturn(userEntity);

        assertEquals(true, productInfoService.addReport(1L, reportEntity, 1L));

        verify(productInfoDAO).findOne(anyLong());
        verify(userDAO).findOne(anyLong());
        verify(productInfoEntity).addReport(reportEntity);
        verify(userEntity).addReport(reportEntity);
        verify(reportEntity).setProduct(productInfoEntity);
        verify(reportEntity).setAuthor(userEntity);
        verify(reportDAO).create(reportEntity);
    }

    @Test
    void addReport_negative() {
        ReportEntity reportEntity = mock(ReportEntity.class);

        when(productInfoDAO.findOne(anyLong())).thenThrow(NullPointerException.class);

        Assertions.assertThrows(DBExeption.class, () -> {
            productInfoService.addReport(1L, reportEntity, 1L);
        }, "Cant get reports, error while finding data in database");

        verify(productInfoDAO).findOne(anyLong());
    }

    @Test
    void updateProductTags_positive() {
        ProductInfoEntity productInfoEntity = mock(ProductInfoEntity.class);
        Set<TagEntity> tags = mock(Set.class);

        when(productInfoDAO.findOne(anyLong())).thenReturn(productInfoEntity);

        assertEquals(true, productInfoService.updateProductTags(tags, 2L));

        verify(productInfoDAO).findOne(anyLong());
        verify(productInfoEntity).setTags(tags);
        verify(productInfoDAO).update(productInfoEntity);
    }

    @Test
    void updateProductTags_negative() {
        ProductInfoEntity productInfoEntity = mock(ProductInfoEntity.class);
        Set<TagEntity> tags = mock(Set.class);

        when(productInfoDAO.findOne(anyLong())).thenThrow(NullPointerException.class);

        Assertions.assertThrows(DBExeption.class, () -> {
            productInfoService.updateProductTags(tags, 2L);
        }, "Cant get reports, error while finding data in database");

        verify(productInfoDAO).findOne(anyLong());
    }

    @Test
    void getFiltrationProducts_positive() {
        List<ProductInfoEntity> productInfoEntities = mock(List.class);
        Stream<ProductInfoEntity> productInfoEntityStream = mock(Stream.class);
        Stream<ProductDTO> productDTOStream = mock(Stream.class);
        List<ProductDTO> productDTOList = mock(List.class);
        FiltrationProductDTO filtrationProductDTO = mock(FiltrationProductDTO.class);

        when(productInfoDAO.findByFiltration(filtrationProductDTO)).thenReturn(productInfoEntities);
        when(productInfoEntities.stream()).thenReturn(productInfoEntityStream);
        when(productInfoEntityStream.map(o -> mappingUtils.mapToProductDTO(o))).thenReturn(productDTOStream);
        when(productDTOStream.collect(Collectors.toList())).thenReturn(productDTOList);

        productInfoService.getFiltrationProducts(filtrationProductDTO);

        verify(productInfoDAO).findByFiltration(filtrationProductDTO);
        verify(productInfoEntities).stream();
        verify(productInfoEntityStream).map(any());
    }

    @Test
    void getFiltrationProducts_negative() {
        FiltrationProductDTO filtrationProductDTO = mock(FiltrationProductDTO.class);

        when(productInfoDAO.findByFiltration(filtrationProductDTO)).thenThrow(NullPointerException.class);

        Assertions.assertThrows(DBExeption.class, () -> {
            productInfoService.getFiltrationProducts(filtrationProductDTO);
        }, "Cant get reports, error while finding data in database");

        verify(productInfoDAO).findByFiltration(filtrationProductDTO);
    }
}