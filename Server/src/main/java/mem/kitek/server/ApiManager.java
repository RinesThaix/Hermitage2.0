package mem.kitek.server;

import mem.kitek.server.api.*;

/**
 * Created by RINES on 20.10.17.
 */
public class ApiManager {

    public static void init() {
        new TestMethod();
        new GetPeopleInHall();
        new GetHallById();
        new GetHallCategoryById();
        new GetHallsGeneralInfo();
        new GetPictures();
    }

}
