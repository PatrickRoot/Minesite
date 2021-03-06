package tech.minesoft.mine.site.common.vo;

import tech.minesoft.mine.site.mysql.models.MsUser;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tech.minesoft.mine.site.core.vo.MineAuthority;

import java.util.Collection;
import java.util.List;

@Data
public class LoginUser implements UserDetails {
    private MsUser msUser;
    private List<MineAuthority> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return msUser.getPassword();
    }

    @Override
    public String getUsername() {
        return msUser.getUsername();
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
        return 1 == msUser.getStatus();
    }
}
