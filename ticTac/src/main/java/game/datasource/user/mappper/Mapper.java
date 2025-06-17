package game.datasource.user.mappper;

import game.datasource.user.model.UserData;
import game.domain.user.model.UserDomain;

public class Mapper {
    public static UserData domainToData (UserDomain user) {
        return new UserData(user.getUuid() , user.getLogin(), user.getPassword());
    }

    public static UserDomain dataToDomain (UserData user) {
        return new UserDomain(user.getUuid() , user.getLogin(), user.getPassword());
    }
}

