package com.waiwaiwai.demo.netty.onedemo.control;

import com.waiwaiwai.demo.netty.onedemo.MessageRequest;
import com.waiwaiwai.demo.netty.onedemo.service.IPushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author LHL
 */
@RestController
@RequestMapping("/push")
public class PushController {

    @Autowired
    private IPushService pushService;

    /**
     * 推送给所有用户
     *
     * @param msg 消息
     */
    @PostMapping("/pushAll")
    public void pushToAll(@RequestParam("msg") String msg) {
        pushService.pushMsgToAll(msg);
    }

    /**
     * 推送给指定用户
     *
     * @param request 消息信息
     */
    @PostMapping("/pushOne")
    public void pushMsgToOne(@RequestBody MessageRequest request) {
        pushService.pushMsgToOne(request);
    }

}

