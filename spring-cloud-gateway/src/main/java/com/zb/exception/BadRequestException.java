package com.zb.exception;

import com.zb.i18n.InternationalizationField;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BadRequestException extends RuntimeException {

    private InternationalizationField internationalizationField;

    @Override
    public String getMessage() {
        if (internationalizationField != null) {
            return internationalizationField.getCode() + internationalizationField.getDefaultMsg();
        }
        return super.getMessage();
    }
}
