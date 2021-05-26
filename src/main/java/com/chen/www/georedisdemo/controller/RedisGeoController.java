package com.chen.www.georedisdemo.controller;

import com.chen.www.georedisdemo.bean.DriverPosition;
import com.chen.www.georedisdemo.service.RedisGeoService;
import com.chen.www.georedisdemo.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("redisGeo")
public class RedisGeoController {

    @Autowired
    private RedisGeoService redisGeoService;

    private final String GEO_KEY="geo_key";

    /**
     * 使用redis+geo 上传位置
     * @param cityId
     * @param driverId
     * @param lng
     * @param lat
     * @return
     */
    @PostMapping("addDriverPosition")
    public  Long addDriverPosition(String cityId,String driverId,Double lng,Double lat){
        String redisKey= CommonUtil.buildRedisKey(GEO_KEY,cityId);
        long addnum=redisGeoService.geoAdd(redisKey,new Point(lng,lat),driverId);
        List<Point> points=redisGeoService.geoGet(redisKey,driverId);
        System.out.println("添加坐标点的位置"+points);
        return  addnum;
    }

    /**
     * 使用reidis+geo 查找附近司机位置
     * @param cityId
     * @param lng
     * @param lat
     * @return
     */

    @GetMapping("getNearDrivers")
    public List<DriverPosition> getNearDrivers(String cityId,Double lng,Double lat){
        String redisKey=CommonUtil.buildRedisKey(GEO_KEY,cityId);
        Circle circle=new Circle(lng,lat, Metrics.KILOMETERS.getMultiplier());
        GeoResults<RedisGeoCommands.GeoLocation<String>> results=redisGeoService.nearByXY(redisKey,circle,5);
        System.out.println("查询附近司机位置"+results);

        List<DriverPosition> list =new ArrayList<>();
        results.forEach(item->{
            RedisGeoCommands.GeoLocation<String> location=item.getContent();
            Point point=location.getPoint();
            DriverPosition position=DriverPosition.builder().cityCode(cityId).driverId(location.getName()).lng(point.getX()).lat(point.getY()).build();
            list.add(position);
        });
        return  list;
    }




}
