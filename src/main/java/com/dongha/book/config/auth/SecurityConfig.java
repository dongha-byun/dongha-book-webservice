package com.dongha.book.config.auth;

import com.dongha.book.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity // 1. SpringSecurity 설정 활성화 어노테이션
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // cross site request forgery(csrf) 비활성화
                .headers().frameOptions().disable() //
                .and()
                    .authorizeRequests() // url 별로 권한관리를 설정하기 위한 시작점 - 해당 메서드 이후로 antMatcher 메서드를 호출하여 권한을 지정할 수 있다.
                    .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll() // antMatcher 에 권한을 지정할 대상 uri 를 나열하고, permitAll() 메서드로 모두에게 호출 가능하게끔 권한을 열어둔다.
                    .antMatchers("/api/v1/**").hasRole(Role.USER.name()) // antMatcher 에 나열된 uri 들은 USER 권한을 가진 사용자에게만 허용된다.
                    .anyRequest().authenticated() // anyRequest 메서드는 위에서 antMatcher 메서드를 통해 지정되지 않은 모든 uri 를 의미하고, authenticated 메서드를 통해 인가를 받은 요청에서만 접근을 허용한다.
                .and()
                    .logout() // logout 에 대한 설정의 시작점
                    .logoutSuccessUrl("/") // logout 성공 후, 인덱스(/) 페이지로 돌아간다는 의미
                .and()
                    .oauth2Login() // oauth2 인증 로그인에 대한 설정의 시작점
                    .userInfoEndpoint() // oauth2 로그인 성공 후에 user 정보를 가져올 때의 설정의 시작점
                    .userService(customOAuth2UserService); // 로그인 성공 후, 후속 처리에 대한 service 를 등록 / 사용자 정보를 가져온 이후, 추가적인 작업을 명시한다. 이 때 service 는 OAuth2UserService interface 의 구현체여야 한다.
    }
}
