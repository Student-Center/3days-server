package com.threedays.aws.s3.user

import com.threedays.application.user.port.outbound.UserProfileImagePort
import com.threedays.aws.s3.config.S3Properties
import com.threedays.domain.user.entity.UserProfileImage
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetUrlRequest
import software.amazon.awssdk.services.s3.model.ObjectCannedACL
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

    companion object {

        private val logger = KotlinLogging.logger { }
    }


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
                acl(ObjectCannedACL.PUBLIC_READ)
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
        extension: UserProfileImage.Extension,
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

    override fun deleteImageById(id: UserProfileImage.Id) {
        val bucketName = s3Properties.userProfileImage.bucketName
        val objectKey = getObjectKey(id.value, UserProfileImage.Extension.PNG)

        try {
            logger.debug { "[S3] Deleting object from bucket: $bucketName, key: $objectKey" }
            val response = s3Client.deleteObject { builder ->
                builder.bucket(bucketName)
                builder.key(objectKey)
            }
            logger.debug { "[S3] Delete response: $response" }
        } catch (e: Exception) {
            logger.error { "[S3] Error deleting object: ${e.message}" }
            throw e
        }
    }

    private fun getObjectKey(
        id: UUID,
        extension: UserProfileImage.Extension,
    ): String {
        return "${s3Properties.userProfileImage.keyPrefix}/$id.${extension.value}"
    }

}
