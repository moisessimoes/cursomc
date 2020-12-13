package com.moises.cursomc.services;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class S3Service {  ////Capítulo: Armazenamento de imagens usando Amazon S3  (NÃO ESTOU USANDO DE VERDADE, É APENAS PARA CONHECIMENTO)
	
	private Logger LOG = LoggerFactory.getLogger(S3Service.class);
	
	@Autowired
	private AmazonS3 s3Client;
	
	@Value("${s3.bucket}")
	private String bucketName;
	
	//=============================================================================================================================================
	
	public void uploadFile(String localFilePath) { // METODO PARA FAZER UPLOAD DE ARQUIVO PARA A AMAZON S3
		
		try {
			
			File file = new File(localFilePath);
			
			LOG.info("INCIANDO UPLOAD...");
			
			s3Client.putObject(new PutObjectRequest(bucketName, "teste", file));
			
			LOG.info("UPLOAD FINALIZADO!");
			
		} catch (AmazonServiceException e) {
			LOG.info("AMAZON EXCEPTION: " + e.getMessage());
			LOG.info("Status Code: " + e.getErrorCode());
			
		} catch(AmazonClientException e) {
			LOG.info("AMAZON CLIENT EXCEPTION: " + e.getMessage());
		}
	}
}
