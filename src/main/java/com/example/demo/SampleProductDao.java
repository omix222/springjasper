package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public class SampleProductDao {
	public List<SampleProductModel> findByAll(){
        List<SampleProductModel> result = new ArrayList<SampleProductModel>();

        SampleProductModel data1 = new SampleProductModel("のり弁",400);
        SampleProductModel data2 = new SampleProductModel("幕の内弁当",500);

        result.add(data1);
        result.add(data2);

        return result;

    }
}
