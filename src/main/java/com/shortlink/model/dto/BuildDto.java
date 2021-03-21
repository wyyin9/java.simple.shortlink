package com.shortlink.model.dto;

import lombok.Data;

/**
 * 构建请求dto
 */
@Data
public class BuildDto {

    /**
     * url
     */
    public String url;

    /**
     * 来源
     */
    public String source;
}
