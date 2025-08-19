package com.sky.aicsr.controller;

import com.sky.aicsr.assistant.CsrAgent;
import com.sky.aicsr.bean.ChatForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Api(tags = "智能客服")
@RestController
@RequestMapping("/admin/aicsr")
public class CsrController {

    @Autowired
    private CsrAgent csrAgent;

    @PostMapping(value = "/chatbot", produces = "text/stream;charset=utf-8")
    @ApiOperation("对话")
    public Flux<String> chat(@RequestBody ChatForm chatForm){
        Long memoryId = chatForm.getMemoryId();
        if(memoryId == null){
            memoryId = System.currentTimeMillis();
        }
        return csrAgent.chat(memoryId,chatForm.getMessage());
    }
}
