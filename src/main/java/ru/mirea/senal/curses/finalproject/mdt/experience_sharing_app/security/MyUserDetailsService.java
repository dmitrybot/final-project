package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.security;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.entity.UserEntity;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.dao.abstracts.IUserDAO;

import javax.persistence.NoResultException;

@Service("userDetailsServiceImpl")
public class MyUserDetailsService implements UserDetailsService {

    private final IUserDAO userDAO;

    private static final Logger log = Logger.getLogger(MyUserDetailsService.class);

    public MyUserDetailsService(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Transactional(timeout = 10)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            UserEntity user = userDAO.findByEmail(email);
            /*return SecurityUser.fromUser(user);*/
            return new SecurityUser(user, true);
        } catch (HibernateException | NoResultException | NullPointerException e) {
            log.error("db user find failed");
            throw new UsernameNotFoundException("User doesn't exists");
        }
    }
}