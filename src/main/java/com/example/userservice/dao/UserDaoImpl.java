package com.example.userservice.dao;

import com.example.userservice.model.UserEntity;
import com.example.userservice.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserDaoImpl implements UserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    @Override
    public void save(UserEntity userEntity) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(userEntity);
            tx.commit();
            logger.info("Пользователь сохранён: {}", userEntity);
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            logger.error("Ошибка сохранения пользователя", e);
        }
    }

    @Override
    public UserEntity getById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(UserEntity.class, id);
        } catch (Exception e) {
            logger.error("Ошибка получения пользователя по id: " + id, e);
            return null;
        }
    }

    @Override
    public List<UserEntity> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from UserEntity", UserEntity.class).list();
        } catch (Exception e) {
            logger.error("Ошибка получение всех пользователей", e);
            return null;
        }
    }

    @Override
    public void update(UserEntity userEntity) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(userEntity);
            tx.commit();
            logger.info("Данные о пользователе обновлены: {}", userEntity);
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            logger.error("Ошибка обновления данных о пользователе", e);
        }
    }

    @Override
    public void delete(Long id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            UserEntity userEntity = session.get(UserEntity.class, id);
            if (userEntity != null) {
                session.remove(userEntity);
                tx.commit();
                logger.info("Пользователь удалён: {}", userEntity);
            } else {
                logger.warn("Пользователь с id {} не найдён", id);
                if (tx != null) tx.rollback();
            }
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            logger.error("Ошибка удаление пользователя", e);
        }
    }
}
