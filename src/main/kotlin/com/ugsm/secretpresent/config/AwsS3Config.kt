package com.ugsm.secretpresent.config

import aws.sdk.kotlin.runtime.auth.credentials.StaticCredentialsProvider
import aws.sdk.kotlin.services.s3.S3Client
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AwsS3Config {
    @Value("\${aws.access_key_id}")
    private val accessKey: String ?= null;
    @Value("\${aws.secret_access_key}")
    private val secretAccessKey: String ?= null;
    @Bean
    fun amazonS3Client(): S3Client {
        return S3Client{
            region="ap-northeast-2"
            credentialsProvider = StaticCredentialsProvider{
                accessKeyId=accessKey
                secretAccessKey=this@AwsS3Config.secretAccessKey
            }
        }
    }
}