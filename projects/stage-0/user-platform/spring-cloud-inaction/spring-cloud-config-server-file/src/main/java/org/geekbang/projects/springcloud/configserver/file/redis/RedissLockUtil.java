package org.geekbang.projects.springcloud.configserver.file.redis;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * redis分布式锁帮助类
 * @author wangyongfei
 */

public class RedissLockUtil {

  private static final Logger logger = LoggerFactory.getLogger(RedissLockUtil.class);

  private static RedissonClient redissonClient;

    
    public void setRedissonClient(RedissonClient locker) {
    	redissonClient = locker;
    }
    
    /**
     * 加锁
     * @param lockKey
     * @return
     */
    public static RLock lock(String lockKey) {
    	RLock lock = redissonClient.getLock(lockKey);
    	lock.lock();
    	return lock;
    }

    /**
     * 释放锁
     * @param lockKey
     */
    public static void unlock(String lockKey) {
    	RLock lock = redissonClient.getLock(lockKey);
		  lock.unlock();
    }
    
    /**
     * 释放锁
     * @param lock
     */
    public static void unlock(RLock lock) {
    	lock.unlock();
    }

    /**
     * 带超时的锁
     * @param lockKey
     * @param timeout 超时时间   单位：秒
     */
    public static RLock lock(String lockKey, int timeout) {
    	RLock lock = redissonClient.getLock(lockKey);
      lock.lock(timeout, TimeUnit.SECONDS);
      return lock;
    }
    
    /**
     * 带超时的锁
     * @param lockKey
     * @param unit 时间单位
     * @param timeout 超时时间
     */
    public static RLock lock(String lockKey, TimeUnit unit ,int timeout) {
    	RLock lock = redissonClient.getLock(lockKey);
      lock.lock(timeout, unit);
      return lock;
    }
    
    /**
     * 尝试获取锁
     * @param lockKey
     * @param waitTime 最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     * @return
     */
    public static boolean tryLock(String lockKey, int waitTime, int leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
          return lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
          logger.error("尝试获取锁失败......[{}]",e);
          return false;
        }
    }
    
    /**
     * 尝试获取锁
     * @param lockKey
     * @param unit 时间单位
     * @param waitTime 最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     * @return
     */
    public static boolean tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime) {
    	RLock lock = redissonClient.getLock(lockKey);
      try {
        return lock.tryLock(waitTime, leaseTime, unit);
      } catch (InterruptedException e) {
        logger.error("尝试获取锁失败......[{}]",e);
        return false;
      }
    }

}