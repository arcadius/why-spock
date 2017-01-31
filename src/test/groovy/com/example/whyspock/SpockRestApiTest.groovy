package com.example.whyspock

import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.github.tomjankes.wiremock.WireMockGroovy
import groovyx.net.http.RESTClient
import org.junit.Rule
import spock.lang.Specification

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;


public class SpockRestApiTest extends Specification {

    @Rule
    WireMockRule wireMockRule = new WireMockRule(wireMockConfig().dynamicPort().dynamicHttpsPort())

    def "Find all users using a WireMock stub server"() {

        given: "A stubbed GET "

        def wireMockStub = new WireMockGroovy(wireMockRule.port())

        and:

        wireMockStub.stub {
            request {
                method "GET"
                url "/some/users"
            }
            response {
                status 200
                bodyFileName 'users.json'
                headers { 'Content-Type' 'application/json' }
            }
        }


        when: "client is called"
        def response = new RESTClient("http://localhost:${wireMockRule.port()}").get(path: '/some/users')

        then: 'status is OK'
        response.status == 200

        and: "we expect 2 users"
        response.data.size == 2

        and: 'Check second firstName only'

        response.data[1].firstName == 'Jane'

        and: 'Check entire response'
        response.data == [[id: 1, firstName: 'Joe', lastName: 'Doe'], [id: 2, firstName: 'Jane', lastName: 'Doe']]

        and: "the mock to be invoked exactly once"
        1L == wireMockStub.count {
            method "GET"
            url "/some/users"
        }
    }

}
