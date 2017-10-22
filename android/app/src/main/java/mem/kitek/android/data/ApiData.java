package mem.kitek.android.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Created by cat on 10/21/17.
 */

public class ApiData {
    @Data
    public static class ApiErr {
        String error_message;
        int error_id;
        boolean error;
    }

    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class ApiException extends RuntimeException {
        public final ApiErr err;
        public @Getter
        String description;
    }

    @Data
    public static class HallInfo {
        int hall_id;
        int hall_floor;
        int people;
        String hall_name;
        Category category;
        Building building;
    }

    @Data
    public static class Category {
        int images;
        String name;
        int id;
    }

    @Data
    public static class Building {
        int people_on_floor;
        int floor_limit;
        String name;
        int id;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "amount",
            "pictures"
    })
    public static class PictureData {

        @JsonProperty("amount")
        public Integer amount;
        @JsonProperty("pictures") @Getter
        public List<Picture> pictures = null;

    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "pid",
            "cid"
    })
    public static class Picture {

        @JsonProperty("pid")
        public Integer pid;
        @JsonProperty("cid")
        public Integer cid;

    }

    @NoArgsConstructor
    public static class PeopleList {
        @JsonProperty("people")
        public List<Integer> people;
    }

    public static class HallsPath {
        @Getter
        List<Integer> halls;
    }

    public static class Mins {
        public int minutes;
    }
}
