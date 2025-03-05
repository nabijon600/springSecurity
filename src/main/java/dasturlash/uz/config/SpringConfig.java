package dasturlash.uz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

@Configuration //Bean contaninerga bu yerdagi methodlarniyam qo'sh
@EnableWebSecurity //web security bor
public class SpringConfig {

    @Bean
    public AuthenticationProvider authenticationProvider() {
        // authentication - Foydalanuvchining identifikatsiya qilish.
        // Ya'ni berilgan login va parolli user bor yoki yo'qligini aniqlash.
        String pass = UUID.randomUUID().toString(); //p1
        pass="12345"; //p2
        BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
        pass = bc.encode("123456"); //p3
        System.out.println("user pass mazgi: "+pass);
        UserDetails user = User.builder()
                .username("mazgi")
                //.password("{noop}"+pass)//pass1
                .password("{bcrypt}"+pass)
                .roles("USER")
                .build(); //ecurity ga defoult user ni ruchnoy yarattik

        final DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(); //<- shu obyekt orqali
        daoAuthenticationProvider.setUserDetailsService(new InMemoryUserDetailsManager(user)); //va InM.. xotiraga joylab qoydik /baza/file bolMumkun
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // authorization - Foydalanuvchining tizimdagi huquqlarini tekshirish.
        // Ya'ni foydalanuvchi murojat qilayotgan API-larni ishlatishga ruxsati bor yoki yo'qligini tekshirishdir.
        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
            authorizationManagerRequestMatcherRegistry
                    .requestMatchers("/task").permitAll() //murojat qlyotkan url /task mos bolsa, permitAll-ruxsat | authenticated qilmaydi m:/task
                    .requestMatchers("/task/*").permitAll() //murojat qlyotkan url /task mos bolsa va qandaydir qiymati bolsa, permitAll-ruxsat | authenticated qilmaydi
                    .requestMatchers(HttpMethod.GET,"/task/*").permitAll() //murojat qlyotkan url /task mos bolsa va qandaydir qiymati bolsa va GET type bolsa, permitAll-ruxsat | authenticated qilmaydi m:/task/, /task/qqq-aaaa-zzzz-xxx, /task/activ
                    .requestMatchers("/task/**").permitAll() //murojat qlyotkan url /tast bn boshlangan /tast/activ/all/../.. barchasiga permitAll-ruxsat | authenticated qilmaydi m:/task/../../../..
                    .requestMatchers("/task/finished/all", "/task/my/all").permitAll() //murojat qlyotkan url /tast bn boshlangan /task/finished/all va /task/my/all barchasiga permitAll-ruxsat | authenticated qilmaydi
                    .anyRequest() //krb kelyotgan barcha requestlarni
                    .authenticated();    //avtorizatsiyadan otkaz
        }).formLogin(Customizer.withDefaults()); //sp sec taqdm qilgan defoult lognPageni chaqir

        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(AbstractHttpConfigurer::disable);

        return http.build(); //shu methodni obyektni build qlb yubor
    }
}
