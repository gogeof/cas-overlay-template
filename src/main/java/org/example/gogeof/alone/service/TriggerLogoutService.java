package org.example.gogeof.alone.service;

import org.apereo.cas.CentralAuthenticationService;
import org.apereo.cas.authentication.Authentication;
import org.apereo.cas.ticket.Ticket;
import org.apereo.cas.ticket.TicketGrantingTicket;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.Collection;

public class TriggerLogoutService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TriggerLogoutService.class);
    private CentralAuthenticationService service;

    public TriggerLogoutService(CentralAuthenticationService service) {
        this.service = service;
    }

    /**
     * 触发其他用户退出
     *
     * @param id  用户id
     * @param tgt 当前登录的tgt
     */
    public void triggerLogout(String id, String tgt) {
        //找出用户id，并且不为当前tgt的，这里应当考虑数据性能，直接筛选用户再筛选tgt
        Collection<Ticket> tickets = this.service.getTickets(ticket -> {
            if(ticket instanceof TicketGrantingTicket) {
                TicketGrantingTicket t = ((TicketGrantingTicket)ticket).getRoot();
                Authentication authentication = t.getAuthentication();
                return t != null && authentication != null
                        && authentication.getPrincipal() != null && id.equals(authentication.getPrincipal().getId())
                        && !tgt.equals(t.getId());
            } else {
                return false;
            }

        });

        if (tickets != null && tickets.size() > 0) {
            LOGGER.info(String.format("[%s]强制强制注销%s", id, tickets.size()));
        }

        //发出注销
        for (Ticket ticket : tickets) {
            service.destroyTicketGrantingTicket(ticket.getId());
        }
    }
}
