package cn.hengyumo.www.learn_ehcache_with_hibernate.dao;

import cn.hengyumo.www.learn_ehcache_with_hibernate.entity.User;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.QueryHint;
import java.util.List;

/**
 * UserDao
 * TODO
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/7
 */
@Repository
public interface UserDao extends JpaRepository<User, Integer> {

    // 无法触发二级缓存，二级缓存只能通过id选择时触发，但是可以选择Query cache
    User findUserByName(String name);

    // spring-data-jpa默认继承实现的一些方法，实现类为
    // SimpleJpaRepository。
    // 该类中的方法不能通过@QueryHint来实现查询缓存。
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
    // @Query("from User u where password = :password")
    List<User> findAll();

    @Query(value = "select * from t_user where password = :password limit 1", nativeQuery = true)
    // 启用Query cache，使用hql或者sql都可以触发
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
    // @Query("from User u where password = :password")
    User findUserByPassword(String password);

    // remove  根据命名规则自定义条件删除所有方法所报的错，需添加事务支持，
    @Transactional
    // @Modifying 自己写query才要加
    void deleteByName(String name);

    // update/delete query 并且自己写query 必须加@Modifying @Transactional
    @Modifying
    @Transactional
    @Query(value = "update t_user set password = :password where id = :id", nativeQuery = true)
    void updatePasswordById(Integer id, String password);
}
