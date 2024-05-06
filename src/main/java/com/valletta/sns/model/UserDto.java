package com.valletta.sns.model;

import com.valletta.sns.model.constant.UserRole;
import com.valletta.sns.model.entity.UserEntity;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

// TODO: implement
@Getter
@Builder
public class UserDto implements UserDetails {

    private Integer id;
    private String userName;
    private String password;
    private UserRole userRole;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static UserDto fromEntity(UserEntity userEntity) {
        return UserDto.builder()
            .id(userEntity.getId())
            .userName(userEntity.getUserName())
            .password(userEntity.getPassword())
            .userRole(userEntity.getUserRole())
            .registeredAt(userEntity.getRegisteredAt())
            .updatedAt(userEntity.getUpdatedAt())
            .deletedAt(userEntity.getDeletedAt())
            .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.getUserRole().toString()));
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.deletedAt == null;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.deletedAt == null;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.deletedAt == null;
    }

    @Override
    public boolean isEnabled() {
        return this.deletedAt == null;
    }
}
