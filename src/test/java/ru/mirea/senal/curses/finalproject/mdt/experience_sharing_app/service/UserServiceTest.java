package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.*;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.ProductDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.SectionDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.UserDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.*;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.*;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.util.IMappingUtils;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class UserServiceTest {
    @Mock
    private IUserDAO userDAO;
    @Mock
    private IMappingUtils mappingUtils;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ILessonDAO lessonDAO;
    @Mock
    private IPaymentDAO paymentDAO;
    @Mock
    private IProductInfoDAO productInfoDAO;
    @Mock
    private ICourseDAO courseDAO;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void updateUser_positive() {
        UserEntity userEntityInput = mock(UserEntity.class);
        UserEntity userEntityDb = mock(UserEntity.class);
        UserEntity userEntityOutput = mock(UserEntity.class);
        UserDTO userDTO = mock(UserDTO.class);

        when(userDAO.findOne(anyLong())).thenReturn(userEntityDb);
        when(userEntityInput.getId()).thenReturn(1L);
        when(userEntityInput.getPassword()).thenReturn("");
        when(passwordEncoder.encode(any())).thenReturn("");
        when(userDAO.update(userEntityDb)).thenReturn(userEntityOutput);
        when(mappingUtils.mapToUserDTO(userEntityOutput)).thenReturn(userDTO);

        assertEquals(userDTO, userService.updateUser(userEntityInput));

        verify(userDAO).findOne(anyLong());
        verify(userEntityInput).getId();
        verify(userEntityInput).getPassword();
        verify(userEntityInput).setPassword(any());
        verify(userEntityDb).update(userEntityInput);
        verify(userDAO).update(userEntityDb);
        verify(mappingUtils).mapToUserDTO(userEntityOutput);
    }

    @Test
    void updateUser_negative_error_with_db() {
        UserEntity userEntityInput = mock(UserEntity.class);

        when(userDAO.findOne(anyLong())).thenThrow(NullPointerException.class);

        Assertions.assertThrows(DBExeption.class, () -> {
            userService.updateUser(userEntityInput);
        }, "Cant update user, error while working with database");

        verify(userDAO).findOne(anyLong());
    }

    @Test
    void updateUser_negative_error_with_input_data() {
        UserEntity userEntityInput = mock(UserEntity.class);

        when(userDAO.findOne(anyLong())).thenThrow(PersistenceException.class);

        Assertions.assertThrows(InputDataExeption.class, () -> {
            userService.updateUser(userEntityInput);
        }, "Cant update user, error while working with database");

        verify(userDAO).findOne(anyLong());
    }

    @Test
    void createUser_positive() {
        UserEntity userEntityInput = mock(UserEntity.class);
        UserEntity userEntityOutput = mock(UserEntity.class);
        UserDTO userDTO = mock(UserDTO.class);

        when(userEntityInput.getPassword()).thenReturn("");
        when(passwordEncoder.encode(any())).thenReturn("");
        when(userDAO.create(userEntityInput)).thenReturn(userEntityOutput);
        when(mappingUtils.mapToUserDTO(userEntityOutput)).thenReturn(userDTO);

        assertEquals(userDTO, userService.createUser(userEntityInput));

        verify(userEntityInput).getPassword();
        verify(userEntityInput).setPassword(any());
        verify(userDAO).create(userEntityInput);
        verify(mappingUtils).mapToUserDTO(userEntityOutput);
    }

    @Test
    void createUser_negative_error_with_db() {
        UserEntity userEntityInput = mock(UserEntity.class);

        when(userEntityInput.getPassword()).thenReturn("");
        when(passwordEncoder.encode(any())).thenReturn("");
        when(userDAO.create(userEntityInput)).thenThrow(NullPointerException.class);

        Assertions.assertThrows(DBExeption.class, () -> {
            userService.createUser(userEntityInput);
        }, "Cant create user, error while working with database");

        verify(userEntityInput).getPassword();
        verify(userEntityInput).setPassword(any());
        verify(userDAO).create(userEntityInput);
    }

    @Test
    void createUser_negative_error_with_input_data() {
        UserEntity userEntityInput = mock(UserEntity.class);

        when(userEntityInput.getPassword()).thenReturn("");
        when(passwordEncoder.encode(any())).thenReturn("");
        when(userDAO.create(userEntityInput)).thenThrow(PersistenceException.class);

        Assertions.assertThrows(InputDataExeption.class, () -> {
            userService.createUser(userEntityInput);
        }, "Cant create user, error while working with database");

        verify(userEntityInput).getPassword();
        verify(userEntityInput).setPassword(any());
        verify(userDAO).create(userEntityInput);
    }

    @Test
    void getUser_positive() {
        UserEntity userEntityDb = mock(UserEntity.class);
        UserDTO userDTO = mock(UserDTO.class);

        when(userDAO.findOne(anyLong())).thenReturn(userEntityDb);
        when(mappingUtils.mapToUserDTO(userEntityDb)).thenReturn(userDTO);

        assertEquals(userDTO, userService.getUser(1L));

        verify(userDAO).findOne(anyLong());
        verify(mappingUtils).mapToUserDTO(userEntityDb);
    }

    @Test
    void getUser_negative_error_with_db() {

        when(userDAO.findOne(anyLong())).thenThrow(NoResultException.class);

        Assertions.assertThrows(DBExeption.class, () -> {
            userService.getUser(1L);
        }, "Cant get user, error while working with database");

        verify(userDAO).findOne(anyLong());
    }

    @Test
    void getUserCreatedProducts_positive() {
        UserEntity userEntity = mock(UserEntity.class);
        List<ProductInfoEntity> productInfoEntities = mock(List.class);
        Stream<ProductInfoEntity> productInfoEntityStream = mock(Stream.class);
        Stream<ProductDTO> productDTOStream = mock(Stream.class);
        List<ProductDTO> productDTOList = mock(List.class);

        when(userDAO.findOne(anyLong())).thenReturn(userEntity);
        when(userEntity.getCreatedProducts()).thenReturn(productInfoEntities);
        when(productInfoEntities.stream()).thenReturn(productInfoEntityStream);
        when(productInfoEntityStream.map(o -> mappingUtils.mapToProductDTO(o))).thenReturn(productDTOStream);
        when(productDTOStream.collect(Collectors.toList())).thenReturn(productDTOList);

        userService.getUserCreatedProducts(1L);

        verify(userDAO).findOne(anyLong());
        verify(userEntity).getCreatedProducts();
        verify(productInfoEntities).stream();
        verify(productInfoEntityStream).map(any());
    }

    @Test
    void getUserCreatedProducts_negative() {
        when(userDAO.findOne(anyLong())).thenThrow(NoResultException.class);

        Assertions.assertThrows(DBExeption.class, () -> {
            userService.getUserCreatedProducts(1L);
        }, "Cant get user, error while working with database");

        verify(userDAO).findOne(anyLong());
    }

    @Test
    void getSubscribedUsers_positive() {
        UserEntity userEntity = mock(UserEntity.class);
        List<ProductInfoEntity> productInfoEntities = mock(List.class);
        Stream<ProductInfoEntity> productInfoEntityStream = mock(Stream.class);
        Stream<Long> productIdsStream = mock(Stream.class);
        List<Long> productIdsList = mock(List.class);

        List<PaymentEntity> paymentEntities = mock(List.class);
        Stream<PaymentEntity> paymentEntityStream = mock(Stream.class);
        Stream<UserDTO> userDTOStream = mock(Stream.class);
        List<UserDTO> userDTOList = mock(List.class);

        when(userDAO.findOne(anyLong())).thenReturn(userEntity);
        when(userEntity.getCreatedProducts()).thenReturn(productInfoEntities);
        when(productInfoEntities.stream()).thenReturn(productInfoEntityStream);
        when(productInfoEntityStream.map(o -> o.getId())).thenReturn(productIdsStream);
        when(productIdsStream.collect(Collectors.toList())).thenReturn(productIdsList);
        when(mappingUtils.mapListLongToString(productIdsList)).thenReturn("");
        when(paymentDAO.findByFiltration("")).thenReturn(paymentEntities);
        when(paymentEntities.stream()).thenReturn(paymentEntityStream);
        when(paymentEntityStream.map(o -> mappingUtils.mapToUserDTO(o.getBuyer()))).thenReturn(userDTOStream);
        when(userDTOStream.collect(Collectors.toList())).thenReturn(userDTOList);

        userService.getSubscribedUsers(1L);

        verify(userDAO).findOne(anyLong());
        verify(userEntity).getCreatedProducts();
        verify(productInfoEntities).stream();
        verify(productInfoEntityStream).map(any());
        verify(mappingUtils).mapListLongToString(any());
        verify(paymentDAO).findByFiltration(any());
    }

    @Test
    void getSubscribedUsers_negative() {
        when(userDAO.findOne(anyLong())).thenThrow(NullPointerException.class);

        Assertions.assertThrows(DBExeption.class, () -> {
            userService.getSubscribedUsers(1L);
        }, "Cant get user, error while working with database");

        verify(userDAO).findOne(anyLong());
    }

    @Test
    void makeDonation_positive() {
        UserEntity buyer = mock(UserEntity.class);
        PaymentEntity paymentEntity = mock(PaymentEntity.class);
        LessonEntity lessonEntity = mock(LessonEntity.class);
        LessonInfoEntity lessonInfoEntity = mock(LessonInfoEntity.class);
        ProductInfoEntity productInfoEntity = mock(ProductInfoEntity.class);
        UserEntity seller = mock(UserEntity.class);

        when(userDAO.findOne(anyLong())).thenReturn(buyer);
        when(mappingUtils.makePaymentEntity()).thenReturn(paymentEntity);
        when(lessonDAO.findOne(anyLong())).thenReturn(lessonEntity);
        when(lessonEntity.getLessonInfo()).thenReturn(lessonInfoEntity);
        when(lessonInfoEntity.getProductInfoEntity()).thenReturn(productInfoEntity);
        when(productInfoEntity.getAuthor()).thenReturn(seller);
        when(lessonEntity.getMinDonation()).thenReturn(1.0);
        when(lessonEntity.getId()).thenReturn(anyLong());

        assertEquals(true, userService.makeDonation(1L, 1L, 2.0));

        verify(userDAO).findOne(anyLong());
        verify(mappingUtils).makePaymentEntity();
        verify(lessonDAO).findOne(anyLong());
        verify(lessonEntity, times(2)).getLessonInfo();
        verify(lessonInfoEntity, times(2)).getProductInfoEntity();
        verify(productInfoEntity).getAuthor();
        verify(lessonEntity).getMinDonation();
        verify(lessonEntity).getId();
        verify(paymentEntity).setPaymentDate(any());
        verify(paymentEntity).setPrice(any());
        verify(paymentEntity).setSeller(any());
        verify(paymentEntity).setBuyer(any());
        verify(paymentEntity).setPaymentType(any());
        verify(paymentEntity).setProduct(any());
        verify(paymentEntity).setChecked(any());
        verify(paymentEntity).setProductRealization(any());
        verify(paymentDAO).create(paymentEntity);
    }

    @Test
    void makeDonation_negative_not_enough_money() {
        UserEntity buyer = mock(UserEntity.class);
        PaymentEntity paymentEntity = mock(PaymentEntity.class);
        LessonEntity lessonEntity = mock(LessonEntity.class);
        LessonInfoEntity lessonInfoEntity = mock(LessonInfoEntity.class);
        ProductInfoEntity productInfoEntity = mock(ProductInfoEntity.class);
        UserEntity seller = mock(UserEntity.class);

        when(userDAO.findOne(anyLong())).thenReturn(buyer);
        when(mappingUtils.makePaymentEntity()).thenReturn(paymentEntity);
        when(lessonDAO.findOne(anyLong())).thenReturn(lessonEntity);
        when(lessonEntity.getLessonInfo()).thenReturn(lessonInfoEntity);
        when(lessonInfoEntity.getProductInfoEntity()).thenReturn(productInfoEntity);
        when(productInfoEntity.getAuthor()).thenReturn(seller);
        when(lessonEntity.getMinDonation()).thenReturn(3.0);
        when(lessonEntity.getId()).thenReturn(anyLong());

        Assertions.assertThrows(NotEnoughMoneyExeption.class, () -> {
            userService.makeDonation(1L, 1L, 2.0);
        }, "Cant make donation, not enough money sended");

        verify(userDAO).findOne(anyLong());
        verify(mappingUtils).makePaymentEntity();
        verify(lessonDAO).findOne(anyLong());
        verify(lessonEntity).getLessonInfo();
        verify(lessonInfoEntity).getProductInfoEntity();
        verify(productInfoEntity).getAuthor();
        verify(lessonEntity).getMinDonation();
    }

    @Test
    void makeDonation_negative_error_with_db() {
        when(userDAO.findOne(anyLong())).thenThrow(NoResultException.class);

        Assertions.assertThrows(DBExeption.class, () -> {
            userService.makeDonation(1L, 1L, 2.0);
        }, "Cant make donation, not enough money sended");

        verify(userDAO).findOne(anyLong());
    }

    @Test
    void buyProduct_positive() {
        UserEntity buyer = mock(UserEntity.class);
        PaymentEntity paymentEntity = mock(PaymentEntity.class);
        ProductInfoEntity productInfoEntity = mock(ProductInfoEntity.class);
        UserEntity seller = mock(UserEntity.class);

        when(userDAO.findOne(anyLong())).thenReturn(buyer);
        when(mappingUtils.makePaymentEntity()).thenReturn(paymentEntity);
        when(productInfoDAO.findOne(anyLong())).thenReturn(productInfoEntity);
        when(productInfoEntity.getAuthor()).thenReturn(seller);
        when(productInfoEntity.getPrice()).thenReturn(2.0);

        assertEquals(true, userService.buyProduct(1L, 1L, 2.0));

        verify(userDAO).findOne(anyLong());
        verify(mappingUtils).makePaymentEntity();
        verify(productInfoDAO).findOne(anyLong());
        verify(productInfoEntity).getPrice();
        verify(productInfoEntity).getAuthor();
        verify(paymentEntity).setPaymentDate(any());
        verify(paymentEntity).setPrice(any());
        verify(paymentEntity).setSeller(any());
        verify(paymentEntity).setBuyer(any());
        verify(paymentEntity).setPaymentType(any());
        verify(paymentEntity).setProduct(any());
        verify(paymentEntity).setChecked(any());
        verify(paymentDAO).create(paymentEntity);
    }

    @Test
    void buyProduct_negative_not_enough_money() {
        UserEntity buyer = mock(UserEntity.class);
        PaymentEntity paymentEntity = mock(PaymentEntity.class);
        ProductInfoEntity productInfoEntity = mock(ProductInfoEntity.class);

        when(userDAO.findOne(anyLong())).thenReturn(buyer);
        when(mappingUtils.makePaymentEntity()).thenReturn(paymentEntity);
        when(productInfoDAO.findOne(anyLong())).thenReturn(productInfoEntity);
        when(productInfoEntity.getPrice()).thenReturn(3.0);

        Assertions.assertThrows(NotEnoughMoneyExeption.class, () -> {
            userService.buyProduct(1L, 1L, 2.0);
        }, "Cant make donation, not enough money sended");

        verify(userDAO).findOne(anyLong());
        verify(mappingUtils).makePaymentEntity();
        verify(productInfoDAO).findOne(anyLong());
        verify(productInfoEntity).getPrice();
    }

    @Test
    void buyProduct_negative_error_with_db() {
        when(userDAO.findOne(anyLong())).thenThrow(NoResultException.class);

        Assertions.assertThrows(DBExeption.class, () -> {
            userService.buyProduct(1L, 1L, 2.0);
        }, "Cant make donation, not enough money sended");

        verify(userDAO).findOne(anyLong());
    }

    @Test
    void subscribeCourse_positive() {
        CourseEntity courseEntity = mock(CourseEntity.class);
        PaymentEntity paymentEntity = mock(PaymentEntity.class);
        UserEntity userEntity = mock(UserEntity.class);
        CourseInfoEntity courseInfoEntity = mock(CourseInfoEntity.class);
        ProductInfoEntity productInfoEntity = mock(ProductInfoEntity.class);
        Set<UserEntity> userEntities = mock(Set.class);
        Date date = mock(Date.class);
        Set<CourseEntity> courseEntities = mock(Set.class);

        when(courseDAO.findOne(anyLong())).thenReturn(courseEntity);
        when(courseEntity.getCourse()).thenReturn(courseInfoEntity);
        when(courseInfoEntity.getProductInfoEntity()).thenReturn(productInfoEntity);
        when(productInfoEntity.getId()).thenReturn(1L);
        when(paymentDAO.findByUserProductRealizationId(1L, 1L)).thenReturn(paymentEntity);
        when(paymentEntity.getBuyer()).thenReturn(userEntity);
        when(courseEntity.getCapacity()).thenReturn(30);
        when(courseEntity.getUsers()).thenReturn(userEntities);
        when(userEntities.size()).thenReturn(20);
        when(courseEntity.getNoteDate()).thenReturn(date);
        when(date.before(any())).thenReturn(false);
        when(userEntity.getCourses()).thenReturn(courseEntities);

        assertEquals(true, userService.subscribeCourse(1L, 1L));

        verify(courseDAO).findOne(anyLong());
        verify(courseEntity).getCourse();
        verify(courseInfoEntity).getProductInfoEntity();
        verify(productInfoEntity).getId();
        verify(paymentDAO).findByUserProductRealizationId(anyLong(), anyLong());
        verify(paymentEntity).getBuyer();
        verify(courseEntity).getCapacity();
        verify(courseEntity).getUsers();
        verify(userEntities).size();
        verify(courseEntity).getNoteDate();
        verify(date).before(any());
        verify(userEntity).getCourses();
        verify(courseEntities).add(any());
        verify(userDAO).update(any());
    }

    @Test
    void subscribeCourse_negative_cant_subscribe_exeption() {
        CourseEntity courseEntity = mock(CourseEntity.class);
        PaymentEntity paymentEntity = mock(PaymentEntity.class);
        UserEntity userEntity = mock(UserEntity.class);
        CourseInfoEntity courseInfoEntity = mock(CourseInfoEntity.class);
        ProductInfoEntity productInfoEntity = mock(ProductInfoEntity.class);
        Set<UserEntity> userEntities = mock(Set.class);
        Date date = mock(Date.class);

        when(courseDAO.findOne(anyLong())).thenReturn(courseEntity);
        when(courseEntity.getCourse()).thenReturn(courseInfoEntity);
        when(courseInfoEntity.getProductInfoEntity()).thenReturn(productInfoEntity);
        when(productInfoEntity.getId()).thenReturn(1L);
        when(paymentDAO.findByUserProductRealizationId(1L, 1L)).thenReturn(paymentEntity);
        when(paymentEntity.getBuyer()).thenReturn(userEntity);
        when(courseEntity.getCapacity()).thenReturn(30);
        when(courseEntity.getUsers()).thenReturn(userEntities);
        when(userEntities.size()).thenReturn(20);
        when(courseEntity.getNoteDate()).thenReturn(date);
        when(date.before(any())).thenReturn(true);

        Assertions.assertThrows(SubscribeExeption.class, () -> {
            userService.subscribeCourse(1L, 1L);
        }, "Cant make donation, not enough money sended");

        verify(courseDAO).findOne(anyLong());
        verify(courseEntity).getCourse();
        verify(courseInfoEntity).getProductInfoEntity();
        verify(productInfoEntity).getId();
        verify(paymentDAO).findByUserProductRealizationId(anyLong(), anyLong());
        verify(paymentEntity).getBuyer();
        verify(courseEntity).getCapacity();
        verify(courseEntity).getUsers();
        verify(userEntities).size();
        verify(courseEntity).getNoteDate();
        verify(date).before(any());
    }

    @Test
    void subscribeCourse_negative_dont_pay_exeption() {
        CourseEntity courseEntity = mock(CourseEntity.class);
        PaymentEntity paymentEntity = null;
        CourseInfoEntity courseInfoEntity = mock(CourseInfoEntity.class);
        ProductInfoEntity productInfoEntity = mock(ProductInfoEntity.class);

        when(courseDAO.findOne(anyLong())).thenReturn(courseEntity);
        when(courseEntity.getCourse()).thenReturn(courseInfoEntity);
        when(courseInfoEntity.getProductInfoEntity()).thenReturn(productInfoEntity);
        when(productInfoEntity.getId()).thenReturn(1L);
        when(paymentDAO.findByUserProductRealizationId(1L, 1L)).thenReturn(paymentEntity);

        Assertions.assertThrows(DontPayForProductExeption.class, () -> {
            userService.subscribeCourse(1L, 1L);
        }, "Cant make donation, not enough money sended");

        verify(courseDAO).findOne(anyLong());
        verify(courseEntity).getCourse();
        verify(courseInfoEntity).getProductInfoEntity();
        verify(productInfoEntity).getId();
        verify(paymentDAO).findByUserProductRealizationId(anyLong(), anyLong());
    }

    @Test
    void subscribeCourse_negative_error_with_db() {
        when(courseDAO.findOne(anyLong())).thenThrow(NullPointerException.class);

        Assertions.assertThrows(DBExeption.class, () -> {
            userService.subscribeCourse(1L, 1L);
        }, "Cant make donation, not enough money sended");

        verify(courseDAO).findOne(anyLong());
    }

    @Test
    void subscribeLesson_positive() {
        LessonEntity lessonEntity = mock(LessonEntity.class);
        PaymentEntity paymentEntity = mock(PaymentEntity.class);
        UserEntity userEntity = mock(UserEntity.class);
        LessonInfoEntity lessonInfoEntity = mock(LessonInfoEntity.class);
        ProductInfoEntity productInfoEntity = mock(ProductInfoEntity.class);
        Set<UserEntity> userEntities = mock(Set.class);
        Date date = mock(Date.class);
        Set<LessonEntity> lessonEntities = mock(Set.class);

        when(lessonDAO.findOne(anyLong())).thenReturn(lessonEntity);
        when(lessonEntity.getLessonInfo()).thenReturn(lessonInfoEntity);
        when(lessonInfoEntity.getProductInfoEntity()).thenReturn(productInfoEntity);
        when(productInfoEntity.getId()).thenReturn(1L);
        when(paymentDAO.findByUserProductRealizationId(1L, 1L)).thenReturn(paymentEntity);
        when(paymentEntity.getBuyer()).thenReturn(userEntity);
        when(lessonEntity.getCapacity()).thenReturn(30);
        when(lessonEntity.getUsers()).thenReturn(userEntities);
        when(userEntities.size()).thenReturn(20);
        when(lessonEntity.getStartDate()).thenReturn(date);
        when(date.before(any())).thenReturn(false);
        when(userEntity.getLessons()).thenReturn(lessonEntities);

        assertEquals(true, userService.subscribeLesson(1L, 1L));

        verify(lessonDAO).findOne(anyLong());
        verify(lessonEntity).getLessonInfo();
        verify(lessonInfoEntity).getProductInfoEntity();
        verify(productInfoEntity).getId();
        verify(paymentDAO).findByUserProductRealizationId(anyLong(), anyLong());
        verify(paymentEntity).getBuyer();
        verify(lessonEntity).getCapacity();
        verify(lessonEntity).getUsers();
        verify(userEntities).size();
        verify(lessonEntity).getStartDate();
        verify(date).before(any());
        verify(userEntity).getLessons();
        verify(lessonEntities).add(any());
        verify(userDAO).update(any());
    }

    @Test
    void subscribeLesson_negative_cant_subscribe_exeption() {
        LessonEntity lessonEntity = mock(LessonEntity.class);
        PaymentEntity paymentEntity = mock(PaymentEntity.class);
        UserEntity userEntity = mock(UserEntity.class);
        LessonInfoEntity lessonInfoEntity = mock(LessonInfoEntity.class);
        ProductInfoEntity productInfoEntity = mock(ProductInfoEntity.class);
        Set<UserEntity> userEntities = mock(Set.class);
        Date date = mock(Date.class);

        when(lessonDAO.findOne(anyLong())).thenReturn(lessonEntity);
        when(lessonEntity.getLessonInfo()).thenReturn(lessonInfoEntity);
        when(lessonInfoEntity.getProductInfoEntity()).thenReturn(productInfoEntity);
        when(productInfoEntity.getId()).thenReturn(1L);
        when(paymentDAO.findByUserProductRealizationId(1L, 1L)).thenReturn(paymentEntity);
        when(paymentEntity.getBuyer()).thenReturn(userEntity);
        when(lessonEntity.getCapacity()).thenReturn(30);
        when(lessonEntity.getUsers()).thenReturn(userEntities);
        when(userEntities.size()).thenReturn(20);
        when(lessonEntity.getStartDate()).thenReturn(date);
        when(date.before(any())).thenReturn(true);

        Assertions.assertThrows(SubscribeExeption.class, () -> {
            userService.subscribeLesson(1L, 1L);
        }, "Cant subscribe lesson, lesson is already started");

        verify(lessonDAO).findOne(anyLong());
        verify(lessonEntity).getLessonInfo();
        verify(lessonInfoEntity).getProductInfoEntity();
        verify(productInfoEntity).getId();
        verify(paymentDAO).findByUserProductRealizationId(anyLong(), anyLong());
        verify(paymentEntity).getBuyer();
        verify(lessonEntity).getCapacity();
        verify(lessonEntity).getUsers();
        verify(userEntities).size();
        verify(lessonEntity).getStartDate();
        verify(date).before(any());
    }

    @Test
    void subscribeLesson_negative_dont_pay_exeption() {
        LessonEntity lessonEntity = mock(LessonEntity.class);
        PaymentEntity paymentEntity = null;
        LessonInfoEntity lessonInfoEntity = mock(LessonInfoEntity.class);
        ProductInfoEntity productInfoEntity = mock(ProductInfoEntity.class);

        when(lessonDAO.findOne(anyLong())).thenReturn(lessonEntity);
        when(lessonEntity.getLessonInfo()).thenReturn(lessonInfoEntity);
        when(lessonInfoEntity.getProductInfoEntity()).thenReturn(productInfoEntity);
        when(productInfoEntity.getId()).thenReturn(1L);
        when(paymentDAO.findByUserProductRealizationId(1L, 1L)).thenReturn(paymentEntity);

        Assertions.assertThrows(DontPayForProductExeption.class, () -> {
            userService.subscribeLesson(1L, 1L);
        }, "Cant subscribe lesson, dont pay for product");

        verify(lessonDAO).findOne(anyLong());
        verify(lessonEntity).getLessonInfo();
        verify(lessonInfoEntity).getProductInfoEntity();
        verify(productInfoEntity).getId();
        verify(paymentDAO).findByUserProductRealizationId(anyLong(), anyLong());
    }

    @Test
    void subscribeLesson_negative_error_with_db() {
        when(lessonDAO.findOne(anyLong())).thenThrow(NullPointerException.class);

        Assertions.assertThrows(DBExeption.class, () -> {
            userService.subscribeLesson(1L, 1L);
        }, "Cant subscribe lesson, error with db");

        verify(lessonDAO).findOne(anyLong());
    }

    @Test
    void findByEmail_positive() {
        UserEntity userEntity = mock(UserEntity.class);

        when(userDAO.findByEmail(any())).thenReturn(userEntity);

        assertEquals(userEntity, userService.findByEmail(""));

        verify(userDAO).findByEmail(any());
    }

    @Test
    void findByEmail_negative() {
        when(userDAO.findByEmail(any())).thenThrow(NoResultException.class);

        Assertions.assertThrows(DBExeption.class, () -> {
            userService.findByEmail("");
        }, "Cant get user by email, error while working with database");

        verify(userDAO).findByEmail(any());
    }
}