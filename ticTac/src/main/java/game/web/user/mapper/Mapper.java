package game.web.user.mapper;

import game.domain.user.model.UserDomain;
import game.web.user.model.UserWeb;

public class Mapper {
    public static UserWeb domainToWeb (UserDomain user) {
        return new UserWeb(user.getUuid() , user.getLogin(), user.getPassword());
    }

    public static UserDomain webToDomain (UserWeb user) {
        return new UserDomain(user.getUuid() , user.getName(), user.getPassword());
    }
}
