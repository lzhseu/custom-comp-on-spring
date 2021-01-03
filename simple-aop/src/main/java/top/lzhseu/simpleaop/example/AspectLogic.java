package top.lzhseu.simpleaop.example;

import org.springframework.stereotype.Component;
import top.lzhseu.simpleaop.anno.Aspect;
import top.lzhseu.simpleaop.anno.PointCut;
import top.lzhseu.simpleaop.enums.PointCutTypeEum;

/**
 * @author lzh
 * @date 2021/1/2 15:23
 */
@Component
@Aspect
public class AspectLogic {

    @PointCut(pointcuts = {"top.lzhseu.simpleaop.example.HelloService.sayHello", "top.lzhseu.simpleaop.example.HelloService.sayHello(java.lang.String)"}, type = PointCutTypeEum.BEFORE)
    public void before() {
        System.out.println("before ======");
    }

    @PointCut(pointcuts = "top.lzhseu.simpleaop.example.HelloService.sayHello(java.lang.String)", type = PointCutTypeEum.AFTER)
    public void after() {
        System.out.println("after ======");
    }

    @PointCut(pointcuts = "top.lzhseu.simpleaop.example.HelloService.sayHello(java.lang.String, java.lang.String, java.util.Date)", type = PointCutTypeEum.AROUND)
    public void around() {
        System.out.println("=== around ===");
    }
}
