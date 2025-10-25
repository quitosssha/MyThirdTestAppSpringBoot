package ru.arkhipov.MyThirdTestAppSpringBoot.service;

import org.springframework.stereotype.Service;
import ru.arkhipov.MyThirdTestAppSpringBoot.model.Response;

@Service
public interface ModifyResponseService {
    Response modify(Response response);
}
