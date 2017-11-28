package com.qqShopping.user.util;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis工具类
 * 
 * @author lujun.chen
 * @version [版本号, 2016年7月27日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public final class RedisUtil {

    private static Logger logger = LogManager.getLogger(RedisUtil.class);

    private static String ADDR = "192.168.111.128";

    private static int PORT = 6379;
    
    private static int TIMEOUT = 10000;

    private static JedisPool jedisPool = null;
    
    private static String AUTH = "admin";

    private static Jedis jedis = null;

    /**
     * 初始化Redis连接池
     */
    static {
        try {
            init();
        } catch (Exception e) {
            logger.error("初始化Redis出错，" + e);
        }
    }

    /**
     * 初始化连接池
     * 
     * @see [类、类#方法、类#成员]
     */
    private synchronized static void init() {

        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(200);
        config.setMaxIdle(50);
        config.setMinIdle(8);//设置最小空闲数
        config.setMaxWaitMillis(10000);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
      //Idle时进行连接扫描
        config.setTestWhileIdle(true);
        //表示idle object evitor两次扫描之间要sleep的毫秒数
        config.setTimeBetweenEvictionRunsMillis(30000);
        //表示idle object evitor每次扫描的最多的对象数
        config.setNumTestsPerEvictionRun(10);
        //表示一个对象至少停留在idle状态的最短时间，然后才能被idle object evitor扫描并驱逐；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义
        config.setMinEvictableIdleTimeMillis(60000);
        jedisPool = new JedisPool(config, ADDR,PORT,TIMEOUT,AUTH);   
    }

    /**
     * 获取Jedis实例
     * 
     * @return
     */
    private static Jedis getJedis() {
        try {
            if (jedisPool != null) {
                jedis = jedisPool.getResource();
            } else {
                init();
                jedis = jedisPool.getResource();
            }
        } catch (Exception e) {
            logger.error("获取Redis实例出错，" + e);
        }
        return jedis;
    }

    /**
     * 设置单个值
     * 
     * @param key
     * @param value
     * @return
     */
    public static String set(String key, String value) {
        return set(key, value, null);
    }

    /**
     * 设置单个值，并设置超时时间
     * 
     * @param key
     *            键
     * @param value
     *            值
     * @param timeout
     *            超时时间（秒）
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String set(String key, String value, Integer timeout) {
        String result = null;

        Jedis jedis = RedisUtil.getJedis();
        if (jedis == null) {
            return result;
        }
        try {
            result = jedis.set(key, value);
            if (null != timeout) {
                jedis.expire(key, timeout);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return result;
    }
    
    public static Long setList(String key,String value){
    	Long result = null;

        Jedis jedis = RedisUtil.getJedis();
        if (jedis == null) {
            return result;
        }
        try {
            result = jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return result;
    }

    /**
     * 获取单个值
     * 
     * @param key
     * @return
     */
    public static String get(String key) {
        String result = null;
        Jedis jedis = RedisUtil.getJedis();
        if (jedis == null) {
            return result;
        }
        try {
            result = jedis.get(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return result;
    }
    
    public static List<String> getList(String key){
    	List<String> result=null;
    	Jedis jedis=RedisUtil.getJedis();
    	 if (jedis == null) {
             return result;
         }
    	 try {
             result = jedis.lrange(key, 0, -1);
         } catch (Exception e) {
             logger.error(e.getMessage(), e);
         } finally {
             if (null != jedis) {
                 jedis.close();
             }
         }
    	return result;
    }

    /**
     * 删除redis中数据
     * 
     * @param key
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static boolean del(String key) {
        Boolean result = Boolean.FALSE;
        Jedis jedis = RedisUtil.getJedis();
        if (null == jedis) {
            return Boolean.FALSE;
        }
        try {
            jedis.del(key);
        } catch (Exception e) {
            logger.error("删除redis数据出错，" + e);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return result;
    }

    /**
     * 追加
     * 
     * @param key
     * @param value
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static Long append(String key, String value) {
        Long result = Long.valueOf(0);
        Jedis jedis = RedisUtil.getJedis();
        if (null == jedis) {
            return result;
        }
        try {
            result = jedis.append(key, value);
        } catch (Exception e) {
            logger.error("追加redis数据出错，" + e);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return result;
    }

    /**
     * 检测是否存在
     * 
     * @param key
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static Boolean exists(String key) {
        Boolean result = Boolean.FALSE;
        Jedis jedis = RedisUtil.getJedis();
        if (null == jedis) {
            return result;
        }
        try {
            result = jedis.exists(key);
        } catch (Exception e) {
            logger.error("检查是否存在出错：，" + e);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return result;
    }
}