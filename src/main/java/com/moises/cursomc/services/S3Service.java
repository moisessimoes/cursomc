package com.moises.cursomc.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.moises.cursomc.services.exceptions.FileException;

@Service
public class S3Service {  ////Capítulo: Armazenamento de imagens usando Amazon S3  (NÃO ESTOU USANDO DE VERDADE, É APENAS PARA CONHECIMENTO)
	
	private Logger LOG = LoggerFactory.getLogger(S3Service.class);
	
	@Autowired
	private AmazonS3 s3Client;
	
	@Value("${s3.bucket}")
	private String bucketName;
	
	//=============================================================================================================================================
	
	public URI uploadFile(MultipartFile multiPartFile) { // METODO PARA FAZER UPLOAD DE ARQUIVO PARA A AMAZON S3
		
		try {
			
			String fileName = multiPartFile.getOriginalFilename();
			InputStream is = multiPartFile.getInputStream();
			String contentType = multiPartFile.getContentType();
				
			return uploadFile(is, fileName, contentType);
			
		} catch (IOException e) {
			throw new FileException("Erro de IO: " + e.getMessage());
		}
			
	}
	
	//=============================================================================================================================================
	
	public URI uploadFile(InputStream is, String fileName, String contentType) {
		
		try {
			
			ObjectMetadata objMeta = new ObjectMetadata();
			objMeta.setContentType(contentType);
			
			LOG.info("INCIANDO UPLOAD...");
			
			s3Client.putObject(new PutObjectRequest(bucketName, fileName, is, objMeta));
			
			LOG.info("UPLOAD FINALIZADO!");
			
			return s3Client.getUrl(bucketName, fileName).toURI();
			
		} catch (URISyntaxException e) {
			throw new FileException("Erro ao converter a URL para URI.");
		}
	}
}
