package com.chen.www.georedisdemo.bean;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverPosition {
    private String driverId;
    private  String cityCode;
    private  double lng;
    private  double lat;
}
