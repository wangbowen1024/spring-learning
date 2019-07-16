package com.spring.service.impl;

import com.spring.service.StudentService;

import java.util.*;

/**
 * StudentServiceImpl class
 *
 * @author BowenWang
 * @date 2019/07/15
 */
public class StudentServiceImpl3 implements StudentService {

    private String[] myArray;
    private List<String> myList;
    private Set<String> mySet;
    private Map<String, String> myMap;
    private Properties myProperties;

    public void setMyArray(String[] myArray) {
        this.myArray = myArray;
    }

    public void setMyList(List<String> myList) {
        this.myList = myList;
    }

    public void setMySet(Set<String> mySet) {
        this.mySet = mySet;
    }

    public void setMyMap(Map<String, String> myMap) {
        this.myMap = myMap;
    }

    public void setMyProperties(Properties myProperties) {
        this.myProperties = myProperties;
    }

    public void saveStudent() {
        System.out.println("myArray=" + Arrays.toString(myArray));
        System.out.println(", myList=" + myList);
        System.out.println(", mySet=" + mySet);
        System.out.println(", myMap=" + myMap);
        System.out.println(", myProperties=" + myProperties);
    }
}
