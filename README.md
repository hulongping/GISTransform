# 国内几种坐标体系
## WGS1984
* OpenStreetMap
* Google中国遥感地图
* Google地图国际版（中国除外）

## GCJ-02 火星坐标 国测局坐标系
* 高德地图
* Google行政地图

## BD-09
* 百度地图

## 使用第三方SDK开发应用的注意事项
- 使用移动端app SDK获取坐标
    
        百度SDK默认获得BD-09坐标
        高德SDK默认是GCJ-02坐标系   
    
- Web Api使用
        
        参考各厂商的开发文档

# Web墨卡托投影与WGS1984坐标转换
* 以整个世界范围，赤道作为标准纬线，本初子午线作为中央经线，两者交点为坐标原点，向东向北为正，向西向南为负。
* 注意：纬度只到了85度，去到南极或者北极的时候，Web墨卡托地图是会迷路的。


