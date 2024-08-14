package com.sc.weave2.support.common.uuid

import com.github.f4b6a3.uuid.UuidCreator
import java.util.*

object UUIDCreator {

    /**
     * uuid version 7 기반으로 uuid 를 생성합니다.
     * millisecond 단위로 정렬된 uuid 를 생성하며,
     * 동일한 시간에 생성될 경우 단조 증가 counter를 활용해, unique 하게 생성됩니다.
     *
     * @return uuid version 7 type 2
     * @see <a href=https://datatracker.ietf.org/doc/draft-ietf-uuidrev-rfc4122bis>Universally Unique IDentifiers (UUID)</a>
     * @see UuidCreator.getTimeOrdered()
     * @author San Kim
     */
    fun create(): UUID {
        return UuidCreator.getTimeOrderedEpochPlus1()
    }

}
