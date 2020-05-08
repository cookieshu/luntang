package xgl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xgl.mapper.UserMapper;
import xgl.model.User;
import xgl.model.UserExample;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public void createOrUpdate(User user) {
        //通过AccountId查询user
        UserExample example=new UserExample();
        example.createCriteria().andAccountIdEqualTo(user.getAccountId());
        List<User> users = userMapper.selectByExample(example);
        if (users.size()==0) {
            //插入
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        } else {
            //更新
            User dbUser=users.get(0);//原来的user
            User updateUser=new User();//创建要更新的user
            updateUser.setGmtModified(System.currentTimeMillis());
            updateUser.setAvatarUrl(user.getAvatarUrl());
            updateUser.setName(user.getName());
            updateUser.setToken(user.getToken());
            UserExample userExample=new UserExample();
            userExample.createCriteria().andIdEqualTo(dbUser.getId());
            //更新
            userMapper.updateByExampleSelective(updateUser,userExample);
        }
    }
}
