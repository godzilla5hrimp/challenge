package com.n26.dto.validator;

import com.n26.dto.TransactionDto;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class TansactionValidator  {

//    /**
//     * This Validator validates *just* Person instances
//     */
//    public boolean supports(Class clazz) {
//        return TransactionDto.class.equals(clazz);
//    }
//
//    public void validate(Object obj, Errors e) {
//        ValidationUtils.rejectIfEmpty(e, "name", "name.empty");
//        TransactionDto tr = (TransactionDto) obj;
//        if (tr.getTimestamp() < 0) {
//            e.rejectValue("age", "negativevalue");
//        } else if (tr.getAmount()) {
//            e.rejectValue("age", "too.darn.old");
//        }
//    }
}
