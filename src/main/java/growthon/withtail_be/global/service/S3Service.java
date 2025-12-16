package growthon.withtail_be.global.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import growthon.withtail_be.global.code.ErrorStatus;
import growthon.withtail_be.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 업로드
    public String upload(MultipartFile file, String dir) {
        if (file == null || file.isEmpty()) {
            throw new GeneralException(ErrorStatus.S3_FILE_EMPTY);
        }

        String originalName = file.getOriginalFilename();
        if (originalName == null || originalName.isBlank()) {
            throw new GeneralException(ErrorStatus.S3_FILE_NAME_INVALID);
        }

        String ext = getExt(originalName);
        String key = dir + "/" + UUID.randomUUID() + (ext.isBlank() ? "" : "." + ext);

        try (InputStream in = file.getInputStream()) {
            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentLength(file.getSize());
            meta.setContentType(file.getContentType());

            amazonS3.putObject(new PutObjectRequest(bucket, key, in, meta));

            return amazonS3.getUrl(bucket, key).toString();

        } catch (IOException e) {
            log.error("S3 업로드 실패 key={}", key, e);
            throw new GeneralException(ErrorStatus.S3_UPLOAD_FAILED);
        } catch (Exception e) {
            log.error("S3 업로드 실패(unknown) key={}", key, e);
            throw new GeneralException(ErrorStatus.S3_UPLOAD_FAILED);
        }
    }

    // URL로 삭제
    public void deleteByUrl(String url) {
        if (url == null || url.isBlank()) return;

        String key = extractKey(url);

        try {
            amazonS3.deleteObject(bucket, key);
        } catch (Exception e) {
            log.error("S3 삭제 실패 key={}", key, e);
            throw new GeneralException(ErrorStatus.S3_DELETE_FAILED);
        }
    }

    // 교체
    public String replace(MultipartFile newFile, String dir, String oldUrl) {
        String newUrl = upload(newFile, dir);

        try {
            deleteByUrl(oldUrl);
        } catch (Exception e) {
            log.warn("S3 기존 파일 삭제 실패 oldUrl={}", oldUrl, e);
        }

        return newUrl;
    }

    // helpers

    private String getExt(String filename) {
        int idx = filename.lastIndexOf('.');
        if (idx < 0 || idx == filename.length() - 1) return "";
        return filename.substring(idx + 1).toLowerCase();
    }

    private String extractKey(String url) {
        int idx = url.indexOf(".amazonaws.com/");
        if (idx == -1) {
            throw new GeneralException(ErrorStatus.S3_URL_INVALID);
        }
        return url.substring(idx + ".amazonaws.com/".length());
    }
}
