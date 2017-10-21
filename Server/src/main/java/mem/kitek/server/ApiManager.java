package mem.kitek.server;

import mem.kitek.server.api.*;

/**
 * Created by RINES on 20.10.17.
 */
public class ApiManager {

    public static void init() {
        new TestMethod();
        new GetHallById();
        new GetHallCategoryById();
        new GetBuildingById();
        new GetHallsGeneralInfo();
        new GetPictures();
        new TinderFinished();
    }

}
