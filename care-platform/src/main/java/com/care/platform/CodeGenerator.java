package com.care.platform;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

public class CodeGenerator {
    public static void main(String[] args) {
        // 1. 配置云端数据库连接
        String url = "jdbc:mysql://47.111.2.69:3306/care_platform?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai";
        String username = "care_platform";
        // 👇 【注意】换成你刚才在宝塔里设置的最新的数据库密码！
        String password = "YnxhiSWssRCb2iMC";

        FastAutoGenerator.create(url, username, password)
                // 2. 全局配置
                .globalConfig(builder -> {
                    builder.author("care-platform") // 作者名
                            .fileOverride() // 🌟 重点：开启文件覆盖！这样即使有旧文件残留也会被强行替换成最新的
                            .outputDir(System.getProperty("user.dir") + "/src/main/java"); // 指定输出到咱们的 java 目录下
                })
                // 3. 包配置 (告诉它 Controller/Service/Mapper 放在哪里)
                .packageConfig(builder -> {
                    builder.parent("com.care.platform") // 父包名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, System.getProperty("user.dir") + "/src/main/resources/mapper")); // xml文件输出到 resources 下
                })
                // 4. 策略配置 (告诉它要生成哪几张表)
                .strategyConfig(builder -> {
                    // 👉 锁定咱们最新的 6 张核心表
                    builder.addInclude("user", "child", "timeslot", "reservation", "volunteer", "schedule")
                            // 实体类配置
                            .entityBuilder()
                            .enableLombok() // 自动生成 get/set
                            // Controller 配置
                            .controllerBuilder()
                            .enableRestStyle(); // 自动打上 @RestController 注解
                })
                // 5. 模板引擎配置 (咱们 pom.xml 里引入的是 freemarker)
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}