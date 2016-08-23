package com.rob.betBot.dao.hibernate;

import org.springframework.stereotype.Repository;

import com.rob.betBot.dao.WhEventParentDao;
import com.rob.betBot.model.wh.WhEventParentData;

@Repository
public class HibernateWhEventParentDao
    extends AbstractHibernateDao<WhEventParentData>
    implements WhEventParentDao {

    public HibernateWhEventParentDao() {
        super(WhEventParentData.class);
    }
}
