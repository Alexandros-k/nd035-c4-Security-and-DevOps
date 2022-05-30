package com.udacity.examples.Testing;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;

public class HelperTest extends TestCase {
    List<String> empNames = Arrays.asList("sareeta", "", "john","");
    List<Integer> yrsOfExperience = Arrays.asList(13,4,15,6,17,8,19,1,2,3);
    @Test
    public void testGetCount() {

        long count = Helper.getCount(empNames);
        assertEquals(count,2);
    }

    @Test
    public void testGetStats() {
        IntSummaryStatistics stats = Helper.getStats(yrsOfExperience);
        assertEquals(stats.getCount(),10);
    }

    @Test
    public void testGetStringsOfLength3() {
        long stringsOfLength3 = Helper.getStringsOfLength3(empNames);
        assertEquals(stringsOfLength3,0);
    }

    @Test
    public void testGetSquareList() {
        List<Integer> squareList = Helper.getSquareList(yrsOfExperience);
        assertEquals(squareList.size(),10);
    }

    @Test
    public void testGetMergedList() {
        String mergedList = Helper.getMergedList(empNames);
        assertTrue(!mergedList.isEmpty());
    }

    @Test
    public void testGetFilteredList() {
        String mergedList = Helper.getMergedList(empNames);
        assertTrue(!mergedList.isEmpty());
    }
}