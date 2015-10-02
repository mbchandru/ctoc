package ciss.in.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import ciss.in.repositories.UserRepository;

@EnableWebMvcSecurity
	//@Configuration
	public class SecurityConfig {
	
	@Configuration
	@Order(1)
	public static class UserSecurity extends WebSecurityConfigurerAdapter {

	    @Override
	    protected void configure(HttpSecurity http) throws Exception {
	    	CustomUserAuthenticationSuccessHandler userHandle = new CustomUserAuthenticationSuccessHandler();    
	    	
	        http
	        /*.antMatcher("/user/**")*/.authorizeRequests()
	            .antMatchers("/user/register","/","/js/**","/assets/**","/css/**","/fonts/**","/images/**","/home/**","/webjars/**").permitAll()
	            .anyRequest().hasRole("USER")// .authenticated()//.hasRole("USER")
	            .and()
	        .formLogin()
	            .loginPage("/user/login").successHandler(userHandle)
	            .permitAll()
	            .and()
	        .httpBasic().and()
	    	.csrf()//.disable();
	    	.csrfTokenRepository(csrfTokenRepository()).disable()
	        .logout().logoutUrl("/user/logout").invalidateHttpSession(true).logoutSuccessUrl("/")
	            .permitAll();
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
	
			    	  org.springframework.security.core.userdetails.User newUser = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), true, true, true, true,
			    			  getAuthorities(user.getRole()));//AuthorityUtils.createAuthorityList("USER"));
			    	  	CustomUserDetails customUser = new CustomUserDetails(newUser,getAuthorities(user.getRole()));
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
			    auth.userDetailsService(user1DetailsService())
			    		.passwordEncoder(new BCryptPasswordEncoder());
		}	
	}
	
/*	@Configuration
    public static class SupplierSecurity extends WebSecurityConfigurerAdapter {
    	CustomSupplierAuthenticationSuccessHandler supplierHandle = new CustomSupplierAuthenticationSuccessHandler();    
    
        @Override
        protected void configure(HttpSecurity http) throws Exception {
        	
            http
            .antMatcher("/supplier/**").authorizeRequests()
                .antMatchers("/js/**","/assets/**","/css/**","/fonts/**","/images/**","/home/**","/webjars/**").permitAll()
                .anyRequest().hasRole("SUPPLIER")//.authenticated()
                .and()
            .formLogin()
                .loginPage("/supplier/login").successHandler(supplierHandle)
                .permitAll()
                .and()
            .httpBasic().and()
        	.csrf()//.disable();
        	.csrfTokenRepository(csrfTokenRepository()).disable()
            .logout().logoutUrl("/supplier/logout").invalidateHttpSession(true).logoutSuccessUrl("/")
                .permitAll();
        }
        
        private CsrfTokenRepository csrfTokenRepository()  { 
            HttpSessionCsrfTokenRepository repository1 = new HttpSessionCsrfTokenRepository(); 
            repository1.setSessionAttributeName("_csrf");
            return repository1; 
        }
        
		@Autowired
		SupplierRepository supplierRepository;
		 
		@Bean
		UserDetailsService supplierDetailsService() {
			return new UserDetailsService() {
 
				@Override
				public CustomSupplierDetails loadUserByUsername(String supplierName) throws UsernameNotFoundException {
					Supplier supplier = supplierRepository.findByUsername(supplierName);
	
					org.springframework.security.core.userdetails.User newUser = new org.springframework.security.core.userdetails.User(supplier.getUsername(), supplier.getPassword(), true, true, true, true,
							AuthorityUtils.createAuthorityList("SUPPLIER"));
					CustomSupplierDetails customUser = new CustomSupplierDetails(newUser,AuthorityUtils.createAuthorityList("SUPPLIER"));
					return customUser;
				}    		      
			};
		}
		  
	    @Override
	    public void configure(AuthenticationManagerBuilder auth1) throws Exception {
		    auth1.userDetailsService(supplierDetailsService())
    		.passwordEncoder(new BCryptPasswordEncoder());	    	
	    }      
    }*/
}    