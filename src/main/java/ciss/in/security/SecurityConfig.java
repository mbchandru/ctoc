package ciss.in.security;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import ciss.in.repositories.UserRepository;
//import ciss.in.xmpp.template.XmppAuthenticationProvider;

@EnableWebMvcSecurity
public class SecurityConfig {
	
	//@Autowired
    //private static XmppAuthenticationProvider xmppAuthenticationProvider;

/*	@Autowired
    private static CustomLogoutSuccessHandler logoutSuccessHandler;*/
	
	@Configuration
	@Order(1)
	public static class UserSecurity extends WebSecurityConfigurerAdapter {

	    @Override
	    protected void configure(HttpSecurity http) throws Exception {
	    	CustomUserAuthenticationSuccessHandler userHandle = new CustomUserAuthenticationSuccessHandler();    
	    	CustomLogoutSuccessHandler logoutSuccessHandler = new CustomLogoutSuccessHandler();
	        http
	        /*.antMatcher("/user/**")*/.authorizeRequests()
	            .antMatchers("/index","/user/error","/user/forgotpassword","/user/resetpassword","/user/register","/","/js/**","/store/**","/assets/**","/css/**","/fonts/**","/images/**","/home/**","/webjars/**").permitAll()
	            .anyRequest().hasRole("USER")// .authenticated()//.hasRole("USER")
	            .and()
	        .formLogin()
	            .loginPage("/user/login").successHandler(userHandle)
	            .permitAll()
	            .and()
	        .httpBasic().and()
	    	.csrf()//.disable();
	    	.csrfTokenRepository(csrfTokenRepository()).disable()
	        .logout()
			.logoutSuccessHandler(logoutSuccessHandler)
			.logoutUrl("/user/logout")
	        .logoutSuccessUrl("/")
            .deleteCookies("JSESSIONID")
	        .invalidateHttpSession(true)
	        .permitAll()
	        .and().sessionManagement().maximumSessions(1).maxSessionsPreventsLogin(true).expiredUrl("/user/login").sessionRegistry(sessionRegistry())
	            ;
	    }
	    
	    @Bean
	    public SessionRegistry sessionRegistry() {
	        SessionRegistry sessionRegistry = new SessionRegistryImpl();
	        return sessionRegistry;
	    }
	    
	    // Register HttpSessionEventPublisher
	    @Bean
	    public static ServletListenerRegistrationBean<EventListener> httpSessionEventPublisher() {
	        return new ServletListenerRegistrationBean<EventListener>(new HttpSessionEventPublisher());
	    }
	    
	    private CsrfTokenRepository csrfTokenRepository()  { 
	        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository(); 
	        repository.setSessionAttributeName("_csrf");
	        return repository; 
	    }
	    

	    @Autowired UserRepository userRepository;
	    ciss.in.models.User user;
		@Bean
		UserDetailsService user1DetailsService() {
			return new UserDetailsService() {
			      @Override
			      public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
			    	  user = userRepository.findByUsername(username);
			    	  org.springframework.security.core.userdetails.User newUser = null;
			    	  CustomUserDetails customUser = null;
			    	  if (user==null) {
			    		  	throw new UsernameNotFoundException("No such user: " + username);
			    	  }
			    	  else {
			    		  newUser = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), true, true, true, true,
			    			  getAuthorities(user.getRole()));//AuthorityUtils.createAuthorityList("USER"));
			    		  		customUser = new CustomUserDetails(newUser,getAuthorities(user.getRole()));
			    	  }
				        return customUser;
			      }	      
			    };
		}   

		public List<GrantedAuthority> getAuthorities(Integer role) {
			    
			List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
				if (role.intValue() == 1) {
				  authList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
				} else if (role.intValue() == 2) {
				  authList.add(new SimpleGrantedAuthority("ROLE_USER"));
				}
				return authList;
		  }
			
	    @Override
	    public void configure(AuthenticationManagerBuilder auth) throws Exception {	
			    auth.eraseCredentials(false)
			    .userDetailsService(user1DetailsService())
			    		.passwordEncoder(new BCryptPasswordEncoder());
		}	
	}
	
/*    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(xmppAuthenticationProvider);
    }*/
}    