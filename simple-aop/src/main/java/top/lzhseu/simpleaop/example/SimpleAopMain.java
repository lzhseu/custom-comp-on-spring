package top.lzhseu.simpleaop.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import top.lzhseu.simpleaop.config.AppConfig;

import java.util.Date;

/**
 * @author lzh
 * @date 2021/1/2 19:46
 */
public class SimpleAopMain {

    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        HelloService helloService = context.getBean(HelloService.class);
        helloService.sayHello();
        helloService.sayHello("simple-aop");
        helloService.sayHello("lzh", "everything will be better", new Date());

    }
}
