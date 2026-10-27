package in.ameya.schedulers.service.Impl.autoMatchSchedulerImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.ameya.schedulers.dao.autoMatchDao.AutoMatchDao;
import in.ameya.schedulers.service.autoMatchService.AutoMatchService;

@Service
public class AutoMatchServiceImpl implements AutoMatchService {

    @Autowired
    private AutoMatchDao autoMatchDao;

    @Override
    public void executeAutoMatch(Integer userKey) {
        autoMatchDao.runAutoMatch(userKey);
    }
}

