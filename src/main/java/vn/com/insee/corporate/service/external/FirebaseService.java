package vn.com.insee.corporate.service.external;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import org.springframework.stereotype.Service;
import vn.com.insee.corporate.exception.FirebaseAuthenException;

@Service
public class FirebaseService {

    public  String verifyTokenId(String tokenId) throws FirebaseAuthenException{
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(tokenId);
            return decodedToken.getUid();
        } catch (FirebaseAuthException ex) {
            throw new FirebaseAuthenException(FirebaseAuthenException.FirebaseAuthenError.AUTH_ERROR, ex);
        }
    }

    public String getUserPhoneNumberByUid(String uid) throws FirebaseAuthenException{
        try {
            UserRecord userRecord = FirebaseAuth.getInstance().getUser(uid);
            return userRecord.getPhoneNumber();
        } catch (FirebaseAuthException ex) {
            throw new FirebaseAuthenException(FirebaseAuthenException.FirebaseAuthenError.AUTH_ERROR, ex);
        }
    }

}
