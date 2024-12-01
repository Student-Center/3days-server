package com.threedays.aws.s3.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "aws.s3")
class S3Properties(
    val userProfileImage: UserProfileImage,
) {

    data class UserProfileImage(
        val bucketName: String,
        val keyPrefix: String,
    )

}
