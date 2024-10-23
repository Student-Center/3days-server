package com.threedays.domain.user.vo

/**
 * 직업군
 * @param koreanName 한국어 이름
 * @param icon 아이콘
 */
enum class JobOccupation(
    val koreanName: String,
    val icon: String
) {

    BUSINESS_ADMIN("경영·관리", "💼"),
    SALES_MARKETING("영업·마케팅", "📊"),
    RESEARCH_DEVELOPMENT("연구·개발", "🔬"),
    IT_INFORMATION("IT·정보통신", "💻"),
    FINANCE_ACCOUNTING("금융·회계", "💰"),
    MANUFACTURING_PRODUCTION("생산·제조", "⚙️"),
    EDUCATION_ACADEMIA("교육·학술", "🏫"),
    LAW_ADMINISTRATION("법률·행정", "⚖️"),
    MILITARY_SECURITY("경찰·소방·군인", "🪖"),
    HEALTHCARE_MEDICAL("의료·보건", "🩺"),
    MEDIA_ENTERTAINMENT("미디어·언론", "📹"),
    ARTS_DESIGN("예술·디자인", "🎨"),
    SPORTS("스포츠", "⚽"),
    CONSTRUCTION_ENGINEERING("건설·토목", "🏗️"),
    TRANSPORTATION_LOGISTICS("운송·물류", "🚆"),
    AGRICULTURE_FARMING("농림어업", "🥬"),
    SERVICE_INDUSTRY("서비스", "💬"),
    OTHER("기타", "🏢"),

}
