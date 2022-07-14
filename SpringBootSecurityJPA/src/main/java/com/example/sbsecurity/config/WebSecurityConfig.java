package com.example.sbsecurity.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.example.sbsecurity.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig  {
	
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	
	@Autowired
	private DataSource dataSource;
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder;
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		
		// set đặt dịch vụ để tìm kiếm User trong Database.
		// Và set đặt PasswordEncoder.
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
	
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf().disable();
		
		// Các trang không yêu cầu login
		http.authorizeRequests().antMatchers("/","/login","/logout").permitAll();
		
		// Trang /userInfo yêu cầu phải login với vai trò ROLE_USER hoặc ROLE_ADMIN.
		// Nếu chưa login, nó sẽ direct tới trang /login.
		http.authorizeRequests().antMatchers("/userInfor").access("hasAnyRole('ROLE_USER','ROLE_ADMIN')");
		
		// Trang chỉ dành cho ADMIN
		http.authorizeRequests().antMatchers("/admin").access("hasRole('ROLE_ADMIN')");
		
		// Khi người dùng đã login, với vai trò XX
		// Nhưng truy cập vào trang yêu cầu vai trò YY,
		// Ngoại lệ AccessDeniedException sẽ ném ra.
		http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");
		
		// Cấu hình cho Login Form.
		http.authorizeRequests().and().formLogin()//
				// Submit URL của trang Login
				.loginProcessingUrl("/j_spring_security_check")// Submit URL
				.loginPage("/login")//
				.defaultSuccessUrl("/userAccountInfo")//
				.failureUrl("/login?error=true")//
				.usernameParameter("username")//
				.passwordParameter("password")
				// Cấu hình cho Logout Page.
				.and().logout().logoutUrl("/logout").logoutSuccessUrl("/logoutSuccessful");
		
		// Cấu hình Remember Me.
		http.authorizeRequests().and()//
				.rememberMe().tokenRepository(this.persistentTokenRepository())//
				.tokenValiditySeconds(1*24*60*60); // 86400s = 24h
	}
	
	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
		db.setDataSource(dataSource);
		return db;
	}

}