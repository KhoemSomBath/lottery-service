package com.hacknovation.systemservice.v1_0_0.utility;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.hacknovation.systemservice.constant.MessageConstant;
import com.hacknovation.systemservice.exception.httpstatus.BadRequestException;
import com.hacknovation.systemservice.exception.httpstatus.InternalServerError;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UploadFileUtility {

    private final AmazonS3 s3client;


    @Value("${spring.profile:#{'local'}}")
    String profile;

    @Value("${spring.AWS_CONFIG.BUCKET}")
    String bucketName;

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Implement upload image
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @Param url
     * @Param fileName
     * @Param largPath
     * @Param smallPath
     * @Return String
     */
    public String uploadFile(MultipartFile multipartFile, String path) {

        String imageEx = "";
        String uploadedUrl = "";
        try {
            File file = convertMultiPartToFile(multipartFile);
            String fileName = multipartFile.getOriginalFilename();
            Optional<String> optExt = getExtensionByStringHandling(fileName);
            if (optExt.isPresent()) {
                imageEx = optExt.get();
                fileName = Objects.requireNonNull(fileName).replace("." + imageEx, "_").concat(timestamp());
            }
            limitFileType(imageEx, new String[]{"APK"});

            uploadedUrl = s3bucket(fileName + "." + imageEx, file, path+profile);
            file.delete();

        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerError(MessageConstant.SOMETHING_WENT_WRONG, null);
        }

        return uploadedUrl;

    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException
    {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private void limitFileType(String ext, String[] fileType)
    {
        boolean b = false;

        for (String s : fileType) {
            if (s.equals(ext.toUpperCase())) {
                b = true;
                break;
            }
        }
        if (!b) {
            throw new BadRequestException(MessageConstant.FILE_NOT_ALLOW);
        }
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Generate unique name for file
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @Return String
     */
    public String generateFileName(String ext)
    {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String name = UUID.randomUUID().toString().replace("-", "").substring(0, 10) + timeStamp;

        if (ext != null) {
            name = name + "." + ext;
        }
        return name;
    }

    public String timestamp()
    {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }


    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Get extension image
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @Param filename String
     */
    public Optional<String> getExtensionByStringHandling(String filename)
    {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Upload file to amazon
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @Param filename
     * @Param file
     */
    private String s3bucket(String fileName, File file, String path)
    {
        try {
            String fullPath = bucketName + path;
            s3client.putObject(new PutObjectRequest(fullPath, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));
            return s3client.getUrl(fullPath, fileName).toString();
        } catch (AmazonServiceException e) {
            e.printStackTrace();
        }
        return null;
    }

}
