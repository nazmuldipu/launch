package com.ship.nazmul.ship.services.impl;

import com.ship.nazmul.ship.commons.PageAttr;
import com.ship.nazmul.ship.entities.Activity;
import com.ship.nazmul.ship.entities.User;
import com.ship.nazmul.ship.repositories.ActivityRepository;
import com.ship.nazmul.ship.services.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {
    private final ActivityRepository activityRepo;

    @Autowired
    public ActivityServiceImpl(ActivityRepository activityRepo) {
        this.activityRepo = activityRepo;
    }

    public Activity save(Activity activity) {
        if (activity.getId() == null) { // new activity (user logged in)
            Activity firstActivity = this.findFirst();
            if (firstActivity != null) {
                Long total = firstActivity.getTotalVisitors();
                activity.setTotalVisitors(++total);
                firstActivity.setTotalVisitors(total);
                this.activityRepo.save(firstActivity);
            }
        }
        return this.activityRepo.save(activity);
    }

    @Override
    public Activity findFirst() {
        return this.activityRepo.findFirstBy();
    }

    @Override
    public Activity findLast(User user) {
        return activityRepo.findFirstByUserOrderByIdDesc(user);
    }

    @Override
    public Page<Activity> findByUser(User user, int page, int size) {
        return this.activityRepo.findByUser(user, new PageRequest(page, size, Sort.Direction.DESC, PageAttr.SORT_BY_FIELD_ID));
    }

    @Override
    public Activity findOne(long id) {
        return this.activityRepo.findOne(id);
    }

    @Override
    public List<Activity> findAll() {
        return this.activityRepo.findAll();
    }

    @Override
    public void delete(Long id) {
        this.activityRepo.delete(id);
    }

}
