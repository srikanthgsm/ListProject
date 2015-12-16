package com.telstra.mobile.model;

import java.util.ArrayList;

public class JSONResponseModel {
    public String title;
    public ArrayList<InfoListItem> rows;
    public static class InfoListItem{
        public String title;
        public String description;
        public String imageHref;
    }

}


