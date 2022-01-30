package com.hust.webchat.util;

public interface Constants {

    interface ENTITY_STATUS {
        Integer ACTIVE = 1;
        Integer INACTIVE = 0;
    }

    enum JOB_TYPE {
        FULL_TIME(1, "Full-time"),
        PART_TIME(2, "Part-time"),
        CONTRACT(3, "Contract"),
        TEMPORARY(4, "Temporary"),
        OTHER(5, "Other"),
        VOLUNTEER(6, "Volunteer"),
        INTERNSHIP(7, "Internship");

        public Integer key;
        public String value;

        JOB_TYPE(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    interface WORK_PLACE_POLICY {
        Integer ONSITE = 1;
        Integer HYBRID = 2;
        Integer REMOTE = 3;
    }

    interface TYPE_IMAGE {
        String AVATAR = "AVATAR";
        String COVER = "COVER";
    }

    interface EMOTION_TYPE {
        String LIKE = "LIKE";
        String DISLIKE = "DISLIKE";
    }
}
