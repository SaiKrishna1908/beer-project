package guru.sfg.brewery.security;

import guru.sfg.brewery.domain.BeerOrder;
import guru.sfg.brewery.domain.security.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Slf4j
public class BeerOrderAuthenticationManager {

    public Boolean customerIdMatches(Authentication authentication, UUID customerId){

        User  authenticatedUser =(User) authentication.getPrincipal();


        log.debug("Auth user id "+authenticatedUser.getCustomer().getId()+" customer Id"+customerId);

        return  authenticatedUser.getCustomer().getId().equals(customerId);
    }

    public Boolean customerOrderIdMatches(Authentication authentication, UUID customerId, UUID orderId){

        User user = (User) authentication.getPrincipal();

        return
                user.getCustomer().getBeerOrders().stream()
                        .map(BeerOrder::getId).collect(Collectors.toList()).contains(orderId);
    }
}
