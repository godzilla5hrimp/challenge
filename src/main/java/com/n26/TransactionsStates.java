package com.n26;

import lombok.Getter;

@Getter
public enum TransactionsStates {
    TR_FINE("TR_FINE"), //Transaction created and stored succesfully
    TR_OLD("TR_OLD"),   //Transcation is older than 60 sec
    TR_INV("TR_INV");   //Transaction is in future

    private String state;

    TransactionsStates(String state) {
        this.state = state;
    }
}
