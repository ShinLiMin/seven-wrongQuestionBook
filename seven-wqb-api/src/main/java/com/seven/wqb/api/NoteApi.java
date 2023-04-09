package com.seven.wqb.api;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.seven.wqb.api.support.UserSupport;
import com.seven.wqb.domain.JsonResponse;
import com.seven.wqb.domain.NoteComment;
import com.seven.wqb.domain.PageResult;
import com.seven.wqb.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class NoteApi {
    @Autowired
    private UserSupport userSupport;

    @Autowired
    private NoteService noteService;

    @PostMapping("/note-comments")
    public JsonResponse<String> addNoteComment(@RequestBody NoteComment noteComment) {
        Long userId = userSupport.getCurrentUserId();
        noteService.addNoteComment(noteComment, userId);
        return JsonResponse.success();
    }

    @GetMapping("/note-comments-by-time")
    public JsonResponse<PageResult<NoteComment>> pageListNoteCommentsByTime(@RequestParam Long size,
                                                                       @RequestParam Long no,
                                                                       @RequestParam Long noteId) {
        PageResult<NoteComment> result = noteService.pageListNoteCommentsByTime(size, no, noteId);
        return new JsonResponse<>(result);
    }

    @GetMapping("/note-comments-by-hot")
    public JsonResponse<PageResult<NoteComment>> pageListNoteCommentsByHot(@RequestParam Long size,
                                                                       @RequestParam Long no,
                                                                       @RequestParam Long noteId) {
        PageResult<NoteComment> result = noteService.pageListNoteCommentsByHot(size, no, noteId);
        return new JsonResponse<>(result);
    }


    @PostMapping("/note-comment-likes")
    public JsonResponse<String> addNoteCommentLike(@RequestParam Long noteId, @RequestParam Long commentId) {
        Long userId = userSupport.getCurrentUserId();
        noteService.addNoteCommentLike(noteId, commentId, userId);
        return JsonResponse.success();
    }

    @DeleteMapping("/note-comment-likes")
    public JsonResponse<String> deleteNoteCommentLike(@RequestParam Long noteId, @RequestParam Long commentId) {
        Long userId = userSupport.getCurrentUserId();
        noteService.deleteNoteCommentLike(noteId, commentId, userId);
        return JsonResponse.success();
    }

}
