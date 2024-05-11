package com.valletta.sns.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.valletta.sns.model.constant.UserRole;
import com.valletta.sns.model.entity.UserEntity;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
//@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class UserDto implements UserDetails {

    private Integer id;
    private String username;
    private String password;
    private UserRole userRole;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static UserDto fromEntity(UserEntity userEntity) {
        return UserDto.builder()
            .id(userEntity.getId())
            .username(userEntity.getUserName())
            .password(userEntity.getPassword())
            .userRole(userEntity.getUserRole())
            .registeredAt(userEntity.getRegisteredAt())
            .updatedAt(userEntity.getUpdatedAt())
            .deletedAt(userEntity.getDeletedAt())
            .build();
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.getUserRole().toString()));
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return this.deletedAt == null;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return this.deletedAt == null;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return this.deletedAt == null;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return this.deletedAt == null;
    }
}
