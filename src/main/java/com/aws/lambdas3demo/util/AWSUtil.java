package com.aws.lambdas3demo.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public final class AWSUtil {

    public static final String AWS_BUCKET = "TestBucket";
    
    private AWSUtil() {
    }
    
    public static boolean createBucket(String name){
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard().build();
    }
}
