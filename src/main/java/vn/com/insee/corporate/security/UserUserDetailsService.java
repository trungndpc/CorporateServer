package vn.com.insee.corporate.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.insee.corporate.entity.UserEntity;
import vn.com.insee.corporate.exception.InseeException;
import vn.com.insee.corporate.repository.UserRepository;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            UserEntity user = userRepository.findByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("Incorrect credentials");
            }
            return buildUserDetails(user);
        } catch (DataAccessException ex) {
            throw new UsernameNotFoundException("Database error");
        }
    }

    private UserUserDetails buildUserDetails(UserEntity user) {
        List<GrantedAuthority> authorityList = new ArrayList<>();
//        for (UserPermission userPermission : user.getUserPermissions()) {
//            authorityList.add(new SimpleGrantedAuthority(userPermission.getPermission().getPermissionName()));
//        }
        return new UserUserDetails(user, authorityList);
    }

    public UserDetails loadUserById(Integer id) throws InseeException {
        Optional<UserEntity> user = userRepository.findById(id);
        return buildUserDetails(user.orElseThrow(() -> new InseeException()));
    }
}
