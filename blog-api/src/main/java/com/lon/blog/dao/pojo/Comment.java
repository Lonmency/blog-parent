package com.lon.blog.dao.pojo;

import lombok.Data;

// TODO 日期全部改成yyyy-mm-dd形式
@Data
public class Comment {

    private Long id;

    private String content;

    private Long createDate;

    private Long articleId;

    private Long authorId;

    private Long parentId;

    private Long toUid;

    private Integer level;
}
