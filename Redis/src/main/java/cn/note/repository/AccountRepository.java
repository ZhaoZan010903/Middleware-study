package cn.note.repository;

import cn.note.pojo.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * CrudRepository 提供基本CRUD
 * PagingAndSortingRepository 基本CRUD提供分页排序
 *
 * 实现机制: JDK动态代理 , 调用对应jedis命令
 * @author ASUS
 */

public interface AccountRepository extends CrudRepository<Account,Integer>, PagingAndSortingRepository<Account,Integer> {

}
