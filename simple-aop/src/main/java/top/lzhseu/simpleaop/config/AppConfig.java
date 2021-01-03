package top.lzhseu.simpleaop.config;

import org.springframework.context.annotation.ComponentScan;
import top.lzhseu.simpleaop.anno.EnableSimpeAop;

/**
 * @author lzh
 * @date 2021/1/2 19:52
 */
@ComponentScan("top.lzhseu")
@EnableSimpeAop
public class AppConfig {
}
