package com.chao.cloud.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import com.chao.cloud.common.extra.ftp.annotation.EnableFtp;
import com.chao.cloud.common.extra.mybatis.annotation.EnableMybatisPlus;
import com.chao.cloud.common.extra.token.annotation.EnableFormToken;
import com.chao.cloud.common.web.annotation.EnableGlobalException;
import com.chao.cloud.common.web.annotation.EnableWeb;

/**
 * admin-后台管理系统
 * @功能：
 * @author： 薛超
 * @时间：2019年3月13日
 * @version 1.0.0
 */
@SpringBootApplication
@EnableCaching // 缓存
@EnableWeb // web
@EnableGlobalException // 全局异常处理
@EnableFormToken // 防止表单重复提交
@EnableMybatisPlus // 数据库插件-乐观锁
@EnableFtp // 文件上传
public class ChaoAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChaoAdminApplication.class, args);
	}

}
