package org.server.db.repository.criteria;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSearchCriteria {
    String name;
    Integer kills;
    Integer page;
    Integer pageSize;
    String country;

    public UserSearchCriteria() {
    }

    public UserSearchCriteria(String name, Integer kills, Integer page, Integer pageSize, String country) {
        this.name = name;
        this.kills = kills;
        this.page = page;
        this.pageSize = pageSize;
        this.country = country;
    }

}
