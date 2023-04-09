package com.seven.wqb.dao;

import com.seven.wqb.domain.NoteComment;
import com.seven.wqb.domain.NoteCommentLike;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface NoteDao {

    Integer addNoteComment(NoteComment noteComment);

    Integer pageCountNoteComments(Map<String, Object> params);

    List<NoteComment> pageListNoteComments(Map<String, Object> params);

    List<NoteComment> batchGetNoteCommentByRootIds(List<Long> parentIdList);

    NoteComment getNoteCommentByCommentId(Long commentId);

    NoteCommentLike getNoteCommentLikeByCommentIdAndUid(@Param("commentId") Long commentId,@Param("userId") Long userId);

    Integer addNoteCommentLike(NoteCommentLike noteCommentLike);

    Integer deleteNoteCommentLike(@Param("noteId")Long noteId,@Param("commentId")  Long commentId,@Param("userId") Long userId);

    List<NoteCommentLike> getAllCommentLikeByNoteId(Long noteId);
}
