package org.geekbang.projects.springcloud.configserver.file.redis;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * redis操作工具类
 * @author  wangyongfei
 */
@Component
public class RedisUtile {

  private static final Logger logger = LoggerFactory.getLogger(RedisUtile.class);



  @Autowired
  private RedisTemplate<Serializable,Serializable>  redisTemplate;

  /**
   * 设置过期操作
   * @param key
   * @param value
   * @param time 按seconds计算
   * @return
   */

  public boolean cacheValue(String key,Serializable value,long time){
    try{
      ValueOperations<Serializable, Serializable> valueOps =  redisTemplate.opsForValue();
      valueOps.set(key, value);
      if (time > 0){
        redisTemplate.expire(key, time, TimeUnit.SECONDS);
      }
      return true;
    }catch (Throwable t){
      logger.error("缓存[{}]失败, value[{}]",key,value,t);
    }
    return false;
  }

  /**
   * 设置过期操作，自定义时间单位
   * @param key
   * @param value
   * @param time
   * @param unit
   * @return
   */
  public  boolean cacheValue(String key, Serializable value, long time,TimeUnit unit) {
    try {
      ValueOperations<Serializable, Serializable> valueOps =  redisTemplate.opsForValue();
      valueOps.set(key, value);
      if (time > 0) redisTemplate.expire(key, time, unit);
      return true;
    } catch (Throwable t) {
      logger.error("缓存[{}]失败, value[{}]",key,value,t);
    }
    return false;
  }

  /**
   *  redis set操作
   * @param k
   * @param v
   * @return
   */
  public  boolean set(String k, Serializable v) {
    return cacheValue(k, v, -1);
  }

  /**
   * 判断key是否存在
   * @param key
   * @return
   */
  public boolean containsValueKey(String key){
      try{
        return redisTemplate.hasKey(key);
      }catch (Throwable t ){
        logger.error("判断缓存存在失败key[" + key + ", error[" + t + "]");
      }
      return false;
  }
  /**
   * 获取缓存
   * @param key
   * @return
   */
  public  Serializable getValue(String key) {
    try {
      ValueOperations<Serializable, Serializable> valueOps =  redisTemplate.opsForValue();
      return valueOps.get(key);
    } catch (Throwable t) {
      logger.error("获取缓存失败key[" +key + ", error[" + t + "]");
    }
    return null;
  }

  /**
   * 移除缓存
   * @param key
   * @return
   */
  public  boolean removeValue(String key) {
    try {
      redisTemplate.delete(key);
      return true;
    } catch (Throwable t) {
      logger.error("获取缓存失败key[" + key + ", error[" + t + "]");
    }
    return false;
  }
  /**
   * 递增
   * @param key
   * @param delta 要增加几(大于0)
   * @return
   */
  public long incr(String key, long delta) {
    if (delta < 0) {
      throw new RuntimeException("递增因子必须大于0");
    }
    return redisTemplate.opsForValue().increment(key, delta);
  }
  /**
   * 递减
   * @param key 键
   * @param delta 要减少几(小于0)
   * @return
   */
  public long decr(String key, long delta) {
    if (delta < 0) {
      throw new RuntimeException("递减因子必须大于0");
    }
    return redisTemplate.opsForValue().increment(key, -delta);
  }

    /**
     *
     * @param key
     * @param value
     */
    public  void setValue(String key, Serializable value) {
        try {
            ValueOperations<Serializable, Serializable> valueOps = redisTemplate.opsForValue();
            valueOps.set(key, value);
        }catch(Throwable t){
            logger.error("获取缓存失败key[" + key + ", error[" + t + "]");
        }
    }
    /**
     * 队列 pust
     */
    public  Long leftPushAllValue(String key, List values) {
        try {
            Long aLong = redisTemplate.opsForList().leftPushAll(key, values);
            return aLong;
        } catch (Throwable t) {

            logger.error("获取缓存失败key[" + key + ", error[" + t + "]");
        }
        return 0L;
    }
    /**
     * 队列 出列 pop
     */
    public  Serializable  rightPop(String key) {
        try {
            Serializable  serializable =  redisTemplate.opsForList().rightPop(key);
            return serializable;
        } catch (Throwable t) {

            logger.error("获取缓存失败key[" + key + ", error[" + t + "]");
        }
        return null;
    }
    /**
     * 获取list类型数据
     * @param key
     * @param start
     * @param end
     * @return
     */
    public  List<Serializable> range(String key, long start,long end) {
        try {
            List<Serializable> range = redisTemplate.opsForList().range(key, start, end);
            return range;
        } catch (Throwable t) {

            logger.error("获取缓存失败key[" + key + ", error[" + t + "]");
        }
        return null;
    }
}
