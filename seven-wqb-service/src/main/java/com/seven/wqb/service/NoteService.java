package com.seven.wqb.service;

import com.seven.wqb.dao.NoteDao;
import com.seven.wqb.domain.NoteComment;
import com.seven.wqb.domain.NoteCommentLike;
import com.seven.wqb.domain.PageResult;
import com.seven.wqb.domain.UserInfo;
import com.seven.wqb.domain.exception.ConditionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NoteService {
    @Autowired
    private NoteDao noteDao;

    @Autowired
    private UserService userService;

    public void addNoteComment(NoteComment noteComment, Long userId) {
        Long noteId = noteComment.getNoteId();
        if (noteId == null) {
            throw new ConditionException("参数异常");
        }
        //todo
        //Note note = noteDao.getNoteById(noteId);
        //if(note == null) thr Condict("笔记不存在");
        noteComment.setUserId(userId);
        noteComment.setCreateTime(new Date());
        noteDao.addNoteComment(noteComment);
    }

    public PageResult<NoteComment> pageListNoteCommentsByTime(Long size, Long no, Long noteId) {
        //todo
        //Note note = noteDao.getNoteById(noteId);
        //if(note == null) thr Condict("笔记不存在");
        Map<String, Object> params = new HashMap<>();
        params.put("start", size * (no - 1));
        params.put("limit", size);
        params.put("noteId", noteId);
        //一级评论数量
        Integer total = noteDao.pageCountNoteComments(params);

        List<NoteComment> list = new ArrayList<>();
        if (total > 0) {
            //分页查询一级评论列表
            list = noteDao.pageListNoteComments(params);
            //一级评论id列表
            List<Long> parentIdList = list.stream().map(NoteComment::getId).collect(Collectors.toList());
            //获取二级评论列表
            List<NoteComment> childCommentList = noteDao.batchGetNoteCommentByRootIds(parentIdList);
            //一级评论用户id
            Set<Long> userIdList = list.stream().map(NoteComment::getUserId).collect(Collectors.toSet());
            //二级评论用户id
            Set<Long> replyUserIdList = childCommentList.stream().map(NoteComment::getReplyUserId).collect(Collectors.toSet());
            //全部评论用户id
            userIdList.addAll(replyUserIdList);
            //评论用户信息
            List<UserInfo> userInfoList = userService.getUserInfoByUserIds(userIdList);
            //id和信息对应
            Map<Long, UserInfo> userInfoMap = userInfoList.stream().collect(Collectors.toMap(UserInfo::getUserId, userInfo -> userInfo));
            //为一级评论填充信息
            list.forEach(comment -> {
                Long id = comment.getId();
                List<NoteComment> childList = new ArrayList<>();
                childCommentList.forEach(child -> {
                    if (id.equals(child.getRootId())) {
                        child.setUserInfo(userInfoMap.get(child.getUserId()));
                        child.setReplyUserInfo(userInfoMap.get(child.getReplyUserId()));
                        childList.add(child);
                    }
                });
                comment.setChildList(childList);
                comment.setUserInfo(userInfoMap.get(comment.getUserId()));
            });
        }
        return new PageResult<>(total, list);
    }

    public PageResult<NoteComment> pageListNoteCommentsByHot(Long size, Long no, Long noteId) {
        //todo
        //Note note = noteDao.getNoteById(noteId);
        //if(note == null) thr Condict("笔记不存在");
        Map<String, Object> params = new HashMap<>();
        params.put("start", size * (no - 1));
        params.put("limit", size);
        params.put("noteId", noteId);
        //一级评论数量
        Integer total = noteDao.pageCountNoteComments(params);
        //点赞记录列表

        List<NoteComment> list = new ArrayList<>();
        if (total > 0) {
            //分页查询一级评论列表
            list = noteDao.pageListNoteComments(params);
            //一级评论id列表
            List<Long> parentIdList = list.stream().map(NoteComment::getId).collect(Collectors.toList());
            //获取二级评论列表
            List<NoteComment> childCommentList = noteDao.batchGetNoteCommentByRootIds(parentIdList);
            //一级评论用户id
            Set<Long> userIdList = list.stream().map(NoteComment::getUserId).collect(Collectors.toSet());
            //二级评论用户id
            Set<Long> replyUserIdList = childCommentList.stream().map(NoteComment::getReplyUserId).collect(Collectors.toSet());
            //全部评论用户id
            userIdList.addAll(replyUserIdList);
            //评论用户信息
            List<UserInfo> userInfoList = userService.getUserInfoByUserIds(userIdList);

            List<NoteCommentLike> likeList = noteDao.getAllCommentLikeByNoteId(noteId);
            //评论id-点赞数
            Map<Long, Integer> likeCountMap = new HashMap<>();
            for (NoteCommentLike noteCommentLike : likeList) {
                Long commentId = noteCommentLike.getCommentId();
                likeCountMap.put(commentId, likeCountMap.getOrDefault(commentId, 0) + 1);
            }

            //id和信息对应
            Map<Long, UserInfo> userInfoMap = userInfoList.stream().collect(Collectors.toMap(UserInfo::getUserId, userInfo -> userInfo));
            //为一级评论填充信息
            list.forEach(comment -> {
                Long id = comment.getId();
                comment.setLikeCount(likeCountMap.getOrDefault(id, 0));
                //comment.setLike();//todo
                List<NoteComment> childList = new ArrayList<>();
                childCommentList.forEach(child -> {
                    if (id.equals(child.getRootId())) {
                        child.setLikeCount(likeCountMap.getOrDefault(child.getId(), 0));
                        //child.setLike();//todo
                        child.setUserInfo(userInfoMap.get(child.getUserId()));
                        child.setReplyUserInfo(userInfoMap.get(child.getReplyUserId()));
                        childList.add(child);
                    }
                });
                Collections.sort(childList, (a, b) -> a.getLikeCount() - b.getLikeCount());
                comment.setChildList(childList);
                comment.setUserInfo(userInfoMap.get(comment.getUserId()));
            });
            Collections.sort(list,(a, b) -> a.getLikeCount() - b.getLikeCount());
        }
        return new PageResult<>(total, list);
    }



    public void addNoteCommentLike(Long noteId, Long commentId, Long userId) {
        //todo
        //Note note = noteDao.getNoteById(noteId);
        //if(note == null) thr Condict("笔记不存在");
        NoteComment comment = noteDao.getNoteCommentByCommentId(commentId);
        if (comment == null) {
            throw new ConditionException("评论不存在");
        }
        NoteCommentLike noteCommentLikeDB = noteDao.getNoteCommentLikeByCommentIdAndUid(commentId, userId);
        if (noteCommentLikeDB != null) {
            throw new ConditionException("请勿重复点赞");
        }
        NoteCommentLike noteCommentLike = new NoteCommentLike();
        noteCommentLike.setNoteId(noteId);
        noteCommentLike.setCommentId(commentId);
        noteCommentLike.setUserId(userId);
        noteCommentLike.setCreateTime(new Date());
        noteDao.addNoteCommentLike(noteCommentLike);
    }


    public void deleteNoteCommentLike(Long noteId, Long commentId, Long userId) {
        //todo
        //Note note = noteDao.getNoteById(noteId);
        //if(note == null) thr Condict("笔记不存在");
        NoteComment comment = noteDao.getNoteCommentByCommentId(commentId);
        if (comment == null) {
            throw new ConditionException("评论不存在");
        }
        noteDao.deleteNoteCommentLike(noteId, commentId, userId);
    }

}
