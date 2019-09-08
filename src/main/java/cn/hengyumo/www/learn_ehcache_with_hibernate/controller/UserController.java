package cn.hengyumo.www.learn_ehcache_with_hibernate.controller;

import cn.hengyumo.www.learn_ehcache_with_hibernate.dao.UserDao;
import cn.hengyumo.www.learn_ehcache_with_hibernate.entity.User;
import cn.hengyumo.www.learn_ehcache_with_hibernate.service.UserService;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

/**
 * UserController
 * TODO
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/7
 */
@RestController
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private UserDao userDao;

    // 无法触发一级缓存也无法触发二级缓存
    @GetMapping("/user/{name}")
    public User findByName(@PathVariable("name") String name) {

        User result1 = userService.findByName(name);
        showCache();
        User result = userService.findByName(name);
        showCache();
        return result;
    }

    // 事实证明，只有按照id查询的才能触发二级 cache
    // 当Hibernate根据ID访问数据对象的时候，首先从Session一级缓存中查；
    // 查不到，如果配置了二级缓存，那么从二级缓存中查；查不到，再查询数据库，把结果按照ID放入到缓存。
    @GetMapping("/user/id/{id}")
    public User findById(@PathVariable("id") Integer id) {
        User result1 = userDao.findById(id).orElse(null);
        showCache();
        User result = userDao.findById(id).orElse(null);
        showCache();
        return result;
    }

    // jpa自带delete能自动清除缓存，下一次get会发现缓存清空，再次去查表，然后返回空
    @DeleteMapping("/user/id/{id}")
    public void deleteById(@PathVariable("id") Integer id) {
        userDao.deleteById(id);
    }

    // jpa自带方法能够自动刷新缓存，再次get还是从缓存中取，数据正确
    @PutMapping("/user/id/{id}")
    public User updateById(@PathVariable("id") Integer id, User user) {
        user.setId(id);
        return userDao.save(user);
    }

    // jpa 方法生成的delete语句可以自动清除缓存
    @DeleteMapping("user/name/{name}")
    public void deleteByName(@PathVariable("name") String name) {
        userDao.deleteByName(name);
    }

    // 自己通过query写的语句，也能正确触发cache的刷新！！！
    @PutMapping("user/updatePasswordById/{id}")
    public void updatePasswordById(@PathVariable("id") Integer id,
                                   @RequestParam String password) {
        userDao.updatePasswordById(id, password);
    }

    // findAll之后，再findById能触发二级 Cache!!
    @GetMapping("/user")
    public List<User> getAll() {
        return userDao.findAll();
    }

    // 测试如何触发query cache
    @GetMapping("/user/pwd/{pwd}")
    public User findByPassword(@PathVariable("pwd") String pwd) {
        userDao.findUserByPassword(pwd);
        showCache();
        User u = userDao.findUserByPassword(pwd);
        showCache();
        return u;
    }

    private void showCache() {
        CacheManager cacheManager = CacheManager.getInstance();
        String[] cacheNames = cacheManager.getCacheNames();
        System.out.println("缓存的key cacheNames length := "
                + cacheNames.length + " 具体详细列表如下：");
        for (String cacheName : cacheNames) {
            System.out.println("cacheName := " + cacheName);
        }

        Cache cache = cacheManager.getCache("entityCache");
        List cacheKeys = cache.getKeys();
        for (Object key : cacheKeys) {
            System.out.println(key + " = " + cache.get(key));
        }
    }

}
