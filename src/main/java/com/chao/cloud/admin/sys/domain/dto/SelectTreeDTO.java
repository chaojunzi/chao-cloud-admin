package com.chao.cloud.admin.sys.domain.dto;

import java.util.List;

import com.chao.cloud.common.annotation.TreeAnnotation;
import com.chao.cloud.common.constant.TreeEnum;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor(staticName = "of")
public class SelectTreeDTO {

	@TreeAnnotation(TreeEnum.ID)
	private Integer id;
	@TreeAnnotation(TreeEnum.PARENT_ID)
	private Integer pid;
	private String name;
	private Integer value;
	private boolean disabled = false;
	@TreeAnnotation(TreeEnum.SUB_LIST)
	private List<SelectTreeDTO> children;

}
