package com.threedays.application.user.port.outbound

import com.threedays.domain.user.entity.User
import com.threedays.domain.user.vo.Gender
import java.time.Year

interface UserEventPort {

    fun issueRegisterEvent(id: User.Id, gender: Gender, birthYear: Year)

}
