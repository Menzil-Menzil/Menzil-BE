package seoultech.capstone.menjil.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorIntValue {

    // auth
    USER_ALREADY_IN_DB(-1),


    // common
    INTERNAL_SERVER_ERROR(-100);

    private final int value;
}
