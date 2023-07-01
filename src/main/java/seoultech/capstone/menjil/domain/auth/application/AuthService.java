package seoultech.capstone.menjil.domain.auth.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seoultech.capstone.menjil.domain.auth.dao.UserRepository;
import seoultech.capstone.menjil.domain.auth.domain.User;
import seoultech.capstone.menjil.domain.auth.dto.request.SignUpRequestDto;
import seoultech.capstone.menjil.domain.auth.dto.response.SignUpResponseDto;
import seoultech.capstone.menjil.global.exception.CustomException;
import seoultech.capstone.menjil.global.exception.ErrorCode;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {
    private final HttpServletResponse response;
    private final UserRepository userRepository;
    private final String redirectUrl = "https://www.menjil-menjil.com/register";

    /**
     * 회원가입 전, 유저가 이미 db에 존재하는지 조회.
     *
     * @param email
     * @param socialType
     */
    public void checkUserExistsInDb(String email, String socialType) throws IOException {
        List<User> userInDb = userRepository.findUserByEmailAndProvider(email, socialType);

        if (userInDb.size() > 0) {
            throw new CustomException(ErrorCode.USER_DUPLICATED);
        } else {
            try {
                response.sendRedirect(redirectUrl);
            } catch (IOException e) {
                log.error(">> redirect url is wrong :", e);
            }
        }
    }

    /**
     * 닉네임 중복 조회
     */
    @Transactional(readOnly = true)
    public String checkNicknameDuplication(String nickname) {
        User nicknameExistsInDb = userRepository.findUserByNickname(nickname)
                .orElse(null);
        if (nicknameExistsInDb != null) {
            throw new CustomException(ErrorCode.NICKNAME_DUPLICATED);
        }
        return "Nickname is available";
    }

    /**
     * 회원가입 로직 수행
     */
    @Transactional
    public SignUpResponseDto signUp(SignUpRequestDto requestDto) {
        // SignUpRequestDto -> User Entity 변환
        User user = requestDto.toUser();

        // 기존에 중복된 유저가 있는 지 조회
        // 이 부분은 현재는 필요없다.
        /*
        List<User> userInDb = userRepository.findUserByEmailAndProvider(user.getEmail(), user.getProvider());
        if (userInDb.size() > 0) {
            throw new CustomException(ErrorCode.USER_DUPLICATED);
        }
         */

        // 혹시 클라이언트에서 닉네임 중복 검증을 놓친 경우 확인
        User nicknameExistsInDb = userRepository.findUserByNickname(user.getNickname())
                .orElse(null);

        if (nicknameExistsInDb != null) {
            throw new CustomException(ErrorCode.NICKNAME_DUPLICATED);
        }

        // db에 저장
        userRepository.save(user);

        // User Entity -> UserSignupResponseDto
        return new SignUpResponseDto(user);
    }

}
