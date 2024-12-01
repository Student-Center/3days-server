package com.threedays.aws.s3.user

import com.threedays.application.user.port.outbound.UserProfileImagePort
import com.threedays.aws.s3.config.S3Properties
import com.threedays.domain.user.entity.UserProfileImage
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetUrlRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import java.net.URL
import java.time.Duration
import java.util.*

@Component
class UserProfileImageS3Adapter(
    private val s3Client: S3Client,
    private val s3Presigner: S3Presigner,
    private val s3Properties: S3Properties,
) : UserProfileImagePort {

    override fun getUploadUrl(
        id: UUID,
        extension: UserProfileImage.Extension,
        expiresIn: Long,        // seconds
        maxContentLength: Long, // bytes
    ): URL {
        val putObjectRequest: PutObjectRequest = PutObjectRequest
            .builder()
            .apply {
                bucket(s3Properties.userProfileImage.bucketName)
                key(getObjectKey(id, extension))
                contentLength(maxContentLength)
            }.build()


        val presignedRequest: PutObjectPresignRequest = PutObjectPresignRequest
            .builder()
            .apply {
                signatureDuration(Duration.ofSeconds(expiresIn))
                putObjectRequest(putObjectRequest)
            }
            .build()

        return s3Presigner
            .presignPutObject(presignedRequest)
            .url()
    }

    override fun findImageUrlByIdAndExtension(
        id: UserProfileImage.Id,
        extension: UserProfileImage.Extension

    ): URL {
        val getObjectRequest = GetUrlRequest
            .builder()
            .bucket(s3Properties.userProfileImage.bucketName)
            .key(getObjectKey(id.value, extension))
            .build()

        return s3Client
            .utilities()
            .getUrl(getObjectRequest)
    }

    private fun getObjectKey(
        id: UUID,
        extension: UserProfileImage.Extension,
    ): String {
        return "${s3Properties.userProfileImage.keyPrefix}/$id.${extension.value}"
    }

}
