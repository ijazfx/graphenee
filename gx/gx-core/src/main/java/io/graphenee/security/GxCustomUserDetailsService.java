package io.graphenee.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.graphenee.core.GxDataService;
import io.graphenee.core.model.entity.GxUserAccount;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GxCustomUserDetailsService implements UserDetailsService {

    @Autowired
    GxDataService gxDataService;

    @Override
    public UserDetails loadUserByUsername(String usernameAndNamespace) throws UsernameNotFoundException {

        try {
            GxUserAccount user = gxDataService.findUserAccountByUsernameAndNamespace(usernameAndNamespace);
            return User.withUsername(usernameAndNamespace)
                    .password(user.getPassword()) // already encrypted
                    .roles("USER")
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }

    }

}
