# smartMedical
安卓端预约挂号系统

采用mvc开发模式开发项目
网络请求采用volley框架


### 开发安排：一天或两天一个小功能
#### 医生：查看本人值班安排(finish)，病历管理【查看病历，增加病历，删除病历，更新病历】(finish)，查看自己任务量(finish){done}
#### 患者：选择科室、医生进行挂号(finish)，查看病历，查看挂号(finish)，取消挂号(finish){done}
#### 管理员：添加医生(finish)，删除医生(finish)，更新医生（finish），值班管理（界面大调整4.3）：：：添加值班(finish)，删除值班，更新值班，查看值班{done}

# 所有角色都在个人页面添加个人资料信息管理、编辑{done}
# 数据库
使用mysql数据库，在mysql数据库创建smartmedical数据库，在该数据库下执行source smartmedical.sql文件。

# 服务端
smartbackground 文件夹为服务端，使用spring boot框架搭建，
修改resource文件夹下的application.yml文件，修改数据库地址、账号、密码为你的数据库地址、账号、密码
接着idea打开项目，加载依赖，修改编译打包成war包，部署在tomcat上即可。

# 客户端
smartmedical 文件夹android客户端
通过android studio 打开本项目，接着修改src\main\java\com\zuovx\Utils\GlobalVar.java文件中的url为你的服务端地址
接着android studio编译即可生成apk文件，可安装在现在的安卓手机上运行即可。

## 注意，初始的账号密码在smartmedial.sql中可找到。

