package com.example.blogpost.security;

import static org.springframework.security.config.Customizer.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.blogpost.util.constant.Privillage;

//     // @Bean
//     // public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//     // http.authorizeHttpRequests(null)
//     // }

//     @Bean
//     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

//         http
//                 .authorizeHttpRequests((authorize) -> authorize
//                         .requestMatchers(WHITELIST).permitAll() // Use .antMatchers() for matching
//                         .anyRequest().authenticated())
//                 .build();

//         http
//     .authorizeHttpRequests((authorize) -> authorize
//         .requestMatchers(WHITELIST).hasAuthority("USER")
//         .anyRequest().authenticated()
//     );

//         return http.build();
//     }
// }

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
// @EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {
        private static final String[] WHITELIST = {
                        "/",
                        "/login1",
                        "/register",
                        "/db-console/**",
                        "/resources/**",
                        // "/post/**"
                        // "/edit"
                        "/post/**"
                        // "/post_add/**",
                        // "/post_views/**"
        };
        // "/db-console/**",
        // @Bean
        // public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        // http
        // .authorizeHttpRequests(requests -> requests
        // .requestMatchers(WHITELIST)
        // .permitAll()
        // .anyRequest()
        // .authenticated());

        // return http.build();
        // }

        // public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        // http
        // .
        // }


    
    @Bean
    public static PasswordEncoder PasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @SuppressWarnings("removal")
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // http.authorizeHttpRequests(
        //         requests -> {
        //                 try {
        //                     requests.requestMatchers(WHITELIST)
        //                             .permitAll()
        //                             .anyRequest()
        //                             .authenticated()
        //                             .and()
        //                             .formLogin(login -> {
        //                                 try {
        //                                     login
        //                                             .loginPage("/login")
        //                                             .loginProcessingUrl("/login")
        //                                             .usernameParameter("email")
        //                                             .passwordParameter("pass")
        //                                             .defaultSuccessUrl("/",true)
        //                                             .failureUrl("/login?error")
        //                                             .permitAll();
                                                    
        //                                 } catch (Exception e) {
        //                                     e.printStackTrace();
        //                                 }
        //                             })
        //                             .logout(logout-> {
        //                             try {
        //                                 logout
        //                                     .logoutUrl("/logout")
        //                                             .logoutSuccessUrl("/logout?success")
        //                                         .and()
        //                                         .httpBasic(withDefaults());
        //                             } catch (Exception e) {
        //                                 e.printStackTrace();
        //                             }
        //                             });
        //                 } catch (Exception e) {
        //                         e.printStackTrace();
        //                 }
        //         });




        http.authorizeHttpRequests(requests -> {
            requests.requestMatchers(WHITELIST)
                    .permitAll()
                    .requestMatchers("/profile/**").authenticated()
                    .requestMatchers("/update_photo/**").authenticated()
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .requestMatchers("/editor/**").hasAnyRole("ADMIN","EDITOR")
                    .requestMatchers("/admin/**").hasAuthority(Privillage.ACCESS_ADMIN_PANEL.getPrivillage());
        })
                .formLogin(login -> {
                    login
                            .loginPage("/login")
                            .loginProcessingUrl("/login")
                            .usernameParameter("email")
                            .passwordParameter("password")
                            .defaultSuccessUrl("/", true)
                            .failureUrl("/login?error")
                            .permitAll();
                })
                .logout(logout -> {
                    logout
                            .logoutUrl("/logout")
                            .logoutSuccessUrl("/");
                })
                .rememberMe(rememberMe -> rememberMe.key("remember-me"))
                .httpBasic(withDefaults());
        
        
        







        // .loginProcessingUrl("login1")
        // .usernameParameter("email")
        // .passwordParameter("password")
        // .defaultSuccessUrl("/")
                
        // http.logout((logout)-> logout(withDefaults()));

        // http.logout(withDefaults()) .logoutSuccessUrl("").permitAll();

        http.csrf(csrf -> csrf.disable());
        http.headers(headers -> headers.frameOptions().disable());
        // http.headers(headers -> headers.addHeaderWriter(new StaticHeadersWriter("X-Frame-Options", "SAMEORIGIN")));



        return http.build();
    }

    
}