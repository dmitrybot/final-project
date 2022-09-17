package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.service.interfaces;

import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.ProductDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dto.UserDTO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.UserEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.exeption.DBExeption;

import java.util.List;

public interface IUserService {
    UserDTO updateUser(UserEntity userEntity) throws DBExeption;
    UserDTO createUser(UserEntity userEntity) throws DBExeption;
    UserDTO getUser(long id) throws  DBExeption;
    List<ProductDTO> getUserCreatedProducts(long id) throws DBExeption;
    List<UserDTO> getSubscribedUsers(long id) throws DBExeption;
    Boolean subscribeCourse(long id, long courseId) throws DBExeption;
    Boolean subscribeLesson(long id, long lessonId) throws DBExeption;
    Boolean makeDonation(long id, long lessonId, double cost) throws DBExeption;
    Boolean buyProduct(long id, long productId, double cost) throws DBExeption;
    UserEntity findByEmail(String email) throws DBExeption;
}
