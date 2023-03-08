package com.guoran.controller;

import com.guoran.common.ResponseResult;
import com.guoran.domain.Comment;
import com.guoran.service.CommentService;
import com.guoran.utils.RedisCache;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Api
@RequestMapping("/comment")
@RestController
public class CommentController {
    @Autowired
    private CommentService commentService;

    /**
     * 提交评论
     */
    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseResult addComment(HttpServletRequest req) {
        ResponseResult result = new ResponseResult<>();
        String userId = req.getParameter("user_id");
        String type = req.getParameter("type");
        String songListId = req.getParameter("song_list_id");
        String songId = req.getParameter("song_id");
        String content = req.getParameter("content").trim();

        Comment comment = new Comment();
        comment.setUserId(Integer.parseInt(userId));
        comment.setType(new Byte(type));
        if (new Byte(type) == 0) {
            comment.setSongId(Integer.parseInt(songId));
        } else if (new Byte(type) == 1) {
            comment.setSongListId(Integer.parseInt(songListId));
        }
        comment.setContent(content);
        comment.setCreateTime(new Date());
        boolean res = commentService.addComment(comment);
        if (res) {
            return result.success("提交评论成功", comment);
        } else {
            return result.error("评论失败");
        }
    }

    /**
     * 删除评论
     */
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public ResponseResult deleteComment(HttpServletRequest req) {
        ResponseResult result = new ResponseResult<>();
        String id = req.getParameter("id");

        boolean res = commentService.deleteComment(Integer.parseInt(id));
        if (res) {
            return result.success("删除成功");
        } else {
            return result.error("删除失败");
        }
    }

    /**
     * 获得指定歌曲 ID 的评论列表
     */
    @RequestMapping(value = "/song/detail", method = RequestMethod.GET)
    public ResponseResult commentOfSongId(HttpServletRequest req) {
        ResponseResult result = new ResponseResult<>();
        String songId = req.getParameter("song_id");
        List<Comment> list = commentService.commentOfSongId(Integer.parseInt(songId));
        return result.success(list);
    }

    /**
     * 获得指定歌单 ID 的评论列表
     */
    @RequestMapping(value = "/songList/detail", method = RequestMethod.GET)
    public ResponseResult commentOfSongListId(HttpServletRequest req) {
        ResponseResult result = new ResponseResult<>();
        String songListId = req.getParameter("song_list_id");
        List<Comment> list = commentService.commentOfSongListId(Integer.parseInt(songListId));
        return result.success(list);
    }

    /**
     * 点赞数
     */
    @ResponseBody
    @RequestMapping(value = "/like", method = RequestMethod.POST)
    public ResponseResult commentOfLike(HttpServletRequest req) {
        ResponseResult result = new ResponseResult<>();
        String id = req.getParameter("id").trim();
        String up = req.getParameter("up").trim();

        Comment comment = new Comment();
        comment.setId(Integer.parseInt(id));
        comment.setUp(Integer.parseInt(up));

        boolean res = commentService.updateCommentMsg(comment);
        if (res) {
            return result.success("点赞成功", comment);
        } else {
            return result.error("点赞失败");
        }
    }
}
