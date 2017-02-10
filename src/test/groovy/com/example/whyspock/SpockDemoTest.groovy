package com.example.whyspock

import spock.lang.Specification
import groovyx.net.http.RESTClient
import spock.lang.Unroll


//@CompileStatic
class SpockDemoTest extends Specification {

    void setup() {

    }

    void cleanup() {

    }

    def "expect stuff "() {
        expect: 'Just a false assumption'
        Math.max(2, 18) == 3 * 6
    }

    def "test given when then"() {
        given: 'initialize stuff'
        def x = 1

        when: 'the real operation'
        def y = Math.max(2, 12)
        def z = 3 * 4

        then: 'check'
        y == z
        x == 1

    }


    @Unroll
    def "data driven test : (#a times #b equals #c)"() {

        when: 'the real operation'
        def x = first * last
        then: 'check'
        x == result;

        where:
        first | last | result
        1     | 2    | 2
        2     | 8    | 16
        7     | 3    | 21

    }


    def "mocking and stubbing should throw exception if an Integer is added to the list"() {
        given:
        List list = Mock(List)
        list.add(_ as Integer) >> { throw new IllegalArgumentException() }

        when:
        list.add(2)
        then:
        thrown(IllegalArgumentException)

        when:
        list.add("String")


        then:
        notThrown(IllegalArgumentException)

        and:
        1 * list.add(_)
    }
}