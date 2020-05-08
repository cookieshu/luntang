package xgl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"xgl.mapper"})
public class LuntangApplication {
	public static void main(String[] args) {
		SpringApplication.run(LuntangApplication.class, args);
	}

}
