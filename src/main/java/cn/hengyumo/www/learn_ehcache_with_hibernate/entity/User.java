package cn.hengyumo.www.learn_ehcache_with_hibernate.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * User
 * TODO
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/7
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_user")
@Cacheable
// 如果在配置文件中加入了 mode: ENABLE_SELECTIVE
//  # 则不需要在实体内配置hibernate的 @cache标记，
//  # 会使用实体类名作为CacheName
//  # 只要打上JPA的@cacheable标记即可默认开启该实体的2级缓存。
// 但是通过@Cache实现的可以设置Cache位置，如entityCache
// @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class User implements Serializable {
    @Id
    private Integer id;

    @Length(min = 0, max = 25, message = "姓名应该在0-25之间")
    @NotBlank
    private String name;

    @Length(min = 6, max = 12, message = "密码应该在6-12位之间")
    @NotBlank
    private String password;
}
