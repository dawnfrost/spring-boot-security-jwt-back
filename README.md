# spring-boot-security-jwt-back（正在开发）
> 本工程为spring boot security restful通用框架的后端部分
>> 主要目的是搭建一个通用的快速开发框架，供以后新项目直接上手开发
- 采用Spring Boot架搭建
  - 使用mysql数据库，在resource/sql下有脚本
  - application.yml中可配置数据源
  - 集成mybatis、mybatis-generator
- 內建几个通用用户管理表
  - bm_user:用户信息
  - bm_role:角色信息
  - bm_group:用户组
  - bm_auth:权限信息
  - bm_user_role:用户与角色的关联表
  - bm_user_group:用户与用户组的关联表
  - bm_group_role:用户组与角色的关联表
  - bm_role_auth:角色与权限的关联表
  - 有两种渠道可以为用户设置角色
    + 第一种可以为用户直接指定多个角色，通过用户与角色的关联表建立角色关联
    + 第二种可以先为用户组指定多个角色，然后再将用户关联至用户组
    - 之后所查询到的用户角色=用户直接关联的角色+用户所在用户组所关联的角色
- 本工程参考了wpcfan的一个jwt示例工程为基础
  - [github 链接](https://github.com/wpcfan/spring-boot-tut/tree/chap04)
- 已实现的功能
  - 集成mybatis分页插件
  - xa数据源
  - jwt生成
  - jwt刷新
- 之后需补充的功能
  - refresh_token机制
  - 用户管理接口
  - 权限管理接口
  - 用户菜单配置接口
  - 集成流程引擎

