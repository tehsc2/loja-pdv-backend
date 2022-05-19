package com.pdv.pdv.integrationTests;

import com.pdv.pdv.entities.Entrega;
import com.pdv.pdv.entities.ItemPedido;
import com.pdv.pdv.entities.Pedido;
import com.pdv.pdv.entities.Produto;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;


public class StepsIntegrationTest extends SpringIntegrationTest {

    private Long id;

    @Given("that I want to see my first pedido")
    public void that_i_want_to_see_my_first_pedido() {
        id = 1L;
    }

    @When("the client calls /pedidos")
    public void the_client_issues_GET_pedido() throws Throwable {
        executeGet("http://localhost:8080/pedidos/" + id);
    }

    @Then("the client receives status code of (\\d+)$")
    public void the_client_receives_status_code_of(int statusCode) throws Throwable {
        final HttpStatus currentStatusCode = latestResponse.getTheResponse().getStatusCode();
        assertThat("status code is incorrect : " + latestResponse.getBody(), currentStatusCode.value(), is(statusCode));
    }

    @And("the client receives a pedido")
    public void the_client_receives_server_version_body() throws Throwable {
        assertThat(latestResponse.getBody(), notNullValue());
    }
}
