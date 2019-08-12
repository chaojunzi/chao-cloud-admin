package com.chao.cloud.admin.sys.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chao.cloud.admin.sys.constant.AdminConstant;
import com.chao.cloud.admin.sys.dal.entity.SysDept;
import com.chao.cloud.admin.sys.dal.mapper.SysDeptMapper;
import com.chao.cloud.admin.sys.domain.dto.SelectTreeDTO;
import com.chao.cloud.admin.sys.service.SysDeptService;
import com.chao.cloud.common.util.EntityUtil;

import cn.hutool.core.collection.CollUtil;

/**
 * @author 薛超
 * @since 2019年8月12日
 * @version 1.0.7
 */
@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {

	@Override
	public List<SelectTreeDTO> selectTree() {
		List<SysDept> list = this.list(Wrappers.<SysDept>lambdaQuery().orderByAsc(SysDept::getOrderNum));
		if (CollUtil.isNotEmpty(list)) {
			List<SelectTreeDTO> trees = list.stream().map(r -> {
				return SelectTreeDTO.of()//
						.setId(r.getDeptId()) //
						.setPid(r.getParentId()) //
						.setValue(r.getDeptId()) //
						.setName(r.getName());
			}).collect(Collectors.toList());
			// 递归转化
			return EntityUtil.toTreeAnnoList(trees, AdminConstant.TREE_DEFAULT_VALUE);
		}
		return Collections.emptyList();
	}

}
