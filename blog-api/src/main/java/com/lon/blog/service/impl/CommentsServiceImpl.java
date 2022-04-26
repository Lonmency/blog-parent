package com.lon.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lon.blog.dao.mapper.CommentMapper;
import com.lon.blog.dao.pojo.Comment;
import com.lon.blog.dao.pojo.SysUser;
import com.lon.blog.utils.UserThreadLocal;
import com.lon.blog.vo.CommentVo;
import com.lon.blog.vo.Result;
import com.lon.blog.vo.UserVo;
import com.lon.blog.vo.params.CommentParam;
import com.lon.blog.service.CommentsService;
import com.lon.blog.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//TODO 扩展为多级评论
@Service
@Slf4j
public class CommentsServiceImpl implements CommentsService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public Result commentsByArticleId(Long id) {
        /**
         * 1. 根据文章id 查询 评论列表 从 comment 表中查询 ,且查询level是1的记录
         * 2. 根据作者的id 查询作者的信息
         * 3. 如果有 根据评论id 进行查询 （parent_id）
         */
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId,id);
        queryWrapper.eq(Comment::getLevel,1); //这里不可删除
        queryWrapper.orderByDesc(Comment::getCreateDate);
        List<Comment> comments = commentMapper.selectList(queryWrapper);
        List<CommentVo> commentVoList = convertToVoList(comments);
        return Result.success(commentVoList);
    }

    /**
     *
     * 评论
     * 评论数放到redis里自增
     * @param commentParam
     * @return
     */
    @Override
    public Result comment(CommentParam commentParam) {
        SysUser sysUser = UserThreadLocal.get();
        Comment comment = new Comment();
        comment.setArticleId(commentParam.getArticleId());
        comment.setAuthorId(sysUser.getId());
        comment.setContent(commentParam.getContent());
        comment.setCreateDate(System.currentTimeMillis());
        Long parent = commentParam.getParent();
        if (parent == null || parent == 0) {
            comment.setLevel(1);
        }else{
            comment.setLevel(2);
        }
        comment.setParentId(parent == null ? 0 : parent);
        Long toUserId = commentParam.getToUserId();
        comment.setToUid(toUserId == null ? 0 : toUserId);
        this.commentMapper.insert(comment);

        return Result.success(null);
    }

    /**
     * 转换成Vo LIST
     * @param comments
     * @return
     */
    private List<CommentVo> convertToVoList(List<Comment> comments) {
        List<CommentVo> commentVoList = new ArrayList<>();
        for (Comment comment : comments) {
            commentVoList.add(convertToVo(comment));
        }
        return commentVoList;
    }

    /**
     * 转换成Vo
     * @param comment
     * @return
     */
    private CommentVo convertToVo(Comment comment) {
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment,commentVo);
        commentVo.setId(String.valueOf(comment.getId()));
        //作者信息
        Long authorId = comment.getAuthorId();
        UserVo userVo = this.sysUserService.findUserVoById(authorId);
        commentVo.setAuthor(userVo);
        //子评论
        Integer level = comment.getLevel();
        //第一层
        if (1 == level){
            Long id = comment.getId();
            //查询pareint_id是id的评论记录，查询到的评论为该评论的子评论
            List<CommentVo> commentVoList = findCommentsByParentId(id);
            commentVo.setChildrens(commentVoList);
        }
        //to User 给谁评论
        //第二层，是一个子评论，子评论是对文章评论的评论
        if (level > 1){
                Long toUid = comment.getToUid();
            //查询该评论的目的方的信息（这个评论是对哪个文章评论进行评论）
            UserVo toUserVo = this.sysUserService.findUserVoById(toUid);
            commentVo.setToUser(toUserVo);
        }
        return commentVo;
    }

    /**
     * 查询指定评论的子评论
     * @param id
     * @return
     */
    private List<CommentVo> findCommentsByParentId(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getParentId,id);
        queryWrapper.eq(Comment::getLevel,2);
        return convertToVoList(commentMapper.selectList(queryWrapper));
    }
}
