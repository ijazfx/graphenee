package io.graphenee.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.graphenee.core.GxDataService;
import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.core.model.entity.GxUserAccount;

@Service
public class GxCustomUserDetailsService implements UserDetailsService {

    @Autowired
    GxDataService gxDataService;

    @Override
    public UserDetails loadUserByUsername(String usernameAndNamespace) throws UsernameNotFoundException {
        String[] userData = usernameAndNamespace.split("@");

        String name = userData[0];
        String namespaceName = userData[1];

        GxNamespace namespace = gxDataService.findNamespace(namespaceName);

        GxUserAccount user = gxDataService.findUserAccountByUsernameAndNamespace(name, namespace);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return User.withUsername(user.getUsername())
                .password(user.getPassword()) // already encrypted
                .roles("USER")
                .build();
    }

}
