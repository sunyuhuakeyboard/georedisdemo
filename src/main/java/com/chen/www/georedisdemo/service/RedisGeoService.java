package com.chen.www.georedisdemo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RedisGeoService {

    @Autowired
    private StringRedisTemplate  redisTemplate;

    /**
     * 添加经纬度信息
     * redis 命令; geoadd key 120.223213 39.123123 "上海"
     * @param key
     * @param point
     * @param member
     * @return
     */
    public Long geoAdd(String key, Point point, String member){
        log.info((redisTemplate==null)+"=====");
        if(redisTemplate.hasKey(key)){
            redisTemplate.opsForGeo().remove(key,member);
        }
        return redisTemplate.opsForGeo().add(key,point,member);
    }

    /**
     * 根据key members批量获取坐标点
     *
     * redis 命令： geopos key 上海
     * @param key
     * @param members
     * @return
     */
    public List<Point> geoGet(String key, String... members){
        return redisTemplate.opsForGeo().position(key,members);
    }

    /**
     * 返回两个地方的距离，可以指定单位，比如：米m千米km，英里mi 英尺ft
     * redis 命令 ： deodist key 北京 上海
     * @param key
     * @param member1
     * @param member2
     * @param metric
     * @return
     */
    public Distance geoDist(String key, String member1, String member2, Metric metric){
        return redisTemplate.opsForGeo().distance(key, member1, member2, metric);
    }

    /**
     * 根据给定的经纬度，返回半径不超过指定距离的元素
     *
     * redis命令 georedius key 116.2323 39.123123123 100 Km WITHDIST WITHCOORDASC COUNT 5
     * @param key
     * @param circle
     * @param count
     * @return
     */
    public GeoResults<RedisGeoCommands.GeoLocation<String>> nearByXY(String key, Circle circle, long count){
        // includeDIstance 包含距离
        // includeCoordinates 包含经纬度
        // sortAscending 正序排序
        // limit 限定返回的记录数
        RedisGeoCommands.GeoRadiusCommandArgs args=RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().includeCoordinates().sortAscending().limit(count);
        return  redisTemplate.opsForGeo().radius(key, circle, args);
    }

    /**
     * 根据指定的地点查询半径在指定范围内的位置
     *
     * redis命令 ： georadiusbymember key 北京 100 Km WITHDIST WITHCOORD ASC COUNT 5
     * @param key
     * @param member
     * @param distance
     * @param count
     * @return
     */
    public GeoResults<RedisGeoCommands.GeoLocation<String>> nearByPlace(String key,String member,Distance distance,long count){
        //includeDistance 包含距离
        //includeCoordinates 包含经纬度
        //sortAscending 正序排序
        //limit 限定返回的记录数
        RedisGeoCommands.GeoRadiusCommandArgs args=RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().includeCoordinates().sortAscending().limit(count);
        return  redisTemplate.opsForGeo().radius(key,member,distance,args);
    }

    /**
     * 返回 geohash值
     * redis命令： geohash key 北京
     * @param key
     * @param member
     * @return
     */
    public  List<String> geoHash(String key,String member){
        return redisTemplate.opsForGeo().hash(key,member);
    }





}


