package com.threedays.domain.user.entity

import com.threedays.domain.user.vo.JobId
import com.threedays.support.common.base.domain.DomainEntity

/**
 * 직업 엔티티
 * @property id 직업 ID
 * @property occupation 직업군
 * @property name 직업명
 */
data class Job(
    override val id: JobId,
    val occupation: Occupation,
    val name: String
) : DomainEntity<Job, JobId>() {

    enum class Occupation(
        val koreanName: String,
        val icon: String
    ) {
        BUSINESS_ADMIN("경영·사무", "💼"),
        FINANCE_INSURANCE("금융·보험", "💰"),
        RESEARCH_ENGINEERING("연구·공학기술", "🔬"),
        EDUCATION("교육", "🏫"),
        LAW("법률", "⚖️"),
        POLICE_MILITARY("경찰·소방·군인", "🪖"),
        IT_SOFTWARE("IT·소프트웨어", "💻"),
        DESIGN_ART("디자인·예술", "🎨"),
        SPORTS("스포츠", "⚽"),
        HEALTHCARE("보건·의료", "🩺"),
        FREELANCER("프리랜서", "💡"),
        OTHER("기타", "🏢")
    }

}
