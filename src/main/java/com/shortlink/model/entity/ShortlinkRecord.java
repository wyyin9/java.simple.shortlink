package com.shortlink.model.entity;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 短链记录实体
 */
@TableName("shortlink_record") // 对应数据库表名
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShortlinkRecord {
    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.INPUT) // 对应数据库主键，字段名，主键类型
    public long id;

    /**
     * 短码
     */
    @TableField("short_code") //对应数据库字段名
    public String shortCode;

    /**
     * 原始链接
     */
    @TableField("original_url")
    public String originalUrl;

    /**
     * 来源
     */
    @TableField("source")
    public String source;
}
