package com.qqShopping.user.util;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis������
 * 
 * @author lujun.chen
 * @version [�汾��, 2016��7��27��]
 * @see [�����/����]
 * @since [��Ʒ/ģ��汾]
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
     * ��ʼ��Redis���ӳ�
     */
    static {
        try {
            init();
        } catch (Exception e) {
            logger.error("��ʼ��Redis����" + e);
        }
    }

    /**
     * ��ʼ�����ӳ�
     * 
     * @see [�ࡢ��#��������#��Ա]
     */
    private synchronized static void init() {

        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(200);
        config.setMaxIdle(50);
        config.setMinIdle(8);//������С������
        config.setMaxWaitMillis(10000);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
      //Idleʱ��������ɨ��
        config.setTestWhileIdle(true);
        //��ʾidle object evitor����ɨ��֮��Ҫsleep�ĺ�����
        config.setTimeBetweenEvictionRunsMillis(30000);
        //��ʾidle object evitorÿ��ɨ������Ķ�����
        config.setNumTestsPerEvictionRun(10);
        //��ʾһ����������ͣ����idle״̬�����ʱ�䣬Ȼ����ܱ�idle object evitorɨ�貢������һ��ֻ����timeBetweenEvictionRunsMillis����0ʱ��������
        config.setMinEvictableIdleTimeMillis(60000);
        jedisPool = new JedisPool(config, ADDR,PORT,TIMEOUT,AUTH);   
    }

    /**
     * ��ȡJedisʵ��
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
            logger.error("��ȡRedisʵ������" + e);
        }
        return jedis;
    }

    /**
     * ���õ���ֵ
     * 
     * @param key
     * @param value
     * @return
     */
    public static String set(String key, String value) {
        return set(key, value, null);
    }

    /**
     * ���õ���ֵ�������ó�ʱʱ��
     * 
     * @param key
     *            ��
     * @param value
     *            ֵ
     * @param timeout
     *            ��ʱʱ�䣨�룩
     * @return
     * @see [�ࡢ��#��������#��Ա]
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
     * ��ȡ����ֵ
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
     * ɾ��redis������
     * 
     * @param key
     * @return
     * @see [�ࡢ��#��������#��Ա]
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
            logger.error("ɾ��redis���ݳ���" + e);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return result;
    }

    /**
     * ׷��
     * 
     * @param key
     * @param value
     * @return
     * @see [�ࡢ��#��������#��Ա]
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
            logger.error("׷��redis���ݳ���" + e);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return result;
    }

    /**
     * ����Ƿ����
     * 
     * @param key
     * @return
     * @see [�ࡢ��#��������#��Ա]
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
            logger.error("����Ƿ���ڳ�����" + e);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return result;
    }
}