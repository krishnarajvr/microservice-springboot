package com.company.micro.common.validator;

import com.company.micro.common.ErrorData;
import com.company.micro.common.MessageByLocale;
import com.company.micro.common.validator.schema.ValidationMessage;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class microValidatorMessage {

    protected static final Logger log = LoggerFactory.getLogger(microValidatorMessage.class);
    /**
     * Message Locale.
     */
    protected MessageByLocale messageByLocale;

    @Autowired
    public microValidatorMessage(MessageByLocale messageByLocale) {
        this.messageByLocale = messageByLocale;
    }

    public ErrorData getMessage(ValidationMessage vm) {



        log.info("******************************");
        log.info("Code:" + vm.getCode());
        log.info("Type:" + vm.getType());
        log.info("Path:" + vm.getPath());
        log.info("MessageTemplate:" + vm.getFormat().toPattern());
        log.info("Message:" + vm.getMessage());
        log.info("Argument length:" + vm.getArguments().length);
        Map<String, Object> details = vm.getDetails();

        if(details != null) {
            details.forEach((k, v) -> {
                log.info("Details : " + k + ":" + v);
            });
        }

        String[] currentArgs = vm.getArguments();
        String[] args = ArrayUtils.add(currentArgs, 0, vm.getPath());
        for (String arg : args) {
            log.info("Arg:" + arg);
        }
        String microMessage = messageByLocale.getMessage("validation." + vm.getType(), args);
        log.info("Micro Message : " + microMessage);
        log.info("******************************");

        if (!StringUtils.isBlank(microMessage)) {
            microMessage = StringUtils.remove(microMessage, "$.");
        }
        HashMap<String, String> key = new HashMap<String, String>();
        key.put(vm.getType(), vm.getType());
        return new ErrorData.Builder()
                .code(vm.getCode())
                .message(vm.getMessage())
                .path(vm.getPath())
                .key(key)
                .target(args[0])
                .build();

    }


}
