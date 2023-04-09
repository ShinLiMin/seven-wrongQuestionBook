package com.seven.wqb.api;

import com.seven.wqb.domain.JsonResponse;
import com.seven.wqb.service.utils.SensitiveWordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class SensitiveWordApi {

    @Autowired
    private SensitiveWordUtils utils;

    @PostMapping("/add-words")
    public JsonResponse<String> addWord(@RequestParam String value) {
        utils.addWord(value);
        return JsonResponse.success();
    }

    @GetMapping("/check-context")
    public JsonResponse<Boolean> checkContext(@RequestParam String context) {
        Boolean result = utils.checkContext(context);
        return new JsonResponse<>(result);
    }

}
