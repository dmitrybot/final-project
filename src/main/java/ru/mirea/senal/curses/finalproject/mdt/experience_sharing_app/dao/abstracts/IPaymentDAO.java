package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts;

import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.PaymentEntity;

import java.util.List;

public interface IPaymentDAO extends GenericDAO<PaymentEntity> {
    List<PaymentEntity> findByFiltration(String listId);
    PaymentEntity findByUserProductRealizationId(long userId, long productId);
}
