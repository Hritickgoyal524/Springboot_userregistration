package com.checkdynamo.demo.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class Otpservice {
    private static final Integer ExpireNp = 10;
    private LoadingCache<String, Integer> otpcach;

    public Otpservice() {
        super();
        otpcach = CacheBuilder.newBuilder().expireAfterWrite(ExpireNp, TimeUnit.MINUTES).build(new CacheLoader<String, Integer>() {
            @Override
            public Integer load(String s) {
                return 0;

            }

        });
    }

    public int generateOTP(String Key) {
        Random random = new Random();
        int otp = 1000 + random.nextInt(900000);
        otpcach.put(Key, otp);
        return otp;


    }

    public int getotp(String Key) {
        try {
            return otpcach.get(Key);
        } catch (Exception e) {
            return 0;
        }

    }

    public void clearotp(String Key){
        otpcach.invalidate(Key);
    }
}
