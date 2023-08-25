package com.example.validation.config;

import com.example.validation.model.User;
import com.example.validation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class UserScheduleConfiguration {

    private final UserRepository userRepository;

    @Scheduled(fixedDelay = 10, initialDelay = 3, timeUnit = TimeUnit.SECONDS)
    private void print(){
        System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()));
    }

//    second,
//    minute,
//    hour,
//    day of month,
//    month,
//    day of week

//    @Scheduled(cron = "* * * * * *")
//    private void sale(){
//        List<User> userList = this.userRepository.findAllByDeletedAtIsNull()
//                .stream().filter(user -> {
//                    if (user.getAge() > 20){
//                        user.setAge(user.getAge() - 1);
//                        return true;
//                    }else
//                        return false;
//                }).toList();
//        this.userRepository.saveAll(userList);
//    }
}
