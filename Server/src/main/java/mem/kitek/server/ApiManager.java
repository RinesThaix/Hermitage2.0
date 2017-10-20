package mem.kitek.server;

import mem.kitek.server.api.GetPeopleInHall;
import mem.kitek.server.api.TestMethod;

/**
 * Created by RINES on 20.10.17.
 */
public class ApiManager {

    public static void init() {
        new TestMethod();
        new GetPeopleInHall();
    }

}
