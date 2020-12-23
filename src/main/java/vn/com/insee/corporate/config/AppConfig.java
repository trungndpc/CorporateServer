package vn.com.insee.corporate.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.AuthenticationEntryPoint;
import vn.com.insee.corporate.response.BaseResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;


@Configuration
public class AppConfig {
    private static final String CONFIG_FIREBASE = "{\"type\":\"service_account\",\"project_id\":\"insee-1ce3f\",\"private_key_id\":\"dea4b5aac2533b542c40d686a4e52f85c5386bdb\",\"private_key\":\"-----BEGIN PRIVATE KEY-----\\nMIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC7kcBlk6b8ochI\\nXokmFeFphQvPLfTIFL5+PWqsqB5aWNQzy3IBk8H/q2jw5KycIY1piEn9Rz+3XzTF\\n3pFc/kHMgxKXee0DUnTINmvXv/5AwBTx/4kcAayPQMjjkQ6jAkD2FjwDM0EwVMH9\\njnGCma9MVjao0S6m6aSWw4s4V0pQRpsW4C15taHVDUZoB2SgFQM31f0Da3Fshuwh\\nFuHuwcxCaAx5ZEVTf1ssLUbhh1kl/ebWbGmxEtvF03xhmUBuWrOmmxnzeO+scIHF\\npCvSLP4wPcCkuOtTNWy9IY+U8LaEHQVi2AIDVV623UCmZhGGXbTMXsi5gpkF2DPo\\nUl4qn1MRAgMBAAECggEACtGgvXMnbPe3bc4vHrhhHrUe/eGelnpU1QL9n80YKBmq\\nzrx/vE3NoS29wTRNbPmF9rX235IwH4dxK68XZxvex3kxdbWBiWEkjYZPENwZAhra\\nMm2s9sW1cnNYCwE7TQV0ogUdC8m1a0BeSp3gV/o9mVt5LmJXbKb6GHyZr4OgbJIW\\nhiw3ZHN2XJPZ/tpII9YNBT9hdhZ2XFlVQWBy77P3XiN3GHdKG0Oe1uP/Hn+o57eY\\ngGHmMvhMgQWDVULjCpaf339looMDPudAX4i7Qm728bH+lQs0zlJedbI8vzvKE7KW\\nw91tIIPpCKbeAGWb0hIKKKZ/wklLCGr4p6fhV2fIwQKBgQD4eWaVKSqrBkcdgo3e\\nL4y1oYjMemMT1I0stUgYwIVfAVu86X8C2lqYA0xVlhB/bLWWsMv1f8wfEpZcTBEH\\nkBZNvcdLdRw1G69Hd1nGLGW5jHy274Q8RQymZ1Yyy6RFSuo0/IYnq8MnM5rCtwBC\\nLGCWXVAHjI/Mit7yLLRS8/xMoQKBgQDBQByMUyj07PsY0uZhnapEaEyVNy/ESEuV\\nXjuCoPTQU0BbUQ6F8lw2MbxuUc2GaGnC/2DxA5948oZADlawHM5nCRq0B1i9uGGV\\nrZyRKkVgPiPx6MHNc9DnCm5f6XkM6yu4/wV0L7sViYOLyrxAa1Ok+AhrkmBgs9OW\\n769qM1GAcQKBgQDOVko4vdnf8jOmLt9Ozh/ESSNH4q5aLl0Z7mSzoVRgA8nBxAkk\\nPl2S9NI0FI0YtBbLz39gHENO3mmctdJCfIX5Awr3/4VwGNBiwZr8dav3/NEoPRiL\\n0C9kFFZBkliU7lQ1sipZ0UobSMBQyBVf3mhLFaw6yKA1lHD1nOgWJuSyYQKBgFbe\\njva1Wy/eZ3FdPJqc2TEvAV4CFMLTDMb0OSQbXnFXOxLReQnVTIZADUg6FX1Z6lDx\\no44z5rfjrV2WFHnOOSov+YT72HRQMe2qwTqwGceNp3LTAmtsqHdzRPJwbS3VeS4L\\n1VPJMPwZeLyaQc1jcZIyvPY+MnzF2VN8IwpGT1jBAoGBAPS6Hg0PfIVvzFYRvsB4\\n20lOh/XqJUDJn+T/C1N98+WWICWw5eUQjiJQ8mRllunYqPM9jVDho+iLMdVmW/Fr\\n49AOdib6y/WAxuk81OTfCGXE8DNbf0sKHtAfhZIw7PLxDM7HFFbFNDH4W7w6QBGP\\nbmLHJcD/0ucVajfmnE2t7h39\\n-----END PRIVATE KEY-----\\n\",\"client_email\":\"firebase-adminsdk-2j8tv@insee-1ce3f.iam.gserviceaccount.com\",\"client_id\":\"102623768136418258507\",\"auth_uri\":\"https://accounts.google.com/o/oauth2/auth\",\"token_uri\":\"https://oauth2.googleapis.com/token\",\"auth_provider_x509_cert_url\":\"https://www.googleapis.com/oauth2/v1/certs\",\"client_x509_cert_url\":\"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-2j8tv%40insee-1ce3f.iam.gserviceaccount.com\"}";

    @Autowired
    private ObjectMapper objectMapper;

    @EventListener(ApplicationReadyEvent.class)
    public void initOnStartUp() throws Exception{
        InputStream targetStream = new ByteArrayInputStream(CONFIG_FIREBASE.getBytes());
        FirebaseOptions options = FirebaseOptions.builder().setCredentials(GoogleCredentials.fromStream(targetStream))
                .build();
        FirebaseApp.initializeApp(options);
    }


    @Bean
    public AuthenticationEntryPoint unauthenticatedEntryPoint() {
        return (request, resp, authException) -> {
            authException.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
            resp.setHeader("Content-Type", "application/json");
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setError(HttpStatus.UNAUTHORIZED.value());
            resp.getWriter().println(objectMapper.writeValueAsString(baseResponse));
        };
    }
}
