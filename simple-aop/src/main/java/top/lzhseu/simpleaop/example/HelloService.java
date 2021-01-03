package top.lzhseu.simpleaop.example;

import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author lzh
 * @date 2021/1/2 17:36
 */
@Service
public class HelloService {

    public void sayHello() {
        System.out.println("hello default");
    }

    public void sayHello(String msg) {
        System.out.println("hello " + msg);
    }

    public void sayHello(String speaker, String msg, Date date) {
        System.out.println(speaker + "said that " + msg + " on " + date);
    }

}
