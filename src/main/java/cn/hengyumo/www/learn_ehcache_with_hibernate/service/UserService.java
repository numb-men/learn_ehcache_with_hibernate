package cn.hengyumo.www.learn_ehcache_with_hibernate.service;

import cn.hengyumo.www.learn_ehcache_with_hibernate.dao.UserDao;
import cn.hengyumo.www.learn_ehcache_with_hibernate.entity.User;
import org.hibernate.annotations.Cache;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * UserService
 * TODO
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/7
 */
@Service
public class UserService {

    @Resource
    private UserDao userDao;

    // 普通缓存使用，将返回结果缓存，如果下一次遇到同样的参数则直接返回
    // @Cacheable(cacheNames = "entityCache", key = "#root.methodName + '_' + #name")
    public User findByName(String name) {
        return userDao.findUserByName(name);
    }
}
