package vn.edu.iuh.fit.www_lab_week_8.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    public void globalConfig(AuthenticationManagerBuilder auth, PasswordEncoder encoder) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(User.withUsername("admin")
                        .password(encoder.encode("admin"))
                        .roles("ADMIN")
                        .build())
                .withUser(User.withUsername("thuan")
                        .password(encoder.encode("thuan"))
                        .roles("THUAN")
                        .build())
                .withUser(User.withUsername("user")
                        .password(encoder.encode("user"))
                        .roles("USER")
                        .build()
                );
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws  Exception {
        http.authorizeHttpRequests(auth->auth
                .requestMatchers("/","/home","/index").permitAll()
                .requestMatchers("/api/**").hasAnyRole("ADMIN","USER","THUAN")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated());
        http.httpBasic(Customizer.withDefaults());
    return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
