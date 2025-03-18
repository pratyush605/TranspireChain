package org.blockchain.TranspireChain.Security.Model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MyUserDetails implements UserDetails {

    private Long userId;

    private String username;

    private String password;

    private String email;

    private List<String> hasAuthority;

    private List<String> pagePermission;

    public MyUserDetails() {
    }

    public MyUserDetails(Long userId, String username, String password, String email, List<String> hasAuthority, List<String> pagePermission) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.hasAuthority = hasAuthority;
        this.pagePermission = pagePermission;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for(String authority : hasAuthority){
            authorities.add(new SimpleGrantedAuthority(authority));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getHasAuthority() {
        return hasAuthority;
    }

    public void setHasAuthority(List<String> hasAuthority) {
        this.hasAuthority = hasAuthority;
    }

    public List<String> getPagePermission() {
        return pagePermission;
    }

    public void setPagePermission(List<String> pagePermission) {
        this.pagePermission = pagePermission;
    }
}
