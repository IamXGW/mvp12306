1、首先保证安装了jdk，并配置环境变量
2、根据自己需要，修改generator.xml
   修改数据库url、用户名，密码及路径等。
3、运行cmd，进入当前包后，执行命令：
    java -jar mybatis-generator-core-1.3.2.jar -configfile generator.xml -overwrite
