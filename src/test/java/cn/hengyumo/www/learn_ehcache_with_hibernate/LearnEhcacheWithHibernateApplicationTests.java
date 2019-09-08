package cn.hengyumo.www.learn_ehcache_with_hibernate;

import cn.hengyumo.www.learn_ehcache_with_hibernate.entity.User;
import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class LearnEhcacheWithHibernateApplicationTests {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Test
    public void contextLoads() {
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        Session session1 = sessionFactory.openSession();
        Session session2 = sessionFactory.openSession();
        showCache();
        User user1 = session1.load(User.class, 1);
        System.out.println(user1);
        showCache();
        // 验证一级缓存生效，同一个session
        User user3 = session1.load(User.class, 1);
        System.out.println(user3);
        session1.close();
        showCache();
        // 验证二级缓存生效，同一个sessionFactory
        User user2 = session2.load(User.class, 1);
        System.out.println(user2);
        session2.close();
        showCache();
    }

    private void showCache() {
        CacheManager cacheManager = CacheManager.getInstance();
        String[] cacheNames = cacheManager.getCacheNames();
        log.info("缓存的key cacheNames length := "
                + cacheNames.length + " 具体详细列表如下：");
        for (String cacheName : cacheNames) {
            log.info("cacheName := " + cacheName);
        }

        Cache cache = cacheManager.getCache("entityCache");
        List cacheKeys = cache.getKeys();
        for (Object key : cacheKeys) {
            log.info(key + " = " + cache.get(key));
        }

        log.info("-------------------------------------------------");
    }
}
