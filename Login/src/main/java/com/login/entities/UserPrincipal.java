package com.login.entities;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class UserPrincipal implements UserDetails {
 
	private static final long serialVersionUID = -1291239721603442855L;
	private UserModel user;
	private List<GrantedAuthority> authorities = new ArrayList<>();

    public UserPrincipal(UserModel user) {
        this.user = user;
        for (RightModel rights: this.user.getRole().getRights()) {
    		authorities.add(new SimpleGrantedAuthority(rights.getRightName()));
        }
        authorities.add(new SimpleGrantedAuthority(user.getRole().getRoleName()));
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
    	
    	
    	log.info("ar) User Principal -> authorities : " +authorities);
    	return authorities;
    }
    
//    public String getRole() {
//    	return this.user.getRole().getRoleName();
//    }
    
	@Override
    public String getPassword() {
        return this.user.getUserPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUserEmail();
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
//        return this.user.isActive();
    }
}