package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao;

import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.AbstractJpaDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.IPaymentDAO;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.PaymentEntity;

import java.util.List;

public class PaymentDAO extends AbstractJpaDAO<PaymentEntity> implements IPaymentDAO {
    public List<PaymentEntity> findByFiltration(String listId) {
        String qlQuery = "select p" +
                " from PaymentEntity as p" +
                " where p.checked = false and" +
                " p.paymentType = 'SUBSCRIPTION' and" +
                " p.product.id in ( " + listId + " )";
        return getEntityManager().createQuery(qlQuery).getResultList();
    }

    public PaymentEntity findByUserProductRealizationId(long userId, long productId) {
        String qlQuery = "select p" +
                " from PaymentEntity as p" +
                " where p.checked = false and" +
                " p.seller.id = " + userId + " and" +
                " p.product.id = " + productId;

        System.out.println(qlQuery);
        return (PaymentEntity) getEntityManager().createQuery(qlQuery).getSingleResult();
    }
}
