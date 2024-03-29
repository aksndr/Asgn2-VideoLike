package org.magnum.mobilecloud.video.auth;

/**
 * User: a.arzamastsev Date: 08.09.2014 Time: 14:06
 */

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class User implements UserDetails {
    private final Collection<GrantedAuthority> authorities_;
    private final String password_;
    private final String username_;

    @SuppressWarnings("unchecked")
    private User(String username, String password) {
        this(username, password, Collections.EMPTY_LIST);
    }

    private User(String username, String password,
                 String... authorities) {
        username_ = username;
        password_ = password;
        authorities_ = AuthorityUtils.createAuthorityList(authorities);
    }

    private User(String username, String password,
                 Collection<GrantedAuthority> authorities) {
        super();
        username_ = username;
        password_ = password;
        authorities_ = authorities;
    }

    public static UserDetails create(String username, String password,
                                     String... authorities) {
        return new User(username, password, authorities);
    }

    public Collection<GrantedAuthority> getAuthorities() {
        return authorities_;
    }

    public String getPassword() {
        return password_;
    }

    public String getUsername() {
        return username_;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}