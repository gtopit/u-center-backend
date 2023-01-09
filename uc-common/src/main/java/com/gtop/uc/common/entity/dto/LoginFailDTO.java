package com.gtop.uc.common.entity.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author hongzw
 */
@Data
public class LoginFailDTO {
	private Integer times;
	private Date lastDate;
}
