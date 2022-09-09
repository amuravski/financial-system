package com.solvd.financialsystem.domain;

import com.solvd.financialsystem.domain.connection.Connection;

@FunctionalInterface
public interface IUseAndRelease {

    void useAndRelease(Connection connection);

}
