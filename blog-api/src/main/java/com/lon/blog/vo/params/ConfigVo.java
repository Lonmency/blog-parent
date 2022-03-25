package com.lon.blog.vo.params;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName lt_config
 */
@Data
public class ConfigVo implements Serializable {
    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private String value;

    /**
     * 
     */
    private String comment;
}