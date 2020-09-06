package com.example.app3;

import java.util.List;

/**
 */

public class CityDataModel {


    public String areaId;
    public String areaName;
    public List<CitiesBean> cities;

    public static class CitiesBean {

        public String areaId;
        public String areaName;
        public List<CountiesBean> counties;
        public static class CountiesBean {

            public String areaId;
            public String areaName;

        }
    }
}
