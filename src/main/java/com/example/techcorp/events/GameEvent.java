package com.example.techcorp.events;

import com.example.techcorp.Company;

public interface GameEvent {
    void apply(Company company);
}
