package cn.hengyumo.www.learn_ehcache_with_hibernate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class LearnEhcacheWithHibernateApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearnEhcacheWithHibernateApplication.class, args);
    }
}
