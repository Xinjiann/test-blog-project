package com.blog.shiro;


import cn.hutool.core.bean.BeanUtil;
import com.blog.entity.User;
import com.blog.service.UserService;
import com.blog.util.JwtUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountRealm extends AuthorizingRealm {

  @Autowired
  JwtUtils jwtUtils;

  @Autowired
  UserService userService;

  @Override
  public boolean supports(AuthenticationToken token) {
    return token instanceof JwtToken;
  }

  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
    return null;
  }

  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
      throws AuthenticationException {
    JwtToken jwtToken = (JwtToken) token;

    String userId = jwtUtils.getClaimByToken((String) jwtToken.getPrincipal()).getSubject();

    User user = userService.getById(Long.valueOf(userId));
    if (user == null) {
      throw new UnknownAccountException("account not exist");
    }

    if (user.getStatus() == -1) {
      throw new LockedAccountException("account locked");
    }

    AccountProfile profile = new AccountProfile();
    BeanUtil.copyProperties(user, profile);

    return new SimpleAuthenticationInfo(profile, jwtToken.getCredentials(), getName());
  }
}
