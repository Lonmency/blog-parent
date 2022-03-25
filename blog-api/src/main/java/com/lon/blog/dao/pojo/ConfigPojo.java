package com.lon.blog.dao.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName lt_config
 */
@TableName(value ="lt_config")
@Data
public class ConfigPojo implements Serializable {
    /**
     * 
     */
    @TableId
    private String name;

    /**
     * 
     */
    private String value;

    /**
     * 
     */
    private String comment;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}