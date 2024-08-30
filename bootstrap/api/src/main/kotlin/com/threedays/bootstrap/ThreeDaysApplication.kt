package com.threedays.bootstrap

import jakarta.annotation.PostConstruct
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.util.*

@SpringBootApplication
class ThreeDaysApplication {

    @PostConstruct
    fun started() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"))
    }

}


fun main(args: Array<String>) {
    runApplication<ThreeDaysApplication>(*args)
}
