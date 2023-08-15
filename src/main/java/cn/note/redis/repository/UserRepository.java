package cn.note.redis.repository;

import cn.note.redis.pojo.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * CrudRepository 提供基本CRUD
 * PagingAndSortingRepository 基本CRUD提供分页排序
 *
 * 实现机制: JDK动态代理 , 调用对应jedis命令
 */
public interface UserRepository extends CrudRepository<User,Integer>,PagingAndSortingRepository<User,Integer>{

}
