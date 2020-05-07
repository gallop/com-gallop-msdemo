## 使用方法如下：
在IDEA中，--> Maven projects --> Plugins --> mybatis-generator --> 双击 mybatis-generator:generate

## 在 mybatisgenerator 中 支持lombok
1、在pom.xml中添加： 

``
<dependency>
    <groupId>com.chrm</groupId>
    <artifactId>mybatis-generator-lombok-plugin</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
``

2、在generatorConfig.xml 中添加如下代码：

``
<plugin type="com.chrm.mybatis.generator.plugins.LombokPlugin">
    <property name="hasLombok" value="true"/>
</plugin>
``