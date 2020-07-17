package com.ticketmaster.sponsorship.upsell.service;

@FunctionalInterface
public interface DateProvider {
    long getCurrentTs();
}
