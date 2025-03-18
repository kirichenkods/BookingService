package ru.sber.bookingservice.model.userdetails;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.sber.bookingservice.model.enums.Role;

import java.util.Collection;

@Builder
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final String username;
    private final Long id;
    private final String email;
    private final String phone;
    private final String name;
    private final String surname;
    private final Role role;
    private final Boolean enabled;
    private final Boolean accountNotExpired;
    private final Boolean accountNotLocked;
    private final Boolean credentialsNotExpired;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNotExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNotLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNotExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return this.name;
    }

    public String getSurname() {
        return this.surname;
    }
}
