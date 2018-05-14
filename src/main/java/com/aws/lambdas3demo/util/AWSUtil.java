package com.aws.lambdas3demo.util;

import java.io.File;
import java.util.function.Consumer;

import com.amazonaws.AmazonClientException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressEventType;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;

public final class AWSUtil {

	private static final String AWS_KEY = "AKIAJTJHIWZN6KO3X4SA";
	private static final String AWS_SECRET = "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";
	
    public static final String AWS_BUCKET = "lambdas3-testbucket";
    
    private AWSUtil() {
    }
    
    public static void uploadFileToS3(String bucketName, String objKey, String filePath, Consumer<ProgressEvent> onLoad, Consumer<ProgressEvent> onResult){
        try {
        	AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
            		.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(AWS_KEY, AWS_SECRET)))
            		.withRegion(Regions.US_WEST_1)
            		.build();
        	
        	if(s3Client.doesBucketExistV2(AWS_BUCKET)) {
        		//PutObjectResult res = s3Client.putObject(AWS_BUCKET, objKey, new File(filePath));
        		
            	TransferManager tm = TransferManagerBuilder.standard()
            			.withS3Client(s3Client)
            			.build();

            	Upload upload = tm.upload(bucketName, objKey, new File(filePath));
            	upload.addProgressListener(new ProgressListener() {
					
					@Override
					public void progressChanged(ProgressEvent progressEvent) {
						if(progressEvent.getEventType() == ProgressEventType.TRANSFER_COMPLETED_EVENT) {
							//System.out.println(upload.getDescription());
							onResult.accept(progressEvent);
						}
						else
							onLoad.accept(progressEvent);
					}
				});
            	
            	//BLOCKS CURRENT THREAD
            	//upload.waitForCompletion();
            	
            	//res.getMetadata().getRawMetadata().forEach((k, v) -> System.out.printf("%s - %s\n", k, v));
        		
        	} else {
        		throw new RuntimeException("Specified bucket does not exist");
        	}
        	
		} catch (SdkClientException e) {
			throw new RuntimeException(e);
		} catch (AmazonClientException e) {
			throw new RuntimeException(e);
		} /*catch (InterruptedException e) {
			throw new RuntimeException(e);
		}*/
    }
}
