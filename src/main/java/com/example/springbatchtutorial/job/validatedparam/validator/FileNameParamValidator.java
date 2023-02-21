package com.example.springbatchtutorial.job.validatedparam.validator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.util.StringUtils;

public class FileNameParamValidator implements JobParametersValidator {

    @Override
    public void validate(final JobParameters parameters) throws JobParametersInvalidException {
        final String fileName = parameters.getString("fileName");

        if (!StringUtils.endsWithIgnoreCase(fileName, ".csv")) {
            throw new JobParametersInvalidException("csv 파일이 아닙니다.");
        }
    }
}
