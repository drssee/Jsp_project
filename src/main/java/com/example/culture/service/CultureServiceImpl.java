package com.example.culture.service;

import com.example.api.ApiProvider;
import com.example.api.ApiRatePolicy;
import com.example.domain.Culture;
import com.example.culture.repository.CultureRepository;
import com.example.domain.PageRequest;
import com.example.domain.PageResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CultureServiceImpl implements CultureService {

    private final ApiProvider apiProvider;
    private final ApiRatePolicy apiRatePolicy;
    private final CultureRepository<Culture> cultureRepository;

    public CultureServiceImpl(
            ApiProvider apiProvider, ApiRatePolicy apiRatePolicy , CultureRepository<Culture> cultureRepository) {
        this.apiProvider = apiProvider;
        this.apiRatePolicy = apiRatePolicy;
        this.cultureRepository = cultureRepository;
    }

    @Override
    public void register() {
        try {
            //가져온 api 데이터로 culture 객체를 완성 후 insert
            Map<String,Integer> map = apiRatePolicy.apiRatePolicy();
            List<Culture> list = (List<Culture>) apiProvider.apiProvide();
            for (Culture culture : list) {
                culture.setCapacity(map.get("capacity"));
                if(culture.getPay_ay_nm().contains("유료")){
                    culture.setPrice(map.get("price"));
                }
                else{
                    culture.setPrice(0);
                }
                String dtlcont = culture.getDtlcont();
                dtlcont = dtlcont.replace("\r\n","000");
                dtlcont = dtlcont.replace("\t","000");
                System.out.println("dtlcont = "+dtlcont);
                culture.setDtlcont(dtlcont);
                //repository에 insert
                cultureRepository.insert(culture);
            }//for

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("api 등록에 실패했습니다");
            throw new RuntimeException(e);
        }
    }

    @Override
    public PageResponse<Culture> getCultures(PageRequest pageRequest) {
        return null;
    }

    @Override
    public String test() {
        return cultureRepository.test();
    }
}
