//package com.yumiao.usdttransfer.utils.redis;
//
//import com.yumiao.usdttransfer.utils.DateUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//import java.util.concurrent.TimeUnit;
//
//
//@Component
//public class RedisSequenceUtil {
//
//
//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;
//
//    @Autowired
//    private RedisTemplate redisTemplate;
//
//    /**
//     * @param key
//     * @param value
//     * @param expireTime
//     * @Title: set
//     * @Description: set cache.
//     */
//    public void set(String key, int value, Date expireTime) {
//        RedisAtomicLong counter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
//        counter.set(value);
//        counter.expireAt(expireTime);
//    }
//
//    /**
//     * @param key
//     * @param value
//     * @param timeout
//     * @param unit
//     * @Title: set
//     * @Description: set cache.
//     */
//    public void set(String key, int value, long timeout) {
//        RedisAtomicLong counter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
//        counter.set(value);
//        counter.expire(timeout, TimeUnit.SECONDS);
//    }
//
//    /**
//     * @param key
//     * @param value
//     * @param timeout
//     * @param unit
//     * @Title: set
//     * @Description: set cache.
//     */
//    public void set(String key, int value, long timeout, TimeUnit unit) {
//        RedisAtomicLong counter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
//        counter.set(value);
//        counter.expire(timeout, unit);
//    }
//    /**
//     * @param key
//     * @return
//     * @Title: generate
//     * @Description: Atomically increments by one the current value.
//     */
//    public long getAtomicLong(String key) {
//        RedisAtomicLong counter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
//        return counter.get();
//    }
//
//    public void del(String key) {
//        redisTemplate.delete(key);
//    }
//
//    public  long incr(String key){
//        long num= redisTemplate.getConnectionFactory().getConnection().incr(
//                redisTemplate.getKeySerializer().serialize(key)
//        );
//        Date date= DateUtils.addDay(new Date(),1);
//        redisTemplate.expireAt(key,date);
//        return  num;
//    }
//    public  long incr(String key,int num){
//       long numresult= redisTemplate.getConnectionFactory().getConnection().incrBy(
//                redisTemplate.getKeySerializer().serialize(key), num
//        );
//        Date date=DateUtils.addDay(new Date(),1);
//        redisTemplate.expireAt(key,date);
//        return  numresult;
//    }
//
//    /**
//     * @param key
//     * @return
//     * @Title: generate
//     * @Description: Atomically increments by one the current value.
//     */
//    public long generate(String key) {
//        RedisAtomicLong counter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
//        return counter.incrementAndGet();
//    }
//
//    public long generateExpire(String key, int    min) {
//        RedisAtomicLong counter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
//        Date expireTime=new Date();
//        expireTime=DateUtils.getAddMinuteDate(expireTime,min);
//        counter.expireAt(expireTime);
//        return counter.incrementAndGet();
//    }
//
//    /**
//     * @param key
//     * @return
//     * @Title: generate
//     * @Description: Atomically increments by one the current value.
//     */
//    public long generate(String key, Date expireTime) {
//        RedisAtomicLong counter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
//        counter.expireAt(expireTime);
//        return counter.incrementAndGet();
//    }
//
//    /**
//     * @param key
//     * @param increment
//     * @return
//     * @Title: generate
//     * @Description: Atomically adds the given value to the current value.
//     */
//    public long generate(String key, int increment) {
//        RedisAtomicLong counter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
//        return counter.addAndGet(increment);
//    }
//
//    /**
//     * @param key
//     * @param increment
//     * @param expireTime
//     * @return
//     * @Title: generate
//     * @Description: Atomically adds the given value to the current value.
//     */
//    public long generate(String key, int increment, Date expireTime) {
//        RedisAtomicLong counter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
//        counter.expireAt(expireTime);
//        return counter.addAndGet(increment);
//    }
//
//    public long generate(String key, int increment, int minute) {
//        RedisAtomicLong counter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
//        Date date= DateUtils.getAddMinuteDate(new Date(),minute);
//        counter.expireAt(date);
//        return counter.addAndGet(increment);
//    }
//}