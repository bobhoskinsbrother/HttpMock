package com.hosksoft.httpmock.http;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class HeaderTest {

    @Test(expected = IllegalArgumentException.class)
    public void failsWhenNoArgs() {
        new Header("name");
    }

    @Test
    public void canReturnWithOneValue() {
        final Header unit = new Header("name", "value");
        assertThat(unit.toString(), is("name:value"));
    }

    @Test
    public void canReturnWithTwoValues() {
        final Header unit = new Header("name", "value_one", "value_two");
        assertThat(unit.toString(), is("name:value_one, name:value_two"));
    }

    @Test
    public void equalityIsEqual() {
        final Header one = new Header("name", "value_one");
        final Header two = new Header("name", "value_one");
        assertThat(one.equals(two), is(true));
    }

    @Test
    public void equalityIsEqualAfterDeDup() {
        final Header one = new Header("name", "value_one", "value_one");
        final Header two = new Header("name", "value_one");
        assertThat(one.equals(two), is(true));
    }

    @Test
    public void equalityIsNotEqual() {
        final Header one = new Header("name", "value_one");
        final Header two = new Header("name", "value_two");
        assertThat(one.equals(two), is(false));
    }

    @Test
    public void equalityIsNotEqualWhenMoreValues() {
        final Header one = new Header("name", "value_one", "value_two");
        final Header two = new Header("name", "value_two");
        assertThat(one.equals(two), is(false));
    }

}
