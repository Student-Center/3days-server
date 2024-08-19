package com.sc.weave2.bootstrap

import jakarta.annotation.PostConstruct
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.util.*

@SpringBootApplication
class WeaveApiApplication {

    @PostConstruct
    fun started() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"))
    }

}


fun main(args: Array<String>) {
    runApplication<WeaveApiApplication>(*args)
}
