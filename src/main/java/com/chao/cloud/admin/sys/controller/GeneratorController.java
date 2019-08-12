package com.chao.cloud.admin.sys.controller;

import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.chao.cloud.admin.sys.constant.IDatabaseInfo;
import com.chao.cloud.admin.sys.domain.vo.GenCodeVO;
import com.chao.cloud.admin.sys.log.AdminLog;
import com.chao.cloud.admin.sys.service.GeneratorService;
import com.chao.cloud.common.entity.Response;
import com.chao.cloud.common.extra.mybatis.generator.ZipAutoGenerator;
import com.chao.cloud.common.extra.mybatis.generator.menu.MenuEnum;
import com.chao.cloud.common.extra.mybatis.generator.menu.MenuMapping;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;

@RequestMapping("/sys/generator")
@Controller
@Validated
@MenuMapping
public class GeneratorController {
	String prefix = "sys/generator";

	@Autowired
	private GeneratorService generatorService;
	@Autowired
	private ZipAutoGenerator autoGenerator;

	@MenuMapping(value = "代码生成", type = MenuEnum.MENU)
	@RequiresPermissions("sys:generator:list")
	@RequestMapping
	String generator() {
		return prefix + "/list";
	}

	@AdminLog(AdminLog.STAT_PREFIX + "数据表")
	@MenuMapping("列表")
	@RequiresPermissions("sys:generator:list")
	@RequestMapping("/list")
	@ResponseBody
	Response<IPage<IDatabaseInfo>> list(Page<IDatabaseInfo> page, String tableName) {
		List<IDatabaseInfo> list = generatorService.list(tableName);
		page.setTotal(list.size());
		// 分页
		if (CollUtil.isNotEmpty(list)) {
			// 判断总页数
			long pages = page.getPages();
			if (pages > 0 && page.getCurrent() <= pages) {// 当前页不超过总页数
				// 获取偏移量
				long offset = page.offset();
				list = list.stream().skip(offset).limit(page.getSize()).collect(Collectors.toList());
				page.setRecords(list);
			}
		}
		return Response.ok(page);
	}

	@MenuMapping("生成策略")
	@RequiresPermissions("sys:generator:edit")
	@RequestMapping("/edit")
	public String edit(Model model) {
		// 分块
		DataSourceConfig config = autoGenerator.getDataSource();
		GenCodeVO.Base base = new GenCodeVO.Base();
		base.setAuthor(autoGenerator.getGlobalConfig().getAuthor());
		base.setParent(autoGenerator.getPackageInfo().getParent());
		base.setUrl(config.getUrl());
		base.setPassword(config.getPassword());
		base.setUsername(config.getUsername());
		base.setDriverName(config.getDriverName());
		model.addAttribute("config", base);
		return prefix + "/edit";
	}

	@MenuMapping("生成全套代码")
	@RequiresPermissions("sys:generator:code")
	@RequestMapping("/code/{tableName}")
	public void code(HttpServletResponse response, @PathVariable("tableName") String tableName) throws Exception {
		autoGenerator.getStrategy().setInclude(tableName);
		genCode(tableName, response);
	}

	@MenuMapping("批量生成全套代码")
	@RequiresPermissions("sys:generator:batchCode")
	@RequestMapping("/batchCode")
	public void batchCode(HttpServletResponse response, @NotEmpty @RequestParam String tables[]) throws Exception {
		autoGenerator.getStrategy().setInclude(tables);
		genCode(tables[0], response);
	}

	@RequiresPermissions("sys:generator:edit")
	@RequestMapping("/update")
	@ResponseBody
	Response<String> update(@Valid GenCodeVO.Base vo) {
		DataSourceConfig config = autoGenerator.getDataSource();
		if (!vo.getDriverName().equals(config.getDriverName())) {
			config.setTypeConvert(null);
			config.setDbQuery(null);
			config.setDbType(null);
		}
		BeanUtil.copyProperties(vo, config);
		autoGenerator.getPackageInfo().setParent(vo.getParent());
		autoGenerator.getGlobalConfig().setAuthor(vo.getAuthor());
		return Response.ok();
	}

	private void genCode(String tableName, HttpServletResponse response) throws Exception {
		// 生成代码
		response.reset();
		response.setHeader("Content-Disposition", "attachment; filename=\"" + tableName + "-more.zip\"");
		response.setContentType("application/octet-stream; charset=UTF-8");
		try (OutputStream out = response.getOutputStream()) {
			autoGenerator.execute(out);
		}
	}

}
