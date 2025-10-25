package ru.arkhipov.MyThirdTestAppSpringBoot.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.arkhipov.MyThirdTestAppSpringBoot.model.Response;
import ru.arkhipov.MyThirdTestAppSpringBoot.util.DateTimeUtil;

import java.time.Instant;

@Service
@Qualifier("ModifySystemTimeResponseService")
public class ModifySystemTimeResponseService implements ModifyResponseService {

    @Override
    public Response modify(Response response) {
        response.setSystemTime(DateTimeUtil.getCustomFormat().format(Instant.now()));
        return response;
    }

}
