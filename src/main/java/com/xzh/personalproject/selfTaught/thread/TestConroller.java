package com.xzh.personalproject.selfTaught.thread;

import com.xzh.personalproject.selfTaught.thread.model.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author XZHH
 * @Description:
 * @create 2019/3/8 0008 11:49
 * @modify By:
 **/
@RestController
@RequestMapping("/test")
public class TestConroller {
    private static User user = new User("xiaoxu", 18);

    public static void main(String[] args) {
        new Thread(() -> {
            user.changeUser("2xu", 20);
        }, "T1").start();

        new Thread(() -> {
            user.changeUser("3xu", 21);
        }, "T2").start();
    }
}
