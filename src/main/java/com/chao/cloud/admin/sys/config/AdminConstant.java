package com.chao.cloud.admin.sys.config;

public interface AdminConstant {
	// 管理员属性
	String ADMIN = "admin";
	Integer ADMIN_ID = 1;
	// 禁用-启用
	Boolean DISABLE = false;
	Boolean ENABLE = true;
	// 停止计划任务
	String STATUS_RUNNING_STOP = "stop";
	// 开启计划任务
	String STATUS_RUNNING_START = "start";
	// 部门根节点id
	Integer DEPT_ROOT_ID = 0;
	// 权限默认值
	String RIGHTS_DEFAULT_VALUE = "0";
	// tree 默认根目录
	Integer TREE_DEFAULT_VALUE = 0;
}
